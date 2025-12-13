package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Wang Ruijie
 * This class represents a property in the game.
 * It contains the property's position, name, price, rent, effect type, owner, and bought status.
 * It also contains a method to initialize the properties on the game board.
 */
public class Property {

    private int position;
    private String name;
    private int price;
    private int rent;
    private EffectType effectType;
    private boolean isBought;
    private Player owner;

    /**
     * Constructor for the Property class.
     * @param position the position of the property on the game board
     * @param name the name of the property
     * @param price the price of the property
     * @param rent the rent of the property
     */
    public Property(int position, String name, int price, int rent) {
        this.position = position;
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.effectType = null;
        this.owner = null;
        this.isBought = false;
    }

    /**
     * Initializes the properties on the game board from a file.
     * @param gameBoardFileName the name of the file containing the properties
     * @return the list of properties initialized
     */
    public static ArrayList<Property> initializeProperties(String gameBoardFileName) {
        ArrayList<Property> properties = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(gameBoardFileName))) {
            String line;
            while (!Objects.equals(line = bufferedReader.readLine(), null)) {
                String[] data = line.split(",");
                int position = Integer.parseInt(data[0]);
                String name = data[1];
                int price = Integer.parseInt(data[2]);
                int rent = Integer.parseInt(data[3]);
                properties.add(new Property(position, name, price, rent));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the effect type of each property according to its position on the game board
        for (Property property : properties) {
            switch (property.getPosition()) {
                case 2, 3, 5, 7, 8, 10, 12, 14, 15, 17, 18, 20 -> property.setEffectType(EffectType.NORMAL);
                case 1 -> property.setEffectType(EffectType.GO);
                case 4 -> property.setEffectType(EffectType.INCOME_TAX);
                case 6, 11 -> property.setEffectType(EffectType.NO_EFFECT);
                case 9, 13, 19 -> property.setEffectType(EffectType.CHANCE);
                case 16 -> property.setEffectType(EffectType.GO_TO_JAIL);
            }
        }

        return properties;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    private void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String toString() {
        return position + "," + name + "," + price + "," + rent;
    }
}
