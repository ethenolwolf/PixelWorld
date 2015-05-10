package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.StatType;

/**
 * Healing cross is a sigil that can heal the owner.
 */
public class HealingCross extends ManaSigil {

    private final Player player;

    public HealingCross(Player player) {
        damage = 0;
        price = 80;
        name = SigilName.healingCross;
        this.player = player;
        img = new StaticDrawData(AssetType.SIGIL, "healingCross");
        empty = false;
    }

    @Override
    public void activate(Vector2 abs) {
        player.getStats().addStat(StatType.HEALTH, 100);
        DrawHitValue.add(player, -100);
    }

    @Override
    public boolean checkIfInside(Enemy e) {
        return false;
    }
}
