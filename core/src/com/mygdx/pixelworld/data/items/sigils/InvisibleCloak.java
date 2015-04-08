package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
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
        img = new StaticDrawData(AssetType.SIGIL, "invisibleCloak");
        gameClass = player.getGameClass();
        empty = false;
    }

    @Override
    public void update() {
        if (playerStats.isVisible()) return;
        currentTime += Gdx.graphics.getDeltaTime();
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
}
