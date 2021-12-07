// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.event;

import org.terasology.gestalt.entitysystem.event.Event;

/**
 * Gets triggered when the player enters <b>Combat State</b>
 * <p>
 * <b>Combat State</b> begins(basic <b>CombatSystem</b> functionality):
 * <ul>
 * <li>when the player hits an enemy. (first blood kind of thing)
 * <li>when an enemy hits a player. (first blood kind of thing)
 * </ul>
 */
public class CombatEnteredEvent implements Event {

}
