// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.systems;

import com.google.common.collect.Maps;
import org.joml.Vector3f;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatForceEvent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.CollisionGroup;
import org.terasology.engine.physics.HitResult;
import org.terasology.engine.physics.Physics;
import org.terasology.engine.physics.StandardCollisionGroup;
import org.terasology.engine.physics.components.TriggerComponent;
import org.terasology.engine.physics.components.shapes.BoxShapeComponent;
import org.terasology.engine.physics.components.shapes.CapsuleShapeComponent;
import org.terasology.engine.physics.components.shapes.CylinderShapeComponent;
import org.terasology.engine.physics.components.shapes.SphereShapeComponent;
import org.terasology.engine.physics.events.CollideEvent;
import org.terasology.engine.registry.In;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * handles various physical operations applied to an entity with {@code MassComponent}
 */
@RegisterSystem
public class CombatPhysicsSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    private static final float TUNNELING_MIN_VELOCITY_SQ = 400.0f;

    private final Map<EntityRef, CollisionGroup[]> entityCollidesWithGroup = Maps.newHashMap();

    @In
    private EntityManager entityManager;
    @In
    private Physics physics;

    @ReceiveEvent(components = TriggerComponent.class)
    public void addCollisionGroupOnMap(OnActivatedComponent event, EntityRef entity) {
        entityCollidesWithGroup.put(entity, collisionGroupListToArray(entity));
    }

    @ReceiveEvent(components = TriggerComponent.class)
    public void updateCollisionGroupOnMap(OnChangedComponent event, EntityRef entity) {
        entityCollidesWithGroup.put(entity, collisionGroupListToArray(entity));
    }

    @ReceiveEvent(components = TriggerComponent.class)
    public void removeCollisionGroupOnMap(BeforeDeactivateComponent event, EntityRef entity) {
        entityCollidesWithGroup.remove(entity);
    }

    /**
     * Applies impulse to specified entity.
     *
     * @param event stores the value of impulse to be applied.
     * @param entity the entity on which the impulse is applied.
     */
    @ReceiveEvent(components = {MassComponent.class})
    public void onImpulse(CombatImpulseEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);

        if (body == null) {
            return;
        }
        Vector3f impulse = new Vector3f(event.getImpulse());
        impulse.div(body.mass);
        if (impulse.length() == 0) {
            return;
        }
        body.velocity.add(impulse);

        entity.saveComponent(body);
    }

    /**
     * Applies force to specified entity.
     * <p>
     * the force is added to total force on entity. Therefore, force is resolved in this system.
     *
     * @param event stores the value of force to be applied.
     * @param entity the entity on which the impulse is applied.
     */
    @ReceiveEvent(components = MassComponent.class)
    public void onApplyForce(CombatForceEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);

        body.force.add(event.getForce());

        entity.saveComponent(body);
    }

    /**
     * changes velocity and location of an entity based on delta as well as checks for world collisions. This method is
     * also responsible for raycasting to prevent tunneling for small fast objects.
     */
    @Override
    public void update(float delta) {

        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(MassComponent.class);
        for (EntityRef entity : entitiesWith) {
            MassComponent body = entity.getComponent(MassComponent.class);
            LocationComponent location = entity.getComponent(LocationComponent.class);

            if (location != null) {
                // change velocity based on acceleration
                body.acceleration.mul(delta);
                body.velocity.add(body.acceleration);
                body.acceleration.set(0, 0, 0);

                // raycasting for world collisions
                float velocityMagSq = body.velocity.lengthSquared();

                CollisionGroup[] group = entityCollidesWithGroup.get(entity);
                short combinedGroup = combineGroups(group);
                if ((combinedGroup & StandardCollisionGroup.WORLD.getFlag()) != 0) {
                    Vector3f direction = new Vector3f(body.velocity);
                    float distance = ((float) Math.sqrt(velocityMagSq)) * delta;
                    Vector3f from = calculateStartingPoint(location.getWorldPosition(new Vector3f()), direction, entity);
                    direction.normalize();

                    HitResult result = physics.rayTrace(from, direction, distance, group);

                    if (result.isHit() && result.isWorldHit()) {
                        EntityRef otherEntity = result.getEntity();
                        Vector3f hitPoint = result.getHitPoint();
                        Vector3f normal = result.getHitNormal();

                        // updating position so that tip of entity is at point of contact
                        Vector3f newPos = new Vector3f(hitPoint);
                        from.sub(location.getWorldPosition(new Vector3f()));
                        newPos.sub(from);
                        location.setWorldPosition(newPos);

                        entity.saveComponent(location);

                        entity.send(new CollideEvent(otherEntity, hitPoint,
                            hitPoint, 0.0f, normal));

                        continue;
                    }
                }

                // raycasting to resolve tunneling in fast moving small objects
                if (velocityMagSq >= TUNNELING_MIN_VELOCITY_SQ
                    && group != null) {
                    Vector3f direction = new Vector3f(body.velocity);
                    float distance = ((float) Math.sqrt(velocityMagSq)) * delta;
                    Vector3f from = calculateStartingPoint(location.getWorldPosition(new Vector3f()), direction, entity);
                    direction.normalize();

                    HitResult result = physics.rayTrace(from, direction, distance, group);

                    if (result.isHit()) {
                        EntityRef otherEntity = result.getEntity();
                        Vector3f hitPoint = result.getHitPoint();
                        Vector3f normal = result.getHitNormal();

                        // updating position so that tip of entity is at point of contact
                        Vector3f newPos = new Vector3f(hitPoint);
                        from.sub(location.getWorldPosition(new Vector3f()));
                        newPos.sub(from);
                        location.setWorldPosition(newPos);

                        entity.saveComponent(location);

                        entity.send(new CollideEvent(otherEntity, hitPoint,
                            hitPoint, 0.0f, result.getHitNormal()));

                        if (otherEntity.hasComponent(TriggerComponent.class)) {
                            otherEntity.send(new CollideEvent(entity, hitPoint,
                                hitPoint, 0.0f, normal));
                        }

                        continue;
                    }
                }

                // change location based on velocity
                Vector3f velocity = new Vector3f(body.velocity);
                velocity.mul(delta);
                Vector3f initialLoc = location.getWorldPosition(new Vector3f());
                initialLoc.add(velocity);
                location.setWorldPosition(initialLoc);

                entity.saveComponent(body);
                entity.saveComponent(location);


            }

        }
    }

    //-----------------------------private methods----------------------

    /**
     * converts {@link List} data structure of type {@link CollisionGroup} into array
     *
     * @param entity
     * @return group Array with all elements of CollisionGroup List.
     */
    private CollisionGroup[] collisionGroupListToArray(EntityRef entity) {
        TriggerComponent trigger = entity.getComponent(TriggerComponent.class);
        if (trigger == null) {
            return null;
        }
        List<CollisionGroup> collidesWith = trigger.detectGroups;

        return collidesWith.toArray(new CollisionGroup[0]);
    }

    private short combineGroups(CollisionGroup... collisionGroups) {
        if (collisionGroups == null) {
            return 0;
        }
        return combineGroups(Arrays.asList(collisionGroups));
    }

    /**
     * combines the {@code CollisionGroup} in filter mask through & bit operation and returns the value
     *
     * @param collisionGroup
     * @return flag
     */
    private short combineGroups(List<CollisionGroup> collisionGroup) {
        short flag = 0;
        if (collisionGroup == null) {
            return flag;
        }
        for (CollisionGroup group : collisionGroup) {
            flag |= group.getFlag();
        }
        return flag;
    }

    /**
     * calculates the tip of collision shape in its direction of motion. Right now it only works for entities whose +ve
     * z-axis always faces the direction of movement.
     *
     * @param origin the location of entity
     * @param direction the direction of motion of entity
     * @param entity the entity whose tip point is to be calculated
     * @return
     */
    private Vector3f calculateStartingPoint(Vector3f origin, Vector3f direction, EntityRef entity) {
        direction.normalize();
        BoxShapeComponent box = entity.getComponent(BoxShapeComponent.class);
        if (box != null) {
            direction.mul(box.extents.z / 2.0f);
            origin.add(direction);
        }
        SphereShapeComponent sphere = entity.getComponent(SphereShapeComponent.class);
        if (sphere != null) {
            direction.mul(sphere.radius);
            origin.add(direction);
        }
        CapsuleShapeComponent capsule = entity.getComponent(CapsuleShapeComponent.class);
        if (capsule != null) {
            direction.mul((capsule.radius + (capsule.height / 2.0f)));
            origin.add(direction);
        }
        CylinderShapeComponent cylinder = entity.getComponent(CylinderShapeComponent.class);
        if (cylinder != null) {
            direction.mul(cylinder.height / 2.0f);
            origin.add(direction);
        }

        return origin;
    }
}
