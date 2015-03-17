package com.mygdx.pixelworld.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.DebugOptions;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Blocker;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Chest;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.sigils.PowerShock;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Map {


    private final static Vector2 offset = new Vector2();
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private final List<Bullet> bullets = new ArrayList<Bullet>();
    private final List<Chest> chests = new ArrayList<Chest>();

    public static Vector2 getOffset() {
        return offset;
    }

    public static int getWidth() {
        return Assets.getTexture(AssetType.BACKGROUND, Map.class).getWidth();
    }

    public static int getHeight() {
        return Assets.getTexture(AssetType.BACKGROUND, Map.class).getHeight();
    }

    void addEnemy(Class<Blocker> type, float x, float y) {
        if (type == Blocker.class) enemies.add(new Blocker(x, y));
    }

    public void addChest(List<Item> items, Vector2 pos) {
        chests.add(new Chest(items, pos));
    }

    public void update(Player player) {
        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            e.update(player, this);
            if (!e.isAlive()) {
                player.addExperience(e.getExperience());
                addChest(e.getDropItems(), e.getPos());
                enemyIterator.remove();
            }
        }

        GUI.updateChest(null);
        ListIterator<Chest> chestIterator = chests.listIterator();
        while (chestIterator.hasNext()) {
            Chest c = chestIterator.next();
            if (c.checkIfInside(player)) GUI.updateChest(c);
            if (c.isEmpty()) {
                chestIterator.remove();
            }
        }

        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update();
            if (b.isPlayer()) {
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
        for (Chest c : chests) c.draw(batch);
        DrawHitValue.draw(batch);
    }

    public void fire(Vector2 startPos, Vector2 targetPos, EntityStats es, WeaponStats ws) {
        bullets.add(new Bullet(startPos, targetPos, es, ws));
    }

    public void shapeDraw(ShapeRenderer shapeRenderer, Player player) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Health bar background
        shapeRenderer.setColor(0.678f, 0.074f, 0.074f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 330, 140, 20);
        //Health bar
        shapeRenderer.setColor(0.95f, 0.05f, 0.05f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 330, 140 * player.getHealthPercentage(), 20);
        //Mana bar background
        shapeRenderer.setColor(0.0f, 0.0f, 0.392f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 300, 140, 20);
        //Mana bar
        shapeRenderer.setColor(0.0f, 0.05f, 0.95f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 300, 140 * player.getManaPercentage(), 20);
        //Exp bar background
        shapeRenderer.setColor(0.125f, 0.321f, 0.095f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 270, 140, 20);
        //Exp bar
        shapeRenderer.setColor(0.313f, 0.800f, 0.214f, 1.0f);
        shapeRenderer.rect(Constants.gameWidth + 10, 270, 140 * player.getExpPercentage(), 20);

        if (DebugOptions.SHOW_BOUNDING) {
            DrawData img = player.getImg();
            Vector2 pos = img.getEffectivePosition(img.getOriginalPosition(player.getPos()));
            shapeRenderer.circle(pos.x, pos.y, 2);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            BoundingCircle bc = player.getImg().getBoundingCircle(player.getPos());
            shapeRenderer.circle(bc.getCenter().x + Map.getOffset().x, bc.getCenter().y + Map.getOffset().y, bc.getRadius());

            shapeRenderer.setColor(1.0f, 0, 0, 1.0f);
            for (Bullet b : bullets) {
                Vector2 center = b.getBoundingCircle().getCenter();
                shapeRenderer.circle(center.x + getOffset().x, center.y + getOffset().y, b.getBoundingCircle().getRadius());
            }

            shapeRenderer.setColor(0f, 1.0f, 0, 1.0f);
            for (Enemy e : enemies) {
                Vector2 center = e.getBoundingCircle().getCenter();
                shapeRenderer.circle(center.x + getOffset().x, center.y + getOffset().y, e.getBoundingCircle().getRadius());
            }

            shapeRenderer.setColor(1.0f, 1.0f, 0, 1.0f);
            if (player.getManaSigil() instanceof PowerShock)
                for (BoundingCircle bdc : ((PowerShock) player.getManaSigil()).getBoundingCircle()) {
                    Vector2 center = bdc.getCenter();
                    shapeRenderer.circle(center.x + getOffset().x, center.y + getOffset().y, bdc.getRadius());
                }
        }

        shapeRenderer.end();
    }

    public void generateEnemies(int enemyNumber) {
        Random random = new Random();
        for (int i = 0; i < enemyNumber; i++)
            addEnemy(Blocker.class, random.nextInt(getWidth()), random.nextInt(getHeight()));
    }
}
