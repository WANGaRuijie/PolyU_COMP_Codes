/**
 * @file factory.h
 * @brief This file contains the declaration of the factory struct and its functions.
 * @author Wang Ruijie
 * @date 10 April 2024
*/

#ifndef _FACTORY_H_
#define _FACTORY_H_

#include "order.h"
#include <stdbool.h>

/**
 * @brief This struct represents a factory.
 * @note The factory struct contains the following fields:
 * - factory_name: a char representing the name of the factory
 * - capacity_per_day: an int representing the capacity of the factory per day
 * - is_available: a bool representing the availability of the factory
 * - order_produced: a pointer to the order struct representing the order produced by the factory
 * - quantity_produced: an int representing the number of orders produced by the factory
 * @note The factory struct contains the following functions:
 * - init_factory_x: a function to initialize factory x
 * - init_factory_y: a function to initialize factory y
 * - init_factory_z: a function to initialize factory z
 * - allocate_order: a function to allocate an order to the factory
 * - modify_factory_status: a function to modify the status of the factory
 * - modify_quantity_produced: a function to modify the quantity produced by the factory
 * - get_factory_name: a function to get the name of the factory
 * - get_capacity_per_day: a function to get the capacity of the factory per day
 * - get_factory_status: a function to get the availability of the factory
 * - get_order_produced: a function to get the order produced by the factory
 * - get_quantity_produced: a function to get the quantity produced by the factory
 * @note The factory struct is used in the day struct.
 * @see day.h
 * @author Wang Ruijie
*/

typedef struct factory{
    char factory_name;
    int capacity_per_day;
    bool is_available;
    int quantity_produced;
    order *order_produced;
} factory;

void init_factory_x(factory *this);
void init_factory_y(factory *this);
void init_factory_z(factory *this);

void allocate_order(factory *this, order *order);
void modify_factory_status(factory *this, bool is_available);
void modify_quantity_produced(factory *this, int quantity_produced);

char get_factory_name(factory *this);
int get_capacity_per_day(factory *this);
bool get_factory_status(factory *this);
order* get_order_produced(factory *this);
int get_quantity_produced(factory *this);

#endif