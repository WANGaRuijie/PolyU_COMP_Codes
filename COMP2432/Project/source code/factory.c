/**
 * @file factory.c
 * @brief This file contains the definition of the factory struct and its functions.
 * @note The factory struct is used in the day struct.
 * @see day.h
 * @author Wang Ruijie
 * @date 10 April 2024
*/

#include "order.h"
#include "factory.h"

#include <stdio.h>
#include <stdbool.h>

void init_factory_x(factory *this) {
    this->factory_name = 'x';
    this->capacity_per_day = 300;
    this->is_available = true;
    this->quantity_produced = 0;
    this->order_produced = NULL;

}

void init_factory_y(factory *this) {
    this->factory_name = 'y';
    this->capacity_per_day = 400;
    this->is_available = true;
    this->quantity_produced = 0;
    this->order_produced = NULL;
}

void init_factory_z(factory *this) {
    this->factory_name = 'z';
    this->capacity_per_day = 500;
    this->is_available = true;
    this->quantity_produced = 0;
    this->order_produced = NULL;
}

void allocate_order(factory *this, order *order) {
    this->order_produced = order;
}

void modify_factory_status(factory *this, bool status) {
    this->is_available = status;
}

void modify_quantity_produced(factory *this, int quantity_produced) {
    this->quantity_produced = quantity_produced;
}

char get_factory_name(factory *this) {
    return this->factory_name;
}

int get_capacity_per_day(factory *this) {
    return this->capacity_per_day;
}

bool get_factory_status(factory *this) {
    return this->is_available;
}

order* get_order_produced(factory *this) {
    return this->order_produced;
}

int get_quantity_produced(factory *this) {
    return this->quantity_produced;
}