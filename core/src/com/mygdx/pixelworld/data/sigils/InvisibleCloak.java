package com.mygdx.pixelworld.data.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.SigilName;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.EntityStats;

public class InvisibleCloak extends ManaSigil {

    private static final float TIME = 5.0f;
    private final EntityStats playerStats;
    private float currentTime = 0;

    public InvisibleCloak(Player player) {
        damage = 0;
        price = 70;
        playerStats = player.getStats();
        name = SigilName.invisibleCloack;
        img = new DrawData(AssetType.SIGIL, player.getClass(), new Vector2(1, 1), 0);
        empty = false;
    }

    @Override
    public void update() {
        if (playerStats.isVisible()) return;
        currentTime += Game.deltaTime;
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
