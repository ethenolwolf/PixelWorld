package com.mygdx.pixelworld.data.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.utilities.Damaging;
import com.mygdx.pixelworld.data.utilities.EntityStats;

public abstract class Entity {
    protected Vector2 pos;
    protected DrawData img;
    protected EntityStats stats;

    public Vector2 getPos() {
        return new Vector2(pos);
    }

    public EntityStats getStats() {
        return stats;
    }

    protected void getHit(int damage) {
        stats.getHit(this, damage);
    }

    public void getHit(Damaging d) {
        stats.getHit(this, d.getDamage());
    }

    public DrawData getImg() {
        return img;
    }
}
