package model;

import controller.SaveGame;

import view.Box;
import view.Color;
import view.GameBoard;
import view.Info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static controller.Initialization.initiate;
import static java.lang.Thread.sleep;
import static model.Player.initializePlayers;
import static model.Property.initializeProperties;
import static view.GameBoard.initializeBoxes;
import static view.Info.gameOverInfo;

/**
 * @author Wang Ruijie
 * This class is responsible for the game logic.
 * It contains the main game loop and the methods for the player's turn.
 * It also contains the methods for rolling the dice and moving the player's position.
 */
public class Game {

    private final String gameBoardFileName;
    private ArrayList<Player> players;
    private final GameBoard gameBoard;
    private boolean gameOver;
    private final ArrayList<Property> properties;
    private int roundCount;
    private int callerIndex;

    /**
     * Constructor for a new game.
     * @param gameBoardFileName the name of the game board file
     */
    public Game(String gameBoardFileName) {
        this.gameBoardFileName = gameBoardFileName;
        this.gameBoard = new GameBoard(initializeBoxes(gameBoardFileName));
        this.players = initializePlayers();

        int[] boxCoordinate = GameBoard.getBoxCoordinate("1");
        for (Player player : players) {
            this.gameBoard.getBoxes()[boxCoordinate[0]][boxCoordinate[1]].getPlayers().add(player);
        }
        this.gameBoard.getBoxes()[boxCoordinate[0]][boxCoordinate[1]].changePlayerBrickPureContent();

        this.gameOver = false;
        this.properties =  initializeProperties(gameBoardFileName);
        this.roundCount = 1;
        this.callerIndex = 0;
    }

    /**
     * Constructor for loading a game.
     * @param GameBoardFileName the name of the game board file
     * @param gameBoard the game board
     * @param players the players
     * @param properties the properties
     * @param gameOver the game over status
     * @param roundCount the round count
     * @param callerIndex the index of the player who called the saveGame() method
     */
    public Game(String GameBoardFileName, GameBoard gameBoard, ArrayList<Player> players, ArrayList<Property> properties, boolean gameOver, int roundCount, int callerIndex) {
        this.gameBoardFileName = GameBoardFileName;
        this.gameBoard = gameBoard;
        this.players = players;
        this.properties = properties;
        this.gameOver = gameOver;
        this.roundCount = roundCount;
        this.callerIndex = callerIndex;
    }

    /**
     * The main game loop.
     * It goes through each player's turn until the game is over.
     */
    public void start() {

        int numBrokenPlayers = 0;

        gameBoard.printBoard();

        while (!gameOver) {

            for (Player player : players) {

                // If the player is before the caller, skip the player
                if (callerIndex > players.indexOf(player)) {
                    continue;
                }

                // If the player is the caller, reset the caller index and enter the player's turn
                if (callerIndex == players.indexOf(player)) {
                    callerIndex = 0;
                }

                if (player.isBroken()) {
                    continue;
                }

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.println("Round " + roundCount);
                System.out.println();

                // If the player is bankrupted, set the player as broken
                if (player.getMoney() < 0 && !player.isBroken()) {
                    player.setBroken(true, gameBoard);
                    System.out.println("------------------------------------------------------------------------------------------");
                    System.out.println(player.getColor() + player.getName() + Color.RESET + " is just bankrupted with " + player.getMoney() + " dollars.");
                }

                // If all players are broken except one, the game is over
                if (player.isBroken()) {
                    numBrokenPlayers++;
                    if (numBrokenPlayers == players.size() - 1) {
                        gameOver = true;
                        break;
                    }
                }

                if (gameOver) {
                    break;
                }

                playerTurn(player, gameBoard);
            }

            // If the number of rounds reaches 100, the game is over
            roundCount++;
            if (roundCount == 100) {
                gameOver = true;
            }
        }

        gameOverInfo(roundCount, players);

    }

    /**
     * Continues the game from a game save.
     * If the game is over, print the game over information.
     * If the game is not over, start the game.
     */
    public void continueGame() {
        if (gameOver) {
            gameOverInfo(roundCount, players);
        } else {
            start();
        }
    }

