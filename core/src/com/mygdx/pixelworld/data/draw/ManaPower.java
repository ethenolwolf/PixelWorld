package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.Damaging;

public class ManaPower extends DrawData implements Damaging {

    Vector2 center;
    float minScale, maxScale, step;
    Class type;

    public ManaPower(Class type, float minScale, float maxScale, float step, Vector2 center) {
        this.type = type;
        this.minScale = minScale;
        this.scaleFactor = new Vector2(minScale, minScale);
        this.step = step;
        this.maxScale = maxScale;
        this.center = center;
        this.rotationAngle = 0;
        this.texture = new TextureRegion(Assets.getTexture(AssetType.MANA, type));
    }

    public boolean draw(SpriteBatch batch) {
        scaleFactor.add(step, step);
        if (scaleFactor.x > maxScale) return false;
        batch.draw(texture, getEffectivePosition(center).x - getWidth() / 2, getEffectivePosition(center).y - getHeight() / 2, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
        return true;
    }

    public BoundingCircle getBoundingCircle() {
        BoundingCircle out = new BoundingCircle(new Vector2(center), Math.max(getWidth() * scaleFactor.x / 2, getHeight() * scaleFactor.x / 2));
        if (out.isValid()) return out;
        Logger.log("[ManaPower.getBoundingCircle()] Circle not valid!");
        return null;
    }

    public boolean checkIfInside(Enemy e) {
        return getBoundingCircle().intersect(e.getBoundingCircle());
    }

    @Override
    public int getDamage() {
        return Constants.MANA_DAMAGE;
    }
}
