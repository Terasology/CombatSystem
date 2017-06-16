package org.terasology.combatSystem.weaponFeatures.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;

import com.google.common.collect.Lists;

public class ExplosionComponent implements Component{
    public List<CollisionGroup> collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER);
    
    public float innerRadius = 1.0f;
    public float outerRadius = 2.0f;
    public float outerDamageFactor = 0.5f;
    
    public float explosionStartTime = -1.0f;                 //sec
    public float explosionDelayTime = 0.0f;                 //sec
    
    public boolean explosionStarted = false;

}
