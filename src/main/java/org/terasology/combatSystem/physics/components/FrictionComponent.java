package org.terasology.combatSystem.physics.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Adds <b>friction</b>.
 * <p>
 * <b>this</b> can be used to implement <b>Fluid Viscosity</b> as well.
 *
 * TODO implement this component and its code.
 */
public class FrictionComponent implements Component<FrictionComponent> {
    @Replicate
    float friction = 20.0f;

    @Replicate
    float velocity = 20.0f;

    @Override
    public void copy(FrictionComponent other) {
        this.friction = other.friction;
        this.velocity = other.velocity;
    }
}
