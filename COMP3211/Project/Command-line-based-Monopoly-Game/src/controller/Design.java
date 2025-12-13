package controller;

import model.Property;
import view.Color;
import view.GameBoard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static model.Game.pressAnyKeyToContinue;
import static controller.Initialization.initiate;
import static model.Property.initializeProperties;
import static view.GameBoard.initializeBoxes;

/**
 * @author Wang Ruijie, Zeng Tianyi
 * This class is responsible for designing a custom game board based on an existing game board file and saving the design to a new game board file.
 * It allows the user to modify the attributes of the properties on the game board.
 * It is also used for going back to the main menu after the design is finished.
 */
public class Design {

    private final GameBoard gameBoard;
    private final ArrayList<Property> properties;

    public Design(String gameBoardData){
        this.gameBoard = new GameBoard(initializeBoxes(gameBoardData));
        this.properties =  initializeProperties(gameBoardData);
    }

    public void printProperty(Property property){
        System.out.println();
        System.out.println("Please select the attribute of the property to modify.");
        System.out.println("    [1] Name: "+property.getName());
        System.out.println("    [2] Price: "+property.getPrice());
        System.out.println("    [3] Rent: "+property.getRent());
    }

    /**
     * Modifies the attributes of a property on the game board according to the user's input.
     * @param property the property to be modified
     */
    public void modifyProperty(Property property) {

        boolean endModification = false;

        while (!endModification) {

            System.out.print("Please enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.next();

            switch (choice) {

                case "1" -> {
                    for (;;) {
                        System.out.print("Please enter a new name (no longer than 12 characters): ");
                        Scanner name = new Scanner(System.in);
                        String newName = name.nextLine();
                        if (newName.length() > 12) {
                            System.out.println();
                            System.out.println("Invalid input. Please enter a name no longer than 12 characters.");
                        } else {
                            System.out.println();
                            System.out.println("The name of the property has been modified from " + property.getName() + " to " + newName + ".");
                            property.setName(newName);
                            pressAnyKeyToContinue();
                            break;
                        }
                    }
                }

                case "2" -> {
                    for (; ; ) {
                        System.out.print("Please enter a new price: ");
                        Scanner price = new Scanner(System.in);
                        String newPrice = price.next();
                        try {
                            Integer.parseInt(newPrice);
                        } catch (NumberFormatException e) {
                            System.out.println();
                            System.out.println("Invalid input. Please enter a valid number.");
                            continue;
                        }

                        if (Integer.parseInt(newPrice) < 0) {
                            System.out.println();
                            System.out.println("Invalid input. Please enter a positive number.");
                        } else {
                            System.out.println();
                            System.out.println("The price of the property has been modified from " + property.getPrice() + " to " + newPrice + ".");
                            property.setPrice(Integer.parseInt(newPrice));
                            pressAnyKeyToContinue();
                            break;
                        }
                    }
                }

                case "3" -> {
                    for (; ; ) {
                        System.out.print("Please enter a new rent: ");
                        Scanner rent = new Scanner(System.in);
                        String newRent = rent.next();
                        try {
                            Integer.parseInt(newRent);
                        } catch (NumberFormatException e) {
                            System.out.println();
                            System.out.println("Invalid input. Please enter a valid number.");
                            continue;
                        }

                        if (Integer.parseInt(newRent) < 0) {
                            System.out.println();
                            System.out.println("Invalid input. Please enter a positive number.");
                        } else {
                            System.out.println();
                            System.out.println("The rent of the property has been modified from " + property.getRent() + " to " + newRent + ".");
                            property.setRent(Integer.parseInt(newRent));
                            pressAnyKeyToContinue();
                            break;
                        }
                    }
                }

                default -> {
                    System.out.println();
                    System.out.println("Invalid input. Please try again.");
                }
            }

            System.out.println();
            System.out.println("Do you want to continue modifying this property?");
            System.out.println("    [1] Yes");
            System.out.println("    [2] No");

            String choice1 = null;
            while (!Objects.equals(choice1, "1") && !Objects.equals(choice1, "2")) {
                System.out.print("Please enter your choice: ");
                Scanner scanner1 = new Scanner(System.in);
                choice1 = scanner1.next();
                if (Objects.equals(choice1, "2")) {
                    endModification = true;
                    break;
                }
                if (Objects.equals(choice1,"1")){
                    break;
                }
                System.out.println();
                System.out.println("Invalid input. Please Try again.");
            }
        }
    }

    /**
     * Saves the design of the game board to a new game board file.
     */
    public void saveBoardDesign() {
        for (;;) {
            System.out.print("Please name the save of the game board file, or enter '" + Color.PURPLE + "t" + Color.RESET + "' to name the game board file with the current time stamp: ");
            Scanner scanner3 = new Scanner(System.in);

            String fileName = scanner3.nextLine();

            if (fileName.equals("t")) {
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
                Date date = new Date(System.currentTimeMillis());
                fileName = formatter.format(date);
            }

            fileName += ".txt";

            if (fileName.equals("defaultGameBoard.txt")) {
                System.out.println();
                System.out.println("You can not overwrite the default game board. Please try again.");
                continue;
            }

            String filePath = "src/data/gameboard/" + fileName;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
                for (Property prop : properties) {
                    writer.write(prop.toString());
                    writer.newLine();
                    writer.flush();
                }
                System.out.println("Your design has been successfully exported to file " + fileName + ".");
                pressAnyKeyToContinue();
                break;

            } catch (IOException e) {
                System.err.println();
                System.err.println("Invalid file name. Please try again.");
            }
        }
    }

    /**
     * Starts the procedure of the design of the game board.
     */
    public void startDesign() {

        boolean endDesign = false;
        System.out.println("The current game board:");
        gameBoard.printBoard();
        pressAnyKeyToContinue();

        String choice;
        ArrayList<String> correctPosition = new ArrayList<>(List.of("2", "3", "5", "7", "8", "10", "12", "14", "15", "17", "18", "20"));

        while (!endDesign) {

            boolean correctInput = false;
            System.out.println();
            System.out.println("Please enter '"+ Color.PURPLE +"f"  + Color.RESET + "' to finish the design, or enter the position number to select the property that you want to modify.");
            Scanner scanner = new Scanner(System.in);

            while(!correctInput) {

                System.out.print("Please enter your choice: ");
                choice = scanner.next();
                if (correctPosition.contains(choice) || Objects.equals(choice, "f")) {
                    correctInput = true;
                } else {
                    System.out.println();
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                switch (choice) {
                    case "2", "3", "5", "7", "8", "10", "12", "14", "15", "17", "18", "20" -> {
                        int index = Integer.parseInt(choice) - 1;
                        printProperty(properties.get(index));
                        modifyProperty(properties.get(index));
                    }
                    case "f" -> endDesign = true;
                    default -> System.out.println("Invalid input. Please try again.");
                }
            }
        }

        System.out.println();
        System.out.println("Do you want to save your design?");
        System.out.println("    [1] Yes");
        System.out.println("    [2] No");

        String choice3 = null;
        while (!Objects.equals(choice3, "1") && !Objects.equals(choice3, "2")) {

            System.out.print("Please enter your choice: ");
            Scanner scanner3 = new Scanner(System.in);
            choice3 = scanner3.next();

            if (Objects.equals(choice3, "1")) {
                saveBoardDesign();
                break;
            }
            else if(Objects.equals(choice3,"2")){
                System.out.println("Your design will not be saved.");
                pressAnyKeyToContinue();
                break;
            }
            else{
                System.out.println();
                System.out.println("Invalid input. Please try again.");
            }

        }

        //Return to the main page
        initiate();
    }

}