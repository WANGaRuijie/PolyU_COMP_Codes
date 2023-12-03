package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Rational {

    private int numerator;
    private int denominator;

    public Rational(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Rational add(Rational other) {
        int denominatorUnified = this.denominator * other.denominator;
        int numeratorConverted = this.numerator * other.denominator;
        int numeratorConvertedOther = other.numerator * this.denominator;
        int numeratorConvertedSum = numeratorConverted + numeratorConvertedOther;
        return new Rational(numeratorConvertedSum, denominatorUnified);
    }

    public Rational subtract(Rational other) {
        int denominatorUnified = this.denominator * other.denominator;
        int numeratorConverted = this.numerator * other.denominator;
        int numeratorConvertedOther = other.numerator * this.denominator;
        int numeratorConvertedSum = numeratorConverted - numeratorConvertedOther;
        return new Rational(numeratorConvertedSum, denominatorUnified);
    }

    public Rational multiply(Rational other) {
        int numeratorMultiplied = this.numerator * other.numerator;
        int denominatorMultiplied = this.denominator * other.denominator;
        return new Rational(numeratorMultiplied, denominatorMultiplied);
    }

    public Rational divide(Rational other) {
        int numeratorDivided = this.numerator * other.denominator;
        int denominatorDivided = this.denominator * other.numerator;
        return new Rational(numeratorDivided, denominatorDivided);
    }

    public String toString() {
        return numerator + "/" + denominator;
    }

    public void simplify() {
        if (this.numerator != 0) {
            int numeratorAbsolute = this.numerator;
            if (this.numerator < 0) {
                numeratorAbsolute = -this.numerator;
            }
            int GreatestCommonDivisor = calculateGreatestCommonDivisor(numeratorAbsolute, this.denominator);
            this.numerator /= GreatestCommonDivisor;
            this.denominator = this.denominator / GreatestCommonDivisor;
        } else {
            this.denominator = 1;
        }
    }

    private int calculateGreatestCommonDivisor(int a, int b) {
        // Only for positive numbers.
        int smaller = a;
        int greatestCommonDivisor = 1;
        if (a > b) {
            smaller = b;
        }
        for (int i = 1; i <= smaller; i++)
            if (a % i == 0 && b % i == 0) {
                greatestCommonDivisor = i;
            }
        return greatestCommonDivisor;
    }

}
