#include <iostream>
#include <cstring>
using namespace std;

void rotate(char*, int*);

int main() {
    char array[101];
    cin >> array;
    int size = strlen(array);
    rotate(array, &size);
    return 0;
}

void rotate(char *charArray, int *sizeOfArray) {
    for (int i = 0; i < *sizeOfArray; i++) {
        for (int j = 0; j < *sizeOfArray - 1; j++) {
            swap(*(charArray + j), *(charArray + j + 1));
        }
        for (int j = 0; j < *sizeOfArray; j++) {
            cout << *(charArray + j);
            if (j == *sizeOfArray - 1) {
                cout << endl;
            }
        }
    }
}