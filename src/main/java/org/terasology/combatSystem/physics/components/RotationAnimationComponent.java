package org.terasology.combatSystem.physics.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Quat4f;
import org.terasology.network.Replicate;
import org.terasology.reflection.MappedContainer;

import com.google.common.collect.Lists;

public class RotationAnimationComponent implements Component{
    @Replicate
    public List<RotAnimInfo> animInfo = Lists.newArrayList();
    @Replicate
    public AnimMode mode = AnimMode.LOOP;
    @Replicate
    public int currentAnimPointer = 0;
    @Replicate
    public float timeLeft = 0;
    @Replicate
    public RotAnimInfo rotAnim = null;
    @Replicate
    public float startTime = -1;
    @Replicate
    public boolean rotating = false;
    
    @MappedContainer
    public static class RotAnimInfo{
        public Quat4f rotationSpeed;            //speed to rotate by about an axis in rad/sec.
        public float totalTime;
    }
    
    public enum AnimMode{
        ONCE,
        LOOP,
        BOOMRANG
    }

}
