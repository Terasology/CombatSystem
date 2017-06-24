package org.terasology.combatSystem.physics.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.network.Replicate;

import com.google.common.collect.Lists;

/**
 * specifies the <b>Entities</b> that will not be collided with.
 * <p>
 * An {@link EntityRef} with this {@link Component} will not collide with <b>exceptions</b>. 
 */
public class CollisionExceptionsComponent implements Component{
    @Replicate
    public List<EntityRef> exceptions = Lists.<EntityRef>newArrayList();

}
