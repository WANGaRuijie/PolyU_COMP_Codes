#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include <stdbool.h>

#define NUM_CHILDREN 4
#define NUM_CARDS 52
#define NUM_CARDS_PER_PLAYER 13

typedef struct {
    char suit;
    char value;
    bool is_played;
} Card;

Card* string_to_card_array(char cards_string[]) {
    int i;
    Card *cards = (Card *)malloc(NUM_CARDS_PER_PLAYER * sizeof(Card));
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        cards[i].suit = cards_string[3 * i];
        cards[i].value = cards_string[3 * i + 1];
        cards[i].is_played = false;
    }
    return cards;
}

char* card_array_to_string(Card cards[]) {
    int i;
    char *cards_string = (char *)malloc(3 * NUM_CARDS_PER_PLAYER * sizeof(char) + 1);
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        cards_string[3 * i] = cards[i].suit;
        cards_string[3 * i + 1] = cards[i].value;
        cards_string[3 * i + 2] = ' ';
    }
    cards_string[3 * NUM_CARDS_PER_PLAYER] = '\0';
    return cards_string;
}

int suit_evaluate(char suit) {
    switch (suit) {
        case 'S':
            return 4;
        case 'H':
            return 3;
        case 'C':
            return 2;
        case 'D':
            return 1;
    }
    return 0;
}

int value_evaluate(char value) {
    switch (value) {
        case 'A':
            return 14;
        case 'K':
            return 13;
        case 'Q':
            return 12;
        case 'J':
            return 11;
        case 'T':
            return 10;
        default:
            return value - '0';
    }
}

void sort_cards(Card cards[]) {
    int j, k;
    for (j = 0; j < NUM_CARDS_PER_PLAYER; j++) {
        for (k = j + 1; k < NUM_CARDS_PER_PLAYER; k++) {
            if (suit_evaluate(cards[j].suit) < suit_evaluate(cards[k].suit) || (suit_evaluate(cards[j].suit) == suit_evaluate(cards[k].suit) && value_evaluate(cards[j].value) < value_evaluate(cards[k].value))) {
                Card temp = cards[j];
                cards[j] = cards[k];
                cards[k] = temp;
            }
        }
    }
}

int get_discard_card(Card cards[]) {

    int i, highest_card_index = 0, maximum_value = 0;

    // Highest to lowest: S, H, C, D
    int suit_priority[4] = {4, 3, 2, 1}; 
    char suit_order[4] = {'S', 'H', 'C', 'D'};

    // Get SQ
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        if (cards[i].suit == 'S' && cards[i].value == 'Q' && !cards[i].is_played) {
            return i;
        }
    }

    // Get highest heart card
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        if (cards[i].suit == 'H' && value_evaluate(cards[i].value) > maximum_value && !cards[i].is_played) {
            maximum_value = value_evaluate(cards[i].value);
            highest_card_index = i;
        }
    }

    if (maximum_value != 0) {
        return highest_card_index;
    } 

    // Get the highest card
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {

        // Update the highest card
        if (!cards[i].is_played && value_evaluate(cards[i].value) > maximum_value || 
            (value_evaluate(cards[i].value) == maximum_value && 
             suit_evaluate(cards[i].suit) > suit_evaluate(cards[highest_card_index].suit))) {
            maximum_value = value_evaluate(cards[i].value);
            highest_card_index = i;
        }

    }
    return highest_card_index;
}

int get_card_to_play(Card cards[], char suit) {

    int i, minimum_value = 15, minimum_index;

    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        if (cards[i].suit == suit && value_evaluate(cards[i].value) < minimum_value && !cards[i].is_played) {
            minimum_value = value_evaluate(cards[i].value);
            minimum_index = i;
        }
    }

    // Chance of discard
    if (minimum_value == 15) {
        minimum_index = get_discard_card(cards);
    }
    return minimum_index;
}

int get_card_to_lead(Card cards[]) {
    int i, minimum_value = 15, minimum_index;
    for (i = 0; i < NUM_CARDS_PER_PLAYER; i++) {
        if (value_evaluate(cards[i].value) <= minimum_value && !cards[i].is_played) {
            minimum_value = value_evaluate(cards[i].value);
            minimum_index = i;
        }
    }
    return minimum_index;
}

