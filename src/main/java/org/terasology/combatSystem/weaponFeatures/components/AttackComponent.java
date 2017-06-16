package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;

/**
 * Abstract Component to specify if the Component that extends it
 * will be used as primary or secondary attack.
 */
public class AttackComponent implements Component{
    public boolean primaryAttack = true;

}
