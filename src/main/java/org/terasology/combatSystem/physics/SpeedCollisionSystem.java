package org.terasology.combatSystem.physics;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;

/**
 * Checks Collision for entities above a paricular speed. Takes into account the parabolic
 * path of the entity
 * Convex Sweeping is not done due to the parabolic path of the entity instead variable
 * time-step method is used
 * 
 * TODO Decide whether the method should be implemented in core physics engine or here
 */
//@RegisterSystem(RegisterMode.AUTHORITY)
public class SpeedCollisionSystem /*extends BaseComponentSystem*/{
    

}
