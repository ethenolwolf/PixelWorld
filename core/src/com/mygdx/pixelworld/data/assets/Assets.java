package com.mygdx.pixelworld.data.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.pixelworld.GUI.Logger;

public class Assets {

    private static BitmapFont font;

    public static void init() {
        FreeTypeFontGenerator ft = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/Ubuntu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 16;
        p.color = Color.RED;
        font = ft.generateFont(p);
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

    public static void write(SpriteBatch batch, String message, float x, float y) {
        font.draw(batch, message, x, y);
    }

}
