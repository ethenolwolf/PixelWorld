package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.classes.Wizard;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;
import com.mygdx.pixelworld.data.weapons.WeaponStats;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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

        player.checkMana(enemies);
        DrawHitValue.update();

        updateOffset(player.getPos(), player.getStats());
    }

    private void updateOffset(Vector2 playerPos, EntityStats stats) {
        float of = Game.deltaTime * stats.get(StatType.SPD) * 5;
        Vector2 pp = new Vector2(playerPos);
        if (pp.x + offset.x < Constants.gameWidth * Constants.X_LIMIT_MIN) offset.add(of, 0);
        else if (pp.x + offset.x > Constants.gameWidth * Constants.X_LIMIT_MAX) offset.add(-of, 0);
        if (pp.y + offset.y < Constants.gameHeight * Constants.Y_LIMIT_MIN) offset.add(0, of);
        else if (pp.y + offset.y > Constants.gameHeight * Constants.Y_LIMIT_MAX) offset.add(0, -of);
        limitOffset();
    }

    private void limitOffset() {
        if (offset.x > 0) offset.x = 0;
        if (offset.y > 0) offset.y = 0;
        if (offset.x < -getWidth() + Constants.gameWidth) offset.x = -getWidth() + Constants.gameWidth;
        if (offset.y < -getHeight() + Constants.gameHeight) offset.y = -getHeight() + Constants.gameHeight;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.getTexture(AssetType.BACKGROUND, Map.class), offset.x, offset.y);
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) b.draw(batch);
        DrawHitValue.draw(batch);
    }

    public void fire(Vector2 startPos, Vector2 targetPos, EntityStats es, WeaponStats ws) {
        bullets.add(new Bullet(startPos, targetPos, es, ws));
    }

    public void shapeDraw(ShapeRenderer shapeRenderer, Player player) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Panel
        shapeRenderer.setColor(0.2549f, 0.3215f, 0.3340f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth, 0, Constants.panelWidth, Constants.gameHeight);

        //Health bar background
        shapeRenderer.setColor(0.678f, 0.074f, 0.074f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, Constants.gameHeight / 2, 140, 20);
        //Health bar
        shapeRenderer.setColor(0.95f, 0.05f, 0.05f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, Constants.gameHeight / 2, 140 * player.getHealthPercentage(), 20);
        //Mana bar background
        shapeRenderer.setColor(0.0f, 0.0f, 0.392f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, Constants.gameHeight / 2 - 30, 140, 20);
        //Mana bar
        shapeRenderer.setColor(0.0f, 0.05f, 0.95f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, Constants.gameHeight / 2 - 30, 140 * player.getManaPercentage(), 20);

        shapeRenderer.end();
    }

    public void generateEnemies(int enemyNumber) {
        Random random = new Random();
        for (int i = 0; i < enemyNumber; i++)
            addEnemy(Blocker.class, random.nextInt(getWidth()), random.nextInt(getHeight()));
    }
}
