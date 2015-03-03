package com.mygdx.pixelworld.data.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.classes.Wizard;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private static Map<Class, Texture> CHARACTER_TEX = new HashMap<Class, Texture>();
    private static Map<Class, Texture> BULLET_TEX = new HashMap<Class, Texture>();
    private static Map<Class, Texture> MANA_TEX = new HashMap<Class, Texture>();
    private static BitmapFont font;
    private static Texture BACKGROUND_TEX;

    public static void init() {
        Logger.log("[Assets.init()] Initializing...");
        CHARACTER_TEX.put(Wizard.class, new Texture("core/assets/Characters/wizard.png"));
        CHARACTER_TEX.put(Blocker.class, new Texture("core/assets/Enemies/blocker.png"));

        BULLET_TEX.put(Wizard.class, new Texture("core/assets/Bullets/red.png"));
        BULLET_TEX.put(Blocker.class, new Texture("core/assets/Bullets/blue.png"));

        MANA_TEX.put(Wizard.class, new Texture("core/assets/Mana/wizard.gif"));

        font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("core/assets/Ubuntu-MI.ttf"), Constants.FONT_CHARACTERS, 50.5f, 50f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.setColor(1f, 0f, 0f, 1f);

        BACKGROUND_TEX = new Texture("core/assets/background.jpg");
        Logger.log("[Assets.init()] Init complete.");
    }

    public static Texture getTexture(AssetType assetType, Class type) {
        switch (assetType) {
            case BACKGROUND:
                if (BACKGROUND_TEX != null) return BACKGROUND_TEX;
            case CHARACTER:
                if (CHARACTER_TEX.get(type) != null) return CHARACTER_TEX.get(type);
            case BULLET:
                if (BULLET_TEX.get(type) != null) return BULLET_TEX.get(type);
            case MANA:
                if (MANA_TEX.get(type) != null) return MANA_TEX.get(type);
        }
        Logger.log("[Assets.getTexture()] Texture not found. AssetType:" + assetType.toString() + " Class:" + type.toString());
        return new Texture("core/assets/badlogic.jpg");
    }

    public static void write(SpriteBatch batch, String name, float x, float y) {
        font.draw(batch, name, x, y);
    }
}
