/**
 * @file temp.c
 * @brief PolyU COMP2432 Operating Systems Project, Spring 2024
 * @brief A command-line-based steel production line scheduler consists of an input module, a scheduling module, and an output/analysis module
 * @note Please run the program on apollo with the default gcc compiler using the command “gcc -o main main.c factory.c day.c order.c”
 * @authors Wang Ruijie
 * @date 20 April 2024 
*/

#include "order.h"
#include "factory.h"
#include "day.h"

#include <glpk.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>
#include <limits.h>

/**
 * Max buffer size for each line of user input and inter-process communication
*/
#define MAX_BUFFER_SIZE 512

/**
 * Max number of arguments in a command
*/
#define MAX_TOKEN_SIZE 32

/**
 * @brief convert the input string into tokens
 * @param input the input string
 * @return a list of tokens
 * @note input string will be replaced by the first token
 * @author Wang Ruijie
*/
char** get_tokens_from_input(char* input) {
    int i = 0;
    char** tokens = malloc(MAX_TOKEN_SIZE * sizeof(char*));
    char* token = strtok(input, " ");
    while (token != NULL) {
        tokens[i] = malloc(strlen(token) + 1);
        strcpy(tokens[i], token);
        token = strtok(NULL, " ");
        i++;
    }
    tokens[i] = NULL; 

    return tokens;
}

char** get_store_tokens_from_input(char* input, const char* filename) {
    int i = 0;
    char** tokens = malloc(MAX_TOKEN_SIZE * sizeof(char*));
    char* token = strtok(input, " ");
    FILE* file = fopen(filename, "a");

    while (token != NULL) {
        tokens[i] = malloc(strlen(token) + 1);
        strcpy(tokens[i], token);
        fprintf(file, "%s ", token); 
        token = strtok(NULL, " ");
        i++;
    }
    tokens[i] = NULL;
    fprintf(file, "\n"); 
    fclose(file); 

    return tokens;
}

void clear_file(char *filename) {
    FILE *file = fopen(filename, "w"); 
    fclose(file);
}

/**
 * @brief free the memory allocated for the tokens
 * @param tokens the list of tokens
 * @author Wang Ruijie
*/
void free_tokens(char** tokens){
    int i = 0;
    for(; tokens[i] != NULL; i++) {
        free(tokens[i]);
    }
    free(tokens);
}

/**
 * @brief count the number of days between two dates in the format "YYYY-MM-DD"
 * @param date1 the first date
 * @param date2 the second date
 * @return the number of days between the two dates (inclusive of the two dates)
 * @note the function assumes that the two dates are valid and date1 is earlier than date2
 * @author Wang Ruijie
*/
int count_days_between_two_dates(char *date1, char *date2) {

    /**
    * Define a the number of seconds in a day
    */
    long int DAY = 24 * 60 * 60 ;

    /**
     * Variables for time conversion
     * tm1, tm2: tm structure for the two dates
     * t1, t2: time_t structure for the two dates
     * seconds: the number of seconds between the two dates
     * days: the number of days between the two dates
    */
    struct tm tm1, tm2;
    time_t t1, t2;
    double seconds;
    int days;

    /**
     * Parse the date strings into tm structure
    */
    sscanf(date1, "%d-%d-%d", &(tm1.tm_year), &(tm1.tm_mon), &(tm1.tm_mday));
    sscanf(date2, "%d-%d-%d", &(tm2.tm_year), &(tm2.tm_mon), &(tm2.tm_mday));

    /**
     * Adjust the year information, because tm_year is the number of years since 1900
    */
    tm1.tm_year -= 1900;
    tm2.tm_year -= 1900;

    /**
     * Adjust the month information, because tm_mon is the number of months since January
    */
    tm1.tm_mon -= 1;
    tm2.tm_mon -= 1;

    /**
     * Set the time information to 0
    */
    tm1.tm_hour = 0;
    tm1.tm_min = 0;
    tm1.tm_sec = 0;

    tm2.tm_hour = 0;
    tm2.tm_min = 0;
    tm2.tm_sec = 0;

    /**
     * Convert the tm structure to time_t structure
    */
    t1 = mktime(&tm1);
    t2 = mktime(&tm2);

    /**
     * Calculate the number of days between the two dates
    */
    seconds = difftime(t2, t1);

    /**
     * Convert the number of seconds to the number of days
    */
    days = (int)(seconds / DAY) + 1;
    return days;
}

/**
 * @brief add an order to the order list
 * @param head the head of the order list
 * @param is_head a flag indicating whether the head of the order list is empty
 * @param tokens the tokens of the addORDER command
 * @note the function assumes that the tokens are valid and the command is addORDER
 * @author Wang Ruijie
*/
void add_order(order **head, bool *is_head, char **tokens, char *date1, char *date2) {

    char *due_date = tokens[2];

    /**
     * If the due date of the order is later than the end date of the period, set the due date to the end date of the period
    */
    if (strcmp(date2, due_date) < 0) {
        strcpy(due_date, date2);
    }
    
    /**
     * Create a new order
    */
    order *new_order = malloc(sizeof(order));
    create_order(new_order, tokens[1], due_date, tokens[4], atoi(tokens[3]));

    /**
     * If the due date of the order is earlier than the start date of the period, the order cannot be accepted
    */
    if (strcmp(date1, due_date) > 0) {
        modify_order_acceptance(new_order, false);
    }

    /**
     * If the head of the order list is empty, set the new order as the head
     * Otherwise, append the new order to the end of the order list
    */
    if (*is_head) {
        *head = new_order;
        *is_head = false;
    } else {
        order *current = *head;
        while (current->next != NULL) {
            current = current->next;
        }
        current->next = new_order;
    }
}

/**
 * @brief initiate a day and add it to the day list
 * @param head the head of the day list
 * @param is_head a flag indicating whether the head of the day list is empty
 * @author Wang Ruijie
*/
void add_day(day **head, bool *is_head, int day_count, char *date1) {

    day *new_day = malloc(sizeof(day));

    factory *x = malloc(sizeof(factory));
    factory *y = malloc(sizeof(factory));
    factory *z = malloc(sizeof(factory));

    init_factory_x(x);
    init_factory_y(y);
    init_factory_z(z);
    init_day(new_day, x, y, z);

    set_date(new_day, date1, day_count);

    if (*is_head) {
        *head = new_day;
        *is_head = false;
    } else {
        day *current = *head;
        while (current->next != NULL) {
            current = current->next;
        }
        current->next = new_day;
    }
}

