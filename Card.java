public class Card {
    private String name;
    private int value;
    public Card(String name, int value) {
        this.name = name;
        this.value = value;
    }
    public Card(Card newCard) {
        name = newCard.name;
        value = newCard.value;
    }
    public int getValue() {
        return value;
    }
    public String toString() {
        return name;
    }
}
