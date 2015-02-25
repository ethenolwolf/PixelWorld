package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.mygdx.pixelworld.data.enemies.Blocker;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Assets {

    public static Map<CharacterType, Texture> CHARACTER_IMG = new EnumMap<CharacterType, Texture>(CharacterType.class);
    public static Map<Class, Texture> ENEMY_IMG = new HashMap<Class, Texture>();
    public static BitmapFont font;

    public static final String BACKGROUND = "core/assets/background.jpg";
    public static final String FONT = "core/assets/Ubuntu-MI.ttf";


    public static void init() {
        CHARACTER_IMG.put(CharacterType.WIZARD, new Texture("core/assets/Characters/wizard.png"));
        CHARACTER_IMG.put(CharacterType.ARCHER, new Texture("core/assets/Characters/wizard.png"));
        CHARACTER_IMG.put(CharacterType.PRIEST, new Texture("core/assets/Characters/wizard.png"));
        CHARACTER_IMG.put(CharacterType.WARRIOR, new Texture("core/assets/Characters/wizard.png"));

        ENEMY_IMG.put(Blocker.class, new Texture("core/assets/Enemies/blocker.png"));

        font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal(FONT), Costants.FONT_CHARACTERS, 50.5f, 50f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.setColor(1f, 0f, 0f, 1f);


    }
}
