import java.util.ArrayList;
import java.util.Random;
public class Shoe {

    private int decks;
    private int cutoff;
    private int runningCount;
    private ArrayList<Card> shoe;
    private ArrayList<Card> discard;

    public Shoe(int decks, int penetration) {
        this.decks = decks;
        cutoff = (decks - penetration) * 52;
        runningCount = 0;
        shoe = new ArrayList<>();
        discard = new ArrayList<>();
        for (int i = 0; i < decks*4; i++) {
            Card ace = new Card("A", 11);
            Card two = new Card("2", 2);
            Card three = new Card("3", 3);
            Card four = new Card("4", 4);
            Card five = new Card("5", 5);
            Card six = new Card("6", 6);
            Card seven = new Card("7", 7);
            Card eight = new Card("8", 8);
            Card nine = new Card("9", 9);
            Card ten = new Card("T", 10);
            Card jack = new Card("J", 10);
            Card queen = new Card("Q", 10);
            Card king = new Card("K", 10);
            shoe.add(ace);
            shoe.add(two);
            shoe.add(three);
            shoe.add(four);
            shoe.add(five);
            shoe.add(six);
            shoe.add(seven);
            shoe.add(eight);
            shoe.add(nine);
            shoe.add(ten);
            shoe.add(jack);
            shoe.add(queen);
            shoe.add(king);
        }
    }
    public int size() {
        return shoe.size();
    }
    public Card draw() {
        if (shoe.size() <= cutoff || shoe.size() == 0)
            shuffle();

        Random generator = new Random();
        int removalIndex = generator.nextInt(size());
        Card newCard = new Card(shoe.get(removalIndex));
        discard.add(shoe.remove(removalIndex));

        if (newCard.getValue() > 9)
            runningCount--;
        else if (newCard.getValue() < 7)
            runningCount++;

        return newCard;
    }
    public void shuffle() {
        while (discard.size() != 0) {
            shoe.add(discard.remove(0));
        }
        runningCount = 0;
    }
    public int getTrueCount() {
        int decksLeft = (int) Math.round(shoe.size()/52.0);
        if (decksLeft == 0) {
            return runningCount;
        }
        return runningCount/decksLeft;
    }
    public int getRunningCount() {
        return runningCount;
    }
}
