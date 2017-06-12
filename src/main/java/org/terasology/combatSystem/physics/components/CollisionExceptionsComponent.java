package org.terasology.combatSystem.physics.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

import com.google.common.collect.Lists;

public class CollisionExceptionsComponent implements Component{
    public List<EntityRef> exceptions = Lists.<EntityRef>newArrayList();

}