/**
 * @brief allocate an order with SRF algorithm that is with the current smallest quantity to the available factories of a given day
 * @param order_to_allocate the order with the smallest quantity that has not been allocated
 * @param current_day the current day
 * @note the function assumes that the order_to_allocate is valid and the current_day is valid
 * @note if an order can be completed within a day, its remaining quantity will not be changed
 * @note if an order cannot be completed within a day, its remaining quantity will be updated
 * @note remember to pass copies of the order list and the day list to the function, as the contents will be changed
 * @author Wang Ruijie
*/
void allocate_order_by_least_capacity_occupied(order **order_to_allocate, day **current_day) {

    factory *x = get_factory_x(*current_day);
    factory *y = get_factory_y(*current_day);
    factory *z = get_factory_z(*current_day);

    bool is_x_available = get_factory_status(x);
    bool is_y_available = get_factory_status(y);
    bool is_z_available = get_factory_status(z);

    int x_capacity = get_capacity_per_day(x);
    int y_capacity = get_capacity_per_day(y);
    int z_capacity = get_capacity_per_day(z);

    int quantity = get_quantity(*order_to_allocate);

    /**
     * If all factories are available, allocate the order to the factory with the smallest capacity
    */
    if (is_x_available && is_y_available && is_z_available) {
        if (quantity <= x_capacity) {
            modify_factory_status(x, false);
            allocate_order(x, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, quantity);
        } else if (quantity <= y_capacity) {
            modify_factory_status(y, false);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, quantity);
        } else if (quantity <= z_capacity) {
            modify_factory_status(z, false);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(z, quantity);
        } else if (quantity <= x_capacity + y_capacity) {
            modify_factory_status(x, false);
            modify_factory_status(y, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(y, quantity - x_capacity);
        } else if (quantity <= x_capacity + z_capacity) {
            modify_factory_status(x, false);
            modify_factory_status(z, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(z, quantity - x_capacity);
        } else if (quantity <= y_capacity + z_capacity) {
            modify_factory_status(y, false);
            modify_factory_status(z, false);
            allocate_order(y, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, y_capacity);
            modify_quantity_produced(z, quantity - y_capacity);
        } else if (quantity <= x_capacity + y_capacity + z_capacity) {
            modify_factory_status(x, false);
            modify_factory_status(y, false);
            modify_factory_status(z, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(y, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(y, y_capacity);
            modify_quantity_produced(z, quantity - x_capacity - y_capacity);
        } else {
            modify_factory_status(x, false);
            modify_factory_status(y, false);
            modify_factory_status(z, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(y, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - x_capacity - y_capacity - z_capacity);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(y, y_capacity);
            modify_quantity_produced(z, z_capacity);
        }
    }

    /**
     * If two factories are available, allocate the order to the two factories with the smallest capacities
    */
    if (is_x_available && is_y_available && !is_z_available) {
        if (quantity <= x_capacity) {
            modify_factory_status(x, false);
            allocate_order(x, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, quantity);
        } else if (quantity <= y_capacity) {
            modify_factory_status(y, false);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, quantity);
        } else if (quantity <= x_capacity + y_capacity) {
            modify_factory_status(x, false);
            modify_factory_status(y, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(y, quantity - x_capacity);
        } else {
            modify_factory_status(x, false);
            modify_factory_status(y, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(y, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - x_capacity - y_capacity);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(y, y_capacity);
        }
    }

    if (is_x_available && !is_y_available && is_z_available) {
        if (quantity <= x_capacity) {
            modify_factory_status(x, false);
            allocate_order(x, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, quantity);
        } else if (quantity <= z_capacity) {
            modify_factory_status(z, false);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(z, quantity);
        } else if (quantity <= x_capacity + z_capacity) {
            modify_factory_status(x, false);
            modify_factory_status(z, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(z, quantity - x_capacity);
        } else {
            modify_factory_status(x, false);
            modify_factory_status(z, false);
            allocate_order(x, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - x_capacity - z_capacity);
            modify_quantity_produced(x, x_capacity);
            modify_quantity_produced(z, z_capacity);
        }
    }

    if (!is_x_available && is_y_available && is_z_available) {
        if (quantity <= y_capacity) {
            modify_factory_status(y, false);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, quantity);
        } else if (quantity <= z_capacity) {
            modify_factory_status(z, false);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(z, quantity);
        } else if (quantity <= y_capacity + z_capacity) {
            modify_factory_status(y, false);
            modify_factory_status(z, false);
            allocate_order(y, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, y_capacity);
            modify_quantity_produced(z, quantity - y_capacity);
        } else {
            modify_factory_status(y, false);
            modify_factory_status(z, false);
            allocate_order(y, *order_to_allocate);
            allocate_order(z, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - y_capacity - z_capacity);
            modify_quantity_produced(y, y_capacity);
            modify_quantity_produced(z, z_capacity);
        }
    }

    /**
     * If only one factory is available, allocate the order to the available factory with the smallest capacity
    */
    if (is_x_available && !is_y_available && !is_z_available) {
        if (quantity <= x_capacity) {
            modify_factory_status(x, false);
            allocate_order(x, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(x, quantity);
        } else {
            modify_factory_status(x, false);
            allocate_order(x, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - x_capacity);
            modify_quantity_produced(x, x_capacity);
        }
    }

    if (!is_x_available && is_y_available && !is_z_available) {
        if (quantity <= y_capacity) {
            modify_factory_status(y, false);
            allocate_order(y, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(y, quantity);
        } else {
            modify_factory_status(y, false);
            allocate_order(y, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - y_capacity);
            modify_quantity_produced(y, y_capacity);
        }
    }

    if (!is_x_available && !is_y_available && is_z_available) {
        if (quantity <= z_capacity) {
            modify_factory_status(z, false);
            allocate_order(z, *order_to_allocate);
            modify_order_completion(*order_to_allocate, true);
            modify_quantity_produced(z, quantity);
        } else {
            modify_factory_status(z, false);
            allocate_order(z, *order_to_allocate);
            modify_quantity(*order_to_allocate, quantity - z_capacity);
            modify_quantity_produced(z, z_capacity);
        }
    }

    /**
     * If no factory is available, back to the main function
     * It will decide whether to move to the next day or to complete the allocation of the orders
    */
}

/**
 * @brief get the start date of an order produced by a factory
 * @param order the order to be produced
 * @param factory_name the name of the factory
 * @param day_head the head of the day list
 * @param date1 the start date of the period
 * @return the start date of the order produced by the factory
 * @note the function assumes that the order is valid and the factory_name is valid
 * @author Wang Ruijie
*/
char* get_order_start_date(order *order, char factory_name, day* day_head, char *date1){

    day *current_day = day_head;
    int day_count = 0 ,i = 0;
    char *start_date = malloc(11);
    strcpy(start_date, date1);

    while(current_day != NULL) {

        if (get_order_produced(get_factory_x(current_day)) == order && factory_name == 'x') {
            break;
        }

        if (get_order_produced(get_factory_y(current_day)) == order && factory_name == 'y') {
            break;
        }

        if (get_order_produced(get_factory_z(current_day)) == order && factory_name == 'z') {
            break;
        }

        current_day = current_day->next;
        day_count++;

        if (current_day->next == NULL) {
            return NULL;
        }
    }

    for (i = 0; i < day_count; i++) {
        get_next_day(start_date, start_date);
    }

    return start_date;
}

/***
 * @brief get the end date of an order produced by a factory
 * @param order the order to be produced
 * @param factory_name the name of the factory
 * @param day_head the head of the day list
 * @param date1 the start date of the period
 * @return the end date of the order produced by the factory
 * @note the function assumes that the order is valid and the factory_name is valid
 * @author Wang Ruijie
*/
char* get_order_end_date(order *order, char factory_name, day *day_head, char *date1){
    
    day *current_day = day_head;
    int day_count = 0, i = 0;
    char *end_date = malloc(11);
    strcpy(end_date, date1);

    while(current_day != NULL) {
        
        if (get_order_produced(get_factory_x(current_day)) == order && factory_name == 'x') {
            while (current_day != NULL && get_order_produced(get_factory_x(current_day)) == order) {
                current_day = current_day->next;
                day_count++;
            }
            day_count--;
            break;
        }

        if (get_order_produced(get_factory_y(current_day)) == order && factory_name == 'y') {
            while (current_day != NULL && get_order_produced(get_factory_y(current_day)) == order) {
                current_day = current_day->next;
                day_count++;
            }
            day_count--;
            break;
        }

        if (get_order_produced(get_factory_z(current_day)) == order && factory_name == 'z') {
            while (current_day != NULL && get_order_produced(get_factory_z(current_day)) == order) {
                current_day = current_day->next;
                day_count++;
            }
            day_count--;
            break;
        }

        current_day = current_day->next;
        day_count++;
    }

    for (i = 0; i < day_count; i++) {
        get_next_day(end_date, end_date);
    }

    return end_date;
}

/**
 * @brief GLPK linear programming solver
 * @param x_max the maximum quantity of factory x
 * @param y_max the maximum quantity of factory y
 * @param z_max the maximum quantity of factory z
 * @param quantity the total quantity of the orders
 * @return the quantities of the orders produced by the three factories
 * @note the function assumes that the input is valid
 * @note the function returns -1 if the solver is infeasible
 * @note the function returns -1 if the solver is unbounded
 * @note the function returns -1 if the solver is undefined
 * @author Wang Ruijie
*/
int* solver(int x_max, int y_max, int z_max, int quantity) {

    printf("-------GLPK solver is now running-------\n"); 
    int is_optimal = 1;
    int* res = (int*)malloc(3*sizeof(int));

    glp_prob *prob = glp_create_prob();
    glp_set_prob_name(prob, "linear programming");
    glp_set_obj_dir(prob, GLP_MAX);

    glp_add_rows(prob, 1);
    glp_set_row_bnds(prob, 1, GLP_LO, (double) quantity, 0.0);

    glp_add_cols(prob, 3);

    if (x_max == 0) {
        glp_set_col_kind(prob, 1, GLP_IV);
        glp_set_obj_coef(prob, 1, 0.0);
        glp_set_col_bnds(prob, 1, GLP_FX, 0.0, 0.0);
    } else {
        glp_set_col_kind(prob, 1, GLP_IV);
        glp_set_obj_coef(prob, 1, -300.0);
        glp_set_col_bnds(prob, 1, GLP_DB, 0.0, (double) x_max);
    }

    if (y_max == 0) {
        glp_set_col_kind(prob, 2, GLP_IV);
        glp_set_obj_coef(prob, 2, 0.0);
        glp_set_col_bnds(prob, 2, GLP_FX, 0.0, 0.0);
    } else {
        glp_set_col_kind(prob, 2, GLP_IV);
        glp_set_obj_coef(prob, 2, -400.0);
        glp_set_col_bnds(prob, 2, GLP_DB, 0.0, (double) y_max);
    }

    if (z_max == 0) {
        glp_set_col_kind(prob, 3, GLP_IV);
        glp_set_obj_coef(prob, 3, 0.0);
        glp_set_col_bnds(prob, 3, GLP_FX, 0.0, 0.0);
    } else {
        glp_set_col_kind(prob, 3, GLP_IV);
        glp_set_obj_coef(prob, 3, -500.0);
        glp_set_col_bnds(prob, 3, GLP_DB, 0.0, (double) z_max);
    }
    
    int ia[1 + 1000], ja[1 + 1000];
    double ar[1 + 1000];
    ia[1] = 1, ja[1] = 1, ar[1] = 300.0; 
    ia[2] = 1, ja[2] = 2, ar[2] = 400.0;
    ia[3] = 1, ja[3] = 3, ar[3] = 500.0;
    glp_load_matrix(prob, 3, ia, ja, ar);

    glp_iocp param;
    glp_init_iocp(&param);
    param.presolve = GLP_ON;
    int err = glp_intopt(prob, &param);

    if (err != 0 || glp_mip_status(prob) != GLP_OPT) {
        is_optimal = 0;
    }

    if (is_optimal) {
        res[0] = (int)glp_mip_col_val(prob, 1);
        res[1] = (int)glp_mip_col_val(prob, 2);
        res[2] = (int)glp_mip_col_val(prob, 3);
    } else {
        res[0] = -1;
        res[1] = -1;
        res[2] = -1;
    }

    printf("-------GLPK solver finished-------\n");
    glp_delete_prob(prob);

    return res;
}


/**
 * @brief main function
*/
int main() {

    /**
     * Clear the input_commands.txt file and the analysis_report.txt file
    */
    clear_file("input_commands.txt");
    clear_file("analysis_report.txt");

    /**
     * Buffer for user input and inter-process communication
    */
    char buffer[MAX_BUFFER_SIZE];

    /**
     * Variables for loop control throughout the program
    */
    int i = 0, j = 0;

    /**
     * Create uni-directional pipes for inter-process communication between the input module and the scheduling module.
    */
    int input_to_scheduling[2], scheduling_to_input[2];
    if (pipe(input_to_scheduling) == -1 || pipe(scheduling_to_input) == -1) {
        printf("Pipe Failed\n");
        exit(1);
    }

    /**
     * Create a new process for the scheduling module whose parent process is the input module.
    */
    pid_t pid = fork();
    if (pid < 0) {
        printf("Fork Failed\n");
        exit(1);

    } else if (pid == 0) {

        /**
         * Create uni-directional pipes for inter-process communication between the scheduling module and the output module.
        */
        int scheduling_to_output[2], output_to_scheduling[2];
        if (pipe(scheduling_to_output) == -1 || pipe(output_to_scheduling) == -1) {
            printf("Pipe Failed\n");
            exit(1);
        }

        /**
         * Create a new process for the output module whose parent process is the scheduling module.
        */
        pid = fork();
        if (pid < 0) {
            printf("Fork Failed\n");
            exit(1);
        } else if (pid == 0) {

            /**
             * The output module starts here
            */

            FILE *output_file;

            for(;;) {
                
                /**
                 * If the contents of the buffer are the same as the previous buffer, there is no new message
                 * Keep reading from the scheduling module until a new message is received
                */
                char previous_buffer[MAX_BUFFER_SIZE];
                strcpy(previous_buffer, buffer);
                while(strcmp(buffer, previous_buffer) == 0) {
                    read(scheduling_to_output[0], buffer, MAX_BUFFER_SIZE);
                }
                buffer[4] = '\0';

                /**
                 * If the message is "exit", tell the scheduling module the message is received
                 * Then break the loop and exit the module
                */
                if (strcmp(buffer, "exit") == 0) {
                    write(output_to_scheduling[1], "OK1", 3);
                    break;
                }

                /**
                 * If the message is "prrp", receive the data from the scheduling module 
                */
                if (strcmp(buffer, "prrp") == 0) {
                    write(output_to_scheduling[1], "OK2", 3);
                    
                    strcpy(previous_buffer, buffer);
                    while(strcmp(buffer, previous_buffer) == 0) {
                        read(scheduling_to_output[0], buffer, MAX_BUFFER_SIZE);
                    }
                    printf("Output saved to file %s\n", buffer);

                    output_file = fopen(buffer, "w");

                    if (output_file == NULL) {
                        printf("Error opening file");
                        exit(1);
                    }
                    write(output_to_scheduling[1], "OK3", 3);

                    strcpy(previous_buffer, buffer);
                    while(strcmp(buffer, previous_buffer) == 0) {
                        read(scheduling_to_output[0], buffer, MAX_BUFFER_SIZE);
                    }
                    write(output_to_scheduling[1], "OK", 2);

                    int days = atoi(buffer);
                    for (i = 0; i < days; i++) {
                        for (j = 0; j < 3; j++) {
                            strcpy(previous_buffer, buffer);
                            while(strcmp(buffer, previous_buffer) == 0) {
                                read(scheduling_to_output[0], buffer, MAX_BUFFER_SIZE);
                            }
                            printf("----------------\n");
                            printf("%s\n", buffer);
                            fprintf(output_file, "%s\n", buffer);

                            /**
                             * If the last day is the last day of the period and the last factory is the last factory, do not send the "OK" message
                            */
                            if (i == (days - 1) && j == 2) {
                                continue;
                            }

                            write(output_to_scheduling[1], "OK", 2);
                        }
                    }

                    fclose(output_file);
                }
            }

            /**
             * Close the used end of the pipes and exit
            */
            close(output_to_scheduling[1]);
            exit(0);

        } else {

            /**
             * The scheduling module starts here
            */

            /**
             * Close the unnecessary ends of the pipes
            */
            close(scheduling_to_input[1]);
            close(scheduling_to_output[0]);

            /**
             * Period starting and ending dates and the number of days between them
            */
            char date1[11], date2[11];
            int days;

            /**
             * Create the head of an order list
            */
            order *order_head = NULL;
            bool is_order_head = true;
                 
            /**
             * Loop for receiving commands until informed to terminate
            */
            for(;;) {
                  
                /**
                 * If the contents of the buffer are the same as the previous buffer, there is no new message
                 * Keep reading from the input module until a new message is received
                */
                char previous_buffer[MAX_BUFFER_SIZE];
                strcpy(previous_buffer, buffer);
                while(strcmp(buffer, previous_buffer) == 0) {
                    read(input_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                }
                
                /**
                 * Convert the input string into tokens
                */
                //char** tokens = get_tokens_from_input(buffer);
                char** tokens = get_store_tokens_from_input(buffer,"input_commands.txt"); // Specify the filename

                /**
                 * Process the addPeriod command
                 * Store the starting and ending dates and the number of days between them
                */
                if (strcmp(tokens[0], "addPERIOD") == 0) {
                    printf("addPERIOD\n");
                    strcpy(date1, tokens[1]);
                    strcpy(date2, tokens[2]);
                    days = count_days_between_two_dates(date1, date2);
                }

                /**
                 * Process the addOrder command
                 * Store the order information in the order list
                */
                if (strcmp(tokens[0], "addORDER") == 0) {
                    printf("addORDER\n");
                    add_order(&order_head, &is_order_head, tokens, date1, date2);
                }

                /**
                 * Process the addBATCH command
                 * Use addORDER to process each line in the batch file
                 * @note unchecked
                */
                if (strcmp(tokens[0], "addBATCH") == 0) {
                    FILE *file = fopen(tokens[1], "r");
                    if (file == NULL) {
                        printf("File failed to open\n");
                        exit(1);
                    }
                    while(fgets(buffer, MAX_BUFFER_SIZE, file)) { 
                        char **line_tokens = get_store_tokens_from_input(buffer, "input_commands.txt");
                        add_order(&order_head, &is_order_head, line_tokens, date1, date2);
                        free_tokens(line_tokens);
                    }
                    fclose(file);
                }

                /**
                 * Process the runPLS command with the SRF algorithm
                */
                if (strcmp(tokens[0], "runPLS") == 0 && strcmp(tokens[1], "SRF") == 0) {
                    printf("runPLS SRF\n");


                    /**
                     * Create the head of a day list
                    */
                    day *day_head = NULL;
                    bool is_day_head = true;

                    /**
                     * Initiate the day list for the period
                    */
                    for(i = 0; i < days; i++) {
                        char *date1_ = malloc(11);
                        strcpy(date1_, date1);
                        add_day(&day_head, &is_day_head, i, date1_);
                    }

                    /**
                     * Set the current day to the first day of the period
                    */
                    day *current_day = day_head;

                    /**
                     * Copy the order list for the period
                    */
                    order *current_order_head = deep_copy_order_list(order_head);

                    order *previous_order = NULL;
                    day *order_start_date = current_day;

                    while(current_day != NULL) {
                        
                        /**
                         * Iterate over the order list every day
                         * Initialize the order with the smallest quantity to be the first order
                         * Set the current order to the first order
                        */
                        order *order_with_smallest_quantity = current_order_head;
                        order *current_order = current_order_head;

                        /**
                         * Find the order with the smallest quantity that has not been allocated and has not been refused
                        */

                        int smallest_quantity = INT_MAX;
                        while(current_order != NULL)  {
                            if (smallest_quantity > get_quantity(current_order) && get_order_completion(current_order) == false && get_order_acceptance(current_order) == true) {
                                smallest_quantity = get_quantity(current_order);
                                order_with_smallest_quantity = current_order;
                            }
                            current_order = current_order->next;
                        }

                        if (order_with_smallest_quantity != previous_order) {
                            previous_order = order_with_smallest_quantity;
                            order_start_date = current_day;
                        }

                        /**
                         * Check if the order with the smallest quantity is due
                         * If the order is due, re-initialize the factories the order occupied and refuse the order
                        */
                        if (strcmp(get_due_date(order_with_smallest_quantity), get_date(current_day)) < 0) {
                            modify_order_acceptance(order_with_smallest_quantity, false);
                            day *order_start_date_copy = order_start_date;
                            while(order_start_date != current_day->next) {
                                if (get_order_produced(get_factory_x(current_day)) == order_with_smallest_quantity) {
                                    init_factory_x(get_factory_x(current_day));
                                }
                                if (get_order_produced(get_factory_y(current_day)) == order_with_smallest_quantity) {
                                    init_factory_y(get_factory_y(current_day));
                                }
                                if (get_order_produced(get_factory_z(current_day)) == order_with_smallest_quantity) {
                                    init_factory_z(get_factory_z(current_day));
                                }
                                order_start_date = order_start_date->next;
                            }
                            order_start_date = order_start_date_copy;
                            current_day = order_start_date_copy;
                            continue;
                        }

                        /**
                         * Allocate the order with the smallest quantity to the available factories of that day
                        */
                        allocate_order_by_least_capacity_occupied(&order_with_smallest_quantity, &current_day);
                        
                        /**
                         * Check if all orders are completed
                        */
                        current_order = current_order_head;
                        bool all_orders_completed = true;

                        while(current_order != NULL) {

                            if (get_order_acceptance(current_order) == false) {
                                current_order = current_order->next;
                                continue;
                            }
                            all_orders_completed = all_orders_completed && get_order_completion(current_order);
                            current_order = current_order->next;
                        }

                        if (all_orders_completed) {
                            break;
                        }

                        /**
                         * Move to the next day if all factories are unavailable
                        */
                        if (get_factory_status(get_factory_x(current_day)) == false && get_factory_status(get_factory_y(current_day)) == false && get_factory_status(get_factory_z(current_day)) == false) {
                            current_day = current_day->next;
                            printf("next day\n");
                            fflush(stdout);
                        }
                    }

                    printf("all orders completed 2\n");
                    fflush(stdout);

                    if (strcmp(tokens[2], "|") == 0 && strcmp(tokens[3], "printREPORT") == 0 && strcmp(tokens[4], ">") == 0) {
                        printf("\n");
                        printf("printREPORT\n");

                        /**
                         * Tell the output module to print the report
                        */
                        write(scheduling_to_output[1], "prrp", 4);

                        /**
                         * If the response is "OK2", send the file name to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK2") == 0) {
                            write(scheduling_to_output[1], tokens[5], strlen(tokens[5]) + 1);
                        }

                        /**
                         * If the response is "OK3", send the number of days to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK3") == 0) {
                            char days_str[10];
                            snprintf(days_str, 10, "%d", days);
                            write(scheduling_to_output[1], days_str, strlen(days_str) + 1);
                        }
                        

                        char *current_date = malloc(11);
                        strcpy(current_date, date1);

                        day *current_day = day_head;

                        for(i = 0; i < days; i++) {

                            factory *x = get_factory_x(current_day);
                            order *order_produced_x = get_order_produced(x);
                            char quantity_produced_x[10];
                            snprintf(quantity_produced_x, 10, "%d", get_quantity_produced(x));

                            factory *y = get_factory_y(current_day);
                            order *order_produced_y = get_order_produced(y);
                            char quantity_produced_y[10];
                            snprintf(quantity_produced_y, 10, "%d", get_quantity_produced(y));

                            factory *z = get_factory_z(current_day);
                            order *order_produced_z = get_order_produced(z);
                            char quantity_produced_z[10];
                            snprintf(quantity_produced_z, 10, "%d", get_quantity_produced(z));

                            strcpy(previous_buffer, buffer);
                            while(strcmp(buffer, previous_buffer) == 0) {
                                read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                buffer[2] = '\0';
                            }

                            if (strcmp(buffer, "OK") == 0) {

                                strcpy(buffer, "");

                                /**
                                 * Only print the order information if the order has been completed
                                */
                                if (order_produced_x != NULL && get_order_completion(order_produced_x) == true) {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), get_product_name(order_produced_x), get_order_number(order_produced_x), quantity_produced_x, get_due_date(order_produced_x));
                                } else {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), "N/A", "N/A", "N/A", "N/A");   
                                }
                                strcat(buffer, "\0");
                                write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                strcpy(previous_buffer, buffer);
                                while(strcmp(buffer, previous_buffer) == 0) {
                                    read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    buffer[2] = '\0';
                                }

                                if (strcmp(buffer, "OK") == 0) {
                                    strcpy(buffer, "");
                                    if (order_produced_y != NULL && get_order_completion(order_produced_y) == true) {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), get_product_name(order_produced_y), get_order_number(order_produced_y), quantity_produced_y, get_due_date(order_produced_y));
                                    } else {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), "N/A", "N/A", "N/A", "N/A");
                                    }
                                    strcat(buffer, "\0");
                                    write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                    strcpy(previous_buffer, buffer);
                                    while(strcmp(buffer, previous_buffer) == 0) {
                                        read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    }
                                    buffer[2] = '\0';

                                    if (strcmp(buffer, "OK") == 0) {
                                        strcpy(buffer, "");
                                        if (order_produced_z != NULL && get_order_completion(order_produced_z) == true) {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), get_product_name(order_produced_z), get_order_number(order_produced_z), quantity_produced_z, get_due_date(order_produced_z));                                 
                                        } else {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), "N/A", "N/A", "N/A", "N/A");
                                        }
                                        strcat(buffer, "\0");
                                        write(scheduling_to_output[1], buffer, strlen(buffer) + 1);
                                    }
                                }
                            
                            }

                            get_next_day(current_date, current_date);
                            current_day = current_day->next;
                        }
                    }

                    /**
                     * Create a new process for analyzing the scheduling results
                    */
                    pid_t ppid = fork();

                    if (ppid < 0) {
                        printf("Fork Failed\n");
                        exit(1);
                    } else if (ppid == 0) {
                        
                        FILE *analysis_file = fopen("analysis_report.txt", "a");

                        /**
                         * The analysis module starts here
                        */
                        order *current_order = current_order_head;
                        day *current_day = day_head;

                        /**
                         * If an order can not be completed within the period, the order can not be accepted
                         * Also find out orders that has been rejected before
                        */
                        int rejected_orders = 0;
                        while (current_order != NULL) {
                            if (get_order_completion(current_order) == false || get_order_acceptance(current_order) == false) {
                                modify_order_acceptance(current_order, false);
                                rejected_orders++;
                            }
                            current_order = current_order->next;
                        }

                        /**
                         * Get the number of orders that have been accepted
                        */
                        int accepted_orders = 0;
                        current_order = current_order_head;
                        while (current_order != NULL) {
                            if (get_order_acceptance(current_order) == true) {
                                accepted_orders++;
                            }
                            current_order = current_order->next;
                        }


                        /**
                         * Continue....
                         * The following part is written by Tony ZENG
                         */
                        printf("The analysis report of this scheduling algorithm is stored in analysis_report.txt\n");
                        fprintf(analysis_file, "***PLS Schedule Analysis Report***\n");
                        fprintf(analysis_file, "Algorithm used: SRF\n");
                        fprintf(analysis_file, "%d orders ACCEPTED. Details are as follows:\n", accepted_orders);
                        fprintf(analysis_file, "ORDER NUMBER     START           END         DAYS        QUANTITY        PLANT\n");
                        fprintf(analysis_file, "===========================================================================================\n");

                        double x_utilization = 0, y_utilization = 0, z_utilization = 0, overall_utilization = 0;
                        int x_days_used = 0, y_days_used = 0, z_days_used = 0;
                        int x_produced_total = 0, y_produced_total = 0, z_produced_total = 0;
                        char *order_number;
                        char *start_date;
                        char *end_date;
                        int days_order = 0;
                        int quantity_produced_all = 0;
                        char *plant;
                        int day_count = 0;

                        order *previous_analyzed_order_x = NULL;
                        order *previous_analyzed_order_y = NULL;
                        order *previous_analyzed_order_z = NULL;

                        while (current_day != NULL) {

                            if (get_factory_status(get_factory_x(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_x(current_day))) == true && get_order_produced(get_factory_x(current_day)) != previous_analyzed_order_x) {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_x(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                x_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++) {
                                    quantity_produced_all += get_quantity_produced(get_factory_x(temp));
                                    temp = temp->next;
                                }

                                x_produced_total += quantity_produced_all;

                                plant = "Plant_X";

                                previous_analyzed_order_x = get_order_produced(get_factory_x(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_y(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_y(current_day))) == true && get_order_produced(get_factory_y(current_day)) != previous_analyzed_order_y)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_y(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);

                                y_days_used += days_order;

                                quantity_produced_all = 0;
                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_y(temp));
                                    temp = temp->next;
                                }
                                y_produced_total += quantity_produced_all;

                                plant = "Plant_Y";

                                previous_analyzed_order_y = get_order_produced(get_factory_y(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_z(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_z(current_day))) == true && get_order_produced(get_factory_z(current_day)) != previous_analyzed_order_z)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_z(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                z_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_z(temp));
                                    temp = temp->next;
                                }
                                z_produced_total += quantity_produced_all;

                                plant = "Plant_Z";

                                previous_analyzed_order_z = get_order_produced(get_factory_z(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d        %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            current_day = current_day->next;
                        }

                        fprintf(analysis_file, "- END -\n");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "==========================================================================================================");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "There are %d Orders REJECTED.    Details are as follows:\n\n", rejected_orders);
                        fprintf(analysis_file, "ORDER NUMBER    PRODUCT NAME    DUE DATE    QUANTITY\n\n");
                        fprintf(analysis_file, "==========================================================================================================\n\n");

                        current_order = current_order_head;
                        char *rejected_number;
                        char *product_name;
                        char *due_time;
                        int Quantity;
                        while (current_order != NULL)
                        {
                            if (get_order_completion(current_order) == false)
                            {
                                modify_order_acceptance(current_order, false);
                                rejected_number = get_order_number(current_order);
                                product_name = get_product_name(current_order);
                                due_time = get_due_date(current_order);
                                Quantity = get_quantity(current_order);
                                fprintf(analysis_file, "%s         %s          %s          %d\n", rejected_number, product_name, due_time, Quantity);
                            }

                            current_order = current_order->next;
                        }
                        fprintf(analysis_file, "- END -\n\n\n");
                        fprintf(analysis_file, "===========================================================================================================\n\n\n");
                        fprintf(analysis_file, "***PERFORMANCE\n\n");
                        fprintf(analysis_file, "PLANT_X:\n");
                        x_utilization = (double)((x_produced_total) * 100) / (double)(x_days_used * 300);
                        fprintf(analysis_file, "Number of days in use: %d days\n", x_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", x_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", x_utilization);
                        fprintf(analysis_file, "PLANT_Y:\n");
                        y_utilization = (double)((y_produced_total) * 100) / (double)(y_days_used * 400);
                        fprintf(analysis_file, "Number of days in use: %d days\n", y_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", y_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", y_utilization);
                        fprintf(analysis_file, "PLANT_Z:\n");
                        z_utilization = (double)((z_produced_total) * 100) / (double)(z_days_used * 500);
                        fprintf(analysis_file, "Number of days in use: %d days\n", z_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", z_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n\n", z_utilization);
                        overall_utilization = (double)((x_produced_total + y_produced_total + z_produced_total) * 100) / (double)(x_days_used * 300 + y_days_used * 400 + z_days_used * 500);
                        fprintf(analysis_file, "Overall utilization: %f %%\n", overall_utilization);
                        fprintf(analysis_file, "- END -");   

                        
                        fclose(analysis_file);
                        exit(0);
                        
                    } else {
                        
                        /**
                         * The scheduling module waits for the analysis module to finish 
                        */
                        waitpid(ppid, NULL, 0);
                    }
                }




                /**
                 * Process the runPLS command with the Priority(lookup category then due date) algorithm
                */

                if (strcmp(tokens[0], "runPLS") == 0 && strcmp(tokens[1], "PR") == 0) {
                    printf("runPLS PR\n");

                    /**
                     * Create the head of a day list
                    */
                    day *day_head = NULL;
                    bool is_day_head = true;
                    
                    /**
                     * Initiate the day list for the period
                    */
                    for(i = 0; i < days; i++) {
                        char *date1_ = malloc(11);
                        strcpy(date1_, date1);
                        add_day(&day_head, &is_day_head, i, date1_);
                    }

                    /**
                     * Set the current day to the first day of the period
                    */
                    day *current_day = day_head;

                    /**
                     * Copy the order list for the period
                    */
                    order *current_order_head = deep_copy_order_list(order_head);

                    order *previous_order = NULL;
                    day *order_start_date = day_head;

                    while(current_day != NULL) {    
                        
                        /**
                         * Iterate over the order list every day
                         * Initialize the order with the closest due date to be the first order
                         * Set the current order to the first order
                        */
                        order *order_with_closest_due = current_order_head;
                        order *current_order = current_order_head;

                        /**
                         * Find the order with the closest due date that has not been allocated
                        */
                        int closest_due = INT_MAX;
                        int due;
                        while(current_order != NULL) {
                            due = count_days_between_two_dates(date1,get_due_date(current_order));
                            if (closest_due > due && get_order_completion(current_order) == false) {
                                closest_due = due;
                                order_with_closest_due = current_order;
                            }
                            current_order = current_order->next;
                        }

                        if (order_with_closest_due != previous_order) {
                            previous_order = order_with_closest_due;
                            order_start_date = current_day;
                        }

                        /**
                         * If the order can not be completed before its due date, the order can not be accepted
                         * Also re-initiate the factories that have been allocated to the order
                        */
                        if (strcmp(get_due_date(order_with_closest_due), get_date(current_day)) < 0) {
                            modify_order_acceptance(order_with_closest_due, false);

                            day *order_start_date_copy = order_start_date;
                            while(order_start_date != current_day->next) {
                                if (get_order_produced(get_factory_x(current_day)) == order_with_closest_due) {
                                    init_factory_x(get_factory_x(current_day));
                                }
                                if (get_order_produced(get_factory_y(current_day)) == order_with_closest_due) {
                                    init_factory_y(get_factory_y(current_day));
                                }
                                if (get_order_produced(get_factory_z(current_day)) == order_with_closest_due) {
                                    init_factory_z(get_factory_z(current_day));
                                }
                                order_start_date = order_start_date->next;
                            }
                            order_start_date = order_start_date_copy;
                            current_day = order_start_date_copy;
                            continue;
                        }

                        /**
                         * Allocate the order with the smallest quantity to the available factories of that day
                        */
                        allocate_order_by_least_capacity_occupied(&order_with_closest_due, &current_day);

                        
                        /**
                         * Check if all orders are completed
                        */
                        current_order = current_order_head;
                        bool all_orders_completed = true;

                        while(current_order != NULL) {

                            if (get_order_acceptance(current_order) == false) {
                                current_order = current_order->next;
                                continue;
                            }
                            all_orders_completed = all_orders_completed && get_order_completion(current_order);
                            current_order = current_order->next;
                        }

                        if (all_orders_completed) {
                            break;
                        }

                        /**
                         * Move to the next day if all factories are unavailable
                        */
                        if (get_factory_status(get_factory_x(current_day)) == false && get_factory_status(get_factory_y(current_day)) == false && get_factory_status(get_factory_z(current_day)) == false) {
                            current_day = current_day->next;
                            printf("next day\n");
                        }
                        
                    }

                    if (strcmp(tokens[2], "|") == 0 && strcmp(tokens[3], "printREPORT") == 0 && strcmp(tokens[4], ">") == 0) {

                        /**
                         * Tell the output module to print the report
                        */
                        write(scheduling_to_output[1], "prrp", 4);

                        /**
                         * If the response is "OK2", send the file name to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK2") == 0) {
                            write(scheduling_to_output[1], tokens[5], strlen(tokens[5]) + 1);
                        }

                        /**
                         * If the response is "OK3", send the number of days to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK3") == 0) {
                            char days_str[10];
                            snprintf(days_str, 10, "%d", days);
                            write(scheduling_to_output[1], days_str, strlen(days_str) + 1);
                        }
                        

                        char *current_date = current_date = malloc(11);
                        strcpy(current_date, date1);
                        day *current_day = day_head;

                        for(i = 0; i < days; i++) {

                            factory *x = get_factory_x(current_day);
                            order *order_produced_x = get_order_produced(x);
                            char quantity_produced_x[10];
                            snprintf(quantity_produced_x, 10, "%d", get_quantity_produced(x));

                            factory *y = get_factory_y(current_day);
                            order *order_produced_y = get_order_produced(y);
                            char quantity_produced_y[10];
                            snprintf(quantity_produced_y, 10, "%d", get_quantity_produced(y));

                            factory *z = get_factory_z(current_day);
                            order *order_produced_z = get_order_produced(z);
                            char quantity_produced_z[10];
                            snprintf(quantity_produced_z, 10, "%d", get_quantity_produced(z));

                            strcpy(previous_buffer, buffer);
                            while(strcmp(buffer, previous_buffer) == 0) {
                                read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[2] = '\0';
                            }
                            if (strcmp(buffer, "OK") == 0) {

                                strcpy(buffer, "");
                                if (order_produced_x != NULL && get_order_completion(order_produced_x) == true) {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), get_product_name(order_produced_x), get_order_number(order_produced_x), quantity_produced_x, get_due_date(order_produced_x));
                                } else {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), "N/A", "N/A", "N/A", "N/A");   
                                }
                                strcat(buffer, "\0");
                                write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                strcpy(previous_buffer, buffer);
                                while(strcmp(buffer, previous_buffer) == 0) {
                                    read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    buffer[2] = '\0';
                                }

                                if (strcmp(buffer, "OK") == 0) {
                                    strcpy(buffer, "");
                                    if (order_produced_y != NULL && get_order_completion(order_produced_y) == true) {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), get_product_name(order_produced_y), get_order_number(order_produced_y), quantity_produced_y, get_due_date(order_produced_y));
                                    } else {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), "N/A", "N/A", "N/A", "N/A");
                                    }
                                    strcat(buffer, "\0");
                                    write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                    strcpy(previous_buffer, buffer);
                                    while(strcmp(buffer, previous_buffer) == 0) {
                                        read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    }
                                    buffer[2] = '\0';

                                    if (strcmp(buffer, "OK") == 0) {
                                        strcpy(buffer, "");
                                        if (order_produced_z != NULL && get_order_completion(order_produced_z) == true) {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), get_product_name(order_produced_z), get_order_number(order_produced_z), quantity_produced_z, get_due_date(order_produced_z));                                 
                                        } else {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), "N/A", "N/A", "N/A", "N/A");
                                        }
                                        strcat(buffer, "\0");
                                        write(scheduling_to_output[1], buffer, strlen(buffer) + 1);
                                    }
                                }
                            
                            }

                            get_next_day(current_date, current_date);
                            current_day = current_day->next;
                        }
                    }

                    /**
                     * Create a new process for analyzing the scheduling results
                    */
                    pid_t ppid = fork();

                    if (ppid < 0) {
                        printf("Fork Failed\n");
                        exit(1);
                    } else if (ppid == 0) {
                        
                        FILE *analysis_file = fopen("analysis_report.txt", "a");

                        /**
                         * The analysis module starts here
                        */
                        order *current_order = current_order_head;
                        day *current_day = day_head;

                        /**
                         * If an order can not be completed within the period, the order can not be accepted
                         * Also find out orders that has been rejected before
                        */
                        int rejected_orders = 0;
                        while (current_order != NULL) {
                            if (get_order_completion(current_order) == false || get_order_acceptance(current_order) == false) {
                                modify_order_acceptance(current_order, false);
                                rejected_orders++;
                            }
                            current_order = current_order->next;
                        }

                        /**
                         * Get the number of orders that have been accepted
                        */
                        int accepted_orders = 0;
                        current_order = current_order_head;
                        while (current_order != NULL) {
                            if (get_order_acceptance(current_order) == true) {
                                accepted_orders++;
                            }
                            current_order = current_order->next;
                        }


                        /**
                         * Continue....
                         * The following part is written by Tony ZENG
                         */
                        printf("The analysis report of this scheduling algorithm is stored in analysis_report.txt ");
                        fprintf(analysis_file, "***PLS Schedule Analysis Report***\n");
                        fprintf(analysis_file, "Algorithm used: Earliest Due Date PR\n");
                        fprintf(analysis_file, "%d orders ACCEPTED. Details are as follows:\n", accepted_orders);
                        fprintf(analysis_file, "ORDER NUMBER     START           END         DAYS        QUANTITY        PLANT\n");
                        fprintf(analysis_file, "===========================================================================================\n");

                        double x_utilization = 0, y_utilization = 0, z_utilization = 0, overall_utilization = 0;
                        int x_days_used = 0, y_days_used = 0, z_days_used = 0;
                        int x_produced_total = 0, y_produced_total = 0, z_produced_total = 0;
                        char *order_number;
                        char *start_date;
                        char *end_date;
                        int days_order = 0;
                        int quantity_produced_all = 0;
                        char *plant;
                        int day_count = 0;

                        order *previous_analyzed_order_x = NULL;
                        order *previous_analyzed_order_y = NULL;
                        order *previous_analyzed_order_z = NULL;

                        while (current_day != NULL) {

                            if (get_factory_status(get_factory_x(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_x(current_day))) == true && get_order_produced(get_factory_x(current_day)) != previous_analyzed_order_x) {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_x(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                x_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++) {
                                    quantity_produced_all += get_quantity_produced(get_factory_x(temp));
                                    temp = temp->next;
                                }

                                x_produced_total += quantity_produced_all;

                                plant = "Plant_X";

                                previous_analyzed_order_x = get_order_produced(get_factory_x(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_y(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_y(current_day))) == true && get_order_produced(get_factory_y(current_day)) != previous_analyzed_order_y)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_y(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);

                                y_days_used += days_order;

                                quantity_produced_all = 0;
                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_y(temp));
                                    temp = temp->next;
                                }
                                y_produced_total += quantity_produced_all;

                                plant = "Plant_Y";

                                previous_analyzed_order_y = get_order_produced(get_factory_y(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_z(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_z(current_day))) == true && get_order_produced(get_factory_z(current_day)) != previous_analyzed_order_z)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_z(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                z_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_z(temp));
                                    temp = temp->next;
                                }
                                z_produced_total += quantity_produced_all;

                                plant = "Plant_Z";

                                previous_analyzed_order_z = get_order_produced(get_factory_z(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d        %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            current_day = current_day->next;
                        }

                        fprintf(analysis_file, "- END -\n");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "==========================================================================================================");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "There are %d Orders REJECTED.    Details are as follows:\n\n", rejected_orders);
                        fprintf(analysis_file, "ORDER NUMBER    PRODUCT NAME    DUE DATE    QUANTITY\n\n");
                        fprintf(analysis_file, "==========================================================================================================\n\n");

                        current_order = current_order_head;
                        char *rejected_number;
                        char *product_name;
                        char *due_time;
                        int Quantity;
                        while (current_order != NULL)
                        {
                            if (get_order_completion(current_order) == false)
                            {
                                modify_order_acceptance(current_order, false);
                                rejected_number = get_order_number(current_order);
                                product_name = get_product_name(current_order);
                                due_time = get_due_date(current_order);
                                Quantity = get_quantity(current_order);
                                fprintf(analysis_file, "%s         %s          %s          %d\n", rejected_number, product_name, due_time, Quantity);
                            }

                            current_order = current_order->next;
                        }
                        fprintf(analysis_file, "- END -\n\n\n");
                        fprintf(analysis_file, "===========================================================================================================\n\n\n");
                        fprintf(analysis_file, "***PERFORMANCE\n\n");
                        fprintf(analysis_file, "PLANT_X:\n");
                        x_utilization = (double)((x_produced_total) * 100) / (double)(x_days_used * 300);
                        fprintf(analysis_file, "Number of days in use: %d days\n", x_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", x_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", x_utilization);
                        fprintf(analysis_file, "PLANT_Y:\n");
                        y_utilization = (double)((y_produced_total) * 100) / (double)(y_days_used * 400);
                        fprintf(analysis_file, "Number of days in use: %d days\n", y_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", y_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", y_utilization);
                        fprintf(analysis_file, "PLANT_Z:\n");
                        z_utilization = (double)((z_produced_total) * 100) / (double)(z_days_used * 500);
                        fprintf(analysis_file, "Number of days in use: %d days\n", z_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", z_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n\n", z_utilization);
                        overall_utilization = (double)((x_produced_total + y_produced_total + z_produced_total) * 100) / (double)(x_days_used * 300 + y_days_used * 400 + z_days_used * 500);
                        fprintf(analysis_file, "Overall utilization: %f %%\n", overall_utilization);
                        fprintf(analysis_file, "- END -");   

                        
                        fclose(analysis_file);
                        exit(0);
                        
                    } else {
                        
                        /**
                         * The scheduling module waits for the analysis module to finish 
                        */
                        waitpid(ppid, NULL, 0);
                    }
                }
                
                
                if (strcmp(tokens[0], "runPLS") == 0 && strcmp(tokens[1], "SGW") == 0) {
                    printf("runPLS SGW\n");

                    /**
                     * Create the head of a day list
                    */
                    day *day_head = NULL;
                    bool is_day_head = true;

                    /**
                     * Initiate the day list for the period
                    */ 
                    for(i = 0; i < days; i++) {
                        char *date1_ = malloc(11);
                        strcpy(date1_, date1);
                        add_day(&day_head, &is_day_head, i, date1_);
                    }

                    int remaining_x = days;
                    int remaining_y = days;
                    int remaining_z = days;

                    /**
                     * Set the current day to the first day of the period
                    */
                    day *current_day = day_head;

                    /**
                     * Copy the order list for the period
                    */
                    order *current_order_head = NULL;
                    copy_order_list(&order_head, &current_order_head);

                    order *current_order = current_order_head;

                    while(current_order != NULL) {
                        
                        int *results = solver(remaining_x, remaining_y, remaining_z, get_quantity(current_order));
                        printf("%d %d %d\n", results[0], results[1], results[2]);
                        if (results[0] != -1 && results[1] != -1 && results[2] != -1) {

                            if (results[0] > count_days_between_two_dates(date1, get_due_date(current_order)) || results[1] > count_days_between_two_dates(date1, get_due_date(current_order)) || results[2] > count_days_between_two_dates(date1, get_due_date(current_order))) {
                                int *results_ = solver(count_days_between_two_dates(date1, get_due_date(current_order)), count_days_between_two_dates(date1, get_due_date(current_order)), count_days_between_two_dates(date1, get_due_date(current_order)), get_quantity(current_order));  
                                printf("%d %d %d\n", results_[0], results_[1], results_[2]);                  
                                if (results_[0] == -1 && results_[1] == -1 && results_[2] == -1) {
                                    modify_order_completion(current_order, false);
                                    modify_order_acceptance(current_order, false);
                                    current_order = current_order->next;
                                    continue;
                                } else {
                                    results[0] = results_[0];
                                    results[1] = results_[1];
                                    results[2] = results_[2];
                                }
                            }

                            remaining_x -= results[0];
                            remaining_y -= results[1];
                            remaining_z -= results[2];
                            modify_order_completion(current_order, true);
                            modify_order_acceptance(current_order, true);

                        } else {

                            modify_order_completion(current_order, false);
                            modify_order_acceptance(current_order, false);
                            current_order = current_order->next;
                            continue;
                        }

                        day *temp = current_day;
                        while (temp != NULL && get_factory_status(get_factory_x(temp)) == false) {
                            temp = temp->next;
                        }

                        for(i = 0; i < results[0]; i++) {

                            modify_factory_status(get_factory_x(temp), false);
                            allocate_order(get_factory_x(temp), current_order);

                            if (get_quantity(current_order) > 300) {
                                modify_quantity_produced(get_factory_x(temp), 300);
                                modify_quantity(current_order, get_quantity(current_order) - 300);
                            } else {
                                modify_quantity_produced(get_factory_x(temp), get_quantity(current_order));
                                modify_quantity(current_order, 0);
                            }

                            temp = temp->next;
                        }

                        temp = current_day;
                        while (temp != NULL && get_factory_status(get_factory_y(temp)) == false) {
                            temp = temp->next;
                        }

                        for(i = 0; i < results[1]; i++) {

                            modify_factory_status(get_factory_y(temp), false);
                            allocate_order(get_factory_y(temp), current_order);

                            if (get_quantity(current_order) > 400) {
                                modify_quantity_produced(get_factory_y(temp), 400);
                                modify_quantity(current_order, get_quantity(current_order) - 400);
                            } else {
                                modify_quantity_produced(get_factory_y(temp), get_quantity(current_order));
                                modify_quantity(current_order, 0);
                            }

                            temp = temp->next;
                        }
                        
                        temp = current_day;
                        while (temp != NULL && get_factory_status(get_factory_z(temp)) == false) {
                            temp = temp->next;
                        }

                        for(i = 0; i < results[2]; i++) {

                            modify_factory_status(get_factory_z(temp), false);
                            allocate_order(get_factory_z(temp), current_order);

                            if (get_quantity(current_order) > 500) {
                                modify_quantity_produced(get_factory_z(temp), 500);
                                modify_quantity(current_order, get_quantity(current_order) - 500);
                            } else {
                                modify_quantity_produced(get_factory_z(temp), get_quantity(current_order));
                                modify_quantity(current_order, 0);
                            }

                            temp = temp->next;
                        }

                        current_order = current_order->next;
                    
                    }


                    if (strcmp(tokens[2], "|") == 0 && strcmp(tokens[3], "printREPORT") == 0 && strcmp(tokens[4], ">") == 0) {

                        /**
                         * Tell the output module to print the report
                        */
                        write(scheduling_to_output[1], "prrp", 4);

                        /**
                         * If the response is "OK2", send the file name to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK2") == 0) {
                            write(scheduling_to_output[1], tokens[5], strlen(tokens[5]) + 1);
                        }

                        /**
                         * If the response is "OK3", send the number of days to the output module
                        */
                        strcpy(previous_buffer, buffer);
                        while(strcmp(buffer, previous_buffer) == 0) {
                            read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[3] = '\0';
                        }
                        if (strcmp(buffer, "OK3") == 0) {
                            char days_str[10];
                            snprintf(days_str, 10, "%d", days);
                            write(scheduling_to_output[1], days_str, strlen(days_str) + 1);
                        }
                        

                        char *current_date = malloc(11);
                        strcpy(current_date, date1);
                        day *current_day = day_head;

                        for(i = 0; i < days; i++) {

                            factory *x = get_factory_x(current_day);
                            order *order_produced_x = get_order_produced(x);
                            char quantity_produced_x[10];
                            snprintf(quantity_produced_x, 10, "%d", get_quantity_produced(x));

                            factory *y = get_factory_y(current_day);
                            order *order_produced_y = get_order_produced(y);
                            char quantity_produced_y[10];
                            snprintf(quantity_produced_y, 10, "%d", get_quantity_produced(y));

                            factory *z = get_factory_z(current_day);
                            order *order_produced_z = get_order_produced(z);
                            char quantity_produced_z[10];
                            snprintf(quantity_produced_z, 10, "%d", get_quantity_produced(z));

                            strcpy(previous_buffer, buffer);
                            while(strcmp(buffer, previous_buffer) == 0) {
                                read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                            buffer[2] = '\0';
                            }
                            if (strcmp(buffer, "OK") == 0) {

                                strcpy(buffer, "");
                                if (order_produced_x != NULL && get_order_completion(order_produced_x) == true) {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), get_product_name(order_produced_x), get_order_number(order_produced_x), quantity_produced_x, get_due_date(order_produced_x));
                                } else {
                                    sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(x), "N/A", "N/A", "N/A", "N/A");   
                                }
                                strcat(buffer, "\0");
                                write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                strcpy(previous_buffer, buffer);
                                while(strcmp(buffer, previous_buffer) == 0) {
                                    read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    buffer[2] = '\0';
                                }

                                if (strcmp(buffer, "OK") == 0) {
                                    strcpy(buffer, "");
                                    if (order_produced_y != NULL && get_order_completion(order_produced_y) == true) {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), get_product_name(order_produced_y), get_order_number(order_produced_y), quantity_produced_y, get_due_date(order_produced_y));
                                    } else {
                                        sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(y), "N/A", "N/A", "N/A", "N/A");
                                    }
                                    strcat(buffer, "\0");
                                    write(scheduling_to_output[1], buffer, strlen(buffer) + 1);

                                    strcpy(previous_buffer, buffer);
                                    while(strcmp(buffer, previous_buffer) == 0) {
                                        read(output_to_scheduling[0], buffer, MAX_BUFFER_SIZE);
                                    }
                                    buffer[2] = '\0';

                                    if (strcmp(buffer, "OK") == 0) {
                                        strcpy(buffer, "");
                                        if (order_produced_z != NULL && get_order_completion(order_produced_z) == true) {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), get_product_name(order_produced_z), get_order_number(order_produced_z), quantity_produced_z, get_due_date(order_produced_z));                                 
                                        } else {
                                            sprintf(buffer, "%s %c %s %s %s %s", current_date, get_factory_name(z), "N/A", "N/A", "N/A", "N/A");
                                        }
                                        strcat(buffer, "\0");
                                        write(scheduling_to_output[1], buffer, strlen(buffer) + 1);
                                    }
                                }
                            
                            }

                            get_next_day(current_date, current_date);
                            current_day = current_day->next;
                        }
                    }

                    /**
                     * Create a new process for analyzing the scheduling results
                    */
                    pid_t ppid = fork();

                    if (ppid < 0) {
                        printf("Fork Failed\n");
                        exit(1);
                    } else if (ppid == 0) {
                        
                        FILE *analysis_file = fopen("analysis_report.txt", "a");

                        /**
                         * The analysis module starts here
                        */
                        order *current_order = current_order_head;
                        day *current_day = day_head;

                        /**
                         * If an order can not be completed within the period, the order can not be accepted
                         * Also find out orders that has been rejected before
                        */
                        int rejected_orders = 0;
                        while (current_order != NULL) {
                            if (get_order_completion(current_order) == false || get_order_acceptance(current_order) == false) {
                                modify_order_acceptance(current_order, false);
                                rejected_orders++;
                            }
                            current_order = current_order->next;
                        }

                        /**
                         * Get the number of orders that have been accepted
                        */
                        int accepted_orders = 0;
                        current_order = current_order_head;
                        while (current_order != NULL) {
                            if (get_order_acceptance(current_order) == true) {
                                accepted_orders++;
                            }
                            current_order = current_order->next;
                        }


                        /**
                         * Continue....
                         * The following part is written by Tony ZENG
                         */
                        printf("The analysis report of this scheduling algorithm is stored in analysis_report.txt ");
                        fprintf(analysis_file, "***PLS Schedule Analysis Report***\n");
                        fprintf(analysis_file, "Algorithm used: SGW\n");
                        fprintf(analysis_file, "%d orders ACCEPTED. Details are as follows:\n", accepted_orders);
                        fprintf(analysis_file, "ORDER NUMBER     START           END         DAYS        QUANTITY        PLANT\n");
                        fprintf(analysis_file, "===========================================================================================\n");

                        double x_utilization = 0, y_utilization = 0, z_utilization = 0, overall_utilization = 0;
                        int x_days_used = 0, y_days_used = 0, z_days_used = 0;
                        int x_produced_total = 0, y_produced_total = 0, z_produced_total = 0;
                        char *order_number;
                        char *start_date;
                        char *end_date;
                        int days_order = 0;
                        int quantity_produced_all = 0;
                        char *plant;
                        int day_count = 0;

                        order *previous_analyzed_order_x = NULL;
                        order *previous_analyzed_order_y = NULL;
                        order *previous_analyzed_order_z = NULL;

                        while (current_day != NULL) {

                            if (get_factory_status(get_factory_x(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_x(current_day))) == true && get_order_produced(get_factory_x(current_day)) != previous_analyzed_order_x) {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_x(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_x(current_day)), 'x', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                x_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++) {
                                    quantity_produced_all += get_quantity_produced(get_factory_x(temp));
                                    temp = temp->next;
                                }

                                x_produced_total += quantity_produced_all;

                                plant = "Plant_X";

                                previous_analyzed_order_x = get_order_produced(get_factory_x(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_y(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_y(current_day))) == true && get_order_produced(get_factory_y(current_day)) != previous_analyzed_order_y)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_y(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_y(current_day)), 'y', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);

                                y_days_used += days_order;

                                quantity_produced_all = 0;
                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_y(temp));
                                    temp = temp->next;
                                }
                                y_produced_total += quantity_produced_all;

                                plant = "Plant_Y";

                                previous_analyzed_order_y = get_order_produced(get_factory_y(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d         %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            if (get_factory_status(get_factory_z(current_day)) == false && get_order_acceptance(get_order_produced(get_factory_z(current_day))) == true && get_order_produced(get_factory_z(current_day)) != previous_analyzed_order_z)
                            {

                                day *temp = current_day;
                                order_number = get_order_number(get_order_produced(get_factory_z(current_day)));
                                start_date = get_order_start_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                end_date = get_order_end_date(get_order_produced(get_factory_z(current_day)), 'z', day_head, date1);
                                days_order = count_days_between_two_dates(start_date, end_date);
                                z_days_used += days_order;

                                quantity_produced_all = 0;

                                for (day_count = 0; day_count < days_order; day_count++)
                                {
                                    quantity_produced_all += get_quantity_produced(get_factory_z(temp));
                                    temp = temp->next;
                                }
                                z_produced_total += quantity_produced_all;

                                plant = "Plant_Z";

                                previous_analyzed_order_z = get_order_produced(get_factory_z(current_day));
                                fprintf(analysis_file, "%s       %s         %s         %d         %d        %s\n", order_number, start_date, end_date, days_order, quantity_produced_all, plant);
                            }

                            current_day = current_day->next;
                        }

                        fprintf(analysis_file, "- END -\n");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "==========================================================================================================");
                        fprintf(analysis_file, "\n\n\n");
                        fprintf(analysis_file, "There are %d Orders REJECTED.    Details are as follows:\n\n", rejected_orders);
                        fprintf(analysis_file, "ORDER NUMBER    PRODUCT NAME    DUE DATE    QUANTITY\n\n");
                        fprintf(analysis_file, "==========================================================================================================\n\n");

                        current_order = current_order_head;
                        char *rejected_number;
                        char *product_name;
                        char *due_time;
                        int Quantity;
                        while (current_order != NULL)
                        {
                            if (get_order_completion(current_order) == false)
                            {
                                modify_order_acceptance(current_order, false);
                                rejected_number = get_order_number(current_order);
                                product_name = get_product_name(current_order);
                                due_time = get_due_date(current_order);
                                Quantity = get_quantity(current_order);
                                fprintf(analysis_file, "%s         %s          %s          %d\n", rejected_number, product_name, due_time, Quantity);
                            }

                            current_order = current_order->next;
                        }
                        fprintf(analysis_file, "- END -\n\n\n");
                        fprintf(analysis_file, "===========================================================================================================\n\n\n");
                        fprintf(analysis_file, "***PERFORMANCE\n\n");
                        fprintf(analysis_file, "PLANT_X:\n");
                        x_utilization = (double)((x_produced_total) * 100) / (double)(x_days_used * 300);
                        fprintf(analysis_file, "Number of days in use: %d days\n", x_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", x_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", x_utilization);
                        fprintf(analysis_file, "PLANT_Y:\n");
                        y_utilization = (double)((y_produced_total) * 100) / (double)(y_days_used * 400);
                        fprintf(analysis_file, "Number of days in use: %d days\n", y_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", y_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n", y_utilization);
                        fprintf(analysis_file, "PLANT_Z:\n");
                        z_utilization = (double)((z_produced_total) * 100) / (double)(z_days_used * 500);
                        fprintf(analysis_file, "Number of days in use: %d days\n", z_days_used);
                        fprintf(analysis_file, "Number of products produced: %d (in total)\n", z_produced_total);
                        fprintf(analysis_file, "Utilization of the plant: %f %%\n\n", z_utilization);
                        overall_utilization = (double)((x_produced_total + y_produced_total + z_produced_total) * 100) / (double)(x_days_used * 300 + y_days_used * 400 + z_days_used * 500);
                        fprintf(analysis_file, "Overall utilization: %f %%\n", overall_utilization);
                        fprintf(analysis_file, "- END -");   

                        
                        fclose(analysis_file);
                        exit(0);
                        
                    } else {
                        
                        /**
                         * The scheduling module waits for the analysis module to finish 
                        */
                        waitpid(ppid, NULL, 0);
                    }
                }


                /**
                 * Process the exitPLS command
                */
                if (strcmp(tokens[0], "exitPLS") == 0) {

                    /**
                     * Send the exit message to the output module
                     * Wait for the output module's response
                    */
                    write(scheduling_to_output[1], "exit", 4);
                    while(strcmp(buffer, "OK1") != 0) {
                        read(output_to_scheduling[0], buffer, 3);
                        buffer[3] = '\0';
                    }

                    /**
                     * If the response is "OK1", free the memory allocated for the tokens and break the loop
                    */
                    if (strcmp(buffer, "OK1") == 0) {
                        free_tokens(tokens);
                        break;
                    }
                }
                
                free_tokens(tokens);
            }

            /**
             * Close the used end of the pipes
            */
            close(scheduling_to_input[1]);

            /**
             * Wait for the output module to exit
             * Then terminate the scheduling module
            */
            waitpid(pid, NULL, 0);
            exit(0);
        }
    
    } else {

        /**
         * The input module starts here
        */

        /**
         * Loop for user input until informed to terminate
        */
        for(;;) {

            printf("Please enter:\n");
            fgets(buffer, MAX_BUFFER_SIZE, stdin);
            buffer[strcspn(buffer, "\n")] = 0;

            /**
             * Tell the scheduling module the user input
            */
            write(input_to_scheduling[1], buffer, strlen(buffer) + 1);

            /**
             * If the user input is "exitPLS", break the loop
            */
            char** tokens = get_tokens_from_input(buffer);
            if (strcmp(tokens[0], "exitPLS") == 0) {
                break;
            }
            free_tokens(tokens);
        }

        /**
         * Close the used end of the pipes
        */
        close(scheduling_to_input[0]);
        close(input_to_scheduling[1]);

        /**
         * Wait for the scheduling module to exit
        */
        waitpid(pid, NULL, 0);
    }

    return 0;
}