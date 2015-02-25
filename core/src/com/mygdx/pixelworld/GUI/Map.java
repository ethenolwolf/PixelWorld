package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.Constants;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Enemy> enemies = new ArrayList<Enemy>();
    private static Vector2 offset = new Vector2();

    public static Vector2 getOffset() {
        return offset;
    }

    public void addEnemy(Class type, float x, float y) {
        if (type == Blocker.class) enemies.add(new Blocker(x, y));
    }

    public void update(Vector2 playerPos) {

        for (Enemy e : enemies) {
            e.update(playerPos);
        }

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float of = Game.deltaTime * Constants.PLAYER_SPEED;

        if (playerPos.x + offset.x < sw * Constants.X_LIMIT_MIN) offset.add(of, 0);
        else if (playerPos.x + offset.x > sw * Constants.X_LIMIT_MAX) offset.add(-of, 0);
        if (playerPos.y + offset.y < sh * Constants.Y_LIMIT_MIN) offset.add(0, of);
        else if (playerPos.y + offset.y > sh * Constants.Y_LIMIT_MAX) offset.add(0, -of);

        if (offset.x > 0) offset.x = 0;
        if (offset.y > 0) offset.y = 0;
        if (offset.x < -Assets.BACKGROUND.getWidth() + sw) offset.x = -Assets.BACKGROUND.getWidth() + sw;
        if (offset.y < -Assets.BACKGROUND.getHeight() + sh) offset.y = -Assets.BACKGROUND.getHeight() + sh;

    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.BACKGROUND, offset.x, offset.y);
        for (Enemy e : enemies) {
            if (e.getPos().x < Gdx.graphics.getWidth() - offset.x && e.getPos().x >= 0 - offset.x &&
                    e.getPos().y < Gdx.graphics.getHeight() - offset.y && e.getPos().y - offset.y >= 0)
                e.draw(batch, offset);
        }
        Assets.font.draw(batch, "OX = " + String.valueOf((int) offset.x) + " OY = " + String.valueOf((int) offset.y), 0, 250);

    }
}
