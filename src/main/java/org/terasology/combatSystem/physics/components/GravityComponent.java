package org.terasology.combatSystem.physics.components;

import org.joml.Vector3f;
import org.terasology.combatSystem.physics.events.CombatForceEvent;
import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

/**
 * Adds <b>Gravity</b> to the entity.
 * <p>
 * <b>NOTE</b>: use of this to add additional forces other than gravity is <b>discouraged</b>.
 * That would result in removal of those forces too if <b>Gravity</b> is disabled as a whole.
 * Instead use of {@link CombatForceEvent} is encouraged to add forces.
 */
public class GravityComponent implements Component{
    @Replicate
    public Vector3f gravityAccel = new Vector3f(0, -10.0f, 0);

}
