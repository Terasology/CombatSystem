// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.traps.components.ActivateOnPlaceComponent;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.NetFilterEvent;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.logic.MeshComponent;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.engine.world.block.items.OnBlockToItem;
import org.terasology.sensors.ActivateSensorEvent;
import org.terasology.sensors.DeactivateSensorEvent;
import org.terasology.sensors.PhysicalSensorComponent;
import org.terasology.sensors.volumeSensing.VolumeSensorComponent;

import java.util.List;

@RegisterSystem
public class TrapPlacingSystem extends BaseComponentSystem {
    @In
    LocalPlayer player;

    @ReceiveEvent(components = {PhysicalSensorComponent.class, LocationComponent.class})
    public void activateOrDeactivateTrap(ActivateEvent event, EntityRef entity, PhysicalSensorComponent physical) {
        if (!entity.hasComponent(AttackerComponent.class)) {
            return;
        }

        EntityRef character = player.getCharacterEntity();
        List<EntityRef> allOwners = OwnerSpecific.getAllOwners(entity);
        if (allOwners == null) {
            return;
        }

        for (EntityRef attackerEntity : allOwners) {
            if (attackerEntity.equals(character)) {
                if (!physical.activated) {
                    entity.send(new ActivateSensorEvent());
                } else {
                    entity.send(new DeactivateSensorEvent());
                }
            }
        }
    }

    @ReceiveEvent(components = PhysicalSensorComponent.class)
    public void deactivateOnItemConversion(OnBlockToItem event, EntityRef entity, PhysicalSensorComponent physical) {
        if (physical.activated) {
            entity.send(new DeactivateSensorEvent());
        }
    }

    @ReceiveEvent(components = {ActivateOnPlaceComponent.class, BlockComponent.class})
    public void activateOnBlockConversion(OnActivatedComponent event, EntityRef entity) {
        entity.send(new ActivateSensorEvent());
    }

    @NetFilterEvent(netFilter = RegisterMode.CLIENT)
    @ReceiveEvent(components = {VolumeSensorComponent.class, LocationComponent.class},
                  priority = EventPriority.PRIORITY_LOW)
    public void addMeshForClient(ActivateSensorEvent event, EntityRef entity, PhysicalSensorComponent physical) {
        EntityRef sensor = physical.sensor;

        if (!entity.hasComponent(AttackerComponent.class)) {
            return;
        }

        EntityRef character = player.getCharacterEntity();
        List<EntityRef> allOwners = OwnerSpecific.getAllOwners(entity);
        if (allOwners == null) {
            return;
        }

        for (EntityRef attackerEntity : allOwners) {
            if (attackerEntity.equals(character)) {
                if (!sensor.hasComponent(MeshComponent.class)) {
                    MeshComponent mesh = new MeshComponent();
                    mesh.translucent = true;
                    mesh.material = Assets.getMaterial("CombatSystem:forceField").get();
                    mesh.mesh = Assets.getMesh("CombatSystem:zesphere").get();
                    sensor.addComponent(mesh);
                }
            }
        }
    }
}
