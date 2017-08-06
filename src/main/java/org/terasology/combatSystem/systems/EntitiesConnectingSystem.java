package org.terasology.combatSystem.systems;

import java.util.List;

import org.terasology.combatSystem.components.EntitiesConnectorComponent;
import org.terasology.combatSystem.components.EntitiesConnectorComponent.ConnectingInfo;
import org.terasology.combatSystem.physics.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.world.block.BlockComponent;

import com.google.common.collect.Lists;

@RegisterSystem
public class EntitiesConnectingSystem extends BaseComponentSystem{
    
    @In
    EntityManager entityManager;
    
    @ReceiveEvent( components = {EntitiesConnectorComponent.class, BlockComponent.class} )
    public void rootCreated(OnActivatedComponent event, EntityRef entity, EntitiesConnectorComponent component){
        if(component.childEntities.size() < 1){
            return;
        }
        LocationComponent loc = entity.getComponent(LocationComponent.class);
        if(loc == null){
            return;
        }
        
        List<EntityRef> children = Lists.newArrayList();
        
        for(ConnectingInfo info : component.childEntities){
            EntityBuilder builder = entityManager.newBuilder(info.prefab);
            EntityRef child;
            if(builder == null){
                continue;
            }
            LocationComponent location = builder.getComponent(LocationComponent.class);
            if(location != null){
                Vector3f entityLoc = loc.getWorldPosition();
                entityLoc.add(info.offset);
                location.setWorldPosition(entityLoc);
                builder.saveComponent(location);
            }
            child = builder.build();
            if(child != null || child != EntityRef.NULL || child.exists()){
                ParentComponent parent = entity.getComponent(ParentComponent.class);
                if(parent == null){
                    parent = new ParentComponent();
                }
                parent.children.add(child);
                entity.addOrSaveComponent(parent);
                
                if(location != null){
                    Location.attachChild(entity, child);
                }
                
                if(info.addParentAsChild){
                    ParentComponent childPar = child.getComponent(ParentComponent.class);
                    if(childPar == null){
                        childPar = new ParentComponent();
                    }
                    
                    childPar.children.add(entity);
                    child.addOrSaveComponent(childPar);
                }
                
                children.add(child);
            }
        }
        
        for(EntityRef childEntity : children){
            childEntity.send(new AddCollisionExceptionEvent(children));
        }
    }

}
