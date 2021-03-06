package com.mygdx.pixelworld.data;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.background.Chest;
import com.mygdx.pixelworld.data.background.SavePillar;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Blocker;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;
import com.mygdx.pixelworld.data.story.Story;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.Utils;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;
import com.mygdx.pixelworld.data.utilities.bounding.ExitBoundingRect;
import com.mygdx.pixelworld.debug.Debug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Class for managing map loading / world update and draw.
 */
public class World {

    private static final List<Enemy> enemies = new ArrayList<>();
    private static final List<Bullet> bullets = new ArrayList<>();
    private static final List<Chest> chests = new ArrayList<>();
    private static final List<BoundingRect> mapObstacles = new ArrayList<>();
    private static final List<ExitBoundingRect> exits = new ArrayList<>();
    private static final List<BoundingRect> spawnPoints = new ArrayList<>();
    private static final List<SavePillar> savePillars = new ArrayList<>();
    private static final int[] backgroundLayers = {0, 1, 2};
    private static final int[] foregroundLayers = {3};
    private static final Player player = new Player();
    private static final Logger logger = LogManager.getLogger();
    private static GameMap gameMap;
    private static Story story;

    public static void init() {
        Game.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        loadNewMap("core/assets/maps/start.tmx");
        new Blocker(0, 0);
        new SavePillar(new Rectangle());
    }

    /**
     * Loads new map's obstacles, exits, spawn monsters etc.
     *
     * @param mapPath File path of map file
     */
    private static void loadNewMap(String mapPath) {
        logger.trace("Loading map " + mapPath);
        enemies.clear();
        bullets.clear();
        chests.clear();
        mapObstacles.clear();
        exits.clear();
        savePillars.clear();
        spawnPoints.clear();
        story = null;
        if (gameMap != null) gameMap.dispose();
        gameMap = null;
        GameMap.setCurrentMap(mapPath);
        Game.assetManager.load(mapPath, TiledMap.class);
    }

    /**
     * When map is loaded reads all data and initializes the map.
     */
    private static void initMap() {
        logger.trace("Map loaded, initializing");
        gameMap = new GameMap(Game.assetManager.get(GameMap.getCurrentMap(), TiledMap.class));
        int x = 100;
        int y = 100;

        try {
            x = gameMap.getIntProperty("PlayerPositionX") * gameMap.getTileWidth();
            y = gameMap.getIntProperty("PlayerPositionY") * gameMap.getTileHeight();
        } catch (NumberFormatException e) {
            logger.warn("Player position not found", e);
        }
        player.setInitialPos(x, y);
        CameraManager.setOffset(player.getPos());

        if (gameMap.getLayer("Collisions") != null)
            for (RectangleMapObject rectangle : gameMap.getRectangularObjects("Collisions"))
                mapObstacles.add(new BoundingRect(rectangle.getRectangle()));
        else logger.warn("Collisions layer not found");

        if (gameMap.getLayer("Exits") != null)
            for (RectangleMapObject rectangle : gameMap.getRectangularObjects("Exits"))
                exits.add(new ExitBoundingRect(rectangle.getRectangle(), (String) rectangle.getProperties().get("NextMap")));
        else logger.warn("Exits layer not found");

        if (gameMap.getLayer("Spawn") != null) {
            for (RectangleMapObject rectangle : gameMap.getRectangularObjects("Spawn")) {
                String type = rectangle.getProperties().get("type", String.class);
                int number = Integer.parseInt(rectangle.getProperties().get("Number", String.class));
                generateEnemies(rectangle.getRectangle(), type, number);
                spawnPoints.add(new BoundingRect(rectangle.getRectangle()));
            }
        } else logger.warn("Spawn layer not found");

        story = new Story(gameMap.getLayer("NPC"), "core/assets/maps/" + gameMap.getStringProperty("Story"));

        if (gameMap.getLayer("Interactions") != null) {
            for (RectangleMapObject rectangle : gameMap.getRectangularObjects("Interactions")) {
                switch (rectangle.getProperties().get("type", String.class)) {
                    case "savePillar":
                        savePillars.add(new SavePillar(rectangle.getRectangle()));
                        mapObstacles.add(new BoundingRect(rectangle.getRectangle()));
                        break;
                    default:
                        logger.warn("Interaction of type " + rectangle.getProperties().get("type") + " not yet implemented.");
                }
            }
        } else logger.warn("Interactions layer not found");

        logger.info("Loaded %d layers, %d obstacles, %d exits, %d pillars, %d spawn points (%d total enemies).",
                gameMap.getLayerCount(), mapObstacles.size(), exits.size(), savePillars.size(), spawnPoints.size(), enemies.size());

    }

