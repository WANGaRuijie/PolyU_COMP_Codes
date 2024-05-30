/**
 * @file day.c
 * @brief This file contains the definition of the day struct and its functions.
 * @author Wang Ruijie
 * @date 12 April 2024
*/

#include "day.h"
#include "factory.h"
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void init_day(day *this, factory *x, factory *y, factory *z) {
    this->x = x;
    this->y = y;
    this->z = z;
    this->date = NULL;
    this->next = NULL;
}

factory* get_factory_x(day *this) {
    return this->x;
}

factory* get_factory_y(day *this) {
    return this->y;
}

factory* get_factory_z(day *this) {
    return this->z;
}

/**
 * @brief get the string format of the next date of the given date
 * @param dateStr the given date in the format "YYYY-MM-DD"
 * @param nextDayStr the next date in the format "YYYY-MM-DD"
 * @note the function assumes that the dateStr is valid 
 * @author Wang Ruijie
*/
void get_next_day(const char *dateStr, char *nextDayStr) {
    struct tm t = {0};  
    sscanf(dateStr, "%d-%d-%d", &t.tm_year, &t.tm_mon, &t.tm_mday); 
    t.tm_year -= 1900;  
    t.tm_mon -= 1;      
    time_t nextDay = mktime(&t) + 24*60*60;
    struct tm *next = localtime(&nextDay);
    strftime(nextDayStr, 11, "%Y-%m-%d", next);
}

char* get_date(day *this) {
    return this->date;
}

void set_date(day *this, char *date1, int day_count_from_date1) {
    int i = 0;
    char *date_pointer = (char *)malloc(11 * sizeof(char));
    strcpy(date_pointer, date1);

    for (i = 0; i < day_count_from_date1; i++) {
        get_next_day(date_pointer, date_pointer);
    }
    this->date = date_pointer;
}