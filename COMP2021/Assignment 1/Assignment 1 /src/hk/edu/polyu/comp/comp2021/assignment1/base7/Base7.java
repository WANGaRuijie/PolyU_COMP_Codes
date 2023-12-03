package hk.edu.polyu.comp.comp2021.assignment1.base7;

public class Base7 {

    public static String convertToBase7(int X) {
        String converted = "";
        boolean positive = true;
        if (X < 0) {
            positive = false;
            X = -X;
        }
        while (X > 0) {
            int remainder = X % 7;
            X = X / 7;
            converted = remainder + converted;
        }

        if (!positive) {
            converted = "-" + converted;
        }
        return converted;
    }
}