package com.mygdx.pixelworld.data.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.utilities.Damaging;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Class used to generify entity in game
 */
public abstract class Entity implements Disposable {
    protected static final Logger logger = LogManager.getLogger();
    protected Vector2 pos;
    protected DrawData img;
    protected EntityStats stats;

    public Vector2 getPos() {
        return new Vector2(pos);
    }
    public EntityStats getStats() {
        return stats;
    }
    protected void getHit(int damage) {
        stats.getHit(this, damage);
    }
    public void getHit(Damaging d) {
        stats.getHit(this, d.getDamage());
    }

    /**
     * Checks whenever the next movement will be possible, and act consequently
     *
     * @param boundingRectangles Obstacles around the map
     * @param currentMove   Move to be done
     */
    protected void bound(List<BoundingRect> boundingRectangles, Vector2 currentMove) {
        for (BoundingRect b : boundingRectangles) {
            if (currentMove.x > 0) {
                if (BoundingShape.intersect(new BoundingRect(pos, new Vector2(img.getWidth() + currentMove.x, img.getHeight())), b)) {
                    pos.x = b.get().x - img.getWidth();
                    return;
                }
            } else if (currentMove.x < 0) {
                if (BoundingShape.intersect(new BoundingRect(new Vector2(pos.x + currentMove.x, pos.y), new Vector2(img.getWidth() - currentMove.x, img.getHeight())), b)) {
                    pos.x = b.get().x + b.get().width;
                    return;
                }
            }

            if (currentMove.y > 0) {
                if (BoundingShape.intersect(new BoundingRect(pos, new Vector2(img.getWidth(), img.getHeight() + currentMove.y)), b)) {
                    pos.y = b.get().y - img.getHeight();
                    return;
                }
            } else {
                if (BoundingShape.intersect(new BoundingRect(new Vector2(pos.x, pos.y + currentMove.y), new Vector2(img.getWidth(), img.getHeight() - currentMove.y)), b)) {
                    pos.y = b.get().y + b.get().height;
                    return;
                }
            }
        }
        pos.add(currentMove);
    }

    @Override
    public void dispose() {
        img.dispose();
    }
    public void setInitialPos(int x, int y) {
        pos = new Vector2(x, y);
    }
}
