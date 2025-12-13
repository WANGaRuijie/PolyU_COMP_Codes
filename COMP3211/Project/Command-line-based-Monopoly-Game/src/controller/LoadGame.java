package controller;

import model.Game;
import model.Player;
import model.Property;
import view.Box;
import view.Brick;
import view.Color;
import view.GameBoard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static model.Game.pressAnyKeyToContinue;
import static controller.Initialization.initiate;
import static model.Property.initializeProperties;
import static view.GameBoard.*;

/**
 * @author Wang Ruijie
 * This class is responsible for loading a game from a save file.
 * It saves the current state of the elements in model but also the elements in view for ease of reinstating the game.
 */
public class LoadGame {

    public static final int NUM_BOXES = 20;

    private final String fileName;

    public LoadGame(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads a game from a game save file.
     * @return the game loaded to continue playing.
     */
    public Game loadGame() {
        String filePath = "src/data/save/" + fileName;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;

            line = bufferedReader.readLine();
            int callerIndex = Integer.parseInt(line);

            line = bufferedReader.readLine();
            boolean isGameOver = Boolean.parseBoolean(line);

            line = bufferedReader.readLine();
            int roundCount = Integer.parseInt(line);

            line = bufferedReader.readLine();
            String gameBoardFileName = line;

            int boxCount = 0;
            ArrayList<Box> boxes = new ArrayList<>();
            while ((boxCount < NUM_BOXES) && (!Objects.equals(line = bufferedReader.readLine(), null))) {
                boxes.add(parseBox(line));
                boxCount++;
            }

            ArrayList<Property> properties = initializeProperties(gameBoardFileName);

            ArrayList<Player> players = new ArrayList<>();

            while (!Objects.equals(line = bufferedReader.readLine(), null) && !line.isEmpty()) {
                players.add(parsePlayer(line, properties));
            }

            GameBoard gameBoard = parseGameBoard(boxes);

            for (Player player : players) {
                String position = String.valueOf(player.getPosition());
                gameBoard.getBoxes()[getBoxCoordinate(position)[0]][getBoxCoordinate(position)[1]].getPlayers().add(player);
            }

            return new Game(gameBoardFileName, gameBoard, players, properties, isGameOver, roundCount, callerIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Parses a string of data to create a non-player brick.
     * @param brickData the data in the file for a brick.
     * @return the brick restored from the data.
     */
    public Brick parseBrick(String brickData) {
        String[] brickContents = brickData.split(",");
        ArrayList<String> brickArray = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (brickContents[i].equals(">")) {
                brickArray.add("");
                continue;
            }
            brickArray.add(brickContents[i]);
        }
        return new Brick(brickArray.get(1), brickArray.get(2), brickArray.get(3));
    }

    /**
     * Parses a string of data to create a player brick.
     * @param playerBrickData the data in the file for a player brick.
     * @return the player brick restored from the data.
     */
    public Brick parsePlayerBrick(String playerBrickData) {
        String[] playerBrickContents = playerBrickData.split(",");
        ArrayList<String> playerBrickArray = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (playerBrickContents[i].equals(">")) {
                playerBrickArray.add("");
                continue;
            }
            playerBrickArray.add(playerBrickContents[i]);
        }
        return new Brick(playerBrickArray.get(0), playerBrickArray.get(1), playerBrickArray.get(2), playerBrickArray.get(3));
    }

    /**
     * Parses a list of bricks to create a box.
     * @param boxData the data in the file for a box.
     * @return the box restored from the data.
     */
    public Box parseBox(String boxData) {
        String[] bricksData = boxData.split("<");
        ArrayList<Brick> boxBricks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            boxBricks.add(parseBrick(bricksData[i]));
        }
        boxBricks.add(parsePlayerBrick(bricksData[3]));
        return new Box(boxBricks.get(0), boxBricks.get(1), boxBricks.get(2), boxBricks.get(3));
    }

    /**
     * Parses a list of boxes to create a game board.
     * @param boxes the list of boxes to be parsed.
     * @return the game board restored from the boxes.
     */
    public GameBoard parseGameBoard(ArrayList<Box> boxes) {
        Box[][] boxesArray = new Box[GameBoard.ROWS][GameBoard.COLS];
        for (int i = 1; i < GameBoard.ROWS - 1; i++) {
            for (int j = 1; j < GameBoard.COLS - 1; j++) {
                boxesArray[i][j] = new Box(false, new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL), new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL), new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL));
            }
        }

        int k = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                /*If the box is blank then skip it*/
                if (1 <= i && i <= 4 && 1 <= j && j <= 4) {
                    continue;
                }
                boxesArray[i][j] = boxes.get(k++);
            }
        }

        return new GameBoard(boxesArray);
    }

    /**
     * Parses a string of data to create a player.
     * @param playerData the data in the file for a player.
     * @param properties the list of properties in the game.
     * @return the player restored from the data.
     */
    public Player parsePlayer(String playerData, ArrayList<Property> properties) {
        String[] playerDataArray = playerData.split("<");

        String name = playerDataArray[0];
        String color = playerDataArray[1];
        int money = Integer.parseInt(playerDataArray[2]);
        int position = Integer.parseInt(playerDataArray[3]);

        boolean inJail = Boolean.parseBoolean(playerDataArray[4]);
        boolean isBroken = Boolean.parseBoolean(playerDataArray[5]);
        int jailTurns = Integer.parseInt(playerDataArray[6]);
        String propertiesData;
        try {
            propertiesData = playerDataArray[7];
        } catch (Exception e) {
            propertiesData = "";
        }

        Player player = new Player(name, color, money, position, inJail, isBroken, jailTurns, new ArrayList<>());
        parsePropertiesOfPlayer(propertiesData, properties, player);

        return player;
    }

    /**
     * Parses a string of data to create a list of properties owned by a player.
     * Also updates the properties in the game to reflect the ownership.
     * @param propertiesData the data in the file for the properties owned by a player.
     * @param properties the list of properties in the game.
     * @param player the player who owns the properties.
     */
    public void parsePropertiesOfPlayer(String propertiesData, ArrayList<Property> properties, Player player) {
        if (propertiesData.isEmpty()) {
            return;
        }
        String[] propertiesDataArray = propertiesData.split(",");
        for (String propertyData : propertiesDataArray) {
            for (Property property : properties) {
                if (property.getPosition() == Integer.parseInt(propertyData)) {
                    player.getProperties().add(property);
                    property.setBought(true);
                    property.setOwner(player);
                }
            }
        }
    }

    /**
     * Queries the user to load a game from a list of save files.
     * Use indexes to for the user to select the save file to load.
     * @return the game loaded to continue playing.
     */
    public static Game loadGameQuery() {
        File directory = new File("src/data/save");
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("No save files found.");
            pressAnyKeyToContinue();
            initiate();
        } else {
            File[] files = directory.listFiles((dir, name) -> new File(dir, name).isFile());
            if (files != null && files.length > 0) {
                System.out.println("All game save files are listed below:");
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
                            System.out.println("Loading " + files[index - 1].getName() + "...");
                            System.out.println();
                            LoadGame loadGame = new LoadGame(files[index - 1].getName());
                            return loadGame.loadGame();
                        }
                    } catch (Exception e) {
                        System.out.println();
                        System.out.println("Invalid index. Please try again.");
                    }
                }
            } else {
                System.out.println("No save files found.");
                pressAnyKeyToContinue();
                initiate();
            }
        }

        return null;
    }

}
