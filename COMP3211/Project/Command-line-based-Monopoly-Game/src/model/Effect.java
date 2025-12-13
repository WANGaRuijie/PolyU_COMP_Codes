package model;

import view.Color;
import view.GameBoard;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Wang Ruijie
 * This class is responsible for the effects that occur when a player lands on a property.
 * Both the player and the property are passed as parameters to the constructor to be able to apply the effect.
 */
public class Effect {

    private final Property property;
    private final Player player;
    private final GameBoard gameBoard;

    public Effect(Property property, Player player, GameBoard gameBoard) {
        this.property = property;
        this.player = player;
        this.gameBoard = gameBoard;
    }

    /**
     * Applies the effect of the property on the player and the property itself.
     */
    public void applyEffect() {
        System.out.println();
        switch (property.getEffectType()) {

            case NORMAL -> {

                if (property.isBought()) {
                    player.setMoney(player.getMoney() - property.getRent());
                    property.getOwner().setMoney(property.getOwner().getMoney() + property.getRent());
                    System.out.println("You paid " + property.getRent() + " dollars rent to " + property.getOwner().getColor() + property.getOwner().getName() + Color.RESET + "." + " You have " + player.getMoney() + " dollars left.");

                } else {
                    System.out.println("You have " + player.getMoney() + " dollars now.");
                    if (player.getMoney() >= property.getPrice()) {
                        System.out.println("Do you want to buy " + property.getName() + " for " + property.getPrice() + "?");
                        System.out.println("    [1] Yes");
                        System.out.println("    [2] No");

                        for (;;) {
                            System.out.print("Please enter your choice: ");
                            Scanner scanner = new Scanner(System.in);
                            String choice = scanner.next();

                            if (choice.equals("1")) {
                                player.setMoney(player.getMoney() - property.getPrice());
                                property.setOwner(player);
                                property.setBought(true);
                                player.getProperties().add(property);
                                setPropertyColor();
                                System.out.println();
                                System.out.println("You have bought " + property.getName() + " for " + property.getPrice() + ".");
                                System.out.println("You have " + player.getMoney() + " dollars left.");
                                break;

                            } else if (choice.equals("2")) {
                                break;
                            } else {
                                System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("You do not have enough money to buy " + property.getName() + ".");
                    }
                }
            }

            case GO -> {
                player.setMoney(player.getMoney() + 1500);
                System.out.println("You arrive at GO. You earned 1500 dollars salary.");
                System.out.println("You have " + player.getMoney() + " dollars now.");
            }

            case INCOME_TAX -> {
                player.setMoney(player.getMoney() * 9 / 10);
                System.out.println("You paid 10% income tax.");
                System.out.println("You have " + player.getMoney() + " dollars now.");
            }

            case CHANCE -> {
                Random random = new Random();
                int chance = -300 + random.nextInt(51) * 10;
                player.setMoney(player.getMoney() + chance);
                if (chance >= 0) {
                    System.out.println("You earned " + chance + " dollars as a CHANCE.");
                } else {
                    System.out.println("You lost " + chance + " dollars as a CHANCE.");
                }
                System.out.println("You have " + player.getMoney() + " dollars now.");
            }

            case NO_EFFECT -> {
                System.out.println("Nothing happens here.");
                System.out.println("You have " + player.getMoney() + " dollars now.");
            }

            case GO_TO_JAIL -> {
                player.setInJail(true);
                player.setPosition(6);
                player.setJailTurns(3);
                System.out.println("You are sent to jail. This turn will be skipped.");
            }

        }
    }

    /**
     * Sets the background color of the top brick of the box that corresponds to the property based on the color of the player who buy the property.
     */
    protected void setPropertyColor() {
        int position = property.getPosition();
        int[] coordinate = GameBoard.getBoxCoordinate(String.valueOf(position));
        switch (property.getOwner().getColor()) {
            case Color.RED -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.RED_BACKGROUND);
            case Color.GREEN -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.GREEN_BACKGROUND);
            case Color.YELLOW -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.YELLOW_BACKGROUND);
            case Color.BLUE -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.BLUE_BACKGROUND);
            case Color.PURPLE -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.PURPLE_BACKGROUND);
            case Color.CYAN -> gameBoard.getBoxes()[coordinate[0]][coordinate[1]].getTopBrick().setBackgroundColor(Color.WHITE_BACKGROUND);
        }
    }
}
