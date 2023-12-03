package hk.edu.polyu.comp.comp2021.assignment1.base7;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class Base7Test {
    public static String student_ID = "";
    public static String dirName = "";

    public static float totalTestNo = 0;
    public static float passTestNo = 0;

    @Before
    public void count() {
        totalTestNo++;
    }

    @BeforeClass
    public static void init() {
        student_ID = "111111";
        dirName = "task1";
    }

    @AfterClass
    public static void fin() {
        File dir = new File(dirName);
        dir.mkdir();

        String subDirName = Paths.get(dirName, student_ID).toString();
        File subDir = new File(subDirName);
        subDir.mkdir();

        try {
            FileWriter writer = new FileWriter(Paths.get(subDirName ,"Base7Score.txt").toString());
            writer.write(passTestNo / totalTestNo + "");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    @Test(timeout = 2000)
    public void test1() {
        assertEquals("202", Base7.convertToBase7(100));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test2() {
        assertEquals("10", Base7.convertToBase7(7));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test3() {
        assertEquals("-24", Base7.convertToBase7(-18));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test4() {
        assertEquals("0", Base7.convertToBase7(0));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test5() {
        assertEquals("404", Base7.convertToBase7(200));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test6() {
        assertEquals("-230", Base7.convertToBase7(-119));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test7() {
        assertEquals("3", Base7.convertToBase7(3));
        passTestNo++;
    }
    @Test(timeout = 2000)
    public void test8() {
        assertEquals("-12", Base7.convertToBase7(-9));
        passTestNo++;
    }

}
