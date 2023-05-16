#include <iostream>
#include <iomanip>
using namespace std;

int getMax(int arr[], int);
int getMin(int arr[], int);
int getSum(int arr[], int);
double getMean(int arr[], int);

int main() {

    int temp_arr[1000];
    int input, i = 0;

    cout << "Enter a sequence of integer (-999 to finish): ";
    
    do {
        cin >> input;
        temp_arr[i] = input;
        i++;
    }
    while (input != -999 && i < 1000);

    int arr[i-1];

    for (int j = 0; j < i - 1; j++) {
        arr[j] = temp_arr[j];
    }

    cout << "Largest number is " << getMax(arr, i - 1) << endl;
    cout << "Smallest number is " << getMin(arr, i - 1) << endl;
    cout << "Total is " << getSum(arr, i - 1) << endl;
    cout << "Average is " << fixed << setprecision(3) << getMean(arr, i - 1) << endl;

    return 0;
}

int getMax(int arr[], int size) {
    int temp = arr[0];
    for (int i = 0; i < size; i++) {
        if (temp < arr[i]) {
            temp = arr[i];
        }
    }
    return temp;
}

int getMin(int arr[], int size) {
    int temp = arr[0];
    for (int i = 0; i < size; i++) {
        if (temp > arr[i]) {
            temp = arr[i];
        }
    }
    return temp;
}

int getSum(int arr[], int size) {
    int temp = 0;
    for (int i = 0; i< size; i++) {
        temp += arr[i];
    }
    return temp;
}

double getMean(int arr[], int size) {
    int sum = getSum(arr, size);
    double size1 = size;
    double sum1 = sum;
    double mean = sum1/size1;
    return mean;
}
