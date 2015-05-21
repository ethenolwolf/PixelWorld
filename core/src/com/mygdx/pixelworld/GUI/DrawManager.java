package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.CameraManager;
import com.mygdx.pixelworld.data.utilities.Constants;

public abstract class DrawManager {
    private static final SpriteBatch batch = new SpriteBatch();
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static ShapeRenderer.ShapeType currentShapeType = ShapeRenderer.ShapeType.Line;
    private static BitmapFont font;

    public static Color getColor(Type type) {
        switch (type) {
            case BATCH:
                return batch.getColor();
            case SHAPE:
                return shapeRenderer.getColor();
        }
        return new Color();
    }

    public static SpriteBatch getBatch() {
        begin(Type.BATCH);
        return batch;
    }

    public static void line(float startX, float startY, float endX, float endY) {
        begin(Type.SHAPE);
        shapeRenderer.line(startX, startY, endX, endY);
        end();
    }

    public static void circle(float x, float y, float radius) {
        begin(Type.SHAPE);
        shapeRenderer.circle(x, y, radius);
        end();
    }

    public static void begin(Type drawType) {
        switch (drawType) {
            case BATCH:
                if (batch.isDrawing()) return;
                end();
                batch.begin();
                break;
            case SHAPE:
                if (shapeRenderer.isDrawing()) return;
                end();
                shapeRenderer.begin(currentShapeType);
                break;
        }
    }

    public static void end() {
        if (batch.isDrawing()) batch.end();
        if (shapeRenderer.isDrawing()) shapeRenderer.end();
    }

    public static void changeType(ShapeRenderer.ShapeType shapeType) {
        currentShapeType = shapeType;
    }

    public static void setColor(Type type, float r, float g, float b, float a) {
        switch (type) {
            case BATCH:
                batch.setColor(r, g, b, a);
                break;
            case SHAPE:
                shapeRenderer.setColor(r, g, b, a);
                break;
        }
    }

    public static void rect(float x, float y, float w, float h) {
        begin(Type.SHAPE);
        shapeRenderer.rect(x, y, w, h);
        end();
    }

    public static void init() {
        FreeTypeFontGenerator ft = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/various/Ubuntu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 16;
        font = ft.generateFont(p);
    }

    public static void write(String message, float x, float y, Color color) {
        font.setColor(Color.CLEAR);
        font.setColor(color);
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) return;
        begin(Type.BATCH);
        font.draw(batch, message, x + offset.x, y + offset.y);
        end();
    }

    public static void writeOnCenter(String message, float y, Color color) {
        float x = (Constants.gameWidth - font.getBounds(message).width) / 2;
        write(message, x, y, color);
    }

    public static void dispose() {
        end();
        shapeRenderer.dispose();
        batch.dispose();
    }

    public enum Type {
        BATCH, SHAPE
    }
}
