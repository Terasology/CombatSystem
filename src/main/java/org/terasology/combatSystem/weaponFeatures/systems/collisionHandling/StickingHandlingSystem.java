// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import com.google.common.collect.Lists;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.destruction.DestroyEvent;
import org.terasology.engine.logic.destruction.EngineDamageTypes;
import org.terasology.engine.logic.location.Location;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.components.TriggerComponent;
import org.terasology.engine.physics.events.CollideEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.health.logic.BlockDamagedComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;

import java.util.Iterator;
import java.util.List;

@RegisterSystem
public class StickingHandlingSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    List<EntityRef> stuckArrows = Lists.newArrayList();
    List<EntityRef> destroyList = Lists.newArrayList();

    @In
    Time time;

    @ReceiveEvent(components = {StickComponent.class})
    public void stickingCollision(CollideEvent event, EntityRef entity) {
        EntityRef target = event.getOtherEntity();
        if (target.hasComponent(BlockComponent.class)) {
            blockSticking(entity, target);
        } else {
            sticking(entity, target);
        }

        // damage the other entity
        entity.send(new HurtEvent(target));

        event.consume();
    }

    @ReceiveEvent(components = StickComponent.class)
    public void stick(StickEvent event, EntityRef entity) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(BlockComponent.class)) {
            blockSticking(entity, target);
        } else {
            sticking(entity, target);
        }

        // damage the other entity
        entity.send(new HurtEvent(target));
    }

    @ReceiveEvent(components = ParentComponent.class, priority = EventPriority.PRIORITY_HIGH)
    public void removeChildEntities(DestroyEvent event, EntityRef entity) {
        ParentComponent parent = entity.getComponent(ParentComponent.class);
        if (parent == null) {
            return;
        }
        if (parent.children == null) {
            return;
        }

        Iterator<EntityRef> childrenList = parent.children.iterator();
        while (childrenList.hasNext()) {
            EntityRef children = childrenList.next();
            children.send(new DestroyEvent(entity, event.getInstigator(), EngineDamageTypes.DIRECT.get()));
        }
    }

    @Override
    public void update(float delta) {
        for (EntityRef arrow : stuckArrows) {
            if (arrow == null || arrow == EntityRef.NULL || !arrow.exists()) {
                destroyList.add(arrow);
                continue;
            }
            StickComponent stick = arrow.getComponent(StickComponent.class);
            if (stick == null) {
                destroyList.add(arrow);
                continue;
            }
            float currentGameTime = time.getGameTime();
            if (currentGameTime >= stick.stickTime + stick.totalStickingTime) {
                destroyList.add(arrow);
            }
        }

        for (EntityRef arrow : destroyList) {
            stuckArrows.remove(arrow);
            arrow.send(new DestroyEvent(arrow, arrow, EngineDamageTypes.DIRECT.get()));
        }

        destroyList.clear();
    }

    //------------------------------private methods-----------------------

    private void sticking(EntityRef entity, EntityRef target) {
        if (target == EntityRef.NULL || target == null) {
            return;
        }
        LocationComponent location = entity.getComponent(LocationComponent.class);
        LocationComponent otherEntityLocation = target.getComponent(LocationComponent.class);
        if (location == null || otherEntityLocation == null) {
            return;
        }

        // offset of the point of collision on the colliding entity to the
        // location of the colliding entity
        Vector3f offset = new Vector3f(location.getWorldPosition());
        offset.sub(otherEntityLocation.getWorldPosition());

        // to find quaternion rotation that if applied to quaternion 1 (the one which is
        // inversed) would result in quaternion 2 (the one which is multiplied)
        Quat4f relativeRot = new Quat4f(otherEntityLocation.getWorldRotation());
        relativeRot.inverse();
        relativeRot.mul(location.getWorldRotation());

        // world scale of the entity.
        float scale = location.getWorldScale();

        // resting all the movements of the entity
        MassComponent body = entity.getComponent(MassComponent.class);
        if (body != null) {
            pierce(entity);

            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            entity.saveComponent(body);
        }

        if (entity.hasComponent(TriggerComponent.class)) {
            entity.removeComponent(TriggerComponent.class);
        }

        // attaching entity to otherEntity so that it always follows otherEntity
        Location.attachChild(target, entity, offset, relativeRot, scale);

        StickComponent stick = entity.getComponent(StickComponent.class);
        stick.setTarget(target);
        stick.stickTime = time.getGameTime();
        entity.saveComponent(stick);

        ParentComponent parent = target.getComponent(ParentComponent.class);
        if (parent == null) {
            parent = new ParentComponent();
        }

        parent.children.add(entity);

        target.addOrSaveComponent(parent);

        if (entity.hasComponent(GravityComponent.class)) {
            entity.removeComponent(GravityComponent.class);
        }
    }

    // if the entity wants to stick to a block
    private void blockSticking(EntityRef entity, EntityRef target) {
        if (target == EntityRef.NULL || target == null) {
            return;
        }

        BlockComponent block = target.getComponent(BlockComponent.class);
        if (block == null) {
            return;
        }
        if (block.getBlock().isPenetrable()) {
            return;
        }
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if (location == null) {
            return;
        }

        MassComponent body = entity.getComponent(MassComponent.class);
        if (body != null) {
            pierce(entity);

            // resting all the movements of the entity
            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            entity.saveComponent(body);
        }

        if (!target.hasComponent(BlockDamagedComponent.class)) {
            target.addComponent(new BlockDamagedComponent());
        }

        ParentComponent parent = target.getComponent(ParentComponent.class);
        if (parent == null) {
            parent = new ParentComponent();
        }
        parent.children.add(entity);
        target.addOrSaveComponent(parent);

        StickComponent stick = entity.getComponent(StickComponent.class);
        stick.setTarget(target);
        stick.stickTime = time.getGameTime();
        entity.saveComponent(stick);

        if (entity.hasComponent(GravityComponent.class)) {
            entity.removeComponent(GravityComponent.class);
        }

        if (entity.hasComponent(TriggerComponent.class)) {
            entity.removeComponent(TriggerComponent.class);
        }
    }

    public void pierce(EntityRef entity) {
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if (location == null) {
            return;
        }

        StickComponent stick = entity.getComponent(StickComponent.class);
        Vector3f entityLoc = location.getWorldPosition();

        Vector3f direction = location.getWorldDirection();
        direction.scale(stick.pierceAmount);
        entityLoc.add(direction);

        location.setWorldPosition(entityLoc);

        entity.saveComponent(location);

        if (stick.totalStickingTime >= 0) {
            stuckArrows.add(entity);
        }
    }

}