    /**
     * Adds an enemy to the map.
     * @param enemyType Type of enemy
     */
    private static void addEnemy(String enemyType, float x, float y) {
        Enemy e;
        switch (enemyType.toLowerCase()) {
            case "blocker":
                e = new Blocker(x, y);
                break;
            default:
                e = new Blocker(x, y);
                break;
        }
        enemies.add(e);
    }

    /**
     * Adds chest to current map.
     * @param items Items inside the chest
     * @param pos Position of the chest
     */
    private static void addChest(List<Item> items, Vector2 pos) {
        Chest c = new Chest(items, pos);
        for (Chest old : chests)
            if (BoundingShape.intersect(c.getBoundingShape(), old.getBoundingShape())) {
                if (old.move(c)) return;
            }
        chests.add(new Chest(items, pos));
    }

    /**
     * Updates all map component and checks collisions.
     */
    public static void update() {
        player.update();
        if (gameMap == null) initMap();
        BoundingShape playerBoundingShape = player.getBoundingShape();
        story.update();
        for (SavePillar savePillar : savePillars) savePillar.update();
        updateEnemies();
        updateChests();
        updateBullets(playerBoundingShape);
        playerBoundingShape.free();
        player.checkMana(enemies);
        DrawHitValue.update();
        CameraManager.updateOffset(player.getPos(), player.getStats());
    }

