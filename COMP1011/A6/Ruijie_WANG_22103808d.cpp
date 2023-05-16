#include <iostream>
#include <cstring>

using namespace std;

int main() {

    char names[200][51];
    int i = 0;

    cout << "Enter student names and input END to finish the input:\n";

    for (i; i < 200; i++) {

        cin.getline(names[i], 50, '\n');
        
        if (strcmp(names[i], "END") == 0) {
            break;
        }

        else if (strlen(names[i]) > 50) {
            cout << "Wrong input: please input no more than 50 letters\n";
        }

        for (int j = 0; j < strlen(names[i]); j++) {
            if (isalpha(names[i][j]) == 0) {
                --i;
                cout << "Wrong input: please input only upper-case and low-case letters with no space in between\n";
                break;
            }
        }
    }

    for (int j = 0; j < i; j++) {
        for (int k = 0; k < strlen(names[j]); k++) {
            if (names[j][k] >= 'a' && names[j][k] <= 'z') {
                names[j][k] -= 32;
            }
        }
    }

    for (int j = 0; j < i - 1; j++) {
        for (int k = 0; k < i - 1 - j; k++) {
            if (strcmp(names[k], names[k + 1]) > 0) {
                swap(names[k], names[k + 1]);
            }
        }
    }
    
    for (int j = 0; j < i; j++) {
        cout << names[j] << endl;
    }

    return 0;
}