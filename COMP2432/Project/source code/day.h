/**
 * @file factory.h
 * @brief This file contains the declaration of the day struct and its functions.
 * @author Wang Ruijie
 * @date 11 April 2024
*/

#ifndef _DAY_H_
#define _DAY_H_

#include "factory.h"

/**
 * @brief This struct represents a day.
 * @note The day struct contains the following fields:
 * - x: a pointer to the factory struct representing factory x of that day
 * - y: a pointer to the factory struct representing factory y of that day
 * - z: a pointer to the factory struct representing factory z of that day
 * @note The day struct contains the following functions:
 * - init_day: a function to initialize a day
 * - get_factory_x: a function to get factory x of that day
 * - get_factory_y: a function to get factory y of that day
 * - get_factory_z: a function to get factory z of that day
 * @author Wang Ruijie
*/
typedef struct day{
    factory *x;
    factory *y;
    factory *z;
    char *date;
    struct day *next;
} day;

void init_day(day *day, factory *x, factory *y, factory *z);

factory* get_factory_x(day *day);
factory* get_factory_y(day *day);
factory* get_factory_z(day *day);

void get_next_day(const char *dateStr, char *nextDayStr);

char* get_date(day *day);
void set_date(day *day, char *date1, int day_count_from_date1);

#endif