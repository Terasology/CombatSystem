package org.terasology.combatSystem.components;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.PlanetFactionList;
import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class FactionComponent implements Component{
    @Replicate
    public FactionList faction = PlanetFactionList.EARTH;

}
