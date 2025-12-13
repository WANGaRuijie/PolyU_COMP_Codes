package view;

import model.Player;

import java.util.*;

/**
 * @author Wang Ruijie
 * Represents a box on the game board.
 * Each box has a top brick, a name brick, a value brick, a player brick, and a bottom brick.
 * 20 boxes are properties and 16 boxes are empty in a game board.
 */
public class Box {

    /**
     * The bricks that make up the top and bottom of the box.
     */
    private final Brick topBrick;
    private final Brick bottomBrick;
    private final Brick nameBrick;
    private final Brick valueBrick;
    private final Brick playerBrick;

    /**
     * The players whose tokens are currently on this box.
     */
    private final ArrayList<Player> players;

    /**
     * Constructor for the Box class used for initializing a new game.
     * @param propertyBlock whether the box represents a property or not.
     * @param nameBrick the brick representing the name of the property.
     * @param valueBrick the brick representing the value of the property.
     * @param playerBrick the brick representing the players on the property.
     */
    public Box(boolean propertyBlock, Brick nameBrick, Brick valueBrick, Brick playerBrick) {

        if (propertyBlock) {
            this.topBrick = new Brick("  *-------------*  ", Color.NULL_BACKGROUND, Color.NULL);
            this.bottomBrick = new Brick("  *-------------*  ", Color.NULL_BACKGROUND, Color.NULL);
        } else {
            //Leave the top and bottom bricks empty for non-property boxes.
            this.topBrick = new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL);
            this.bottomBrick = new Brick("                   ", Color.NULL_BACKGROUND, Color.NULL);
        }

        this.nameBrick = nameBrick;
        this.valueBrick = valueBrick;
        this.playerBrick = playerBrick;
        this.players = new ArrayList<>();
    }

    /**
     * Used for initializing the box for loading a saved game.
     */
    public Box(Brick topBrick, Brick nameBrick, Brick valueBrick, Brick playerBrick) {
        this.topBrick = topBrick;
        this.nameBrick = nameBrick;
        this.valueBrick = valueBrick;
        this.playerBrick = playerBrick;
        this.bottomBrick = new Brick("  *-------------*  ", Color.NULL_BACKGROUND, Color.NULL);
        this.players = new ArrayList<>();
    }

    /**
     * Changes the player tokens' view in the player brick.
     */
    public void changePlayerBrickPureContent() {
        StringBuilder newPlayerBrickPureContent = new StringBuilder();

        // Artificial intelligence to center the player tokens in the player brick.
        // Algorithms don't work well because the Color strings have lengths to compensate for.
        switch (players.size()) {
            case 0 -> newPlayerBrickPureContent.append("                   ");
            case 1 -> newPlayerBrickPureContent.append("        ");
            case 2 -> newPlayerBrickPureContent.append("      ");
            case 3 -> newPlayerBrickPureContent.append("     ");
            case 4 -> newPlayerBrickPureContent.append("   ");
            case 5 -> newPlayerBrickPureContent.append("  ");
            case 6 -> newPlayerBrickPureContent.append(" ");
        }

        for (Player player : players) {
            if (player.isInJail()) {
                // Jailed tokens are enclosed in square brackets.
                newPlayerBrickPureContent.append(player.getColor()).append(">").append(player.getName().charAt(0)).append("<").append(Color.RESET);
            } else {
                newPlayerBrickPureContent.append(player.getColor()).append("[").append(player.getName().charAt(0)).append("]").append(Color.RESET);
            }
        }

        switch (players.size()) {
            case 1 -> newPlayerBrickPureContent.append("        ");
            case 2 -> newPlayerBrickPureContent.append("       ");
            case 3 -> newPlayerBrickPureContent.append("     ");
            case 4 -> newPlayerBrickPureContent.append("    ");
            case 5 -> newPlayerBrickPureContent.append("  ");
        }

        playerBrick.setContentAsPure(newPlayerBrickPureContent.toString());
    }

    public Brick getTopBrick() {
        return topBrick;
    }

    public Brick getBottomBrick() {
        return bottomBrick;
    }

    public Brick getNameBrick() {
        return nameBrick;
    }

    public Brick getValueBrick() {
        return valueBrick;
    }

    public Brick getPlayerBrick() {
        return playerBrick;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Used for saving the view of a box directly.
     * '<' is used as a delimiter between bricks, as commas and semicolons are used.
     */
    public String toString() {
       return getTopBrick().toString() + "<" +
               getNameBrick().toString() + "<" +
               getValueBrick().toString() + "<" +
               getPlayerBrick().toString();
    }
}
