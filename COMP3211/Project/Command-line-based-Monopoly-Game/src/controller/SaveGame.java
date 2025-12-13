package controller;

import model.Game;
import model.Player;
import view.Box;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static model.Game.pressAnyKeyToContinue;
import static view.GameBoard.COLS;
import static view.GameBoard.ROWS;

/**
 * @author Wang Ruijie
 * This class is responsible for saving a game to a save file.
 * It saves the current state of the elements in model but also the elements in view for ease of reinstating the game.
 */
public class SaveGame {

    private final String fileName;
    private final Game game;
    private final int callerIndex;


    /**
     * Constructor for the SaveGame class.
     * @param game the game to be saved
     * @param fileName the name of the save file
     * @param callerIndex the index of the player in the player list who called the saveGame() method
     */
    public SaveGame(Game game, String fileName, int callerIndex) {
        this.fileName = fileName;
        this.game = game;
        this.callerIndex = callerIndex;
    }

    /**
     * Saves the game to a save file.
     */
    public void saveGame() {
        String filePath = "src/data/save/" + fileName;
        File file = new File(filePath);
        BufferedWriter writer = null;

        StringBuilder content = new StringBuilder();

        content.append(callerIndex).append("\n");
        content.append(game.isGameOver()).append("\n");
        content.append(game.getRoundCount()).append("\n");
        content.append(game.getGameBoardFileName()).append("\n");

        Box[][] boxes = game.getGameBoard().getBoxes();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // If the box is blank then skip it
                if (1 <= i && i <= 4 && 1 <= j && j <= 4) {
                    continue;
                }
                content.append(boxes[i][j].toString()).append("\n");
            }
        }

        for (Player player : game.getPlayers()) {
            content.append(player.toString()).append("\n");
        }

        try {
            // Create the file if it does not exist; if it exists, the file will be overwritten.
            writer = new BufferedWriter(new FileWriter(file, false));  // false indicates overwriting
            writer.write(content.toString());
            System.out.println("The game has been saved as " + fileName + ".");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}