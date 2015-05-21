package com.mygdx.pixelworld.data.story;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.CameraManager;
import com.mygdx.pixelworld.data.entities.NPC;
import com.mygdx.pixelworld.data.utilities.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story {

    private final Map<String, NPC> NPCs = new HashMap<>();
    private final List<StoryAction> storyActions = new ArrayList<>();
    private int storyIndex = 0;

    public Story(MapLayer npcLayer, String storyPath) {
        if (npcLayer == null || storyPath == null) {
            Logger.log("Story()", "Layer NPC or story file is null, assuming there is no story.");
            return;
        }
        for (MapObject object : npcLayer.getObjects()) {
            if (!(object instanceof RectangleMapObject)) continue;
            if (object.getName() == null) continue;
            NPCs.put(object.getName(), new NPC(object));
        }
        parse(storyPath + ".story");
        Logger.log("Story()", "Loaded " + NPCs.size() + " NPCs and " + storyActions.size() + " story actions.");
    }

    public static Vector2 parseCoordinates(String param) {
        String[] coordinates = param.split(":");
        return new Vector2(
                Float.parseFloat(coordinates[0]),
                Float.parseFloat(coordinates[1])
        );
    }

    public void update() {
        boolean canGoForward = true;
        if (!CameraManager.isCameraIdle()) canGoForward = false;
        for (NPC npc : NPCs.values()) {
            npc.update();
            if (!npc.isIdle()) canGoForward = false;
        }
        if (canGoForward) deploy();
    }

    public void draw() {
        for (NPC npc : NPCs.values()) npc.draw();
    }

    private void deploy() {
        GUI.updateDialog(null, null);
        if (storyIndex >= storyActions.size()) return;
        StoryAction action = storyActions.get(storyIndex);
        if (!action.name.equals("Camera")) NPCs.get(action.name).startNewAction(action);
        else CameraManager.setCameraAction(action);
        storyIndex++;
    }

    private void parse(String path) {
        String[] fileLines;
        try {
            fileLines = Utils.readFile(path);
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
            if (NPCs.containsKey(lineParts[0]) || lineParts[0].equals("Camera")) isNameValid = true;
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
                        @SuppressWarnings("UnusedAssignment") float tmp = Float.parseFloat(lineParts[2]);
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
                        @SuppressWarnings("UnusedAssignment") Float tmp = Float.parseFloat(coordinates[0]);
                        @SuppressWarnings("UnusedAssignment") Float tmp2 = Float.parseFloat(coordinates[1]);
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
