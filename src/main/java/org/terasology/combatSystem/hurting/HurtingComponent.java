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
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.network.Replicate;

/**
 * Specifies the amount and damage type that an entity will inflict on other entities.
 * <p>
 * {@link HurtEvent} is used to hurt other entity/entities.
 */
public class HurtingComponent implements Component {
    /**
     * The amount of damage the entity will inflict.
     */
    @Replicate
    public int amount = 3;

    @Replicate
    public long duration = 2000;

    /**
     * The type of damage.
     */
    @Replicate
    public Prefab damageType = EngineDamageTypes.DIRECT.get();

    @Replicate
    public String hurtingType = "damage";
}
