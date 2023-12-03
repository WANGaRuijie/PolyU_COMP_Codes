package hk.edu.polyu.comp.comp2021.assignment1.base7;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base7Test {
    @Test(timeout = 2000)
    public void test1() {
        assertEquals("202", Base7.convertToBase7(100));
    }
    @Test(timeout = 2000)
    public void test2() {
        assertEquals("10", Base7.convertToBase7(7));
    }
    @Test(timeout = 2000)
    public void test3() {
        assertEquals("-24", Base7.convertToBase7(-18));
    }
}
