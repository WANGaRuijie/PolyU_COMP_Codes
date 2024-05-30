/**
 * @file order.c
 * @brief This file contains the implementation of the order struct and its functions.
 * @note Remember to compile this file with PLS_G07.c
 * @author Wang Ruijie
 * @date 9 April 2024
*/

#include "order.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void create_order(order *this, char *order_number, char *due_date, char *product_name, int quantity) {
    this->order_number = strdup(order_number);
    this->due_date = strdup(due_date);
    this->product_name = strdup(product_name);
    this->quantity = quantity;
    this->is_completed = false;
    this->is_accepted = true;
    this->next = NULL;
}

void modify_due_date(order *this, char *due_date) {
    this->due_date = due_date;
}

void modify_order_completion(order *this, bool is_completed) {
    this->is_completed = is_completed;
}

void modify_order_acceptance(order *this, bool is_accepted) {
    this->is_accepted = is_accepted;
}

void modify_quantity(order *this, int new_quantity) {
    this->quantity = new_quantity;
}

char* get_order_number(order *this) {
    return this->order_number;
}

char* get_due_date(order *this) {
    return this->due_date;
}

char* get_product_name(order *this) {
    return this->product_name;
}

int get_quantity(order *this) {
    return this->quantity;
}

bool get_order_completion(order *this) {
    return this->is_completed;
}

bool get_order_acceptance(order *this) {
    return this->is_accepted;
}

void copy_order_list(order **head, order **new_list_head){
    /*
     * Check if the head of the original list is null
     * If yes, initialize the new list head as null and return
     */
    if (*head == NULL){
        *new_list_head = NULL;
        return;
    }

    /*
     * Initialize the new list head with the head of the original list
     */
    *new_list_head = malloc(sizeof(order));
    create_order(*new_list_head, get_order_number(*head), get_due_date(*head), get_product_name(*head), get_quantity(*head));

    /*
     * Start from the second node in the original list
     * Meanwhile, maintain the last node of the new list to connect new nodes
     */
    order *current = (*head)->next;
    order *new_list_current = *new_list_head;

    while (current != NULL){
        /*
         * Create a new order with the details of the current order
         */
        order *new_order = malloc(sizeof(order));
        create_order(new_order, get_order_number(current), get_due_date(current), get_product_name(current), get_quantity(current));

        /*
         * Connect the new created order to the new list and move the pointers to the next
         */
        new_list_current->next = new_order;
        new_list_current = new_list_current->next;
        current = current->next;
    }

    /*
     * Terminate the new list
     */
    new_list_current->next = NULL;
}

order* deep_copy_order_list(order *head) {
    order *new_head = NULL;
    order *new_current = NULL;
    
    // Traverse the existing linked list to deep copy each node
    order *current = head;
    while (current != NULL) {
        order *new_order = (order*)malloc(sizeof(order));
        
        // Perform deep copying of the values
        create_order(new_order, current->order_number, current->due_date, current->product_name, current->quantity);
        new_order->is_completed = current->is_completed;
        new_order->is_accepted = current->is_accepted;
        
        // Add the newly copied node to the new linked list
        if (new_head == NULL) {
            new_head = new_order;
            new_current = new_order;
        } else {
            new_current->next = new_order;
            new_current = new_order;
        }
        
        current = current->next;
    }
    
    // Ensure the last node's next pointer is NULL
    if (new_current != NULL) {
        new_current->next = NULL;
    }
    
    return new_head;
}
