package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.Bullet;
import com.mygdx.pixelworld.data.Constants;
import com.mygdx.pixelworld.data.Player;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Map {

    private static Vector2 offset = new Vector2();
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Bullet> bullets = new ArrayList<Bullet>();

    public static Vector2 getOffset() {
        return offset;
    }

    public void addEnemy(Class type, float x, float y) {
        if (type == Blocker.class) enemies.add(new Blocker(x, y));
    }

    public void update(Player player) {

        ListIterator enemyIterator = enemies.listIterator();
        while (enemyIterator.hasNext()) {
            Enemy e = (Enemy) enemyIterator.next();
            e.update(player.getPos(), this);
            if (!e.isAlive()) enemyIterator.remove();
        }

        ListIterator bulletIterator = bullets.listIterator();

        while (bulletIterator.hasNext()) {
            Bullet b = (Bullet) bulletIterator.next();
            b.update();

            if (b.getType() == Player.class) {
                enemyIterator = enemies.listIterator();
                while (enemyIterator.hasNext()) {
                    Enemy e = (Enemy) enemyIterator.next();
                    if (e.checkIfInside(b)) {
                        e.getHit(b);
                        b.die();
                        break;
                    }
                }
            } else {
                if (player.checkIfInside(b)) {
                    player.getHit(b);
                    b.die();
                    break;
                }
            }

            if (!b.isAlive()) bulletIterator.remove();
        }

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float of = Game.deltaTime * Constants.PLAYER_SPEED;

        if (player.getPos().x + offset.x < sw * Constants.X_LIMIT_MIN) offset.add(of, 0);
        else if (player.getPos().x + offset.x > sw * Constants.X_LIMIT_MAX) offset.add(-of, 0);
        if (player.getPos().y + offset.y < sh * Constants.Y_LIMIT_MIN) offset.add(0, of);
        else if (player.getPos().y + offset.y > sh * Constants.Y_LIMIT_MAX) offset.add(0, -of);

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
        for (Bullet b : bullets) {
            b.draw(batch, offset);
        }
        Assets.font.draw(batch, "OX = " + String.valueOf((int) offset.x) + " OY = " + String.valueOf((int) offset.y), 0, 250);

    }

    public void fire(Vector2 playerPos, Vector2 targetPos, Class type) {
        bullets.add(new Bullet(playerPos, targetPos, type));
    }
}
