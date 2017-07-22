package org.terasology.combatSystem.traps.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.network.Replicate;

public class BeaconComponent implements Component{
    @Replicate
    public EntityRef base = EntityRef.NULL;

}
