package com.mygdx.pixelworld.data.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.EntityStats;

public class InvisibleCloack extends ManaSigil {

    private static final float TIME = 5;
    EntityStats playerStats;
    private float currentTime = 0;

    public InvisibleCloack(Player player) {
        super(player);
        playerStats = player.getStats();
    }

    @Override
    public void update() {
        if (playerStats.isVisible()) return;
        currentTime += 0.01f;
        if (currentTime > TIME) playerStats.setIsVisible(true);
    }

    @Override
    public void activate(Vector2 abs) {
        currentTime = 0;
        playerStats.setIsVisible(false);
    }

    @Override
    public boolean checkIfInside(Enemy e) {
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
    }
}
