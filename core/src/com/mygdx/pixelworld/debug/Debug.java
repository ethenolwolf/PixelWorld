package com.mygdx.pixelworld.debug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class created to contain all debug options that can be easily switched from here.
 */
@SuppressWarnings("PointlessBooleanExpression")
public class Debug {

    //Shows boundings around every object
    public static final boolean SHOW_BOUNDING = false;

    //Print every call to Logger.log()
    public static final boolean VERBOSE = true;

    //Print map offset triggers
    public static final boolean SHOW_OFFSET_TRIGGERS = false;

    //Print debug values
    private static final boolean SHOW_DEBUG_VALUES = false;

    private static final List<Debuggable> debuggable = new ArrayList<Debuggable>();

    public static void addDebuggable(Debuggable debuggable){
        Debug.debuggable.add(debuggable);
    }

    public static void draw(SpriteBatch batch){
        if(!SHOW_DEBUG_VALUES) return;
        float y = Constants.gameHeight - 20 + Game.camera.position.y;
        ListIterator<Debuggable> li = debuggable.listIterator();
        while(li.hasNext()) {
            Debuggable d = li.next();
            if(d == null) {
                li.remove();
                continue;
            }
            Assets.write(batch, d.getWatch(), 10+Game.camera.position.x, y);
            y -= 20;
        }
    }

}
