package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.background.Chest;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.draw.ScreenWriter;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.EmptyItem;
import com.mygdx.pixelworld.data.items.Inventory;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.Direction;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to manage all kind of GUI elements.
 */
public class GUI {

    //Panel
    private static final Vector2[] itemPositions = new Vector2[20];
    private static final Vector2 mousePosition = new Vector2();
    private static final boolean[] isSelected = new boolean[20];
    //Constants
    private final static int LEFT_BORDER = 20;
    private final static int ITEMS_SIZE = 20;
    private final static int SLOT_SIZE = 30;
    private static final Vector2 loadingImagePos = new Vector2(Constants.gameWidth / 2, Constants.gameHeight / 2);
    private static String dialogName = "", dialogSpeech = "";
    //External references
    private static SpriteBatch batch;
    private static Player player;
    private static World world;
    private static Chest chest = null;
    private static Vector2 mouseCatchOffset = new Vector2();
    private static int startSlot;
    //Menu
    private static int currentMenuIndex = 0;
    private static String[] menuOptions = new String[]{
            "NEW GAME",
            "LOAD GAME",
            "HIGH SCORES",
            "SAVE ONLINE",
            "EXIT"
    };
    private static Music menuMusic;
    private static AnimationDrawData loadingImage;

    public static void loadImage() {
        loadingImage = new AnimationDrawData("core/assets/characters/player/", new String[]{"walk"}, 8, 8, BoundingRect.class);
        loadingImage.setScaleFactor(4);
    }

    public static void updateDialog(String owner, String dialog) {
        dialogName = owner;
        dialogSpeech = dialog;
    }

    public static void init(SpriteBatch batch, Player player, World world) {
        GUI.batch = batch;
        GUI.player = player;
        GUI.world = world;
        for (int i = 0; i < 20; i++) {
            if (i < 4)//Equipped
                itemPositions[i] = new Vector2(Constants.gameWidth + LEFT_BORDER + SLOT_SIZE * i, 220);
            else if (i < 12) //Inventory
                itemPositions[i] = new Vector2(Constants.gameWidth + LEFT_BORDER + SLOT_SIZE * (i % 4), 170 - SLOT_SIZE * ((i - 4) / 4));
            else //eventual chest
                itemPositions[i] = new Vector2(Constants.gameWidth + LEFT_BORDER + SLOT_SIZE * (i % 4), 120 - SLOT_SIZE * ((i - 4) / 4));
        }
        clearSelected();
        Game.assetManager.load("core/assets/gui/panel.png", Texture.class);
        Game.assetManager.load("core/assets/background/chest/chest.png", Texture.class);
        Game.assetManager.load("core/assets/gui/dialogPane.png", Texture.class);
    }

