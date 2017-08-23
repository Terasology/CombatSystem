package org.terasology.combatSystem.widgets;

import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.utilities.Assets;

public class ScopeHud extends CoreHudWidget{
    
    @Override
    public void initialise() {
        UIScalableImage scope = find("scope", UIScalableImage.class);
        
        TextureRegion scopeTexture = Assets.getTextureRegion("CombatSystem:scope").get();
        
        scope.setImage(scopeTexture);
        scope.setImageBounds(0, 0, 1152, 720);
    }
}
