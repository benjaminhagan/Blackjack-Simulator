public class Simulator {

    public static void main(String[] args) {
        /*
        Runs trials on large amounts of hands to determine player edge using different strategies and bet spreads
        */

        //Settings
        double bankroll = 0;
        int decks = 4;
        int penetration = 3;
        int hands = 100000000;


        Shoe shoe = new Shoe(decks, penetration);

        //Loop for a full hand
        for (int i = 0; i < hands; i++) {

            //Sets bet size based on count
            double bet = 1;
            double splitBet = 1;
            int count = shoe.getTrueCount();

            if (count > 0) {
                bet = count;
                splitBet = count;
            }

            //Sets up hands: Gives player two cards, Gives dealer 1 card and draws a second but doesn't add it till later
            Hand dealerHand = new Hand(shoe.draw());
            Hand playerHand = new Hand(shoe.draw());
            Hand splitHand = null;
            playerHand.addCard(shoe.draw());
            Card downCard = shoe.draw();

            //Checks for blackjack
            if (playerHand.valueOf() == 21 && (dealerHand.valueOf() + downCard.getValue()) == 21) {
                dealerHand.addCard(downCard);
            } else if (playerHand.valueOf() == 21 && (dealerHand.valueOf() + downCard.getValue()) != 21) {
                bankroll += 1.5 * bet;
                dealerHand.addCard(downCard);
            } else if ((dealerHand.valueOf() + downCard.getValue()) == 21 && playerHand.valueOf() != 21) {
                bankroll -= bet;
                dealerHand.addCard(downCard);
            } else {

                //Loop for calculating and executing player's moves
                while (true) {
                    String move = MoveCalculator.calculate(dealerHand, playerHand, shoe.getTrueCount(),
                            shoe.getRunningCount(), true);
                    if (move.equals("SP")) {
                        splitHand = new Hand(playerHand.split());
                        if (splitHand.valueOf() == 11) {
                            splitHand.setAcesSplit();
                        }
                        splitHand.denySplit();
                        playerHand.addCard(shoe.draw());
                        splitHand.addCard(shoe.draw());
                    } else if (move.equals("H")) {
                        playerHand.addCard(shoe.draw());
                    } else if (move.equals("D")) {
                        bet *= 2;
                        playerHand.addCard(shoe.draw());
                        break;
                    } else if (move.equals("S")) {
                        break;
                    }
                }

                //Loop for split hand moves if the split hand exists
                if (splitHand != null) {
                    while (true) {
                        String splitMove = MoveCalculator.calculate(dealerHand, splitHand, shoe.getTrueCount(),
                                shoe.getRunningCount(), true);
                        if (splitMove.equals("H")) {
                            splitHand.addCard(shoe.draw());
                        } else if (splitMove.equals("D")) {
                            splitBet *= 2;
                            splitHand.addCard(shoe.draw());
                            break;
                        } else if (splitMove.equals("S")) {
                            break;
                        }
                    }
                }

                //Loop for dealer's moves. Adds downcard from earlier
                dealerHand.addCard(downCard);
                while (dealerHand.valueOf() < 17) {
                    dealerHand.addCard(shoe.draw());
                }

                //Determines the winner of the hand
                if (playerHand.valueOf() > 21) {
                    bankroll -= bet;
                } else if (dealerHand.valueOf() > 21) {
                    bankroll += bet;
                } else if (playerHand.valueOf() > dealerHand.valueOf()) {
                    bankroll += bet;
                } else if (playerHand.valueOf() < dealerHand.valueOf()) {
                    bankroll -= bet;
                }

                //Determines winner of split hand
                if (splitHand != null) {
                    if (splitHand.valueOf() > 21) {
                        bankroll -= splitBet;
                    } else if (dealerHand.valueOf() > 21) {
                        bankroll += splitBet;
                    } else if (splitHand.valueOf() > dealerHand.valueOf()) {
                        bankroll += splitBet;
                    } else if (splitHand.valueOf() < dealerHand.valueOf()) {
                        bankroll -= splitBet;
                    }
                }
            }
        }

        //Final statistics
        System.out.println("Bankroll: " + bankroll);
        if (bankroll < 0)
            System.out.println("Casino edge: " + bankroll/hands * -100 + "%");
        else
            System.out.println("Player edge: " + bankroll/hands * 100 + "%");
        System.out.println(bankroll / hands * 100 + " bets/100 hands");
    }
}