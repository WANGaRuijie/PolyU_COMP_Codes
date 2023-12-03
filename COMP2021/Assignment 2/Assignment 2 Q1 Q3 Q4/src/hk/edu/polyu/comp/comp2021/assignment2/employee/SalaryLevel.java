package hk.edu.polyu.comp.comp2021.assignment2.employee;

/**
 * Levels of salary.
 */
enum SalaryLevel {
    ENTRY(1), JUNIOR(1.25), SENIOR(1.5), EXECUTIVE(2);

    // Add missing code here.
    private final double scale;

    SalaryLevel(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

}