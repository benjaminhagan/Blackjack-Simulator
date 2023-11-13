import java.util.Scanner;
public class Practice {

    public static void main(String[] args) {
        /*
        Simulates actually playing blackjack to help the user practice their strategy and counting
        Only lets the user make a correct move
         */

        //Settings
        double bankroll = 100;
        int decks = 4;
        int penetration = 3;
        boolean playDeviations = true;

        Shoe shoe = new Shoe(decks, penetration);

        //Loop for a full hand
        while (true) {

            Scanner scanner = new Scanner(System.in);
            //Can uncomment the following lines of code to practice counting and changing bet sizing
            //System.out.println("True Count: " + shoe.getTrueCount());
            //System.out.print("Bet size: ");
            //double betSize = scanner.nextDouble();
            double betSize = 1;
            System.out.println();
            double bet = betSize;
            double splitBet = betSize;

            //Sets up starting hands and prints them out
            Hand dealerHand = new Hand(shoe.draw());
            Hand playerHand = new Hand(shoe.draw());
            Hand splitHand = null;
            playerHand.addCard(shoe.draw());
            Card downCard = shoe.draw();
            String winner = "";
            String splitWinner = "";

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

                    //Prints hands and prompts user for a move
                    System.out.print("Dealer Hand: ");
                    dealerHand.printHand();
                    System.out.print("Player Hand: ");
                    playerHand.printHand();
                    System.out.println();
                    String move;

                    //Automatically stands on 21 or bust
                    if (playerHand.valueOf() >= 21) {
                        move = "S";
                    } else {
                        System.out.print("Move: ");
                        move = scanner.next().toUpperCase();
                        System.out.println();
                    }

                    //Executes move or informs the user if it is incorrect
                    String correctMove = MoveCalculator.calculate(dealerHand, playerHand, shoe.getTrueCount(),
                            shoe.getRunningCount(), playDeviations);
                    if (!move.equals(correctMove))
                        System.out.println("Incorrect move");
                    else {
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
                }

                //Loop for split hand moves if the split hand exists
                if (splitHand != null) {
                    while (true) {
                        System.out.print("Dealer Hand: ");
                        dealerHand.printHand();
                        System.out.print("Split Hand: ");
                        splitHand.printHand();
                        System.out.println();
                        String splitMove;

                        //Automatically stands on 21 or bust
                        if (splitHand.valueOf() >= 21) {
                            splitMove = "S";
                        } else {
                            System.out.print("Move: ");
                            splitMove = scanner.next().toUpperCase();
                            System.out.println();
                        }

                        //Executes move or informs the user if it is incorrect
                        String correctSplitMove = MoveCalculator.calculate(dealerHand, splitHand, shoe.getTrueCount(),
                                shoe.getRunningCount(), playDeviations);
                        if (!splitMove.equals(correctSplitMove))
                            System.out.println("Incorrect move");
                        else {
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

            //Prints out winners and statistics
            System.out.print("Dealer Hand: ");
            dealerHand.printHand();
            System.out.print("Player Hand: ");
            playerHand.printHand();
            if (splitHand != null) {
                System.out.print("Split Hand: ");
                splitHand.printHand();
            }
            System.out.println("Winner is " + winner);
            if (splitHand != null) {
                System.out.println("Split Winner is " + splitWinner);
            }
            System.out.println(bankroll);
            //System.out.println("Running Count: " + shoe.getRunningCount());
            System.out.println();

        }
    }
}