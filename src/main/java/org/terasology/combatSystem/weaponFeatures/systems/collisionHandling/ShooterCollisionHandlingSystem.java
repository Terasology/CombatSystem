package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ShooterCollisionHandlingSystem extends CollisionHandlingSystem{
    @ReceiveEvent(components = ShooterComponent.class)
    public void resolveShooterState(OnActivatedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(components = ShooterComponent.class)
    public void updateShooterState(OnChangedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
}
