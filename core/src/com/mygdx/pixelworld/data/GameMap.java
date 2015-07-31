package com.mygdx.pixelworld.data;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameMap implements Disposable {
    private static String currentMap;
    private static TiledMapRenderer renderer;
    private TiledMap map;

    public GameMap(TiledMap map) {
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map, DrawManager.getBatch());
    }

    public static String getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(String currentMap) {
        GameMap.currentMap = currentMap;
    }

    /**
     * @return Width of the map.
     */
    public int getWidth() {
        if (map == null) return (int) Constants.gameWidth;
        return getIntProperty("width") * getTileWidth();
    }

    /**
     * @return Height of the map.
     */
    public int getHeight() {
        if (map == null) return (int) Constants.gameHeight;
        return getIntProperty("height") * getTileHeight();
    }

    @Override
    public void dispose() {
        Game.assetManager.unload(currentMap);
        map.dispose();
    }

    public String getStringProperty(String propertyName) {
        return map.getProperties().get(propertyName, String.class);
    }

    public Integer getIntProperty(String propertyName) {
        return map.getProperties().get(propertyName, Integer.class);
    }

    public Integer getTileWidth() {
        return getIntProperty("tilewidth");
    }

    public Integer getTileHeight() {
        return getIntProperty("tileheight");
    }

    public MapLayer getLayer(String name) {
        return map.getLayers().get(name);
    }

    public MapObjects getLayerObjects(String layerName) {
        return getLayer(layerName).getObjects();
    }

    public List<RectangleMapObject> getRectangularObjects(String layerName) {
        MapObjects mapObjects = getLayerObjects(layerName);
        List<RectangleMapObject> out = new ArrayList<>();
        for (MapObject mapObject : mapObjects)
            if (mapObject instanceof RectangleMapObject) out.add((RectangleMapObject) mapObject);
        return out;
    }

    public int getLayerCount() {
        return map.getLayers().getCount();
    }

    public TiledMapRenderer getRenderer() {
        return renderer;
    }

    public void render(int[] layers) {
        renderer.render(layers);
    }
}
