package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.story.Story;
import com.mygdx.pixelworld.data.story.StoryAction;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;

public class CameraManager {

    private static OrthographicCamera camera;
    private static boolean cameraFollow = true;
    private static Vector2 cameraTarget;
    private static float cameraSpeed = 10;
    private static StoryAction cameraAction;
    private static boolean isInitialized = false;

    public static void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.gameWidth, Constants.gameHeight);
        camera.update();
        isInitialized = true;
    }

    private static boolean mustBeInitialized() {
        if (!isInitialized) Logger.log("CameraManager", "Error : camera not initialized!");
        return !isInitialized;
    }


    public static void update(SpriteBatch batch, TiledMapRenderer tiledMapRenderer) {
        if (mustBeInitialized()) return;
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    /**
     * Sets camera offset
     *
     * @param offset New offset
     */
    public static void setOffset(Vector2 offset) {
        limitPosition(offset);
        camera.position.set(offset.x, offset.y, 0);
        camera.update();
    }

    /**
     * @return Offset of camera.
     */
    public static Vector2 getCameraOffset() {
        if (mustBeInitialized()) return null;
        return new Vector2(camera.position.x - Constants.gameWidth / 2, camera.position.y - Constants.gameHeight / 2);
    }

    public static void setCameraAction(StoryAction cameraAction) {
        CameraManager.cameraAction = cameraAction;
        cameraTarget = null;
        cameraFollow = false;
        switch (cameraAction.action) {
            case IDLE:
                cameraFollow = true;
                break;
            case SET:
                setOffset(Story.parseCoordinates(cameraAction.param));
                CameraManager.cameraAction.action = Story.ActionTypes.IDLE;
                break;
            case MOVE:
                Vector2 pureTarget = Story.parseCoordinates(cameraAction.param).add(Constants.gameWidth / 2, Constants.gameHeight / 2);
                limitPosition(pureTarget);
                cameraTarget = new Vector2(pureTarget);
                break;
            case SPD:
                CameraManager.cameraAction.action = Story.ActionTypes.IDLE;
                cameraSpeed = Float.parseFloat(cameraAction.param);
                break;
        }
    }

    /**
     * Updates offset if player tries to move outside thresholds.
     *
     * @param playerPos Position of the player
     * @param stats     Stats of the player
     */
    public static void updateOffset(Vector2 playerPos, EntityStats stats) {
        float movement = Gdx.graphics.getDeltaTime() * stats.get(StatType.SPD) * 5; //Move with player's same speed
        Vector2 newPos = new Vector2(camera.position.x, camera.position.y);
        float t = Constants.gameWidth;
        float h = Constants.gameHeight;

        if (cameraFollow) {
            if (playerPos.x - camera.position.x + t / 2 < Constants.X_LIMIT_MIN) newPos.add(-movement, 0);
            else if (playerPos.x - camera.position.x + t / 2 > Constants.X_LIMIT_MAX) newPos.add(movement, 0);
            if (playerPos.y - camera.position.y + h / 2 < Constants.Y_LIMIT_MIN) newPos.add(0, -movement);
            else if (playerPos.y - camera.position.y + h / 2 > Constants.Y_LIMIT_MAX) newPos.add(0, movement);
        } else {
            movement = Gdx.graphics.getDeltaTime() * cameraSpeed * 5;
            if (cameraTarget != null && cameraAction.action == Story.ActionTypes.MOVE) {
                if (cameraTarget.dst(new Vector2(camera.position.x, camera.position.y)) > movement) {
                    Vector2 direction = new Vector2(cameraTarget.x - camera.position.x, cameraTarget.y - camera.position.y).nor();
                    newPos.add(direction.x * movement, direction.y * movement);
                } else {
                    newPos = new Vector2(cameraTarget);
                    cameraTarget = null;
                    cameraAction.action = Story.ActionTypes.IDLE;
                }
            }
        }
        setOffset(newPos);
    }

    private static void limitPosition(Vector2 in) {
        float t = Constants.gameWidth;
        float h = Constants.gameHeight;
        //Limit offset
        if (in.x < t / 2) in.x = t / 2;
        if (in.x > World.getWidth() - t / 2) in.x = World.getWidth() - t / 2;
        if (in.y < h / 2) in.y = h / 2;
        if (in.y > World.getHeight() - h / 2) in.y = World.getHeight() - h / 2;
    }

    public static boolean isCameraIdle() {
        return cameraAction == null || cameraAction.action == Story.ActionTypes.IDLE;
    }
}
