package hk.edu.polyu.comp.comp2021.assignment1.complex;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class RationalTest {
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
        System.setProperty("studentID", "21039692r");
        System.setProperty("outputDir", "output");
        student_ID = System.getProperty("studentID");
        dirName = System.getProperty("outputDir");
    }

    @Test
    public void testConstructor_01() {
        Rational r1 = new Rational(1, 2);
        r1.simplify();
        assertEquals("1/2", r1.toString());
        passTestNo++;
    }

    @Test
    public void testConstrcutor_02() {
        Rational r2 = new Rational(0, 6);
        r2.simplify();
        assertEquals("0/1", r2.toString());
        passTestNo++;
    }

    @Test
    public void testSimplify_01() {
        Rational r1 = new Rational(4, 10);
        r1.simplify();
        assertEquals("2/5", r1.toString());
        passTestNo++;
    }

    @Test
    public void testSimplify_02() {
        Rational r2 = new Rational(12, 3);
        r2.simplify();
        assertEquals("4/1", r2.toString());
        passTestNo++;
    }

    @Test
    public void testSimplify_03() {
        Rational r3 = new Rational(0, 6);
        r3.simplify();
        assertEquals("0/1", r3.toString());
        passTestNo++;
    }

    @Test
    public void testSimplify_04() {
        Rational r4 = new Rational(2, -6);
        r4.simplify();
        assertEquals("-1/3", r4.toString());
        passTestNo++;
    }

    @Test
    public void testSimplify_05() {
        Rational r5 = new Rational(-4, -16);
        r5.simplify();
        assertEquals("1/4", r5.toString());
        passTestNo++;

    }

    @Test
    public void testAddition() {
        Rational r1 = new Rational(1, 2);
        Rational r2 = new Rational(1, 3);

        Rational rSUm = r1.add(r2);
        rSUm.simplify();

        assertEquals("5/6", rSUm.toString());
        passTestNo++;
    }

    @Test
    public void testSubstraction() {
        Rational r1 = new Rational(2, 3);
        Rational r2 = new Rational(1, 4);

        Rational rSub = r1.subtract(r2);
        rSub.simplify();

        assertEquals("5/12", rSub.toString());
        passTestNo++;
    }

    @Test
    public void testMuliplication() {
        Rational r1 = new Rational(7, 8);
        Rational r2 = new Rational(5, 6);

        Rational rMul = r1.multiply(r2);
        rMul.simplify();

        assertEquals("35/48", rMul.toString());
        passTestNo++;
    }

    @Test
    public void testMultiplication_02() {
        Rational r1 = new Rational(5, 6);
        Rational r2 = new Rational(0, 2);

        Rational rMul = r1.multiply(r2);
        rMul.simplify();

        assertEquals("0/1", rMul.toString());
        passTestNo++;
    }

    @Test
    public void testDevision() {
        Rational r1 = new Rational(2, 3);
        Rational r2 = new Rational(3, 4);

        Rational rDiv = r1.divide(r2);
        rDiv.simplify();

        assertEquals("8/9", rDiv.toString());
        passTestNo++;
    }

    @Test
    public void testDevision_02() {
        Rational r1 = new Rational(0, 1);
        Rational r2 = new Rational(5, 6);

        Rational rDiv = r1.divide(r2);
        rDiv.simplify();

        assertEquals("0/1", rDiv.toString());
        passTestNo++;
    }

    @AfterClass
    public static void fin() {
        File dir = new File(dirName);
        dir.mkdir();

        String subDirName = Paths.get(dirName, student_ID).toString();
        File subDir = new File(subDirName);
        subDir.mkdir();

        try {
            FileWriter writer = new FileWriter(Paths.get(subDirName ,"RationalScore.txt").toString());
            writer.write(passTestNo / totalTestNo + "");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
