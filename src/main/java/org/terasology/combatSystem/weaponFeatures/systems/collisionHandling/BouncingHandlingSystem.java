package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.BounceComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.BounceEvent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem
public class BouncingHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {BounceComponent.class})
    public void bouncingCollision(CollideEvent event, EntityRef entity){
        bounce(entity, event.getOtherEntity(), event.getNormal());
        
        event.consume();
    }
    
    @ReceiveEvent( components = BounceComponent.class)
    public void bouncing(BounceEvent event, EntityRef entity){
        bounce(entity, event.getTarget(), event.getNormal());
    }
    
    //----------------------------private methods----------------------------------
    
    // bounce entity
    private void bounce(EntityRef entity, EntityRef target, Vector3f normal){
        MassComponent mass = entity.getComponent(MassComponent.class);
        BounceComponent bounce = entity.getComponent(BounceComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(mass == null || location == null || bounce == null){
            return;
        }
        
        // check peircing
        if(checkPeircing(normal, mass.velocity, bounce.maxPierceAngle, bounce.minPeirceVelocity)){
            entity.addOrSaveComponent(new StickComponent());
            entity.removeComponent(BounceComponent.class);
            entity.send(new StickEvent(target));
            return;
        }
        
        // check if the velocity is enough for another bounce else just destroy the arrow
        if(mass.velocity.lengthSquared() <= (bounce.minBounceVelocity*bounce.minBounceVelocity)){
            entity.destroy();
        }
        else{
            Vector3f bounceDir = new Vector3f(normal);
            bounceDir.normalize();
            bounceDir.scale(2*bounceDir.dot(mass.velocity));
            bounceDir.sub(mass.velocity);
            bounceDir.scale(bounce.bounceFactor);
            
            mass.velocity.set(bounceDir);
            entity.saveComponent(mass);
        }
        
        entity.send(new AddCollisionExceptionEvent(target));
        
        //-------------------------repetitive code for every HurtingComponent-----------
        
        // damage the other entity
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        if(hurting != null){
            hurting.amount = bounce.amount;
            hurting.damageType = EngineDamageTypes.DIRECT.get();
            entity.saveComponent(hurting);
            entity.send(new HurtEvent(target));
        }
    }
    
    private boolean checkPeircing(Vector3f normal, Vector3f velocity, int maxAngle, float minVelocity){
        Vector3f vel = new Vector3f(velocity);
        
        int angle = (int) Math.toDegrees(vel.angle(normal));
        if(angle > 90){
            angle = 180 - angle;
        }
        
        if(angle <= maxAngle && velocity.lengthSquared() >= (minVelocity*minVelocity)){
            return true;
        }
        
        return false;
    }

}
