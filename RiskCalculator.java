public class RiskCalculator {

    public static void main(String[] args) {
        /*
        Calculates risk of busting given a starting bankroll and desired profit
        */

        //Settings
        double startBankroll = 26;
        double desiredProfit = 50;
        int decks = 8;
        int penetration = 4;
        int hands = 10000000;
        boolean playDeviations = true;

        //Variables to track wins and losses
        double busts = 0;
        double profits = 0;
        double bankroll = startBankroll;

        Shoe shoe = new Shoe(decks, penetration);

        //Loop for a full hand
        for (int i=0; i<hands; i++) {

            //Sets bet size based on count
            double bet = 1;
            double splitBet = 1;
            int originalCount = shoe.getTrueCount();

            //Sets up hands: Gives player two cards, Gives dealer 1 card and draws a second but doesn't add it till later
            Hand dealerHand = new Hand(shoe.draw());
            Hand playerHand = new Hand(shoe.draw());
            Hand splitHand = null;
            playerHand.addCard(shoe.draw());
            Card downCard = shoe.draw();
            String winner = "al;skdjf;lk";
            String splitWinner = "lk;asjfdl";

            //Checks for blackjack
            if (playerHand.valueOf() == 21 && (dealerHand.valueOf() + downCard.getValue()) == 21) {
                winner = "push";
                dealerHand.addCard(downCard);
            } else if (playerHand.valueOf() == 21 && (dealerHand.valueOf() + downCard.getValue()) != 21) {
                winner = "player";
                bankroll += 1.5 * bet;
                dealerHand.addCard(downCard);
            } else if ((dealerHand.valueOf() + downCard.getValue()) == 21 && playerHand.valueOf() != 21) {
                bankroll -= bet;
                winner = "dealer";
                dealerHand.addCard(downCard);
            } else {

                //Loop for calculating and executing player's moves
                while (true) {
                    String move = MoveCalculator.calculate(dealerHand, playerHand, shoe.getTrueCount(),
                            shoe.getRunningCount(), playDeviations);
                    if (move.equals("SP")) {
                        splitHand = new Hand(playerHand.split());
                        splitHand.denySplit();
                        playerHand.addCard(shoe.draw());
                        splitHand.addCard(shoe.draw());
                        //System.out.println("Split");
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
                                shoe.getRunningCount(), playDeviations);
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
                    winner = "dealer";
                } else if (dealerHand.valueOf() > 21) {
                    bankroll += bet;
                    winner = "player";
                } else if (playerHand.valueOf() > dealerHand.valueOf()) {
                    bankroll += bet;
                    winner = "player";
                } else if (playerHand.valueOf() < dealerHand.valueOf()) {
                    bankroll -= bet;
                    winner = "dealer";
                } else if (playerHand.valueOf() == dealerHand.valueOf()) {
                    winner = "push";
                }

                //Determines winner of split hand
                if (splitHand != null) {
                    if (splitHand.valueOf() > 21) {
                        bankroll -= splitBet;
                        splitWinner = "dealer";
                    } else if (dealerHand.valueOf() > 21) {
                        bankroll += splitBet;
                        splitWinner = "player";
                    } else if (splitHand.valueOf() > dealerHand.valueOf()) {
                        bankroll += splitBet;
                        splitWinner = "player";
                    } else if (splitHand.valueOf() < dealerHand.valueOf()) {
                        bankroll -= splitBet;
                        splitWinner = "dealer";
                    } else if (splitHand.valueOf() == dealerHand.valueOf()) {
                        splitWinner = "push";
                    }
                }
            }

            if (bankroll >= desiredProfit) {
                bankroll = startBankroll;
                profits++;
            }
            if (bankroll <= 0) {
                bankroll = startBankroll;
                busts++;
            }

        }

        //Final statistic
        System.out.println("Risk of busting: " + busts/(busts+profits) * 100 + "%");
    }
}