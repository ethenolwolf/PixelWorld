package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
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
import com.mygdx.pixelworld.debug.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class World implements Disposable {

    private static TiledMapRenderer tiledMapRenderer;
    private static TiledMap tiledMap;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Chest> chests = new ArrayList<>();

    public World() {
        tiledMap = new TmxMapLoader().load("core/assets/map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //for(int i = 0; i < tiledMap.getLayers().getCount(); i++) System.out.println("Layer " + i + " loaded, name = " + tiledMap.getLayers().get(i).getName());
    }

    public static Vector2 getCameraOffset() {
        Vector3 tmp = Game.camera.position;
        return new Vector2(tmp.x - (Constants.panelWidth + Constants.gameWidth)/2, tmp.y - Constants.gameHeight/2);
    }

    public static int getWidth() {
        int tileNumber = tiledMap.getProperties().get("width", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        return tileNumber * tileWidth;
    }

    public static int getHeight() {
        int tileNumber = tiledMap.getProperties().get("height", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        return tileNumber * tileHeight;
    }

    private void addEnemy(Class enemyType, float x, float y) {
        Enemy e;
        if (enemyType == Blocker.class) e = new Blocker(x, y);
        else e = new Blocker(x,y);
        enemies.add(e);
        Debug.addDebuggable(e);
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
        float of = Gdx.graphics.getDeltaTime() * stats.get(StatType.SPD) * 5; //Move with player's same speed
        Vector2 newPos = new Vector2(Game.camera.position.x, Game.camera.position.y);

        float t = Constants.gameWidth + Constants.panelWidth;
        float h = Constants.gameHeight;

        if      (playerPos.x - Game.camera.position.x + t/2 < Constants.X_LIMIT_MIN) newPos.add(-of, 0);
        else if (playerPos.x - Game.camera.position.x + t/2 > Constants.X_LIMIT_MAX) newPos.add(of, 0);

        if      (playerPos.y - Game.camera.position.y + h/2 < Constants.Y_LIMIT_MIN) newPos.add(0, -of);
        else if (playerPos.y - Game.camera.position.y + h/2 > Constants.Y_LIMIT_MAX) newPos.add(0, of);

        //Limit offset
        if(newPos.x < t/2) newPos.x = t/2;
        if(newPos.x > getWidth() + Constants.panelWidth - t/2) newPos.x = getWidth() + Constants.panelWidth - t/2;
        if(newPos.y < h/2) newPos.y = h/2;
        if(newPos.y > getHeight() - h/2) newPos.y = getHeight() - h/2;

        Game.camera.position.set(newPos.x, newPos.y, 0);
        Game.camera.update();
    }

    public void drawBottom(SpriteBatch batch, OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(new int[]{0, 1});
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Enemy e : enemies) e.draw(batch);
        for (Bullet b : bullets) b.draw(batch);
        for (Chest c : chests) c.draw(batch);
        DrawHitValue.draw(batch);
    }

    public void drawTop(){
        tiledMapRenderer.render(new int[]{3});
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

        if (Debug.valueOf("SHOW_BOUNDING")) {
            Vector2 pos = player.getPos();
            shapeRenderer.circle(pos.x, pos.y, 2);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            BoundingCircle bc = player.getImg().getBoundingCircle(player.getPos());
            shapeRenderer.circle(bc.getCenter().x, bc.getCenter().y, bc.getRadius());

            shapeRenderer.setColor(1.0f, 0, 0, 1.0f);
            for (Bullet b : bullets) {
                Vector2 center = b.getBoundingCircle().getCenter();
                shapeRenderer.circle(center.x + getCameraOffset().x, center.y + getCameraOffset().y, b.getBoundingCircle().getRadius());
            }

            shapeRenderer.setColor(0f, 1.0f, 0, 1.0f);
            for (Enemy e : enemies) {
                Vector2 center = e.getBoundingCircle().getCenter();
                shapeRenderer.circle(center.x + getCameraOffset().x, center.y + getCameraOffset().y, e.getBoundingCircle().getRadius());
            }

            shapeRenderer.setColor(1.0f, 1.0f, 0, 1.0f);
            if (player.getManaSigil() instanceof PowerShock)
                for (BoundingCircle bdc : ((PowerShock) player.getManaSigil()).getBoundingCircle()) {
                    Vector2 center = bdc.getCenter();
                    shapeRenderer.circle(center.x + getCameraOffset().x, center.y + getCameraOffset().y, bdc.getRadius());
                }
        }

        if (Debug.valueOf("SHOW_OFFSET_TRIGGERS")) {
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.line(0, Constants.Y_LIMIT_MAX, Constants.gameWidth,Constants.Y_LIMIT_MAX);
            shapeRenderer.line(0, Constants.Y_LIMIT_MIN, Constants.gameWidth,Constants.Y_LIMIT_MIN);
            shapeRenderer.line(Constants.X_LIMIT_MAX, 0, Constants.X_LIMIT_MAX, Constants.gameHeight);
            shapeRenderer.line(Constants.X_LIMIT_MIN, 0, Constants.X_LIMIT_MIN, Constants.gameHeight);
        }

        shapeRenderer.end();
    }

    public void generateEnemies(int enemyNumber) {
        Random random = new Random();
        for (int i = 0; i < enemyNumber; i++)
            addEnemy(Blocker.class, random.nextInt(getWidth()), random.nextInt(getHeight()));
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        for (Enemy e : enemies) e.dispose();
        for (Bullet b : bullets) b.dispose();
        for (Chest c : chests) c.dispose();
    }
}