    public static void menuLoop() {
        if (menuMusic == null) {
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("core/assets/sounds/heartbeat.mp3"));
            menuMusic.setLooping(true);
            menuMusic.play();
        }
        batch.begin();
        float step = Constants.gameHeight / (2 * (menuOptions.length - 1));
        for (int i = 0; i < menuOptions.length; i++) {
            ScreenWriter.writeOnCenter(batch, menuOptions[i], Constants.gameHeight * 3 / 4 - i * step, i == currentMenuIndex ? Color.YELLOW : Color.WHITE);
        }
        batch.end();
    }

    public static void pauseLoop() {

    }

    public static void splashScreen(ShapeRenderer shapeRenderer, float progress) {
        loadingImage.update();
        batch.begin();
        loadingImage.draw(batch, loadingImagePos);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 0.0f, 0.392f, 1.0f);
        shapeRenderer.rect(50, 50, Constants.gameWidth - 100, 30);
        shapeRenderer.setColor(0.0f, 0.05f, 0.95f, 1.0f);
        shapeRenderer.rect(50, 50, (Constants.gameWidth - 100) * progress, 30);
        shapeRenderer.end();
    }

    public static void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void draw() {
        //batch.draw(Game.assetManager.get("core/assets/gui/panel.png", Texture.class), Constants.gameWidth + World.getCameraOffset().x, World.getCameraOffset().y);
        //drawEquipped();
        //drawInventory();
        //if (chest != null) drawChest();
        if (dialogSpeech != null && dialogName != null) {
            batch.draw(Game.assetManager.get("core/assets/gui/dialogPane.png", Texture.class), 50 + World.getCameraOffset().x, 50 + World.getCameraOffset().y);
            ScreenWriter.write(batch, dialogName, 70, 120, Color.RED);
            ScreenWriter.write(batch, dialogSpeech, 160, 100, Color.BLACK);
        }
    }

    /**
     * Draw equipped items for player
     */
    private static void drawEquipped() {
        if (!isSelected[0]) player.getWeapon().getImg().drawOnScreen(batch, itemPositions[0]);
        else player.getWeapon().getImg().drawOnScreen(batch, new Vector2(mousePosition).add(mouseCatchOffset));

        if (!isSelected[1]) player.getManaSigil().getImg().drawOnScreen(batch, itemPositions[1]);
        else player.getManaSigil().getImg().drawOnScreen(batch, new Vector2(mousePosition).add(mouseCatchOffset));

        if (!isSelected[2]) player.getArmor().getImg().drawOnScreen(batch, itemPositions[2]);
        else player.getArmor().getImg().drawOnScreen(batch, new Vector2(mousePosition).add(mouseCatchOffset));
    }

    /**
     * Draws player's inventory
     */
    private static void drawInventory() {
        Item[] inv = player.getInventory().getItems();
        for (int i = 0; i < inv.length; i++) {
            if (!isSelected[i + 4]) inv[i].getImg().drawOnScreen(batch, itemPositions[i + 4]);
            else inv[i].getImg().drawOnScreen(batch, new Vector2(mousePosition).add(mouseCatchOffset));
        }
    }

    /**
     * Draws chest content if player is on it
     */
    private static void drawChest() {
        Item[] inv = chest.getInventory().getItems();
        for (int i = 0; i < inv.length; i++) {
            if (!isSelected[i + 12]) inv[i].getImg().drawOnScreen(batch, itemPositions[i + 12]);
            else inv[i].getImg().drawOnScreen(batch, new Vector2(mousePosition).add(mouseCatchOffset));
        }
    }

    public static void clickDown(int screenX, int screenY) {
        clearSelected();
        updateMousePosition(screenX, screenY);
        for (int i = 0; i < 20; i++) {
            Vector2 center = new Vector2(itemPositions[i].x + ITEMS_SIZE / 2, itemPositions[i].y + ITEMS_SIZE / 2);
            if (BoundingShape.intersect(new BoundingCircle(center, ITEMS_SIZE / 2), new BoundingCircle(new Vector2(screenX, screenY), 1))) {
                isSelected[i] = true;
                startSlot = i;
                mouseCatchOffset = new Vector2(itemPositions[i].x - screenX, itemPositions[i].y - screenY);
                return;
            }
        }
    }

    public static void updateMousePosition(int screenX, int screenY) {
        mousePosition.x = screenX;
        mousePosition.y = screenY;
    }

    public static void updateChest(Chest c) {
        chest = c;
    }

    public static void clickUp(int screenX, int screenY) {

        if (screenX >= Constants.gameWidth) {
            for (int endSlot = 0; endSlot < 20; endSlot++) {
                Vector2 center = new Vector2(itemPositions[endSlot].x + ITEMS_SIZE / 2, itemPositions[endSlot].y + ITEMS_SIZE / 2);
                if (!BoundingShape.intersect(new BoundingCircle(center, ITEMS_SIZE / 2), new BoundingCircle(new Vector2(screenX, screenY), 1)))
                    continue;

                if (endSlot < 4) {//Equipped
                    if (startSlot < 4) continue;
                    else if (startSlot < 12) player.equip(player.getInventory(), startSlot - 4);
                    else player.equip(chest.getInventory(), startSlot - 12);
                } else if (endSlot < 12) //Inventory
                {
                    if (startSlot < 4) player.unequip(startSlot, player.getInventory(), endSlot - 4);
                    else if (startSlot < 12) player.getInventory().swap(startSlot - 4, endSlot - 4);
                    else Inventory.swap(chest.getInventory(), startSlot - 12, player.getInventory(), endSlot - 4);
                } else //eventual chest
                {
                    if (startSlot < 4) player.unequip(startSlot, chest.getInventory(), endSlot - 12);
                    else if (startSlot < 12)
                        Inventory.swap(player.getInventory(), startSlot - 4, chest.getInventory(), endSlot - 12);
                    else chest.getInventory().swap(startSlot - 12, endSlot - 12);
                }

                clearSelected();
            }
        } else {
            //Drop down
            if (!isSelected[startSlot]) return;
            Item dropItem;
            if (startSlot < 4) dropItem = player.getEquipped().tryReplace(new EmptyItem(), startSlot);
            else if (startSlot < 12) dropItem = player.getInventory().replace(new EmptyItem(), startSlot - 4);
            else dropItem = chest.getInventory().replace(new EmptyItem(), startSlot - 12);

            List<Item> dropList = new ArrayList<>();
            dropList.add(dropItem);

            world.addChest(dropList, player.getPos());
        }
        clearSelected();
    }

    private static void clearSelected() {
        for (int i = 0; i < 20; i++) isSelected[i] = false;
    }

    public static void cursorEvent(Direction direction) {
        switch (direction) {
            case UP:
                if (currentMenuIndex > 0) currentMenuIndex--;
                break;
            case DOWN:
                if (currentMenuIndex < menuOptions.length - 1) currentMenuIndex++;
                break;
            case RIGHT:
                switch (currentMenuIndex) {
                    case 0:
                        Game.gameState = Game.GameStates.GAME;
                        menuMusic.stop();
                        menuMusic.dispose();
                        break;
                    case 1:
                        Game.gameState = Game.GameStates.LOAD;
                        menuMusic.stop();
                        menuMusic.dispose();
                        break;
                    default:
                        menuMusic.stop();
                        menuMusic.dispose();
                        Gdx.app.exit();
                }
        }
    }
}
