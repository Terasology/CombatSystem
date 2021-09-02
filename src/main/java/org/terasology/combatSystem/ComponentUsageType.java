// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem;

/**
 * Specifies the usage limit for {@code FeatureComponent}.
 */
public enum ComponentUsageType {
    /**
     * {@link FeatureComponent} will be removed after using the feature
     */
    ONCE,
    /**
     * {@code FeatureComponent} will be never be removed
     */
    UNLIMITED;

}
