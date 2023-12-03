package hk.edu.polyu.comp.comp2021.assignment2.employee;

/**
 * Levels of salary.
 */
enum SalaryLevel {
    ENTRY(1), JUNIOR(1.25), SENIOR(1.5), EXECUTIVE(2);

    SalaryLevel(double scale){
        this.scale = scale;
    }

    private final double scale;

    public double getScale(){
        return scale;
    }

}