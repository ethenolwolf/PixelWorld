package com.mygdx.pixelworld.data.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.enemies.Blocker;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Assets {

    private static final Map<GameClasses, Texture> CHARACTER_TEX = new EnumMap<GameClasses, Texture>(GameClasses.class);
    private static final Map<Class, Texture> ENEMY_TEX = new HashMap<Class, Texture>();
    private static final Map<GameClasses, Texture> MANA_TEX = new EnumMap<GameClasses, Texture>(GameClasses.class);
    private static final Map<GameClasses, Texture> SIGIL_TEX = new EnumMap<GameClasses, Texture>(GameClasses.class);
    private static BitmapFont font;
    private static Texture BACKGROUND_TEX;
    private static Texture PANEL_TEX;
    private static Texture chest_texture;

    public static void init() {
        Logger.log("[Assets.init()] Initializing...");
        CHARACTER_TEX.put(GameClasses.WIZARD, new Texture("core/assets/Characters/wizard.png"));
        CHARACTER_TEX.put(GameClasses.CLERIC, new Texture("core/assets/Characters/cleric front.png"));
        CHARACTER_TEX.put(GameClasses.NINJA, new Texture("core/assets/Characters/ninja.png"));
        ENEMY_TEX.put(Blocker.class, new Texture("core/assets/Enemies/blocker.png"));

        MANA_TEX.put(GameClasses.WIZARD, new Texture("core/assets/Mana/wizard.gif"));

        SIGIL_TEX.put(GameClasses.NINJA, new Texture("core/assets/Sigils/invisibleCloak.png"));

        font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("core/assets/Ubuntu-MI.ttf"), Constants.FONT_CHARACTERS, 20f, 20f, 1.0f, Constants.gameWidth, Constants.gameHeight);
        font.setColor(1f, 1f, 1f, 1f);

        BACKGROUND_TEX = new Texture("core/assets/background.png");
        PANEL_TEX = new Texture("core/assets/panel.png");
        chest_texture = new Texture("core/assets/chest.png");
        Logger.log("[Assets.init()] Init complete.");
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
        }
        Logger.log("[Assets.getTexture()] Texture not found. AssetType:" + type.toString() + " Name:" + name);
        return new Texture("core/assets/badlogic.jpg");
    }

    public static Texture getTexture(AssetType assetType, Class type) {
        switch (assetType) {
            case BACKGROUND:
                if (BACKGROUND_TEX != null) return BACKGROUND_TEX;
                break;
            case CHEST:
                if (chest_texture != null) return chest_texture;
                break;
            case PANEL:
                if (PANEL_TEX != null) return PANEL_TEX;
            case ENEMY:
                if (ENEMY_TEX.get(type) != null) return ENEMY_TEX.get(type);
                break;
        }
        Logger.log("[Assets.getTexture()] Texture not found. AssetType:" + assetType.toString() + " Class:" + type.toString());
        return new Texture("core/assets/badlogic.jpg");
    }

    public static void write(SpriteBatch batch, String name, float x, float y) {
        font.draw(batch, name, x, y);
    }

    public static Texture getTexture(GameClasses classType) {
        if (CHARACTER_TEX.get(classType) != null) return CHARACTER_TEX.get(classType);
        else return null;
    }
}
