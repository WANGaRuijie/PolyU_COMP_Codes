package hk.edu.polyu.comp.comp2021.assignment1.base7;

public class Base7 {
    //Not to add other method
    public static String convertToBase7(int num) {
        if(num == 0)
            return "0";
        boolean isPositive = true;
        if(num<0){
            isPositive = false;
            num = -num;
        }
        String result = "";
        int quotient, remainder;
        while (num > 0) {
            quotient  = num / 7;
            remainder = num % 7;
            result = remainder + result;
            num = quotient;
        }
        if(isPositive){
            return result;
        }
        else
            return "-"+result;
    }

//    public static void main(String[] args) {
//        Base7 base7 = new Base7();
//        System.out.println(base7.convertToBase7(-100));
//    }
}



