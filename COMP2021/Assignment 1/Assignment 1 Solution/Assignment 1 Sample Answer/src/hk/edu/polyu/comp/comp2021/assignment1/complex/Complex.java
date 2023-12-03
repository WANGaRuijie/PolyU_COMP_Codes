package hk.edu.polyu.comp.comp2021.assignment1.complex;

public class Complex {

    // Todo: add the missing fields

    private Rational real;
    private Rational imag;

    public Complex(Rational real, Rational imag) {
        // Todo: complete the constructor
        setReal(real);
        setImag(imag);
    }

    public Complex add(Complex other) {
        // Todo: complete the method
        return new Complex(getReal().add(other.getReal()), getImag().add(other.getImag()));
    }

    public Complex subtract(Complex other) {
        // Todo: complete the method
        return new Complex(getReal().subtract(other.getReal()), getImag().subtract(other.getImag()));
    }

    public Complex multiply(Complex other) {
        // Todo: complete the method
        //z1 = a + bi
        //z2 = c + di
        //
        //To multiply z1 and z2, you can use the formula:
        //
        //z = (a + bi) * (c + di)
        //  = (a * c - b * d) + (a * d + b * c)i
        return new Complex(getReal().multiply(other.getReal()).subtract(getImag().multiply(other.getImag())),
                getReal().multiply(other.getImag()).add(getImag().multiply(other.getReal())));
    }


    public Complex divide(Complex other) {

        // Todo: complete the method
        // you may assume 'other' is never equal to '0+/-0i'.
        //z1 = a + bi
        //z2 = c + di
        //
        //To divide z1 by z2, you can use the formula:
        //
        //z = (a + bi) / (c + di)
        //= [(a * c + b * d) + (b * c - a * d)i] / (c^2 + d^2)
        Rational a = this.getReal();
        Rational b = this.getImag();

        Rational c = other.getReal();
        Rational d = other.getImag();

        Rational real_num = a.multiply(c).add(b.multiply(d));
        Rational imag_num = b.multiply(c).subtract(a.multiply(d));

        Rational den = c.multiply(c).add(d.multiply(d));

        return new Complex(real_num.divide(den), imag_num.divide(den));
    }

    public void simplify() {
        // Todo: complete the method
        this.real.simplify();
        this.imag.simplify();
    }

    public String toString() {
        // Todo: complete the method
        return "(" + getReal().toString() + "," + getImag().toString() + ")";
    }

    // ===========================


    private Rational getReal() {
        return real;
    }

    private void setReal(Rational real) {
        this.real = real;
    }

    private Rational getImag() {
        return imag;
    }

    private void setImag(Rational imag) {
        this.imag = imag;
    }
}
