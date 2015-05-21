package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.CameraManager;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.Direction;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;

/**
 * Class used to manage all kind of GUI elements.
 */
public class GUI {

    //Constants
    private static final Vector2 loadingImagePos = new Vector2(Constants.gameWidth / 2, Constants.gameHeight / 2);
    private static final String[] menuOptions = new String[]{
            "NEW GAME",
            "LOAD GAME",
            "HIGH SCORES",
            "SAVE ONLINE",
            "EXIT"
    };
    private static String dialogName = "", dialogSpeech = "";
    //Menu
    private static int currentMenuIndex = 0;
    private static Music menuMusic;
    private static AnimationDrawData loadingImage;

    public static void loadImage() {
        loadingImage = new AnimationDrawData("core/assets/characters/player/", new String[]{"walk"}, BoundingRect.class);
        loadingImage.setScaleFactor(4);
    }

    public static void updateDialog(String owner, String dialog) {
        dialogName = owner;
        dialogSpeech = dialog;
    }

    public static void init() {
        Game.assetManager.load("core/assets/background/chest/chest.png", Texture.class);
        Game.assetManager.load("core/assets/gui/dialogPane.png", Texture.class);
    }

    public static void menuLoop() {
        if (menuMusic == null) {
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("core/assets/sounds/heartbeat.mp3"));
            menuMusic.setLooping(true);
            menuMusic.play();
        }
        float step = Constants.gameHeight / (2 * (menuOptions.length - 1));
        for (int i = 0; i < menuOptions.length; i++) {
            DrawManager.writeOnCenter(menuOptions[i], Constants.gameHeight * 3 / 4 - i * step, i == currentMenuIndex ? Color.YELLOW : Color.WHITE);
        }
    }

    public static void splashScreen(float progress) {
        loadingImage.update();
        loadingImage.draw(loadingImagePos);
        DrawManager.changeType(ShapeRenderer.ShapeType.Filled);
        DrawManager.begin(DrawManager.Type.SHAPE);
        DrawManager.setColor(DrawManager.Type.SHAPE, 0.0f, 0.0f, 0.392f, 1.0f);
        DrawManager.rect(50, 50, Constants.gameWidth - 100, 30);
        DrawManager.setColor(DrawManager.Type.SHAPE, 0.0f, 0.05f, 0.95f, 1.0f);
        DrawManager.rect(50, 50, (Constants.gameWidth - 100) * progress, 30);
        DrawManager.end();
    }

    public static void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void draw() {
        if (dialogSpeech != null && dialogName != null) {
            Vector2 offset = CameraManager.getCameraOffset();
            if (offset == null) return;
            DrawManager.getBatch().draw(Game.assetManager.get("core/assets/gui/dialogPane.png", Texture.class), 50 + offset.x, 50 + offset.y);
            DrawManager.write(dialogName, 70, 120, Color.RED);
            DrawManager.write(dialogSpeech, 160, 100, Color.BLACK);
        }
        if (Game.gameState == Game.GameStates.PAUSE)
            DrawManager.writeOnCenter("PAUSED", Constants.gameHeight / 2, Color.RED);
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
