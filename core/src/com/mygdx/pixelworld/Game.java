package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.debug.Debug;

//TODO Add support for multiple map

public class Game extends ApplicationAdapter implements InputProcessor {

    public static OrthographicCamera camera;
    public static AssetManager assetManager;
    private SpriteBatch batch;
    private World world;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private StaticDrawData loadingImage;

    @Override
    public void create() {
        assetManager = new AssetManager();
        loadingImage = new StaticDrawData("core/assets/loadingImage.png", BoundingRect.class);
        loadingImage.setScaleFactor(new Vector2(4, 4));
        assetManager.finishLoading();
        batch = new SpriteBatch();
        Debug.init();
        world = new World();
        player = new Player(GameClasses.CLERIC);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.gameWidth+Constants.panelWidth, Constants.gameHeight);
        camera.update();

        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        GUI.init(batch, player, world);
    }

    @Override
    public void render() {
        if (assetManager.update()) {
            gameLoop();
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            Vector2 loadingImagePos = new Vector2(Constants.totalWidth / 2 - 50, Constants.gameHeight / 2);
            loadingImage.draw(batch, loadingImagePos);
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.0f, 0.0f, 0.392f, 1.0f);
            shapeRenderer.rect(50, 50, Constants.totalWidth - 100, 30);
            shapeRenderer.setColor(0.0f, 0.05f, 0.95f, 1.0f);
            shapeRenderer.rect(50, 50, (Constants.totalWidth - 100) * assetManager.getProgress(), 30);
            shapeRenderer.end();
        }
    }

    private void gameLoop() {
        //Logic update
        player.update(world);
        world.update(player);

        //Clear screen
        Gdx.gl.glClearColor(0.5f, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw
        world.drawBottom(batch, camera);
        player.draw(batch);
        GUI.draw();
        Debug.draw(batch);
        batch.end();
        world.drawTop();
        world.shapeDraw(shapeRenderer, player);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        world.dispose();
        player.dispose();
        shapeRenderer.dispose();
        assetManager.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) player.manaTrigger();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if (screenX < Constants.gameWidth) { //InScreen click
                player.getFireManager().setIsFiring(true);
                player.getFireManager().setTarget(screenX + World.getCameraOffset().x, Gdx.graphics.getHeight() - screenY + World.getCameraOffset().y);
            } else { //Panel click
                GUI.clickDown(screenX, (int) Constants.gameHeight - screenY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.getFireManager().setIsFiring(false);
        GUI.clickUp(screenX, (int) Constants.gameHeight - screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        player.getFireManager().setTarget(screenX + World.getCameraOffset().x, Gdx.graphics.getHeight() - screenY + World.getCameraOffset().y);
        GUI.updateMousePosition(screenX, (int) Constants.gameHeight - screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}