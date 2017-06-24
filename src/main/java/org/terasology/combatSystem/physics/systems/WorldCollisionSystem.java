//package org.terasology.combatSystem.physics.systems;
//
//import java.util.Iterator;
//import java.util.List;
//
//import org.terasology.combatSystem.physics.components.MassComponent;
//import org.terasology.entitySystem.entity.EntityManager;
//import org.terasology.entitySystem.entity.EntityRef;
//import org.terasology.entitySystem.systems.BaseComponentSystem;
//import org.terasology.entitySystem.systems.RegisterMode;
//import org.terasology.entitySystem.systems.RegisterSystem;
//import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
//import org.terasology.physics.components.TriggerComponent;
//import org.terasology.physics.engine.PhysicsEngine;
//import org.terasology.registry.In;
//
//@RegisterSystem(RegisterMode.AUTHORITY)
//public class WorldCollisionSystem extends BaseComponentSystem implements UpdateSubscriberSystem{
//    @In
//    PhysicsEngine physics;
//    @In
//    EntityManager entityManager;
//    
//    @Override
//    public void update(float delta) {
//        // TODO Auto-generated method stub
//        Iterator<EntityRef> iter = (entityManager.getEntitiesWith(MassComponent.class, TriggerComponent.class)).iterator();
//        while(iter.hasNext()){
//            EntityRef entity = iter.next();
//            
//        }
//        
//    }
//
//}
