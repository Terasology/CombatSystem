// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.components;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.PlanetFactionList;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

/**
 * Makes the entity belong to a specific faction.
 */
public class FactionComponent implements Component {
    @Replicate
    public FactionList faction = PlanetFactionList.EARTH;

}
