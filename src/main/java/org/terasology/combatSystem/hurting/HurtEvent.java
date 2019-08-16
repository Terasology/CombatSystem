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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.AbstractConsumableEvent;

/**
 * Used to hurt the <b>target</b> entity by the amount specified in {@code HurtingComponent}
 */
public class HurtEvent extends AbstractConsumableEvent {
    
    /**
     * the target entity which is to be hurt.
     */
    private EntityRef target = EntityRef.NULL;

    public HurtEvent() {
        
    }
    
    public HurtEvent(EntityRef entity) {
        this.target = entity;
    }
    
    public EntityRef getTarget() {
        return target;
    }

}
