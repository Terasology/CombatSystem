package org.terasology.combatSystem.weaponFeatures.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

import com.google.common.collect.Lists;

public class ParentComponent implements Component{
    public List<EntityRef> children;
    
    public ParentComponent(){
        children = Lists.<EntityRef>newArrayList();
    }

}