    /**
     * The player's turn.
     * If the player is in jail, the player's jail turn is called.
     * If the player is not in jail, the player's turn is called.
     * Player can choose to roll the dice, print the game board, see all players, see a specific player, query the next player, save the game, go back to the main menu, or exit the program.
     * They will move to the new position based on the dice roll.
     * According to the new position, the effect of the property is applied.
     * Then the turn ends.
     * @param player the player
     * @param gameBoard the game board
     */
    public void playerTurn(Player player, GameBoard gameBoard) {

       if (player.isInJail()) {
           jailTurn(player);

       } else {
           System.out.println("It is " + player.getColor() + player.getName() + Color.RESET + "'s turn.");
           System.out.println("You have " + player.getMoney() + " dollars now.");

           label:
           for (;;) {

               printPlayerMenu();
               System.out.print("Please enter your choice: ");
               Scanner scanner = new Scanner(System.in);
               String choice = null;
               if (scanner.hasNextLine()) {
                   choice = scanner.next();
               }
               

               switch (choice) {

                   // Roll the dice
                   case "1" -> {
                       break label;
                   }

                   // Print the game board
                   case "2" -> {
                       gameBoard.printBoard();
                       pressAnyKeyToContinue();
                   }

                   // See all players
                   case "3" -> {
                       System.out.println();
                       Info info = new Info(this);
                       info.printAllPlayerInfo();
                       pressAnyKeyToContinue();
                   }

                   // See a specific player
                   case "4" -> {
                       System.out.println();
                       System.out.println("Which player do you want to see?");
                       for (int i = 0; i < players.size(); i++) {
                           System.out.println("    [" + (i + 1) + "] " + players.get(i).getColor() + players.get(i).getName() + Color.RESET);
                       }
                       for (;;) {
                            System.out.print("Please enter your choice: ");
                            Scanner playerScanner = new Scanner(System.in);
                            String playerChoice = playerScanner.next();
                            if (Integer.parseInt(playerChoice) >= 1 && Integer.parseInt(playerChoice) <= players.size()) {
                                 Info info = new Info(this);
                                 System.out.println();
                                 info.printPlayerInfo(players.get(Integer.parseInt(playerChoice) - 1));
                                 pressAnyKeyToContinue();
                                 break;
                            } else {
                                 System.out.println();
                                 System.out.println("Invalid choice. Please try again.");
                            }
                       }
                   }

                   // Query the next player
                   case "5" -> {
                       System.out.println();
                       Info info = new Info(this);
                       info.queryNextPlayer(player);
                       pressAnyKeyToContinue();
                   }

                   // Save the game
                   case "6" -> {
                       System.out.println();
                       System.out.print("Please enter the name of the game save file, or enter '" + Color.PURPLE + "t" + Color.RESET + "' to name the game file with the current time stamp: ");
                       Scanner saveScanner = new Scanner(System.in);
                       String saveName = saveScanner.next();
                       if (saveName.equals("t")) {
                           SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
                           Date date = new Date(System.currentTimeMillis());
                           saveName = formatter.format(date);
                       }
                       saveName += ".txt";
                       SaveGame save = new SaveGame(this, saveName, players.indexOf(player));
                       save.saveGame();

                       pressAnyKeyToContinue();
                   }

                   // Go back to the main menu
                   case "7" -> {
                       System.out.println();
                       System.out.println("If you go back to the main menu, the unsaved game progress will not be stored. Are you sure?");
                       System.out.println("    [1] Yes");
                       System.out.println("    [2] No");
                       for (;;) {
                           System.out.print("Please enter your choice: ");
                           Scanner exitScanner = new Scanner(System.in);
                           String exitChoice = exitScanner.next();
                           if (exitChoice.equals("1")) {
                               System.out.println();
                               initiate();
                               break;
                           } else if (exitChoice.equals("2")) {
                               break;
                           } else {
                               System.out.println();
                               System.out.println("Invalid choice. Please try again.");
                           }
                       }
                   }

                   // Exit the program
                   case "8" -> {
                       System.out.println();
                       System.out.println("If the program exits, the unsaved game progress will not be stored. Are you sure?");
                       System.out.println("    [1] Yes");
                       System.out.println("    [2] No");
                       for (;;) {
                            System.out.print("Please enter your choice: ");
                            Scanner exitScanner = new Scanner(System.in);
                            String exitChoice = exitScanner.next();
                            if (exitChoice.equals("1")) {
                                 System.out.println();
                                 System.out.println("The program exits. " + Color.PURPLE + "Goodbye!" + Color.RESET);
                                 System.exit(0);
                            } else if (exitChoice.equals("2")) {
                                 break;
                            } else {
                                 System.out.println();
                                 System.out.println("Invalid choice. Please try again.");
                            }
                       }
                   }

                   default -> {
                       System.out.println();
                       System.out.println("Invalid choice. Please try again.");
                   }
               }
           }

           int dice = rollDiceWithPrint();
           positionMove(dice, player);
           gameBoard.printBoard();
           System.out.println("The current game board is printed above.");
           pressAnyKeyToContinue();

       }
    }

    /**
     * Rolls the tetrahedron dice.
     * @return the number of the dice roll
     */
    public static int rollDice() {
        return (int) (Math.random() * 4) + 1;
    }

