package model;

import view.Color;
import view.GameBoard;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Wang Ruijie
 * This class represents a player in the game.
 * It contains the player's name, color, money, position, properties, jail status, and broken status.
 * It also contains methods to initialize players and generate random player names.
 */
public class Player {

    public static final String[] PLAYER_COLORS = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.PURPLE, Color.CYAN};
    private final String name;
    private final String color;
    private int money;
    private int position;
    private ArrayList<Property> properties;
    private boolean inJail;
    private boolean isBroken;
    private int jailTurns;

    /**
     * Constructor used at the beginning of a new game.
     * @param name the name of the player
     * @param color the color of the player
     */
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.money = 1500;
        this.position = 1;
        this.properties = new ArrayList<>();
        this.inJail = false;
        this.isBroken = false;
        this.jailTurns = 0;
    }

    /**
     * Constructor used when loading a game from a save file.
     * @param name the name of the player
     * @param color the color of the player
     * @param money the money of the player
     * @param position the position of the player
     * @param inJail the jail status of the player
     * @param isBroken the broken status of the player
     * @param jailTurns the jail turns of the player
     * @param properties the properties of the player
     */
    public Player(String name, String color, int money, int position, boolean inJail, boolean isBroken, int jailTurns, ArrayList<Property> properties) {
        this.name = name;
        this.color = color;
        this.money = money;
        this.position = position;
        this.properties = properties;
        this.inJail = inJail;
        this.isBroken = isBroken;
        this.jailTurns = jailTurns;
    }

    /**
     * Initializes the players at the beginning of a new game.
     * @return the list of players in the game
     */
    public static ArrayList<Player> initializePlayers() {

        for(;;) {

            System.out.print("Enter the number of players (2-6): ");
            Scanner playGameScanner = new Scanner(System.in);
            String playGameChoice = playGameScanner.next();
            System.out.println();

            if (playGameChoice.equals("2") || playGameChoice.equals("3") || playGameChoice.equals("4") || playGameChoice.equals("5") || playGameChoice.equals("6")) {

                int numPlayers = Integer.parseInt(playGameChoice);
                ArrayList<Player> players = new ArrayList<>();

                for (int i = 1; i <= numPlayers; i++) {
                    System.out.print("Enter the name of Player " + i + ", or enter 'r' to generate a random player: ");
                    Scanner nameScanner = new Scanner(System.in);
                    String name = nameScanner.next();
                    if (name.equals("r")) {
                        name = generateRandomPlayerName();
                    }
                    players.add(new Player(name, PLAYER_COLORS[i - 1]));
                    System.out.println("Player " + i + " is " + PLAYER_COLORS[i - 1] +  name + Color.RESET + ".");
                    System.out.println();
                }

                return players;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Generates a random player name.
     * @return the random player name
     */
    public static String generateRandomPlayerName() {
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Kevin", "Linda", "Mallory", "Nancy", "Oscar", "Peggy", "Quentin", "Randy", "Sue", "Trent", "Ursula", "Victor", "Wendy", "Xander", "Yvonne", "Zelda"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColor() {
        return color;
    }


    public String getName() {
        return name;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public boolean isBroken() {
        return isBroken;
    }

    /**
     * Sets the broken status of the player.
     * If the player is broken, the player loses all properties and the properties are set to unbought.
     * @param status the broken status of the player
     * @param gameBoard the game board
     */
    public void setBroken(boolean status, GameBoard gameBoard) {
        for (Property property : properties) {
            property.setOwner(null);
            property.setBought(false);
            int[] boxCoordinates = GameBoard.getBoxCoordinate(Integer.toString(property.getPosition()));
            gameBoard.getBoxes()[boxCoordinates[0]][boxCoordinates[1]].getNameBrick().setBackgroundColor(Color.NULL_BACKGROUND);
        }
        properties = new ArrayList<>();
        this.isBroken = status;
    }

    public int getJailTurns() {
        return jailTurns;
    }

    public void setJailTurns(int jailTurns) {
        this.jailTurns = jailTurns;
    }

    /**
     * Returns a string representation of the player including information about its name, color, money, position, and states.
     * @return the string representation of the player
     */
    public String toString() {
        StringBuilder propertyPositions = new StringBuilder();
        for (Property property : properties) {
            propertyPositions.append(property.getPosition()).append(",");
        }
        return name + "<" + color + "<" + money + "<" + position + "<" + inJail + "<" + isBroken + "<" + jailTurns+ "<" + propertyPositions;
    }

}