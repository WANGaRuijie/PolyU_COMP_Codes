/**
 * @file order.h
 * @brief This file contains the declaration of the order struct and its functions.
 * @author Wang Ruijie
 * @date 9 April 2024
*/

#ifndef _ORDER_H_
#define _ORDER_H_

#include <stdbool.h>

/**
 * @brief This struct represents an order.
 * @note The order struct contains the following fields:
 * - order_number: a char pointer representing the order number
 * - due_date: a char pointer representing the due date of the order
 * - product_name: a char pointer representing the name of the product
 * - quantity: an int representing the quantity of the product
 * - is_completed: a bool representing the status of the order
 * - is_accepted: a bool representing the acceptance of the order
 * - next: a pointer to the next order struct
 * @note The order struct contains the following functions:
 * - create_order: a function to create an order
 * - modify_due_date: a function to modify the due date of the order
 * - modify_quantity: a function to modify the quantity of the order
 * - modify_order_completion: a function to modify the status of the order
 * - modify_order_acceptance: a function to modify the acceptance of the order
 * - get_order_number: a function to get the order number
 * - get_due_date: a function to get the due date
 * - get_product_name: a function to get the product name
 * - get_quantity: a function to get the quantity
 * - get_order_completion: a function to get the status of the order
 * - get_order_acceptance: a function to get the acceptance of the order
 * - copy_order_list: a function to copy the order list
 * @note The order struct is used in the factory struct.
 * @see PLS_G07_factory.h
 * @note The operation on the list of orders is done in the main file.
 * @see PLS_G07_main.c
 * @author Wang Ruijie
*/

typedef struct order{
    char *order_number;
    char *due_date;
    char *product_name;
    int quantity;
    bool is_completed;
    bool is_accepted;
    struct order *next;
} order;


void create_order(order *this, char *order_number, char *due_date, char *product_name, int quantity);
void modify_due_date(order *this, char *new_due_date);
void modify_quantity(order *this, int new_quantity);
void modify_order_completion(order *this, bool is_completed);
void modify_order_acceptance(order *this, bool is_accepted);

char* get_order_number(order *this);
char* get_due_date(order *this);
char* get_product_name(order *this);
int get_quantity(order *this);
bool get_order_completion(order *this);
bool get_order_acceptance(order *this);

void copy_order_list(order** head, order** new_list_head);
order* deep_copy_order_list(order *head);

#endif
