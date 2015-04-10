package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.StatType;

public class HealingCross extends ManaSigil {

    Player player;

    public HealingCross(Player player) {
        damage = 0;
        price = 80;
        name = SigilName.healingCross;
        this.player = player;
        img = new StaticDrawData(AssetType.SIGIL, "healingCross");
        gameClass = player.getGameClass();
        empty = false;
    }

    @Override
    public void update() {
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

    @Override
    public boolean isSuitable(GameClasses gameClass) {
        return gameClass == GameClasses.CLERIC;
    }
}
