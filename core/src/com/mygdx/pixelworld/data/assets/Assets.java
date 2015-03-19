package com.mygdx.pixelworld.data.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.utilities.Constants;

public class Assets {

    private static BitmapFont font;
    private static Texture BACKGROUND_TEX;
    private static Texture PANEL_TEX;

    public static void init() {
        font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("core/assets/Ubuntu-MI.ttf"), Constants.FONT_CHARACTERS, 20f, 20f, 1.0f, Constants.gameWidth, Constants.gameHeight);
        font.setColor(1f, 1f, 1f, 1f);

        BACKGROUND_TEX = new Texture("core/assets/background.png");
        PANEL_TEX = new Texture("core/assets/panel.png");
    }

    public static Texture getTexture(AssetType type, String name) {
        switch (type) {
            case BULLET:
                return new Texture("core/assets/Bullets/" + name + ".png");
            case WEAPON:
                return new Texture("core/assets/Weapons/" + name + ".png");
            case ARMOR:
                return new Texture("core/assets/Armors/" + name + ".png");
            case SIGIL:
                return new Texture("core/assets/Sigils/" + name + ".png");
            case CHEST:
                return new Texture("core/assets/chest.png");
        }
        Logger.log("Assets.getTexture()", "Texture not found. AssetType:" + type.toString() + " Name:" + name);
        return new Texture("core/assets/badlogic.jpg");
    }

    public static Texture getTexture(AssetType assetType, Class type) {
        switch (assetType) {
            case BACKGROUND:
                if (BACKGROUND_TEX != null) return BACKGROUND_TEX;
                break;
            case PANEL:
                if (PANEL_TEX != null) return PANEL_TEX;
        }
        Logger.log("Assets.getTexture()", "Texture not found. AssetType:" + assetType.toString() + " Class:" + type.toString());
        return new Texture("core/assets/badlogic.jpg");
    }

    public static void write(SpriteBatch batch, String name, float x, float y) {
        font.draw(batch, name, x, y);
    }

}
