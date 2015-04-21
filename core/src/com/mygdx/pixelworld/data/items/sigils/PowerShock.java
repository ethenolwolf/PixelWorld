package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Mana sigil dealing damage with an explosion.
 */
public class PowerShock extends ManaSigil {

    private final List<PowerShockBlast> blasts = new ArrayList<>();
    private TextureRegion texture;

    public PowerShock(Player player) {
        damage = 100;
        price = 40;
        Game.assetManager.load("core/assets/mana/powerShock.png", Texture.class);
        img = new StaticDrawData(AssetType.SIGIL, "powerShock");
        name = SigilName.powerShock;
        empty = false;
        gameClass = player.getGameClass();
    }

    @Override
    public void activate(Vector2 center) {
        float minScale = 0.1f;
        blasts.add(new PowerShockBlast(center, minScale));
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (PowerShockBlast blast : blasts) {
            batch.draw(texture, blast.getCenter().x - (texture.getRegionWidth() / 2), blast.getCenter().y - texture.getRegionHeight() / 2, texture.getRegionWidth() / 2,
                    texture.getRegionHeight() / 2, texture.getRegionWidth(), texture.getRegionHeight(), blast.getCurrentDimension().x, blast.getCurrentDimension().y, 0);
        }
    }

    @Override
    public void update() {
        if (texture == null)
            texture = new TextureRegion(Game.assetManager.get("core/assets/mana/powerShock.png", Texture.class));
        ListIterator<PowerShockBlast> iterator = blasts.listIterator();
        while (iterator.hasNext()) {
            PowerShockBlast b = iterator.next();
            float maxScale = 1f;
            float step = 0.01f;
            if (!b.update(maxScale, step)) iterator.remove();
        }
    }

    private BoundingShape[] getBoundingShape() {
        BoundingShape[] boundingShapes = new BoundingShape[blasts.size()];
        for (int i = 0; i < boundingShapes.length; i++)
            boundingShapes[i] = blasts.get(i).getBoundingShape(texture.getRegionWidth() / 2);
        return boundingShapes;
    }

    @Override
    public boolean checkIfInside(Enemy e) {
        for (BoundingShape boundingShape : getBoundingShape())
            if (BoundingShape.intersect(boundingShape, e.getBoundingShape())) return true;
        return false;
    }

    @Override
    public boolean isSuitable(GameClasses gameClass) {
        return gameClass == GameClasses.WIZARD;
    }

    private class PowerShockBlast {
        private final Vector2 center;
        private final Vector2 currentDimension;

        public PowerShockBlast(Vector2 center, float minScale) {
            this.center = center;
            this.currentDimension = new Vector2(minScale, minScale);
        }

        public Vector2 getCurrentDimension() {
            return currentDimension;
        }

        public Vector2 getCenter() {
            return center;
        }

        public BoundingShape getBoundingShape(float originalRadius) {
            BoundingShape out = new BoundingCircle(new Vector2(center), (int) (currentDimension.x * originalRadius));
            if (out.isValid()) return out;
            Logger.log("PowerShockBlast.getBoundingShape()", "Circle not valid!");
            return null;
        }


        public boolean update(float maxScale, float step) {
            currentDimension.add(new Vector2(step, step));
            return currentDimension.x <= maxScale;
        }
    }
}
