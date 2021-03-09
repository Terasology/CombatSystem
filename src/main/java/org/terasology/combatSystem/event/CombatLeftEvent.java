package org.terasology.combatSystem.event;

import org.terasology.engine.entitySystem.event.Event;

/**
 * Gets triggered when the player leaves <b>Combat State</b>
 * <p>
 * <b>Combat State</b> exits(basic <b>CombatSystem</b> functionality):
 * <ul>
 * <li>when the player kills all enemies in a certain radius.
 * <li>when the player flees i.e. gets out of a certain radius around enemies.
 * </ul>
 * <b>NOTE</b>: The Combat State does not exit if the enemies kill the player.
 */
public class CombatLeftEvent implements Event{

}
