#include <iostream>

using namespace std;

struct Linkedlist {
    int data;
    Linkedlist *next;
};

 void printList(Linkedlist **head) {
    Linkedlist *temp = *head;
    while(temp != 0) {
        cout << temp->data << " ";
        temp = temp->next;
    }
    cout << "\n";
}  

void addAtEnd(Linkedlist **head, int value) {

    Linkedlist *newNode = new Linkedlist();
    newNode->data = value;
    newNode->next = 0;
    
    if (*head == 0) {
        *head = newNode;
    } else {
        Linkedlist *temp = *head;
        while(temp->next != 0) {
            temp = temp->next;
        }
        temp->next = newNode;
    }
}

void addAtBeginning(Linkedlist **head, int value) {
    Linkedlist *newNode = new Linkedlist();
    newNode->data = value;
    newNode->next = *head;
    *head = newNode;
}

bool deleteNthNode(Linkedlist **head, int n) {

    int i = 0;
    Linkedlist *pre = *head;
    Linkedlist *cur = (*head)->next;

    if (*head == NULL) {
        return false;
    }
    else if (n == 1) {
        *head = (*head)->next;
        return true;
    }
   
    while (i < n - 2) {
        if (cur == NULL) {
            return false;
        } else {
        pre = cur;
        cur = cur->next;
        ++i;
        }
    }

    if (cur == 0) {
        return false;
    } else {
        pre->next = cur->next;
        delete cur;
        return true;
    }
}

int main() {
    Linkedlist *head = NULL;
    addAtEnd(&head, 4);
    addAtEnd(&head, 5);
    addAtEnd(&head, 6);
    printList(&head);
    addAtBeginning(&head, 3);
    addAtBeginning(&head, 2);
    addAtBeginning(&head, 1);
    printList(&head);
    deleteNthNode(&head, 3);
    printList(&head);
    deleteNthNode(&head, 1);
    printList(&head);
    deleteNthNode(&head, 1);
    printList(&head);
    deleteNthNode(&head, 3);
    printList(&head);
    deleteNthNode(&head, 2);
    printList(&head);
    return 0;
}
