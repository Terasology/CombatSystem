package org.terasology.combatSystem.weaponFeatures.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.network.Replicate;

import com.google.common.collect.Lists;

public class ParentComponent implements Component{
    @Replicate
    public List<EntityRef> children;
    
    public ParentComponent(){
        children = Lists.<EntityRef>newArrayList();
    }

}
