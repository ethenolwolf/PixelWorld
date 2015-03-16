package test;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoundingCircleTest {

    @Test
    public void testIntersect() throws Exception {
        BoundingCircle bc1 = new BoundingCircle(new Vector2(0, 0), 6);
        BoundingCircle bc2 = new BoundingCircle(new Vector2(3, 4), 5);
        assertTrue(bc1.isValid());
        assertTrue(bc2.isValid());
        assertTrue(bc1.getCenter().x == 0);
        assertTrue(bc1.getCenter().y == 0);
        assertTrue(bc2.getCenter().x == 3);
        assertTrue(bc2.getCenter().y == 4);
        assertEquals(5, bc1.getCenter().dst(bc2.getCenter()), 0);
        assertTrue(bc1.intersect(bc2));
    }

    @Test
    public void testIsValid() throws Exception {
        BoundingCircle bc = new BoundingCircle(null, 3);
        assertFalse(bc.isValid());
        bc = new BoundingCircle(new Vector2(0, 0), -1);
        assertFalse(bc.isValid());
        bc = new BoundingCircle(null, -1);
        assertFalse(bc.isValid());
        bc = new BoundingCircle(new Vector2(0, 0), 0);
        assertFalse(bc.isValid());
        bc = new BoundingCircle(new Vector2(0, 0), 1);
        assertTrue(bc.isValid());
        bc = new BoundingCircle(new Vector2(-2, -5), 6);
        assertTrue(bc.isValid());
    }
}