package com.mygdx.pixelworld.data.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.story.StoryAction;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;

public class NPC extends Entity {
    private final String name;
    private String speech;
    private Vector2 moveTarget, direction;
    private float speechTimer = 0;
    private int speechIndex = 1;

    public NPC(MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        String relativePath = object.getProperties().get("Texture", String.class);
        String actionsString = object.getProperties().get("Actions", String.class);
        int speed = Integer.parseInt(object.getProperties().get("Speed", String.class));
        name = object.getName();
        String[] actions = actionsString.split(",");
        pos = new Vector2(rect.x, rect.y);
        stats = new EntityStats(0, 0, speed, 0, 0, 0, 0, 0);
        img = new AnimationDrawData("core/assets/characters/npc/" + relativePath + "/", actions, BoundingRect.class);
    }

    public void draw() {
        img.draw(pos);
    }

    public boolean isIdle() {
        return moveTarget == null && speech == null;
    }

    public void update() {
        if (moveTarget != null) {
            float movement = Gdx.graphics.getDeltaTime() * stats.get(StatType.SPD);
            if (moveTarget.dst(pos) > movement) pos.add(direction.x * movement, direction.y * movement);
            else {
                pos.x = moveTarget.x;
                pos.y = moveTarget.y;
                moveTarget = null;
            }
        } else if (speech != null) {
            GUI.updateDialog(name, speech.substring(0, speechIndex));
            speechTimer += Gdx.graphics.getDeltaTime();
            if ((speechIndex < speech.length() && speechTimer > 0.05) || (speechIndex >= speech.length() && speechTimer > 1)) {
                speechTimer = 0;
                speechIndex++;
                if (speechIndex > speech.length()) speech = null;
            }
        }
        img.update();
    }

    public void startNewAction(StoryAction action) {
        moveTarget = null;
        speech = null;
        switch (action.action) {
            case IDLE:
                break;
            case TALK:
                speech = action.param;
                speechTimer = 0;
                speechIndex = 1;
                break;
            case MOVE:
                String[] coordinates = action.param.split(":");
                moveTarget = new Vector2(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
                direction = new Vector2(moveTarget.x - pos.x, moveTarget.y - pos.y).nor();
                break;
        }
    }
}
