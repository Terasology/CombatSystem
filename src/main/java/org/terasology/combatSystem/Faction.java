// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem;

/**
 * Interface used to specify different types of faction lists.
 * <p>
 * Classes implementing this interface should be <b>immutable</b>. This is to allow for a safe reuse when copying the faction in
 * components.
 */
public interface Faction {
    //NOTE: if we identify the need to make the faction implementations mutable, or observe any other issues with only doing a shallow copy
    //      of properties of this type in components, we may add a 'copy()' method to this interface.
}
