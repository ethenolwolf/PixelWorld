package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.AssetsManagement.AssetType;
import com.mygdx.pixelworld.data.AssetsManagement.Assets;
import com.mygdx.pixelworld.data.Bullet;
import com.mygdx.pixelworld.data.Constants;
import com.mygdx.pixelworld.data.Enemies.Blocker;
import com.mygdx.pixelworld.data.Enemies.Enemy;
import com.mygdx.pixelworld.data.GameClasses.Player;
import com.mygdx.pixelworld.data.GameClasses.Wizard;

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

    public static int getWidth() {
        return Assets.getTexture(AssetType.BACKGROUND, Map.class).getWidth();
    }

    public static int getHeight() {
        return Assets.getTexture(AssetType.BACKGROUND, Map.class).getHeight();
    }

    public void addEnemy(Class<Blocker> type, float x, float y) {
        if (type == Blocker.class) enemies.add(new Blocker(x, y));
    }

    public void update(Player player) {
        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            e.update(player.getPos(), this);
            if (!e.isAlive()) enemyIterator.remove();
        }

        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update();

            if (b.getType() == Wizard.class) {
                enemyIterator = enemies.listIterator();
                while (enemyIterator.hasNext()) {
                    Enemy e = enemyIterator.next();
                    if (e.checkIfInside(b)) {
                        System.out.println("Inside tha enemy");
                        e.getHit(b);
                        b.die();
                    }
                }
            } else {
                if (player.checkIfInside(b)) {
                    System.out.println("Inside tha player");
                    player.getHit(b);
                    b.die();
                }
            }

            if (!b.isAlive()) bulletIterator.remove();
        }

        updateOffset(player.getPos());
    }

    private void updateOffset(Vector2 playerPos) {
        float of = Game.deltaTime * Constants.PLAYER_SPEED;
        Vector2 pp = new Vector2(playerPos);
        if (pp.x + offset.x < Gdx.graphics.getWidth() * Constants.X_LIMIT_MIN) offset.add(of, 0);
        else if (pp.x + offset.x > Gdx.graphics.getWidth() * Constants.X_LIMIT_MAX) offset.add(-of, 0);
        if (pp.y + offset.y < Gdx.graphics.getHeight() * Constants.Y_LIMIT_MIN) offset.add(0, of);
        else if (pp.y + offset.y > Gdx.graphics.getHeight() * Constants.Y_LIMIT_MAX) offset.add(0, -of);

        limitOffset();
    }

    private void limitOffset() {
        if (offset.x > 0) offset.x = 0;
        if (offset.y > 0) offset.y = 0;
        if (offset.x < -getWidth() + Gdx.graphics.getWidth()) offset.x = -getWidth() + Gdx.graphics.getWidth();
        if (offset.y < -getHeight() + Gdx.graphics.getHeight()) offset.y = -getHeight() + Gdx.graphics.getHeight();
    }

    public void draw(SpriteBatch batch, Player player) {
        batch.draw(Assets.getTexture(AssetType.BACKGROUND, Map.class), offset.x, offset.y);
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) b.draw(batch);

        Assets.write(batch, "PX = " + (int) player.getPos().x + "  PY = " + (int) player.getPos().y, 0, 450);
        Assets.write(batch, "EX = " + (int) player.getPos().x + "  EY = " + (int) player.getPos().y, 0, 430);
        if (enemies.size() > 0)
            Assets.write(batch, "ENX = " + (int) enemies.get(0).getPos().x + "  ENY = " + (int) enemies.get(0).getPos().y, 0, 410);
        if (bullets.size() > 0)
            Assets.write(batch, "BX = " + (int) bullets.get(0).getPos().x + "  BY = " + (int) bullets.get(0).getPos().y, 0, 390);
    }

    public void fire(Vector2 startPos, Vector2 targetPos, Class type) {
        bullets.add(new Bullet(startPos, targetPos, type));
    }
}
