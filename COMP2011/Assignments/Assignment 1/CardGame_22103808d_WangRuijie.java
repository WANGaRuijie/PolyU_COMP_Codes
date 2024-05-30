//Warning: Don't change this line.  If you change the package name, your code will not compile, and you will get zero points.
package comp2011.a1;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * @author Yixin Cao (September 11, 2023)
 *
 * You have been deliver a hand of cards, and you sorted them in the suit-first order:
 * spades, hearts, clubs, and diamonds, each suit in decreasing order.
 *
 * See {@code main} where the input is in this order.
 * [♠89, ♠23, ♠14, ♠10, ♠8, ♠4, ♠2, ♥99, ♥18, ♥13, ♥11, ♥9, ♥8, ♥2, ♣77, ♣10, ♣4, ♦99, ♦89, ♦77, ♦14, ♦11, ♦9, ♦7, ♦6, ♦4]
 *
 * Your task is to reorder them into rank-first order: for cards of the same rank, you follow the order of spade, heart, club, and then diamond.
 *
 * For the given hand, the correct result should be:
 * [♥99, ♦99, ♠89, ♦89, ♣77, ♦77, ♠23, ♥18, ♠14, ♦14, ♥13, ♥11, ♦11, ♠10, ♣10, ♥9, ♦9, ♠8, ♥8, ♦7, ♦6, ♠4, ♣4, ♦4, ♠2, ♥2]
 *
 * You need to set your "text file encoding" to UTF-8 to run this class.
 * Otherwise, the suits of cards cannot be shown properly.
 *
 * The class {@code Card} is at the end of this file.
 */
public class CardGame_22103808d_WangRuijie { // Please change!

    /**
     * Warning:
     * You are reordering given cards, not creating new cards.
     * If you call "new Card()," your point will be zero.
     *
     * In the <code>main</code> method, the addresses of the input cards and final cards are printed.
     * They should be exactly the same.
     *
     * VERY IMPORTANT.
     *
     * I've discussed this question with the following students:
     *     NEED NO DISCUSSION.
     *
     * I've sought help from the following Internet resources and books:
     *     NEED NO HELP.
     *
     * Running time: O(n^2).
     */
    static void reorder(Card[] hand) {
        for (int i = 0; i < hand.length - 1; i++) {
            for (int j = 0; j < hand.length - 1 - i; j++) {
                if (hand[j].getRank() < hand[j+1].getRank()) {
                    Card temp = hand[j+1];
                    hand[j+1] = hand[j];
                    hand[j] = temp;
                } else if (hand[j].getRank() == hand[j+1].getRank() && hand[j].getSuit() > hand[j+1].getSuit()) {
                    Card temp = hand[j+1];
                    hand[j+1] = hand[j];
                    hand[j] = temp;
                }
            }
        }
    }

    /*static void reorder(Card[] hand) {
        int maximumRank = hand[0].getRank();
        int minimumRank = hand[0].getRank();
        for (int i = 1; i < hand.length - 1; i++) {
            if (hand[i].getRank() < minimumRank) {
                minimumRank = hand[i].getRank();
            } else if (hand[i].getRank() > maximumRank) {
                maximumRank = hand[i].getRank();
            }
        }
        int[] countingArr = new int[maximumRank - minimumRank + 1];

        for (int i = 0; i < hand.length - 1; i++) {
            countingArr[hand[i].getRank()]++;
        }

        int[] copy = new int[maximumRank - minimumRank + 1];
        for (int i = 0; i < maximumRank - minimumRank; i++){
            copy[i] = countingArr[i];
        }

        for (int i = 1; i < maximumRank - minimumRank + 1; i++) {
            countingArr[i] += countingArr[i - 1];
        }
        Card[] sortedHand = new Card[hand.length];
        for (int i = hand.length - 1; i >= 0; i--) {
            sortedHand[countingArr[hand[i].getRank()] - 1] = hand[i];
            countingArr[hand[i].getRank()]--;
        }
        for (int i = 0; i < hand.length - 1; i++) {

        }

    }*/

    /*
     * The following is prepared for your reference.
     * You may freely revise it to test your code.
     * You are also expected to prepare the testing code for the list version.
     */
    public static void main(String[] args) {
        byte[][] data = {{89, 23, 14, 10, 8, 4, 2}, // Spades
                {99, 18, 13, 11, 9, 8, 2}, // Hearts
                {77, 10, 4}, // Clubs
                {99, 89, 77, 14, 11, 9, 7, 6, 4} // Diamonds
        };

        int cardCount = Arrays.stream(data).mapToInt(p -> p.length).sum();
        Card[] hand = new Card[cardCount];
        int i = 0;
        for (byte suit = 0; suit < 4; suit++) {
            for (int j = 0; j < data[suit].length; j++)  {
                hand[i++] = new Card(suit, data[suit][j]);
            }
        }

        System.out.println("The original addresses of the input cards: " +
                Arrays.stream(hand)
                        .map(p -> Integer.toHexString(System.identityHashCode(p)))
                        .sorted()
                        .collect(Collectors.joining(", "))
        );

        System.out.println("original: " + Arrays.toString(hand));
        reorder(hand);
        System.out.println("after reordering: " + Arrays.toString(hand));

        // Make sure the following output is the same as the original one.
        System.out.println("The addresses of the cards after operations: " +
                Arrays.stream(hand)
                        .map(p -> Integer.toHexString(System.identityHashCode(p)))
                        .sorted()
                        .collect(Collectors.joining(", "))
        );
    }
}

/*
 * Each Card has a suit and an *unlimited* rank.
 */
class Card {
    /**
     *
     * No modification to the class {@code Card} is allowed.
     * If you change anything in this class, your work will not be graded.
     */
    private byte suit;
    private byte rank;
    public static final byte SPADE = 0;
    public static final byte HEART = 1;
    public static final byte CLUB = 2;
    public static final byte DIAMOND = 3;

    public Card(byte suit, byte rank) {
        this.suit = suit;
        this.rank = rank;
    }
    byte getSuit() {return suit;}
    byte getRank() {return rank;}

    public String toString() {
        String s = null;
        switch(suit) {
            case SPADE : s = "\u2660"; break;
            case HEART : s = "\u2665"; break;
            case CLUB : s = "\u2663"; break;
            case DIAMOND : s = "\u2666"; break;
        }
        s += String.valueOf(rank);
        return s;
    }
}
