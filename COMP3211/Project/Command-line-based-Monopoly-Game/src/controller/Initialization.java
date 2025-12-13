package controller;

import model.Game;
import view.Color;

import java.io.File;
import java.util.Scanner;

import static model.Game.pressAnyKeyToContinue;
import static controller.LoadGame.loadGameQuery;

/**
 * @author Wang Ruijie
 * This class is responsible for initializing the entire system.
 * It prompts the user to choose between playing the game or designing a custom game board.
 * If the user chooses to play the game, they can choose between starting a standard game, starting a new game on a custom game board, or loading a game save.
 * If the user chooses to design a custom game board, they can choose from a list of available game board files to design the game board.
 * The game is then started or designed based on the user's choices.
 * It is also used for going back to the main menu after the game or design is finished.
 */
public class Initialization {

    /**
     * Initializes the entire system.
     */
    public static void initiate() {

        System.out.println();
        System.out.println("Welcome to the" + Color.PURPLE + " Game of Monopoly" + Color.RESET + "!");
        System.out.println("    [1] Play game");
        System.out.println("    [2] Design game board");

        label1:
        for (;;) {

            System.out.print("Please enter your choice: ");
            Scanner masterScanner = new Scanner(System.in);
            String choice = masterScanner.next();
            System.out.println();

            switch (choice) {

                case "1" -> {
                    System.out.println("Starting the game...");
                    System.out.println("    [1] Standard game");
                    System.out.println("    [2] New game on a custom board");
                    System.out.println("    [3] Load a game save");

                    label2:
                    for (; ; ) {
                        System.out.print("Please enter your choice: ");
                        Scanner playGameScanner = new Scanner(System.in);
                        String playGameChoice = playGameScanner.next();
                        System.out.println();

                        switch (playGameChoice) {

                            case "1" -> {
                                System.out.println("Starting a new game...");
                                System.out.println();
                                Game game = new Game("src/data/gameboard/defaultGameBoard.txt");
                                game.start();
                                break label2;
                            }

                            case "2" -> {
                                System.out.println("Loading custom game boards...");
                                System.out.println();
                                String gameBoardData = startGameOnCustomGameBoardQuery();
                                Game game = new Game(gameBoardData);
                                game.start();
                                break label2;
                            }

                            case "3" -> {
                                System.out.println("Loading game saves...");
                                System.out.println();
                                Game game = loadGameQuery();
                                assert game != null;
                                game.continueGame();
                                break label2;
                            }

                            default -> {
                                System.out.println();
                                System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
                    break label1;
                }

                case "2" -> {
                    System.out.println("Designing the board...");
                    System.out.println();

                    String gameBoardData = startGameOnCustomGameBoardQuery();
                    Design design = new Design(gameBoardData);
                    design.startDesign();

                    break label1;
                }

                default -> {
                    System.out.println();
                    System.out.println("Invalid choice. Please try again.");
                }

            }

        }
    }

    /**
     * Queries the user to choose a custom game board to start a new game on.
     * The user can choose from a list of available game board files by entering the index of the file.
     * @return the file path of the game board file chosen by the user
     */
    public static String startGameOnCustomGameBoardQuery() {

        File directory = new File("src/data/gameboard");
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println();
            System.out.println("No game board files found.");
            pressAnyKeyToContinue();
            initiate();
        } else {
            File[] files = directory.listFiles((dir, name) -> new File(dir, name).isFile());
            if  (files != null && files.length > 0) {
                System.out.println("All game board files are listed below:");
                for (int i = 1; i <= files.length; i++) {
                    System.out.println("    [" + i + "] " + files[i - 1].getName());
                }
                for (;;) {
                    System.out.print("Please enter the index of the save file you want to load: ");
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.next();
                        int index = Integer.parseInt(input);
                        if (index < 1 || index > files.length) {
                            System.out.println();
                            System.out.println("Invalid index. Please try again.");
                        } else {
                            System.out.println();
                            return "src/data/gameboard/" + files[index - 1].getName();
                        }
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Invalid index. Please try again.");
                    }
                }
            } else {
                System.out.println();
                System.out.println("No game board files found.");
                pressAnyKeyToContinue();
                initiate();
            }
        }
        return null;
    }
}