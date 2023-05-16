#include <iostream>
using namespace std;
int main(){
    int n;
    char letter = 'A';
    cout << "Please input the branch size: ";
    cin >> n;
    for (int i=1; i<=n; i++){
        for (int j=1; j<=n-i; j++){
            cout << " ";
        }
        for (int k=1; k<=2*i-1; k++){
            cout << letter;
            letter == 'Z' ? letter = 'A' : letter++;
            if (k==2*i-1){
                cout << "\n";
            }
        }
    }
    for (int l=1; l<=n/2; l++){
        for (int m=1; m<=n-2; m++){
            cout << " ";
        }
        cout << letter << " " << letter << endl;
        letter == 'Z' ? letter = 'A' : letter++;
    }
    return 0;
}
