// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.gestalt.entitysystem.component.EmptyComponent;

/**
 * Marker component to denote that a {@link org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent PrimaryAttackEvent} should
 * be triggered when activating an entity with this component.
 */
public class PrimaryAttackComponent extends EmptyComponent<PrimaryAttackComponent> {
}