    private static void updateBullets(BoundingShape playerBoundingShape) {
        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        ListIterator<Enemy> enemyIterator;
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update(mapObstacles);
            BoundingShape bulletBoundingShape = b.getBoundingShape();
            if (b.isPlayer()) {
                enemyIterator = enemies.listIterator();
                while (enemyIterator.hasNext()) {
                    Enemy e = enemyIterator.next();
                    BoundingShape enemyBoundingShape = e.getBoundingShape();
                    if (BoundingShape.intersect(enemyBoundingShape, bulletBoundingShape)) {
                        e.getHit(b);
                        b.die();
                    }
                    enemyBoundingShape.free();
                }
            } else {
                if (BoundingShape.intersect(playerBoundingShape, bulletBoundingShape)) {
                    player.getHit(b);
                    b.die();
                }
            }
            bulletBoundingShape.free();
            if (!b.isAlive()) bulletIterator.remove();
        }
    }

    private static void updateChests() {
        ListIterator<Chest> chestIterator = chests.listIterator();
        while (chestIterator.hasNext()) {
            Chest c = chestIterator.next();
            BoundingShape boundingShape = c.getBoundingShape();
            if (c.isEmpty()) {
                chestIterator.remove();
            }
            boundingShape.free();
        }
    }

    private static void updateEnemies() {
        ListIterator<Enemy> enemyIterator = enemies.listIterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            e.update();
            if (!e.isAlive()) {
                player.addExperience(e.getExperience());
                addChest(e.getDropItems(), e.getPos());
                enemyIterator.remove();
            }
        }
    }

    /**
     * Adds a bullet
     * @param startPos Bullet starting position
     * @param targetPos Bullet target
     * @param es Stats of firing entity
     * @param ws Stats of firing weapon
     */
    public static void fire(Vector2 startPos, Vector2 targetPos, EntityStats es, WeaponStats ws) {
        bullets.add(new Bullet(startPos, targetPos, es, ws));
    }

    /**
     * Draws shapes for GUI.
     */
    public static void shapeDraw() {
        if (Debug.isTrue("SHOW_BOUNDING")) {
            DrawManager.changeType(ShapeRenderer.ShapeType.Line);
            //Player
            DrawManager.setColor(DrawManager.Type.SHAPE, 1, 0, 1, 1);
            player.getBoundingShape().draw();
            //Enemies
            DrawManager.setColor(DrawManager.Type.SHAPE, 1, 0, 0, 1);
            for (Enemy e : enemies) e.getBoundingShape().draw();
            //Bullets
            DrawManager.setColor(DrawManager.Type.SHAPE, 0, 0, 1, 1);
            for (Bullet b : bullets) b.getBoundingShape().draw();
            //Chests
            DrawManager.setColor(DrawManager.Type.SHAPE, 0, 1, 1, 1);
            for (Chest c : chests) c.getBoundingShape().draw();
            //Obstacles
            DrawManager.setColor(DrawManager.Type.SHAPE, 1, 1, 0, 1);
            for (BoundingRect rect : mapObstacles) rect.drawOnScreen();
            //Exits
            DrawManager.setColor(DrawManager.Type.SHAPE, 0, 1, 0, 1);
            for (ExitBoundingRect rect : exits) rect.drawOnScreen();
            //SpawnPoints
            DrawManager.setColor(DrawManager.Type.SHAPE, 0.5f, 1, 0, 1);
            for (BoundingRect rect : spawnPoints) rect.drawOnScreen();
            //SavePillars
            DrawManager.setColor(DrawManager.Type.SHAPE, 1f, 0.5f, 0, 1);
            for (SavePillar savePillar : savePillars)
                savePillar.getBoundingShape().drawOnScreen();

        }

        if (Debug.isTrue("SHOW_OFFSET_TRIGGERS")) {
            DrawManager.setColor(DrawManager.Type.SHAPE, 0, 0, 1, 1);
            DrawManager.line(0, Constants.Y_LIMIT_MAX, Constants.gameWidth, Constants.Y_LIMIT_MAX);
            DrawManager.line(0, Constants.Y_LIMIT_MIN, Constants.gameWidth, Constants.Y_LIMIT_MIN);
            DrawManager.line(Constants.X_LIMIT_MAX, 0, Constants.X_LIMIT_MAX, Constants.gameHeight);
            DrawManager.line(Constants.X_LIMIT_MIN, 0, Constants.X_LIMIT_MIN, Constants.gameHeight);
        }

        DrawManager.end();
    }

    /**
     * Generate enemies at map loading.
     *
     * @param spawnArea   Spawn area
     * @param type        Type of enemy
     * @param enemyNumber Number of enemies
     */
    private static void generateEnemies(Rectangle spawnArea, String type, int enemyNumber) {
        Random random = new Random();
        for (int i = 0; i < enemyNumber; i++) {
            int x = (int) (random.nextInt((int) (spawnArea.width)) + spawnArea.x);
            int y = (int) (random.nextInt((int) (spawnArea.height)) + spawnArea.y);
            addEnemy(type, x, y);
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public static void dispose() {
        if (gameMap != null) gameMap.dispose();
        for (Enemy e : enemies) e.dispose();
        for (Bullet b : bullets) b.dispose();
        for (Chest c : chests) c.dispose();
        player.dispose();
    }

    public static List<BoundingRect> getMapObstacles() {
        return mapObstacles;
    }

    public static void interaction() {
        for (SavePillar savePillar : savePillars)
            if (BoundingShape.intersect(player.getBoundingShape(), savePillar.getBoundingShape()))
                SavePillar.save();
        for (ExitBoundingRect exit : exits) {
            if (BoundingShape.intersect(player.getBoundingShape(), exit)) {
                loadNewMap(exit.getNextMap());
                return;
            }
        }
    }

    public static void load() {
        String[] saves = Utils.readSave();
        if (saves == null) {
            logger.info("Save file is empty");
            return;
        }
        if (saves[0] == null || saves[0].equals("")) {
            logger.info("Save file corrupted!");
            return;
        }
        loadNewMap(saves[0]);
        player.load();
    }

    public static void manaTrigger() {
        player.manaTrigger();
    }

    public static void playerStartFire(int targetX, int targetY) {
        player.getFireManager().setIsFiring(true);
        playerUpdateTarget(targetX, targetY);
    }

    public static void playerStopFire() {
        player.getFireManager().setIsFiring(false);
    }

    public static void playerUpdateTarget(int targetX, int targetY) {
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) return;
        player.getFireManager().setTarget(targetX + offset.x, targetY + offset.y);
    }

    public static void draw() {
        CameraManager.update(gameMap.getRenderer());
        DrawManager.end();
        gameMap.render(backgroundLayers);
        if (!Game.assetManager.update()) return;
        for (Enemy e : enemies) e.draw();
        for (Bullet b : bullets) b.draw();
        for (Chest c : chests) c.draw();
        player.draw();
        story.draw();
        DrawHitValue.draw();
        DrawManager.end();
        gameMap.render(foregroundLayers);
        for (SavePillar savePillar : savePillars) {
            savePillar.draw();
            if (BoundingShape.intersect(player.getBoundingShape(), savePillar.getBoundingShape()))
                DrawManager.write("Press " + Constants.INTERACTION_KEY + " to save...", 10, 30, Color.GREEN);
        }
        for (ExitBoundingRect e : exits)
            if (BoundingShape.intersect(player.getBoundingShape(), e)) {
                DrawManager.write("Press " + Constants.INTERACTION_KEY + " to exit...", 10, 30, Color.GREEN);
            }
    }

    public static float getWidth() {
        return gameMap.getWidth();
    }

    public static float getHeight() {
        return gameMap.getHeight();
    }
}