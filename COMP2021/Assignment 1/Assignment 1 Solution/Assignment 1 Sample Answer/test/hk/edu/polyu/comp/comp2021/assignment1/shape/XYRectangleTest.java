package hk.edu.polyu.comp.comp2021.assignment1.shape;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class XYRectangleTest {

    Point[][] points;
    XYRectangle r1, r2, r3, r4, r5, r6;

    private Point getPoint(int x, int y){
        return points[x][y];
    }

    @Before
    public void init(){
        points = new Point[6][6];
        for(int i = 0; i < 6; i++)
            for(int j = 0; j < 6; j++){
                points[i][j] = new Point(i, j);
            }
        r1 = new XYRectangle(getPoint(1,2), getPoint(3,1));
        r2 = new XYRectangle(getPoint(1,2), getPoint(2,1));
        r3 = new XYRectangle(getPoint(4,4), getPoint(5,3));
    }

    @Test
    public void testConstructor1(){
        assertEquals("<(1,2),(3,1)>", r1.toString());
    }

    @Test
    public void testConstructor2(){
        XYRectangle r = new XYRectangle(getPoint(1,1), getPoint(3,2));
        assertEquals("<(1,2),(3,1)>", r.toString());
    }

    @Test
    public void testArea(){
        assertEquals(2, r1.area());
    }

    @Test
    public void testRotateClockwise1(){
        r1.rotateClockwise();
        assertEquals("<(0,2),(1,0)>", r1.toString());
    }

    @Test
    public void testMove1(){
        r1.move(1,1);
        assertEquals("<(2,3),(4,2)>", r1.toString());
    }

    @Test
    public void testMove2(){
        r1.move(-1,-1);
        assertEquals("<(0,1),(2,0)>", r1.toString());
    }

    @Test
    public void testContains1(){
        assertTrue(r1.contains(getPoint(1,2)));
    }

    @Test
    public void testContains2(){
        assertFalse(r1.contains(getPoint(0,0)));
    }

    @Test
    public void testContains3(){
        assertTrue(r1.contains(r2));
    }

    @Test
    public void testContains4(){
        assertFalse(r2.contains(r1));
    }

    @Test
    public void testOverlapsWith1(){
        assertTrue(r1.overlapsWith(r2));
    }

    @Test
    public void testOverlapsWith2(){
        assertTrue(r2.overlapsWith(r1));
    }

    @Test
    public void testOverlapsWith3(){
        assertFalse(r1.overlapsWith(r3));
        assertFalse(r3.overlapsWith(r1));
    }





}
