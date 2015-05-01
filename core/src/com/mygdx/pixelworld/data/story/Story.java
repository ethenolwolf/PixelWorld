package com.mygdx.pixelworld.data.story;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.entities.NPC;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story {

    private final Map<String, NPC> npcs = new HashMap<>();
    private final List<StoryAction> storyActions = new ArrayList<>();
    private int storyIndex = 0;

    public Story(MapLayer npcLayer, String storyPath) {
        if (npcLayer == null) {
            Logger.log("Story.parse()", "Error: Layer NPC is null.");
            return;
        }
        for (MapObject object : npcLayer.getObjects()) {
            if (!(object instanceof RectangleMapObject)) continue;
            if (object.getName() == null) continue;
            npcs.put(object.getName(), new NPC(object));
        }
        parse(storyPath + ".story");
        Logger.log("Story()", "Loaded " + npcs.size() + " NPCs and " + storyActions.size() + " story actions.");
    }

    public static Vector2 parseCoordinates(String param) {
        String[] coordinates = param.split(":");
        return new Vector2(
                Float.parseFloat(coordinates[0]),
                Float.parseFloat(coordinates[1])
        );
    }

    private static String[] readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String tmp = new String(encoded, encoding);
        return tmp.split("\n");
    }

    public void update() {
        boolean canGoForward = true;
        if (!World.isCameraIdle()) canGoForward = false;
        for (NPC npc : npcs.values()) {
            npc.update();
            if (!npc.isIdle()) canGoForward = false;
        }
        if (canGoForward) deploy();
    }

    public void draw(SpriteBatch batch) {
        for (NPC npc : npcs.values()) npc.draw(batch);
    }

    private void deploy() {
        GUI.updateDialog(null, null);
        if (storyIndex >= storyActions.size()) return;
        StoryAction action = storyActions.get(storyIndex);
        if (!action.name.equals("Camera")) npcs.get(action.name).startNewAction(action);
        else World.setCameraAction(action);
        storyIndex++;
    }

    private void parse(String path) {
        String[] fileLines;
        try {
            fileLines = readFile(path, Charset.defaultCharset());
        } catch (IOException e) {
            Logger.log("Story.parse()", "Error: Could not read file " + path);
            return;
        }
        for (int line = 0; line < fileLines.length; line++) {
            String[] lineParts = fileLines[line].split("\t");
            if (lineParts.length != 3) {
                Logger.log("Story.parse()", "Error: Line " + line + " malformed (" + lineParts.length + " parts). Skipping");
                continue;
            }
            boolean isNameValid = false;
            if (npcs.containsKey(lineParts[0]) || lineParts[0].equals("Camera")) isNameValid = true;
            if (!isNameValid) {
                Logger.log("Story.parse()", "Error: Line " + line + " malformed (NPC " + lineParts[0] + " not found). Skipping");
                continue;
            }
            ActionTypes action = null;
            for (ActionTypes actionType : ActionTypes.values())
                if (actionType.name().equals(lineParts[1].toUpperCase())) action = actionType;
            if (action == null) {
                Logger.log("Story.parse()", "Error: Line " + line + " malformed (action " + lineParts[1] + " invalid). Skipping");
                continue;
            }
            switch (action) {
                case IDLE:
                    break;
                case TALK:
                    break;
                case SPD:
                    try {
                        float tmp = Float.parseFloat(lineParts[2]);
                    } catch (Exception e) {
                        Logger.log("Story.parse()", "Error: Line " + line + " malformed (speed not float). Skipping");
                        continue;
                    }
                    break;
                case SET:
                case MOVE:
                    String[] coordinates = lineParts[2].split(":");
                    if (coordinates.length != 2) {
                        Logger.log("Story.parse()", "Error: Line " + line + " malformed (coordinates number). Skipping");
                        continue;
                    }
                    try {
                        float tmp = Float.parseFloat(coordinates[0]);
                        tmp = Float.parseFloat(coordinates[1]);
                    } catch (Exception e) {
                        Logger.log("Story.parse()", "Error: Line " + line + " malformed (coordinates not float). Skipping");
                        continue;
                    }
                    break;
            }
            storyActions.add(new StoryAction(lineParts[0], action, lineParts[2]));
        }
    }

    public enum ActionTypes {
        IDLE, TALK, SET, SPD, FOCUS, MOVE
    }

}
