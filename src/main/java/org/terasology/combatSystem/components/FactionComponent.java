package org.terasology.combatSystem.components;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.PlanetFactionList;
import org.terasology.entitySystem.Component;

public class FactionComponent implements Component{
    public FactionList faction = PlanetFactionList.EARTH;

}
