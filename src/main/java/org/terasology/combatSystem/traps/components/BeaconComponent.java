package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;

public class BeaconComponent implements Component{
    @Replicate
    public EntityRef base = EntityRef.NULL;

}
