package org.terasology.combatSystem.components;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.PlanetFactionList;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Makes the entity belong to a specific faction.
 */
public class FactionComponent implements Component<FactionComponent> {
    @Replicate
    public FactionList faction = PlanetFactionList.EARTH;

    @Override
    public void copy(FactionComponent other) {
        this.faction = other.faction;
    }
}
