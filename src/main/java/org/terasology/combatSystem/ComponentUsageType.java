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
