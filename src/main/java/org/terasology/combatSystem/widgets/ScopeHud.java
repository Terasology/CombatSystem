package org.terasology.combatSystem.widgets;

import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.rendering.nui.widgets.UIImage;
import org.terasology.utilities.Assets;

public class ScopeHud extends CoreHudWidget{
    
    @Override
    public void initialise() {
        UIImage scope = find("scope", UIImage.class);
        
        TextureRegion scopeTexture = Assets.getTextureRegion("CombatSystem:scope").get();
        
        scope.setImage(scopeTexture);
    }
}
