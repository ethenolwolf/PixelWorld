package com.mygdx.pixelworld.data.sigils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PowerShock extends ManaSigil {

    private List<PowerShockBlast> blasts = new ArrayList<PowerShockBlast>();
    private TextureRegion texture;

    public PowerShock() {
        damage = 100;
        price = 40;
        texture = new TextureRegion(new Texture("core/assets/Mana/powerShock.png"));
    }

    @Override
    public void activate(Vector2 center) {
        float minScale = 0.1f;
        blasts.add(new PowerShockBlast(center, minScale));
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (PowerShockBlast blast : blasts) {
            batch.draw(texture, (blast.getCenter().x + Map.getOffset().x) - (texture.getRegionWidth() / 2), blast.getCenter().y + Map.getOffset().y - texture.getRegionHeight() / 2, texture.getRegionWidth() / 2,
                    texture.getRegionHeight() / 2, texture.getRegionWidth(), texture.getRegionHeight(), blast.getCurrentDimension().x, blast.getCurrentDimension().y, 0);
        }
    }

    @Override
    public void update() {
        ListIterator<PowerShockBlast> iterator = blasts.listIterator();
        while (iterator.hasNext()) {
            PowerShockBlast b = iterator.next();
            float maxScale = 1f;
            float step = 0.1f;
            if (!b.update(maxScale, step)) iterator.remove();
        }
    }

    public BoundingCircle[] getBoundingCircle() {
        BoundingCircle[] bc = new BoundingCircle[blasts.size()];
        for (int i = 0; i < bc.length; i++) bc[i] = blasts.get(i).getBoundingCircle(texture.getRegionWidth());
        return bc;
    }

    @Override
    public boolean checkIfInside(Enemy e) {
        for (BoundingCircle bc : getBoundingCircle())
            if (bc.intersect(e.getBoundingCircle())) return true;
        return false;
    }

    private class PowerShockBlast {
        private Vector2 center;
        private Vector2 currentDimension;

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

        public BoundingCircle getBoundingCircle(float originalRadius) {
            BoundingCircle out = new BoundingCircle(new Vector2(center), currentDimension.x * originalRadius);
            if (out.isValid()) return out;
            Logger.log("[PowerShockBlast.getBoundingCircle()] Circle not valid!");
            return null;
        }


        public boolean update(float maxScale, float step) {
            currentDimension.add(new Vector2(step, step));
            return currentDimension.x <= maxScale;
        }
    }
}
