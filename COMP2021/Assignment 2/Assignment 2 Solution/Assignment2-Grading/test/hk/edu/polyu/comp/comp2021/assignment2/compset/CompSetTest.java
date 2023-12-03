package hk.edu.polyu.comp.comp2021.assignment2.compset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class CompSetTest {

    List<Integer> intList;
    CompSet<Integer> intSet1;
    CompSet<Integer> intSet2;

    @BeforeEach
    public void prepare(){
        intList = Arrays.asList(1, 2, 3, 4, 5);
        intSet1 = new CompSet<>(intList);
        intSet2 = new CompSet<>();
    }

    @Test
    public void testConstructor01() {
        assertTrue(intSet2.isEmpty());
        assertEquals(0, intSet2.getCount());
    }

    @Test
    public void testConstructor02(){
        assertEquals(intList.size(), intSet1.getCount());
        assertTrue(intSet1.contains(1));
        assertTrue(intSet1.contains(2));
        assertTrue(intSet1.contains(3));
        assertTrue(intSet1.contains(4));
        assertTrue(intSet1.contains(5));
    }

    @Test
    public void testGetCount01(){
        intSet1.add(7);
        assertEquals(6, intSet1.getCount());
        intSet1.remove(1);
        intSet1.remove(2);
        assertEquals(4, intSet1.getCount());
    }

    @Test
    public void testIsEmpty01(){
        intSet2.add(2);
        assertFalse(intSet2.isEmpty());
        intSet2.remove(2);
        assertTrue(intSet2.isEmpty());
    }

    @Test
    public void testContains01(){
        assertTrue(intSet1.contains(1));
        assertFalse(intSet1.contains(8));
        intSet1.add(10);
        assertTrue(intSet1.contains(10));
        intSet1.remove(10);
        assertFalse(intSet1.contains(10));
        assertFalse(intSet1.contains(null));
    }

    @Test
    public void testAdd01(){
        int oldCount = intSet1.getCount();
        assertTrue(intSet1.contains(1));
        intSet1.add(1);
        assertEquals(oldCount, intSet1.getCount());
    }

    @Test
    public void testEquals01(){
        CompSet<Integer> intSet3= new CompSet<>(Arrays.asList(2, 3, 4, 5));
        assertFalse(intSet1.equals(intSet3));
        intSet3.add(1);
        assertTrue(intSet1.equals(intSet3));
    }

    @Test
    public void testEquals02(){
        assertFalse(intSet1.equals(null));
        assertFalse(intSet1.equals(new CompSet<String>()));
    }

    @Test
    public void testGetElements01(){
        List<Integer> list = intSet1.getElements();
        assertEquals(intList.size(), list.size());
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
        assertTrue(list.contains(4));
        assertTrue(list.contains(5));
    }

    @Test
    public void testRemove01(){
        int oldCount = intSet1.getCount();
        assertTrue(intSet1.contains(1));
        intSet1.remove(1);
        assertEquals(oldCount - 1, intSet1.getCount());
        intSet1.remove(1);
        assertEquals(oldCount - 1, intSet1.getCount());
    }

//
//    @Test
//    public void testIntegerCompSet(){
//        assert intSet2.isEmpty();
//        assert intSet2.getCount() == 0;
//        intSet2.add(1);
//        assert !intSet2.isEmpty();
//        intSet2.add(2);
//        intSet2.add(3);
//        assert intSet2.getCount() == 3;
//        assert intSet2.contains(3);
//        assert !intSet2.contains(4);
//        intSet2.remove(3);
//        intSet2.remove(4);
//        assert !intSet2.contains(4);
//    }
//
//    @Test
//    public void testStringCompSet(){
//        stringSet1.add("a");
//        stringSet1.add("b");
//        stringSet1.add("a");
//        stringSet1.add("c");
//        stringSet1.add("d");
//        List<String> uniqueStrings = stringSet1.getElements();
//        CompSet<String> stringSet2 = new CompSet<>(uniqueStrings);
//        assert stringSet1.equals(stringSet2);
//        stringSet2.remove("c");
//        assert !stringSet1.equals(stringSet2);
//    }
}