    /**
     * Rolls the tetrahedron dice and prints the dice roll.
     * @return the number of the dice roll
     */
    public static int rollDiceWithPrint() {
        int dice = rollDice();
        try {
            System.out.print("Rolling the dice...");
            sleep(500);
            System.out.println("You rolled a " + dice);
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return dice;
    }

    /**
     * Rolls two tetrahedron dices.
     * @return the numbers of the two dice rolls
     */
    public static int[] rollTwoDices() {
        int[] dices = new int[2];
        dices[0] = rollDice();
        dices[1] = rollDice();
        try {
            System.out.print("Rolling two dices...");
            sleep(500);
            System.out.println("You rolled a " + dices[0] + " and a " + dices[1]);
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return dices;
    }

    /**
     * Moves the player to the new position based on the dice roll.
     * The player's position is updated.
     * The effect of the property is applied.
     * @param dice the number of the dice roll
     * @param player the player
     */
    public void positionMove(int dice, Player player) {

        // Remove the player from the current box
        int currentPosition = player.getPosition();
        int[] currentBoxCoordinate = GameBoard.getBoxCoordinate(Integer.toString(currentPosition));
        Box currentBox = gameBoard.getBoxes()[currentBoxCoordinate[0]][currentBoxCoordinate[1]];

        currentBox.getPlayers().remove(player);
        currentBox.changePlayerBrickPureContent();

        // Move the player to the new position
        int newPosition = currentPosition + dice;

        // Go through the GO box, not landing on the GO box
        if (newPosition > 21) {
            newPosition -= 20;
            player.setPosition(newPosition);
            player.setMoney(player.getMoney() + 1500);
            System.out.println();
            System.out.println("You passed GO. You earned 1500 dollars salary.");
        } else {
            player.setPosition(newPosition);
        }

        Effect effect = new Effect(properties.get(newPosition - 1), player, gameBoard);
        effect.applyEffect();
        pressAnyKeyToContinue();

        //In case of going to jail, refresh the newPosition to make the player's token on the jail box
        newPosition = player.getPosition();
        int[] newBoxCoordinate = GameBoard.getBoxCoordinate(Integer.toString(newPosition));
        gameBoard.getBoxes()[newBoxCoordinate[0]][newBoxCoordinate[1]].getPlayers().add(player);
        gameBoard.getBoxes()[newBoxCoordinate[0]][newBoxCoordinate[1]].changePlayerBrickPureContent();

    }

    /**
     * The player's jail turn.
     * The player can choose to pay 150 dollars to get out of jail, or roll two dices.
     * If the player rolls two identical doubles, the player will move to the position with the diced number.
     * If the player does not roll two identical doubles, the player will stay in jail.
     * @param player the player
     */
    public void jailTurn(Player player) {

        System.out.print("It is " + player.getColor() + player.getName() + Color.RESET + "'s turn. ");
        System.out.println("You are in jail now. You have " + player.getJailTurns() + " turns to get out of jail.");
        System.out.println("    [1] Pay 150 dollars to get out of jail.");
        System.out.println("    [2] Role two dices. If they are two identical doubles, you will move to the position with the diced number.");

        for(;;) {

            System.out.print("Please enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.next();

            if (choice.equals("2") && player.getJailTurns() >= 1) {

                int[] dices = rollTwoDices();
                if (dices[0] == dices[1] && dices[0] % 2 == 0) {
                    player.setInJail(false);
                    player.setJailTurns(0);
                    System.out.println("You rolled two doubles. You are out of jail now.");
                    pressAnyKeyToContinue();
                    positionMove(dices[0], player);
                    break;

                } else {
                    player.setJailTurns(player.getJailTurns() - 1);
                    System.out.println("You do not roll two identical doubles. You have " + player.getJailTurns() + " turns remained to leave the jail.");
                    break;
                }

            } else if (choice.equals("2") && player.getJailTurns() == 0) {
                System.out.println("You can no longer roll. You have to pay 150 dollars to leave the jail in this turn.");
                pressAnyKeyToContinue();

                player.setInJail(false);
                player.setMoney(player.getMoney() - 150);
                System.out.println("You have " + player.getMoney() + " dollars left.");
                playerTurn(player, gameBoard);
                break;

            } else if (choice.equals("1")) {
                System.out.println("150 dollars are paid to leave the jail.");
                pressAnyKeyToContinue();

                player.setInJail(false);
                player.setMoney(player.getMoney() - 150);
                player.setJailTurns(0);
                System.out.println("You have " + player.getMoney() + " dollars left.");
                playerTurn(player, gameBoard);
                break;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Press any key to continue.
     * Make player see the information before continuing.
     */
    public static void pressAnyKeyToContinue() {
        System.out.print("Press any key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints the player menu.
     */
    public static void printPlayerMenu() {
        System.out.println();
        System.out.println("***** Player Menu *****");
        System.out.println("   [1] Roll the dice");
        System.out.println("   [2] Print the game board");
        System.out.println("   [3] See all players");
        System.out.println("   [4] See a specific player");
        System.out.println("   [5] Query the next player");
        System.out.println("   [6] Save the game");
        System.out.println("   [7] Go back to the main menu");
        System.out.println("   [8] Exit the program");
        System.out.println();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getGameBoardFileName() {
        return gameBoardFileName;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getRoundCount() {
        return roundCount;
    }

}
