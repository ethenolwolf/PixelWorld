package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.classes.Wizard;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.ManaPower;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Map {

    private static Vector2 offset = new Vector2();
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private List<ManaPower> manaPowers = new ArrayList<ManaPower>();

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
                        //System.out.println("Inside tha enemy");
                        e.getHit(b);
                        b.die();
                    }
                }
            } else {
                if (player.checkIfInside(b)) {
                    player.getHit(b);
                    b.die();
                }
            }

            if (!b.isAlive()) bulletIterator.remove();
        }

        for (ManaPower mp : manaPowers) {
            enemyIterator = enemies.listIterator();
            if (enemyIterator.hasNext()) {
                Enemy e = enemyIterator.next();
                if (mp.checkIfInside(e)) e.getHit(mp);
            }

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

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.getTexture(AssetType.BACKGROUND, Map.class), offset.x, offset.y);
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) b.draw(batch);
        ListIterator iterator = manaPowers.listIterator();
        while (iterator.hasNext()) {
            ManaPower mp = (ManaPower) iterator.next();
            if (!mp.draw(batch)) iterator.remove();
        }
    }

    public void fire(Vector2 startPos, Vector2 targetPos, Class type) {
        bullets.add(new Bullet(startPos, targetPos, type));
    }

    public void manaFire(Class type, float minScale, float maxScale, float step, Vector2 center) {
        manaPowers.add(new ManaPower(type, minScale, maxScale, step, center));
    }

    public void shapeDraw(ShapeRenderer shapeRenderer, Player player) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.678f, 0.074f, 0.074f, 1.0f);
        shapeRenderer.rect(0, Gdx.graphics.getHeight() - 20, Gdx.graphics.getWidth() / 2, 20);
        shapeRenderer.setColor(0.95f, 0.05f, 0.05f, 1.0f);
        shapeRenderer.rect(0, Gdx.graphics.getHeight() - 20, Gdx.graphics.getWidth() / 2 * player.getHealthPercentage(), 20);

        shapeRenderer.setColor(0.0f, 0.0f, 0.392f, 1.0f);
        shapeRenderer.rect(0, Gdx.graphics.getHeight() - 40, Gdx.graphics.getWidth() / 2, 20);
        shapeRenderer.setColor(0.0f, 0.05f, 0.95f, 1.0f);
        shapeRenderer.rect(0, Gdx.graphics.getHeight() - 40, Gdx.graphics.getWidth() / 2 * player.getManaPercentage(), 20);
        shapeRenderer.end();
    }
}
