#include <iostream>
#include <cstring>

using namespace std;

struct Student {
    char name[51];
    int id;
};

int main() {

    Student s[100];
    int i = 0;

    cout << "Enter student names and ID, and input END to finish the input:\n";

    for (i; i < 100; i++) {
        cin >> s[i].name;
        if (strcmp(s[i].name, "END") == 0) {
            break;
        } else {
            cin >> s[i].id;
        }
    }

    for (int j = 0; j < i; j++) {
        for (int k = 0; k < i - j; k++) {

            if (s[k].id > s[k + 1].id) {
                int temp_id = s[k].id;
                s[k].id = s[k + 1].id;
                s[k + 1].id = temp_id;

                char temp_name[51];
                strcpy(temp_name, s[k].name);
                strcpy(s[k].name, s[k + 1].name);
                strcpy(s[k + 1].name, temp_name);
            }
        }
    }

    for (int j = 0; j < i; j++) {
        cout << s[j].name << " " << s[j].id << endl;
    }

    return 0;
}