int main() {

    int i, j, k, fd[NUM_CHILDREN][2][2];
    pid_t parent_id = getpid(), child_pids[NUM_CHILDREN];
    char cards_each_player[3 * NUM_CARDS_PER_PLAYER];

    for (i = 0; i < NUM_CHILDREN; i++) {
        if (pipe(fd[i][0]) < 0 || pipe(fd[i][1]) < 0) {
            printf("Pipe Failed\n");
            exit(1);
        }
    }

    if (getpid() == parent_id) {
        for (i = 0; i < NUM_CHILDREN; i++) {
            child_pids[i] = fork();

            if (child_pids[i] < 0) {
                printf("Fork Failed\n");
                exit(1);

            } else if (child_pids[i] == 0) { // child
                
                char buf[3];
                int index_to_play;

                // Close p2c child's write end
                close(fd[i][0][1]);

                // Close c2p child's read end
                close(fd[i][1][0]);
                
                // Read and print allocated cards from parent
                read(fd[i][0][0], cards_each_player, 3 * NUM_CARDS_PER_PLAYER);
                printf("Child %d pid %d: received %s\n", i + 1, getpid(), cards_each_player);

                // Sort and print the arranged cards
                Card *cards = string_to_card_array(cards_each_player);
                sort_cards(cards);
                char *sorted_cards_string = card_array_to_string(cards);
                printf("Child %d pid %d: arranged %s\n", i + 1, getpid(), sorted_cards_string);
                free(sorted_cards_string);

                // Signal to parent that the cards are ready
                write(fd[i][1][1], "OK", 3);

                // Play the game until stop signal is received
                // Each loop is a round of the game
                for(;;) {
                    
                    // Read signal at the beginning of each round
                    read(fd[i][0][0], buf, 3);
                    buf[2] = '\0';

                    // Get the signal to lead
                    if (strcmp(buf, "LD") == 0) {

                        // Get the card to lead
                        index_to_play = get_card_to_lead(cards);
                        cards[index_to_play].is_played = true;
                        buf[0] = cards[index_to_play].suit;
                        buf[1] = cards[index_to_play].value;
                        buf[2] = '\0';

                        // Tell the parent the card to lead
                        write(fd[i][1][1], buf, 3);
                        printf("Child %d pid %d: play %s\n", i + 1, getpid(), buf);

                        // Wait for parent to acknowledge the card
                        read(fd[i][0][0], buf, 3);
                        while (strcmp(buf, "OK") != 0) {
                            sleep(1);
                            read(fd[i][0][0], buf, 3);
                        }

                        // Tell the parent that acknowledge is received
                        write(fd[i][1][1], "OK", 3);

                    } 

                    // Get the signal to play
                    if (buf[0] == 'P') {

                        // Get the suit to play
                        index_to_play = get_card_to_play(cards, buf[1]);

                        // Get the card to play
                        cards[index_to_play].is_played = true;
                        buf[0] = cards[index_to_play].suit;
                        buf[1] = cards[index_to_play].value;
                        buf[2] = '\0';

                        // Tell the parent the card to play
                        write(fd[i][1][1], buf, 3);
                        printf("Child %d pid %d: play %s\n", i + 1, getpid(), buf);

                        // Wait for parent to acknowledge the card
                        read(fd[i][0][0], buf, 3);
                        while (strcmp(buf, "OK") != 0) {
                            sleep(1);
                            read(fd[i][0][0], buf, 3);
                        }

                        // Tell the parent that acknowledge is received
                        write(fd[i][1][1], "OK", 3);

                    }

                    // Get the signal to stop
                    if (strcmp(buf, "ST") == 0) {
                        break;
                    }

                }

                // Close the used pipes and exit
                close(fd[i][0][0]);
                close(fd[i][1][1]);
                exit(0);

            }
        }
        
    }
    
    if (getpid() == parent_id) {

        int round = 1, winner = 0, times_wining[4] = {0, 0, 0, 0}, score[4] = {0, 0, 0, 0};
        char suit_to_play, buf[NUM_CHILDREN][3], cards_won[NUM_CHILDREN][3 * NUM_CARDS];
        int big_winner = 4;

        i = 0;

        // Read the input file until EOF
        char input_buffer[500], c;
        while ((c = getchar()) != EOF) {
            if (c != '\n') {
                input_buffer[i++] = c;
            } else {
                input_buffer[i++] = ' ';
            }
        }
        input_buffer[i] = '\0';

        // Game start
        printf("Parent pid %d: child players are %d %d %d %d\n", parent_id, child_pids[0], child_pids[1], child_pids[2], child_pids[3]);

        // Distribute the cards to the children
        for (i = 0; i < NUM_CHILDREN; i++){

            // Close p2c parent's read end
            close(fd[i][0][0]);
            // Close c2p parent's write end
            close(fd[i][1][1]);
            
            for (j = 0; j < NUM_CARDS_PER_PLAYER; j += 1) {
                strncpy(cards_each_player + 3 * j, input_buffer + 3 * i + 12 * j , 3);
            }
            cards_each_player[3 * NUM_CARDS_PER_PLAYER - 1] = '\0';
            write(fd[i][0][1], cards_each_player, 3 * NUM_CARDS_PER_PLAYER);

            // Read children's signal that the cards are ready
            read(fd[i][1][0], buf[i], 3);
            buf[i][2] = '\0';
        }

        // Wait for all children to finish receiving and arranging the cards and get ready
        for (i = 0; i < NUM_CHILDREN; i++) {
            while (strcmp(buf[i], "OK") != 0) {
                sleep(1);
                read(fd[i][1][0], buf[i], 3);
            }
        }

        // Start the game    
        while (round <= NUM_CARDS_PER_PLAYER) {

            int maximum_value = 0;
            Card cards_played[NUM_CHILDREN];

            // Indicate and notify the child to lead
            printf("Parent pid %d: round %d child %d to lead\n", parent_id, round++, winner + 1);
            write(fd[winner][0][1], "LD", 3);

            // Receive the card to lead
            read(fd[winner][1][0], buf[winner], 3);

            // Record the card to lead
            cards_played[winner] = (Card){buf[winner][0], buf[winner][1], true};
            printf("Parent pid %d: child %d plays %s\n", parent_id, winner + 1, buf[winner]);

            // Acknowledge the card to lead
            suit_to_play = buf[winner][0];
            write(fd[winner][0][1], "OK", 3);

            // Wait for the child to acknowledge the parent's acknowledgement
            read(fd[winner][1][0], buf[winner], 3);


            // Indicate and notify the children to follow the lead
            for (i = 0; i < NUM_CHILDREN; i++) {
                if (i != winner) {
                    buf[i][0] = 'P';
                    buf[i][1] = suit_to_play;
                    buf[i][2] = '\0';
                    
                    // Inform the child to play and the suit of the card to lead
                    write(fd[i][0][1], buf[i], 3);

                    // Receive the card played by the child
                    read(fd[i][1][0], buf[i], 3);

                    // Record the card played by the child
                    cards_played[i] = (Card){buf[i][0], buf[i][1], true};
                    printf("Parent pid %d: child %d plays %s\n", parent_id, i + 1, buf[i]);

                    // Acknowledge the card played by the child
                    write(fd[i][0][1], "OK", 3);

                    // Wait for the child to acknowledge the parent's acknowledgement
                    read(fd[i][1][0], buf[i], 3);

                }
            }


            // Wait for all children to finish playing the card and get ready for the next round
            for (i = 0; i < NUM_CHILDREN; i++) {
                while (strcmp(buf[i], "OK") != 0) {
                    sleep(1);
                    read(fd[i][1][0], buf[i], 3);
                }
            }

            // Determine the winner of this round
            for (i = 0; i < NUM_CHILDREN; i++) {

                // If discard, the player will lose
                if (cards_played[i].suit != suit_to_play) {
                    continue;
                }

                // If not discard, find the player plays the card with the maximum value
                if (value_evaluate(cards_played[i].value) >= maximum_value) {
                    maximum_value = value_evaluate(cards_played[i].value);
                    winner = i;
                }

            }


            // Used for Temporary storing of the cards won by the winner in this round
            char cards_won_by_winner[12];
            strcpy(cards_won_by_winner, card_array_to_string(cards_played));
            cards_won_by_winner[12] = '\0';
            
            // Record the cards won by the winner
            strncpy(cards_won[winner] + 12 * times_wining[winner]++, cards_won_by_winner, 11);
            cards_won[winner][12 * times_wining[winner] - 1] = ' ';

            // Announce the winner of this round
            printf("Parent pid %d: child %d wins the trick\n", parent_id, winner + 1);
            
        }

        // Stop the game
        printf("Parent pid %d: game completed\n", parent_id);
        for (i = 0; i < NUM_CHILDREN; i++) {
            write(fd[i][0][1], "ST", 2);
        }

        // Score each player
        for (i = 0; i < NUM_CHILDREN; i++) {
            for (j = 0; j < strlen(cards_won[i]); j++) {

                // If the player wins a heart, add 1 to the score
                if (cards_won[i][j] == 'H') {
                    score[i] += 1;
                }

                // If the player wins the SQ, add 13 to the score
                if (cards_won[i][j] == 'S' && cards_won[i][j + 1] == 'Q') {
                    score[i] += 13;
                }

                // If the player wins all the hearts and the SQ, the big winner is found
                if (score[i] == 26) {
                    score[i] = 0;
                    big_winner = i;
                }

            }
        }

        // If the big winner is found, the big winner's score is 0 and the other players' scores are 26
        if (big_winner != 4) {
            for (i = 0; i < NUM_CHILDREN; i++) {
                if (i != big_winner) {
                    score[i] = 26;
                }
            }
        }
        
        // Print the score
        printf("Parent pid %d: score = <%d %d %d %d>\n", parent_id, score[0], score[1], score[2], score[3]);

        // Close the used pipes
        for (i = 0; i < NUM_CHILDREN; i++) {
            close(fd[i][0][1]);
            close(fd[i][1][0]);
        }

        // Wait for all children to exit
        for (i = 0; i < NUM_CHILDREN; i++) {
            waitpid(child_pids[i], NULL, 0);
        }
    
    }

    return 0;

}