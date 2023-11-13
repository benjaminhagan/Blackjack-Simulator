import java.util.ArrayList;
public class Hand {
    private ArrayList<Card> hand;
    private String type;
    private int value;
    private boolean firstMove;
    private boolean isDouble;
    private boolean canSplit;
    private boolean acesSplit;
    public Hand(Card card) {
        firstMove = true;
        isDouble = false;
        canSplit = true;
        acesSplit = false;
        type = "hard";
        hand = new ArrayList<>();
        hand.add(card);
        if (card.toString().equals("A"))
            type = "soft";
        value = card.getValue();
    }
    public void addCard(Card card) {
        hand.add(card);

        if (hand.size() == 2 && hand.get(0).getValue() == hand.get(1).getValue())
            isDouble = true;
        if (hand.size() > 2)
            isDouble = false;

        value += card.getValue();
        if (value > 21 && type.equals("soft")) {
            value -= 10;
            type = "hard";
        }
        if (card.getValue() == 11)
            type = "soft";
        if (value > 21 && type.equals("soft")) {
            value -= 10;
            type = "hard";
        }
        if (hand.size() > 2)
            firstMove = false;
    }
    public Card split() {
        if (hand.get(0).getValue() == 11)
            acesSplit = true;
        canSplit = false;
        value = hand.get(0).getValue();
        return hand.remove(0);
    }
    public void setAcesSplit() {
        acesSplit = true;
    }
    public void denySplit() {
        canSplit = false;
    }
    public int valueOf() {
        return value;
    }
    public boolean isFirstMove() {
        return firstMove;
    }
    public boolean isDouble() {
        return isDouble;
    }
    public boolean canBeSplit() {
        return canSplit;
    }
    public boolean acesWereSplit() { return acesSplit; }
    public String getType() {
        return type;
    }
    public void printHand() {
        for (int i = 0; i < hand.size(); i++)
            System.out.print(hand.get(i).toString());
        System.out.print("  (" + value + ")");
        System.out.println();

    }
}
