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
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.math.geom.Vector3f;
import org.terasology.network.Replicate;

/**
 * makes the entity explode into another entity.
 * <p>
 * the other entity may be defined in <b>explosionPrefab</b> or <b>explosionEntity</b>.
 * <b>explosionEntity</b> is given precedence over <b>explosionPrefab</b> if both are present.
 */
public class ExplodeComponent implements Component {
    @Replicate
    public Prefab explosionPrefab;
    @Replicate
    public Vector3f impulseDirection;
    @Replicate
    public String damageType;
}
