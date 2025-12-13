package view;

import model.Game;
import model.Player;
import model.Property;

import java.util.ArrayList;

import static view.GameBoard.getBoxCoordinate;

/**
 * @author Wang Ruijie
 * This class is a gathering of functions to print information about the game and players.
 */
public class Info {

    private final Game game;

    public Info(Game game) {
        this.game = game;
    }

    /**
     * Prints the information of a player, including their name, money, position, properties owned, jail status, and bankrupt status.
     * @param player the player whose information is to be printed
     */
    public void printPlayerInfo(Player player) {

        System.out.print("Player: " + player.getColor() + player.getName() + Color.RESET + " | ");

        if (player.isBroken()) {
            System.out.print("Bankrupted");
            return;
        }

        System.out.print("Current Money: " + player.getMoney() + " | ");

        int[] boxCoordinates = getBoxCoordinate(Integer.toString(player.getPosition()));
        Box currentBox = game.getGameBoard().getBoxes()[boxCoordinates[0]][boxCoordinates[1]];
        System.out.print("Current Position: " + currentBox.getNameBrick().getPureContent());

        if (!player.getProperties().isEmpty()) {
            System.out.print(" | Properties Owned: ");
            for (Property property : player.getProperties()) {
                System.out.print(property.getName());
                if (player.getProperties().indexOf(property) != player.getProperties().size() - 1) {
                    System.out.print(", ");
                }
            }
        }

        if (player.isInJail()) {
            System.out.print(" | In Jail");
        }

        System.out.print("\n");
    }

    /**
     * Prints the information of all players in the game including bankrupted players.
     */
    public void printAllPlayerInfo() {
        for (Player player : game.getPlayers()) {
            printPlayerInfo(player);
        }
    }

    /**
     * Print the name of the next player in the game.
     * @param player the player whose next player is to be queried
     */
    public void queryNextPlayer(Player player) {
        Player nextPlayer = game.getPlayers().get((game.getPlayers().indexOf(player) + 1) % game.getPlayers().size());
        System.out.println("The next player is " + nextPlayer.getColor() + nextPlayer.getName() + Color.RESET + ".");
    }

    /**
     * Prints the winner of the game at the end of the game.
     */
    public static void gameOverInfo(int roundsCount, ArrayList<Player> players) {

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(Color.PURPLE + "GAME OVER" + Color.RESET + " at round " + roundsCount + "!");

        int maximum = 0;
        String winner = "";

        for (Player player : players) {
            if (player.isBroken()) {
                continue;
            }
            if (player.getMoney() > maximum) {
                maximum = player.getMoney();
                winner = player.getName();
            }
        }

        System.out.println("The winner is " + winner + " with " + maximum + " dollars!");
    }

}