package model;

import java.util.Scanner;

/**
 * This class represents a command line scanner that reads input from the user.
 * It provides a method to retrieve the entered command line.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class InputScanner {

    private String input;

    /**
     * Constructs a CommandLineScanner object and reads the command line from the user.
     */
    public InputScanner() {
        Scanner input = new Scanner(System.in);
        if (input.hasNextLine()) {
            this.input = input.nextLine();
        }
    }

    /**
     * Returns the command line entered by the user.
     *
     * @return the command line entered by the user
     */
    public String getInput() {
        return this.input;
    }

}
