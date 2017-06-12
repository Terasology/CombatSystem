package org.terasology.combatSystem.physics.systems;

import java.util.Iterator;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatForceEvent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class CombatPhysicsSystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    
    @In
    private EntityManager entityManager;
    
    @ReceiveEvent(components = {MassComponent.class})
    public void onImpulse(CombatImpulseEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);
        
        if(body == null){
            return;
        }
        Vector3f impulse = event.getImpulse();
        impulse.div(body.mass);
        if(impulse == null || impulse.length() == 0){
            return;
        }
        body.velocity.add(impulse);
        
        entity.saveComponent(body);
    }
    
    @ReceiveEvent(components = {MassComponent.class})
    public void onApplyForce(CombatForceEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);
        
        body.force.add(event.getForce());
        
        entity.saveComponent(body);
    }
    
    @Override
    public void update(float delta){
        
        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(MassComponent.class);
        Iterator<EntityRef> entities = entitiesWith.iterator();
        while(entities.hasNext()){
            EntityRef entity = entities.next();
            MassComponent body = entity.getComponent(MassComponent.class);
            LocationComponent location = entity.getComponent(LocationComponent.class);
            
            if(location != null){
                // change velocity based on acceleration
                body.acceleration.mul(delta);
                body.velocity.add(body.acceleration);
                body.acceleration.set(0, 0, 0);
                
                // change location based on velocity
                Vector3f velocity = new Vector3f(body.velocity);
                velocity.mul(delta);
                Vector3f initialLoc = location.getWorldPosition();
                initialLoc.add(velocity);
                location.setWorldPosition(initialLoc);
                
                entity.saveComponent(body);
                entity.saveComponent(location);
            }
          
        }
    }

}
