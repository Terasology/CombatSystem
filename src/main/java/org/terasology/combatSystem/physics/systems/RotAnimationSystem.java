package org.terasology.combatSystem.physics.systems;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.components.RotationAnimationComponent;
import org.terasology.combatSystem.physics.components.RotationAnimationComponent.AnimMode;
import org.terasology.combatSystem.physics.components.RotationAnimationComponent.RotAnimInfo;
import org.terasology.combatSystem.physics.events.CombatAddAngularVelocityEvent;
import org.terasology.combatSystem.physics.events.EndRotAnimationEvent;
import org.terasology.combatSystem.physics.events.PauseRotAnimationEvent;
import org.terasology.combatSystem.physics.events.StartRotAnimationEvent;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.math.geom.Quat4f;
import org.terasology.registry.In;

@RegisterSystem
public class RotAnimationSystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    
    @In
    Time time;
    
    @In
    EntityManager entityManager;
    
    @ReceiveEvent( components = {RotationAnimationComponent.class, MassComponent.class})
    public void startAnimation(StartRotAnimationEvent event, EntityRef entity, RotationAnimationComponent rotAnim){
        RotAnimInfo rotAnimInfo = rotAnim.animInfo.get(rotAnim.currentAnimPointer);
        rotAnim.rotAnim = rotAnimInfo;
        if(rotAnim.startTime == -1){
            rotAnim.timeLeft = rotAnimInfo.totalTime;
        }
        
        rotAnim.rotating = true;
        rotAnim.startTime = time.getGameTime();
        
        entity.saveComponent(rotAnim);
        entity.send(new CombatAddAngularVelocityEvent(rotAnimInfo.rotationSpeed));
    }
    
    @ReceiveEvent( components = {RotationAnimationComponent.class, MassComponent.class})
    public void pauseAnimation(PauseRotAnimationEvent event, EntityRef entity, RotationAnimationComponent rotAnim){
        Quat4f angularVel = new Quat4f(rotAnim.rotAnim.rotationSpeed);
        angularVel.inverse();
        
        rotAnim.rotating = false;
        
        entity.saveComponent(rotAnim);
        entity.send(new CombatAddAngularVelocityEvent(angularVel));
    }
    
    @ReceiveEvent( components = {RotationAnimationComponent.class, MassComponent.class})
    public void endAnimation(EndRotAnimationEvent event, EntityRef entity, RotationAnimationComponent rotAnim){
        
        rotAnim.startTime = -1;
        rotAnim.currentAnimPointer = 0;
        rotAnim.timeLeft = 0;
        
        if(!rotAnim.rotating){
            entity.saveComponent(rotAnim);
            return;
        }
        
        rotAnim.rotating = false;
        
        entity.saveComponent(rotAnim);
        
        Quat4f angularVel = new Quat4f(rotAnim.rotAnim.rotationSpeed);
        angularVel.inverse();
        entity.send(new CombatAddAngularVelocityEvent(angularVel));
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entities = entityManager.getEntitiesWith(RotationAnimationComponent.class, MassComponent.class);
        
        for(EntityRef entity : entities){
            RotationAnimationComponent rotAnim = entity.getComponent(RotationAnimationComponent.class);
            if(!rotAnim.rotating){
                continue;
            }
            
            rotAnim.timeLeft -= delta;
            entity.saveComponent(rotAnim);
            
            if(rotAnim.timeLeft <= 0){
                rotAnim.currentAnimPointer++;
                if(rotAnim.currentAnimPointer >= rotAnim.animInfo.size()){
                    if(rotAnim.mode == AnimMode.LOOP){
                        rotAnim.currentAnimPointer = 0;
                    }
                    else if(rotAnim.mode == AnimMode.BOOMRANG){
                        
                    }
                    else if(rotAnim.mode == AnimMode.ONCE){
                        entity.send(new EndRotAnimationEvent());
                        continue;
                    }
                }
                
                rotAnim.startTime = -1;
                
                Quat4f angularVel = new Quat4f(rotAnim.rotAnim.rotationSpeed);
                angularVel.inverse();
                entity.send(new CombatAddAngularVelocityEvent(angularVel));
                
                entity.saveComponent(rotAnim);
                
                entity.send(new StartRotAnimationEvent());
            }
        }
    }

}
