package org.terasology.combatSystem.weaponFeatures.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;

import com.google.common.collect.Lists;

public class ExplosionComponent implements Component{
    @Replicate
    public List<CollisionGroup> collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER);
    
    @Replicate
    public float radius = 2.0f;
    
    @Replicate
    public float explosionStartTime = -1.0f;                 //sec
    @Replicate
    public float explosionDelayTime = 0.0f;                 //sec
    
    @Replicate
    public boolean explosionStarted = false;

}
