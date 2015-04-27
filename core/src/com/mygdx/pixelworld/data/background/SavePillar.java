package com.mygdx.pixelworld.data.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

public class SavePillar extends Item {

    private final Vector2 pos;
    private final AnimationDrawData aura;
    private Vector2 auraPos;

    public SavePillar(Rectangle position) {
        pos = new Vector2(position.x, position.y);
        img = new AnimationDrawData("core/assets/background/savePillar/", new String[]{"pillar"}, 8, 8, BoundingRect.class);
        aura = new AnimationDrawData("core/assets/background/savePillar/", new String[]{"aura"}, 8, 8, BoundingCircle.class);
    }

    public static void save() {
        Logger.log("SavePillar.save()", "Saving...");
    }

    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }

    public void update() {
        if (auraPos == null) init();
        img.update();
        aura.update();
    }

    private void init() {
        auraPos = new Vector2(pos.x - aura.getWidth() / 2 + img.getWidth() / 2 - 5, pos.y - aura.getHeight() / 2 + 6);
    }

    public void draw(SpriteBatch batch) {
        aura.draw(batch, auraPos);
        img.draw(batch, pos);
    }
}
