#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>

#define NUM_CHILDREN 4
#define NUM_CARDS 52
#define NUM_CARDS_PER_PLAYER 13
#define NUM_CARDS_PER_SUIT 13

int getCardValue(char card[3]);
int calculatePoints(char cards[NUM_CARDS][3]);
int calculateAdjustedPoints(char suit[3*NUM_CARDS_PER_SUIT]);
void printSortedSuits(char *spades, char *hearts, char *diamonds, char *clubs);

int main(int argc, char *argv[]) {

    int PARENTID = getpid();
    int myid;

    pid_t child_pids[NUM_CHILDREN];

    int i, j, x, y;

    char cards[13][3];

    for (i = 0; i < NUM_CHILDREN; i++) {

        myid = getpid();

        if (myid == PARENTID) {
            child_pids[i] = fork();

            if (child_pids[i] < 0) {
                printf("Fork Failed\n");
                exit(1);

            } else if (child_pids[i] == 0) {

                int k = 0;

                // Assign cards to each child process

                for (j = 1; j < argc; j++) {
                    if ((j - 1) % NUM_CHILDREN == i) { 
                        cards[k][0] = argv[j][0];
                        cards[k][1] = argv[j][1];
                        cards[k][2] = '\0';
                        k++;
                    }
                }

                // Concatenate assigned cards into a string

                char result[3*NUM_CARDS] = "";

                for (j = 0; j < k; j++) {
                    strcat(result, cards[j]);
                    if (j < k - 1) {
                        strcat(result, " ");
                    }
                }

                char spades[3*NUM_CARDS_PER_SUIT] = "";
                char hearts[3*NUM_CARDS_PER_SUIT] = "";
                char diamonds[3*NUM_CARDS_PER_SUIT] = "";
                char clubs[3*NUM_CARDS_PER_SUIT] = "";

                printf("Child %d, pid %d: %s\n", i, getpid(), result);

                // Sort assigned cards in descending order

                for (x = 0; x < k - 1; x++) {
                    for (y = x + 1; y < k; y++) {
                        if (getCardValue(cards[x]) < getCardValue(cards[y])) {
                            char temp[3];
                            strcpy(temp, cards[x]);
                            strcpy(cards[x], cards[y]);
                            strcpy(cards[y], temp);
                        }
                    }
                }

                // Categorize cards by suit

                for (j = 0; j < k; j++) {
                    switch (cards[j][0]) {
                        case 'S':
                            strcat(spades, cards[j]);
                            strcat(spades, " ");
                            break;
                        case 'H':
                            strcat(hearts, cards[j]);
                            strcat(hearts, " "); 
                            break;
                        case 'C':  
                            strcat(clubs, cards[j]);
                            strcat(clubs, " "); 
                            break;            
                        case 'D':
                            strcat(diamonds, cards[j]);
                            strcat(diamonds, " ");
                            break;
                    }
                }

                // Remove trailing space from each suit string
                
                char *p = spades + strlen(spades) - 1;
                *p = '\0';
                p = hearts + strlen(hearts) - 1;
                *p = '\0';
                p = clubs + strlen(clubs) - 1;
                *p = '\0';
                p = diamonds + strlen(diamonds) - 1;
                *p = '\0';

                // Print sorted suits and calculate points

                printf("Child %d, pid %d: ", i, getpid());
                printSortedSuits(spades, hearts, diamonds, clubs);
                int points = calculatePoints(cards);
                int adjustedPoints = points + calculateAdjustedPoints(spades) + calculateAdjustedPoints(hearts) + calculateAdjustedPoints(clubs) + calculateAdjustedPoints(diamonds);
                printf("Child %d, pid %d: %d points, %d adjusted points\n", i + 1, getpid(), points, adjustedPoints);

                exit(0);
            }
        }
    }

    // Wait for all child processes to finish

    if (myid == PARENTID) {
        for (i = 0; i < NUM_CHILDREN; i++) {
            waitpid(child_pids[i], NULL, 0);
        }
    }

    return 0;
}

// Function to translate the value of a card

int getCardValue(char card[3]) {
    int value = 0;
    switch (card[1]) {
        case 'T':
            value = 10;
            break;
        case 'J':
            value = 11;
            break;
        case 'Q':
            value = 12;
            break;
        case 'K':
            value = 13;
            break;
        case 'A':
            value = 14;
            break;
        default:
            value = card[1] - '0'; 
            break;
    }
    return value;
}

// Function to calculate points of honor cards

int calculatePoints(char cards[NUM_CARDS_PER_PLAYER][3]) {
    int points = 0, i;
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        switch (cards[i][1]) {
            case 'A':
                points += 4;
                break;
            case 'K':
                points += 3;
                break;
            case 'Q':
                points += 2;
                break;
            case 'J':
                points += 1;
                break;
        }
    }
    return points;
}

// Function to calculate adjusted points based on suit length and honor cards

int calculateAdjustedPoints(char suit[3*NUM_CARDS_PER_SUIT]) {
    int adjustedPoints = 0;
    switch(strlen(suit)) {
        case 0: // void
            adjustedPoints += 3;
            break;
        case 2: // singleton
            adjustedPoints += 2;
            if (suit[1] == 'A' || suit[1] == 'K' || suit[1] == 'Q'|| suit[1] == 'J') {
                adjustedPoints -= 1;
            }
            break;
        case 5: // doubleton
            adjustedPoints += 1;
            break;
        case 14: // five cards
            adjustedPoints += 1;
            break;
        case 17: // six cards
            adjustedPoints += 2;
            break;    
        default: // seven cards or more
            if (strlen(suit) > 17) {
                adjustedPoints += 3;
            }
            break;    
    }
    return adjustedPoints;
}

// Function to print sorted suits

void printSortedSuits(char *spades, char *hearts, char *diamonds, char *clubs) {

    int len[] = {strlen(spades), strlen(hearts), strlen(diamonds), strlen(clubs)};
    char *suits[] = {spades, hearts, diamonds, clubs};
    int m, n;

    for (m = 0; m < 3; m++) {
        for (n = m + 1; n < 4; n++) {
            if (len[m] < len[n]) {
                int tempLen = len[m];
                len[m] = len[n];
                len[n] = tempLen;
                char *tempSuit = suits[m];
                suits[m] = suits[n];
                suits[n] = tempSuit;
            }
        }
    }

    for (m = 0; m < 4; m++) {
        printf("<%s> ", suits[m]);
    }
    printf("\n");
    
}