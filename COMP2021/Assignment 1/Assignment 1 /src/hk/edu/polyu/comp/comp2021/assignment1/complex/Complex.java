package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Complex {

    private Rational real;
    private Rational imag;


    public Complex(Rational real, Rational imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex add(Complex other) {
        Rational realAdded = this.real.add(other.real);
        Rational imagAdded = this.imag.add(other.imag);
        return new Complex(realAdded, imagAdded);
    }

    public Complex subtract(Complex other) {
        Rational realSubtracted = this.real.subtract(other.real);
        Rational imagSubtracted = this.imag.subtract(other.imag);
        return new Complex(realSubtracted, imagSubtracted);
    }

    public Complex multiply(Complex other) {

        Rational realMinuend = this.real.multiply(other.real);
        Rational realSubtrahend = this.imag.multiply(other.imag);
        Rational realMultiplied = realMinuend.subtract(realSubtrahend);

        Rational imagAddend1 = this.imag.multiply(other.real);
        Rational imagAddend2 = this.real.multiply(other.imag);
        Rational imagMultiplied = imagAddend1.add(imagAddend2);

        return new Complex(realMultiplied, imagMultiplied);
    }

    public Complex divide(Complex other) {

        Rational realNumeratorAddend1 = this.real.multiply(other.real);
        Rational realNumeratorAddend2 = this.imag.multiply(other.imag);
        Rational realNumerator = realNumeratorAddend1.add(realNumeratorAddend2);

        Rational realDenominatorAddend1 = other.real.multiply(other.real);
        Rational realDenominatorAddend2 = other.imag.multiply(other.imag);

        Rational Denominator = realDenominatorAddend1.add(realDenominatorAddend2);

        Rational realDivided = realNumerator.divide(Denominator);

        Rational imagNumeratorMinuend = this.imag.multiply(other.real);
        Rational imagNumeratorSubtrahend = this.real.multiply(other.imag);
        Rational imagNumerator = imagNumeratorMinuend.subtract(imagNumeratorSubtrahend);

        Rational imagDivided = imagNumerator.divide(Denominator);

        return new Complex(realDivided, imagDivided);
    }

    public void simplify() {
        this.real.simplify();
        this.imag.simplify();
    }

    public String toString() {
        return "(" + real.toString() + "," + imag.toString() + ")";
    }

}
