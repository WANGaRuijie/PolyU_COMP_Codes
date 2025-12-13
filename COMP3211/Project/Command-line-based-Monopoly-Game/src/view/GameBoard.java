package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Wang Ruijie
 * Represents the game board of a game of Monopoly.
 * The game board is a 6x6 grid of boxes.
 * The game board is made up of 20 properties and 16 empty boxes.
 * Methods are provided to initialize the game board and print it.
 */
public class GameBoard {

    private final Box[][] boxes;
    public static final int ROWS = 6;
    public static final int COLS = 6;
    public static final String[] SPECIAL_PROPERTY_POSITIONS = {"1", "6", "9", "11", "13", "16", "19"};

    public GameBoard(Box[][] boxes) {
        this.boxes = boxes;
    }

    /**
     * Initializes the game board from a game board file.
     * @param gameBoardFileName the path of the game board file
     * @return the boxes of the game board
     */
    public static Box[][] initializeBoxes(String gameBoardFileName) {

        Box[][] boxes = new Box[GameBoard.ROWS][GameBoard.COLS];

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(gameBoardFileName))) {
            String line;
            while (!Objects.equals(line = bufferedReader.readLine(), null)) {

                String[] data = line.split(",");

                int[] coordinate = getBoxCoordinate(data[0]);

                Brick nameBrick;
                if (List.of(GameBoard.SPECIAL_PROPERTY_POSITIONS).contains(data[0])) {
                    nameBrick = new Brick(data[0] + " " +data[1], Color.NULL_BACKGROUND, Color.RED);
                } else {
                    nameBrick = new Brick(data[0] + " " +data[1], Color.NULL_BACKGROUND, Color.BLUE);
                }

                Brick valueBrick;
                if (Integer.parseInt(data[2]) != -1) {
                    valueBrick = new Brick("P" + data[2] + " R" + data[3], Color.NULL_BACKGROUND, Color.NULL);
                } else {
                    valueBrick = new Brick("", Color.NULL_BACKGROUND, Color.NULL);
                }

                Brick playerBrick = new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL);
                Box box = new Box(true, nameBrick, valueBrick, playerBrick);
                boxes[coordinate[0]][coordinate[1]] = box;

                for (int i = 1; i < GameBoard.ROWS - 1; i++) {
                    for (int j = 1; j < GameBoard.COLS - 1; j++) {
                        if (boxes[i][j] == null) {
                            boxes[i][j] = new Box(false, new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL), new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL), new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL));
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return boxes;
    }

    /**
     * Returns the coordinate of a box on the game board given its position.
     * @param position the position of the box in clockwise order starting from the button right corner
     * @return the coordinate of the box in the array of boxes (game board)
     */
    public static int[] getBoxCoordinate(String position) {
        return switch (Integer.parseInt(position)) {
            case 1 -> new int[]{5, 5};
            case 2 -> new int[]{5, 4};
            case 3 -> new int[]{5, 3};
            case 4 -> new int[]{5, 2};
            case 5 -> new int[]{5, 1};
            case 6 -> new int[]{5, 0};
            case 7 -> new int[]{4, 0};
            case 8 -> new int[]{3, 0};
            case 9 -> new int[]{2, 0};
            case 10 -> new int[]{1, 0};
            case 11 -> new int[]{0, 0};
            case 12 -> new int[]{0, 1};
            case 13 -> new int[]{0, 2};
            case 14 -> new int[]{0, 3};
            case 15 -> new int[]{0, 4};
            case 16 -> new int[]{0, 5};
            case 17 -> new int[]{1, 5};
            case 18 -> new int[]{2, 5};
            case 19 -> new int[]{3, 5};
            case 20 -> new int[]{4, 5};
            default -> null;
        };
    }

    /**
     * Prints the game board to the console.
     * Each brick is regarded as the minimum unit (pixel) of the game board.
     * The game board is printed row by row.
     * Each row consists of 6 bricks from 6 distinct boxes.
     * Note that the game board is not printed box by box.
     */
    public void printBoard() {

        for (int i = 0; i < ROWS; i++) {

            System.out.println();

            for (int j = 0; j < COLS; j++) {
                System.out.print(boxes[i][j].getTopBrick().getBackgroundColor() + boxes[i][j].getTopBrick().getFontColor() + boxes[i][j].getTopBrick().getContent() + Color.RESET);
            }
            System.out.println();

            for (int j = 0; j < COLS; j++) {
                System.out.print(boxes[i][j].getNameBrick().getBackgroundColor() + boxes[i][j].getNameBrick().getFontColor() + boxes[i][j].getNameBrick().getContent() + Color.RESET);
            }
            System.out.println();


            for (int j = 0; j < COLS; j++) {
                System.out.print(boxes[i][j].getValueBrick().getBackgroundColor() + boxes[i][j].getValueBrick().getFontColor() + boxes[i][j].getValueBrick().getContent() + Color.RESET);
            }
            System.out.println();

            for (int j = 0; j < COLS; j++) {
                System.out.print(boxes[i][j].getPlayerBrick().getBackgroundColor() + boxes[i][j].getPlayerBrick().getFontColor() + boxes[i][j].getPlayerBrick().getContent() + Color.RESET);
            }
            System.out.println();

            for (int j = 0; j < COLS; j++) {
                System.out.print(boxes[i][j].getBottomBrick().getBackgroundColor() + boxes[i][j].getBottomBrick().getFontColor() + boxes[i][j].getBottomBrick().getContent() + Color.RESET);
            }
            System.out.println();
        }
    }

    public Box[][] getBoxes() {
        return boxes;
    }

}