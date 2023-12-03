package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Rational {
    // Todo: add the missing fields

    private int numerator;
    private int denominator;

    public Rational(int numerator, int denominator){
        // Todo: complete the constructor
        setNumerator(numerator);
        setDenominator(denominator);
    }

    public Rational add(Rational other){
        // Todo: complete the method
        return new Rational(getNumerator() * other.getDenominator() + getDenominator() * other.getNumerator(),
                getDenominator() * other.getDenominator());
    }

    public Rational subtract(Rational other){
        // Todo: complete the method
        return new Rational(getNumerator() * other.getDenominator() - getDenominator() * other.getNumerator(),
                getDenominator() * other.getDenominator());
    }

    public Rational multiply(Rational other){
        // Todo: complete the method
        return new Rational(getNumerator() * other.getNumerator(), getDenominator() * other.getDenominator());
    }

    public Rational divide(Rational other){
        // Todo: complete the method
        return new Rational(getNumerator() * other.getDenominator(), getDenominator() * other.getNumerator());
    }

    public String toString(){
        // Todo: complete the method
        return getNumerator() + "/" + getDenominator();
    }

    public void simplify(){
        // Todo: complete the method
        if(getNumerator() == 0)
            setDenominator(1);

        int gcd = gcd(Math.abs(getNumerator()), Math.abs(getDenominator()));
        boolean isNegative = getNumerator() > 0 && getDenominator() <0 || getNumerator() < 0 && getDenominator() > 0;
        setNumerator(isNegative ? - Math.abs(getNumerator()) / gcd : Math.abs(getNumerator()) / gcd);
        setDenominator(Math.abs(getDenominator()) / gcd);
    }

    // ==========================================

    private int getNumerator() {
        return numerator;
    }

    private void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    private int getDenominator() {
        return denominator;
    }

    private void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    //The GCD (Greatest Common Divisor) algorithm is used to find the largest positive integer
    // that divides two or more numbers without leaving a remainder.
    private int gcd(int a, int b){
        if(b == 0)
            return a;
        else
            return gcd(b, a % b);
    }

}
