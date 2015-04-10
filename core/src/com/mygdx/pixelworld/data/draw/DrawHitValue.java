package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.pixelworld.data.entities.Entity;

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
            FreeTypeFontGenerator ftFont = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/Ubuntu.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
            p.size = 20;
            font = ftFont.generateFont(p);
        }
        for (Hit hit : hits) {
            if (hit.damage > 0) font.setColor(new Color(1f, 0f, 0f, hit.shade));
            else font.setColor(new Color(0f, 1f, 0f, hit.shade));
            font.draw(batch, String.format("%+d", -hit.damage), hit.x, hit.y);
        }
    }


    private static class Hit {
        public final float x;
        public final int damage;
        public float y;
        public float shade;

        public Hit(Entity e, int damage) {
            this.x = e.getPos().x;
            this.y = e.getPos().y + 60;
            this.shade = 1.0f;
            this.damage = damage;
        }

        public boolean update() {
            y += 1.5f; //ANIMATION SPEED
            shade -= 0.05f; //SHADE SPEED
            return shade > 0f; //if transparent die
        }
    }
}
