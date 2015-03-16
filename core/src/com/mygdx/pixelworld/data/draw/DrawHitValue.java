package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DrawHitValue {
    private static final List<Hit> hits = new ArrayList<Hit>();
    private static BitmapFont font = null;

    public static void add(Entity e, int damage) {
        hits.add(new Hit(e, damage));
    }

    public static void update() {
        ListIterator iterator = hits.listIterator();
        while (iterator.hasNext()) {
            Hit hit = (Hit) iterator.next();
            if (!hit.update()) iterator.remove();
        }
    }

    public static void draw(SpriteBatch batch) {
        if (font == null) {
            font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("core/assets/Ubuntu-MI.ttf"), "-0123456789", 30f, 30f, 1.0f, Constants.gameWidth, Constants.gameHeight);
            font.setColor(Color.RED);
        }
        for (Hit hit : hits) {
            font.setColor(new Color(1.0f, 0f, 0f, hit.shade));
            font.draw(batch, hit.toString(), hit.x, hit.y);
        }
    }


    private static class Hit {
        public final float x;
        public final int damage;
        public float y;
        public float shade;

        public Hit(Entity e, int damage) {
            this.x = e.getImg().getEffectivePosition(e.getPos()).x;
            this.y = e.getImg().getEffectivePosition(e.getPos()).y + e.getImg().getHeight() + 10;
            this.shade = 1.0f;
            this.damage = damage;
        }

        public boolean update() {
            y += 1.5f; //ANIMATION SPEED
            shade -= 0.05f; //SHADE SPEED
            return shade > 0f; //if transparent die
        }

        @Override
        public String toString() {
            return "-" + String.valueOf(damage);
        }
    }
}
