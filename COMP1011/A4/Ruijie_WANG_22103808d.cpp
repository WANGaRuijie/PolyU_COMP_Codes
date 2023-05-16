#include <iostream>

using namespace std;

void upSort(int arr[], int);
void downSort(int arr[], int);

int main(){

    int i = 0, a = 0, b = 0, c = 0, d = 0;
    int arr[1000];
    int input;

    cout << "Enter a sequence of integer (-999 to finish): ";
    
    do {
        cin >> input;
        arr[i] = input;
        i++;
    }
    while (input != -999 && i < 1000);

    for (int j = 0; j < i - 1; j++) {
        if (arr[j] % 2 == 0) {
            a++;
        } else {
            b++;
        }
    }

    int arr1[a], arr2[b];

    for (int j = 0; j < i - 1; j++) {
        if (arr[j] % 2 == 0) {
            arr1[c] = arr[j];
            c++;
        } else {
            arr2[d] = arr[j];
            d++;
        }
    }

    upSort(arr2, b);
    downSort(arr1, a);

    for(int j = 0; j < b; j++) {
        cout << arr2[j] << " ";
    }

    for(int j = 0; j < a; j++) {
        cout << arr1[j] << " ";
    }

    return 0;
}

void upSort(int arr[], int size) {
    for (int j = 0; j < size - 1; j++) {
        for (int k = 0; k < size - 1 - j; k++) {
            if (arr[k] > arr[k + 1]) {
                swap(arr[k], arr[k + 1]);
            }
        }
    }
}

void downSort(int arr[], int size) {
    for (int j = 0; j < size - 1; j++) {
        for (int k = 0; k < size - 1 - j; k++) {
            if (arr[k] < arr[k + 1]) {
                swap(arr[k], arr[k + 1]);
            }
        }
    }
}
