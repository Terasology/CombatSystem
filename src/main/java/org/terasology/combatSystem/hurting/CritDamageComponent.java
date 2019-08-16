/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

/**
 * Damages an entity critically.
 * <p>
 * Damaging critically means multiplying the original damage by <b>critFactor</b>.
 * The critical damage has <b>critChance</b>/100 probability of occurring.
 */
public class CritDamageComponent implements Component {
    
    @Replicate
    public int critChance = 10;                 // in percentage out of 100
    
    @Replicate
    public float critFactor = 2;                // factor to multiply damage with

}
