#include <iostream>
#include <math.h>

using namespace std;

int main() {

    int arr[1000], num, i = 0, sum = 0, input;
    cout << "Enter a sequence of integers (-999 to finish): ";
    
    do{
        cin >> input;
        arr[i] = input;
        i++;
    }
    while (input != -999 && i < 1000);

    for (int k = i-1; k<1000; k++){
        arr[k] = 0;
    }
    
    for (int j = 0; j < 1000; j++) {
        sum += pow(-1, j) * arr[j];
    }

    cout << sum << endl;

    return 0;
}
