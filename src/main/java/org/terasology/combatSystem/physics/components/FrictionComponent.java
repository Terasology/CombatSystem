package org.terasology.combatSystem.physics.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

/**
 * Adds <b>friction</b>.
 * <p>
 * <b>this</b> can be used to implement <b>Fluid Viscosity</b> as well.
 * 
 * TODO implement this component and its code.
 */
public class FrictionComponent implements Component{
    @Replicate
    float friction = 20.0f;
    
    @Replicate
    float velocity = 20.0f;

}
