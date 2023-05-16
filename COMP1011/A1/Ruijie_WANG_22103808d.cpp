#include <iostream>
#include <iomanip>
#include <math.h>

using namespace std;

int main(){

    double x, y, z; 

    cout << "MENU\n\t1. Divide, a/b\n\t2. Multiply, a*b\n\t3. Power, a^b\n";
    cout << "\t4. Square root, sqrt(a)\n";
    cout << "Enter your choice: ";

    cin >> x;

    if (x!=1 && x!=2 && x!=3 && x!=4){
        cout << "Error";
    }
    else if (x != 4){
        cout << "Enter two numbers: ";
        cin >> y >> z;

        if (int(y)-y < 0 || int(z)-z < 0){
            cout << "Error; Only the integer is supported.";
        }
        else{
            switch(int(x)){
                case 1:
                    if (z == 0){
                        cout << "Error";
                    }
                    else{
                        cout << int(y) << "/" << int(z) << "=" << setprecision(3) << fixed << y / z;
                    }
                    break;
                case 2:
                    cout << int(y) << "*" << int(z) << "=" << setprecision(3) << fixed << y * z;
                    break;
                case 3:
                    cout << int(y) << "^" << int(z) << "=" << setprecision(3) << fixed << pow(y, z);
                    break;
            }
        }
    }
    else if (x == 4){
        cout << "Enter a number: ";
        cin >> y;
        if (y < 0){
            cout << "Error";
        }
        else if (int(y)-y < 0){
            cout << "Error; Only the integer is supported.";
        }
        else{
             cout << "sqrt(" << int(y) << ")=" << setprecision(3) << fixed << sqrt(y);
        }
    }
    return 0;
}        



