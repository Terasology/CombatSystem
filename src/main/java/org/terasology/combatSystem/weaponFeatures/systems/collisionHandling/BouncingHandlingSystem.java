package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.features.FeatureComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.BounceComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.AddFeaturesEvent;
import org.terasology.combatSystem.weaponFeatures.events.BounceEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class BouncingHandlingSystem extends CollisionHandlingSystem{
    
    @ReceiveEvent(components = {BounceComponent.class})
    public void bouncing(CollideEvent event, EntityRef entity){
        entity.send(new BounceEvent(event.getOtherEntity(), event.getNormal()));
        
        event.consume();
    }
    
    @ReceiveEvent(components = BounceComponent.class)
    public void bounce(BounceEvent event, EntityRef entity){
        EntityRef target = event.getTarget();
        
        MassComponent mass = entity.getComponent(MassComponent.class);
        BounceComponent bounce = entity.getComponent(BounceComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(mass == null || location == null){
            return;
        }
        
        Vector3f bounceDir = event.getNormal();
        bounceDir.normalize();
        bounceDir.negate();
        bounceDir.scale(2*bounceDir.dot(mass.velocity));
        bounceDir.negate();
        bounceDir.scale(bounce.bounceFactor);
        
        mass.velocity.add(bounceDir);
        entity.saveComponent(mass);
        
        entity.send(new AddExceptionEvent(target));
        
        //-------------------------repetitive code for every HurtingComponent-----------
        
        // damage the other entity
        damageOtherEntity(target, entity, bounce);
        
        //----------------repetitive code for every component that triggers action-------
        
        //send a new AddFeaturesEvent with the collide event info as parameters
        if(entity.hasComponent(FeatureComponent.class)){
            entity.send(new AddFeaturesEvent());
        }
    }

}
