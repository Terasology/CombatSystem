package org.terasology.combatSystem.widgets;

import org.terasology.math.geom.Rect2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.ScaleMode;
import org.terasology.rendering.nui.widgets.UIImage;

public class UIScalableImage extends UIImage{
    private Rect2i imageBounds;
    
    public void setImageBounds(int x, int y, int width, int height){
        imageBounds = Rect2i.createFromMinAndSize(x, y, width, height);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        if (getImage() != null) {
            canvas.drawTexture(getImage());
        }
    }

}
