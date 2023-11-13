public class MoveCalculator {
    public static String calculate(Hand dealerHand, Hand playerHand, int trueCount, int runningCount, boolean deviations) {

        //Value of player hand and dealer upcard
        int pvalue = playerHand.valueOf();
        int upcard = dealerHand.valueOf();

        //Automatically stands after aces are split and hit once
        if (playerHand.acesWereSplit())
            return "S";

        //Implements optional deviations
        if (deviations) {
            if (playerHand.isDouble() && playerHand.canBeSplit() && pvalue == 20) {
                if (upcard == 6 && trueCount >= 4 || upcard == 5 && trueCount >= 5 || upcard == 4 && trueCount >= 6)
                    return "SP";
            }

            if (playerHand.getType().equals("soft")) {
                if (pvalue == 19 && playerHand.isFirstMove()) {
                    if (upcard == 4 && trueCount >= 3 || upcard == 5 && trueCount >= 1 || upcard == 6 && trueCount >=1)
                        return "D";
                }
                if (pvalue == 17 && playerHand.isFirstMove()){
                    if (upcard == 2 && trueCount >= 1)
                        return "D";
                }
            }

            if (playerHand.getType().equals("hard")) {
                if (pvalue == 16) {
                    if (upcard == 9 && trueCount >= 4 || upcard == 10 && runningCount > 0)
                        return "S";
                }
                if (pvalue == 15) {
                    if (upcard == 10 && trueCount >= 4)
                        return "S";
                }
                if (pvalue == 13) {
                    if (upcard == 2 && trueCount <= -1)
                        return "H";
                }
                if (pvalue == 12) {
                    if (upcard == 2 && trueCount >= 3 || upcard == 3 && trueCount >= 2)
                        return "S";
                    if (upcard == 4 && runningCount < 0)
                        return "H";
                }
                if (pvalue == 11 && playerHand.isFirstMove()) {
                    if (upcard == 11 && trueCount >= 1)
                        return "D";
                }
                if (pvalue == 10 && playerHand.isFirstMove()) {
                    if ((upcard == 10 || upcard == 11) && trueCount >= 4)
                        return "D";
                }
                if (pvalue == 9 && playerHand.isFirstMove()) {
                    if (upcard == 2 && trueCount >= 1 || upcard == 7 && trueCount >= 3)
                        return "D";
                }
                if (pvalue == 8 && playerHand.isFirstMove()) {
                    if (upcard == 6 && trueCount >= 2)
                        return "D";
                }
            }
        }

        //Basic strategy for splitting hands
        if (playerHand.isDouble() && playerHand.canBeSplit()) {
            if (pvalue == 12 && playerHand.getType().equals("soft")) {
                return "SP";
            }
            if (pvalue == 18) {
                if (upcard <= 9 && upcard != 7)
                    return "SP";
            }
            if (pvalue == 16) {
                return "SP";
            }
            if (pvalue == 14) {
                if (upcard <= 7)
                    return "SP";
            }
            if (pvalue == 12) {
                if (upcard <= 6)
                    return "SP";
            }
            if (pvalue == 8) {
                if (upcard == 6 || upcard == 5)
                    return "SP";
            }
            if (pvalue == 6 || pvalue == 4) {
                if (upcard <= 7)
                    return "SP";
            }
        }

        //Basic strategy for soft totals
        if (playerHand.getType().equals("soft")) {
           if (pvalue >= 19) {
               if (pvalue == 6 && playerHand.isFirstMove())
                   return "D";
               return "S";
           }
           if (pvalue == 18) {
               if (upcard <= 6) {
                   if (playerHand.isFirstMove())
                       return "D";
                   else
                       return "S";
               }
               if (upcard == 7 || upcard == 8)
                   return "S";

               return "H";
           }
           if (pvalue == 17) {
               if (upcard <= 6 && upcard >= 3) {
                   if (playerHand.isFirstMove())
                       return "D";
                   else
                       return "H";
               }

               return "H";
           }
           if (pvalue == 16 || pvalue == 15) {
               if (upcard <= 6 && upcard >= 4) {
                   if (playerHand.isFirstMove())
                       return "D";
                   else
                       return "H";
               }

               return "H";
           }
           if (pvalue == 14 || pvalue == 13) {
               if (upcard == 5 || upcard == 6) {
                   if (playerHand.isFirstMove())
                       return "D";
                   else
                       return "H";
               }

               return "H";
           }
           if (pvalue == 12) {
               return "H";
           }
        }

        //Basic strategy for hard totals
        if (playerHand.getType().equals("hard")) {
            if (pvalue >= 17) {
                return "S";
            }
            if (pvalue == 16) {
                if (upcard <= 6)
                    return "S";

                return "H";
            }
            if (pvalue == 15) {
                if (upcard <= 6)
                    return "S";

                return "H";
            }
            if (pvalue == 14) {
                if (upcard <= 6)
                    return "S";

                return "H";
            }
            if (pvalue == 13) {
                if (upcard <= 6)
                    return "S";

                return "H";
            }
            if (pvalue == 12) {
                if (upcard >= 4 && upcard <= 6)
                    return "S";

                return "H";
            }
            if (pvalue == 11) {
                if (upcard <= 10 && playerHand.isFirstMove())
                    return "D";

                return "H";
            }
            if (pvalue == 10) {
                if (upcard <= 9 && playerHand.isFirstMove())
                    return "D";

                return "H";
            }
            if (pvalue == 9 ) {
                if ((upcard >= 3 && upcard <= 6) && playerHand.isFirstMove())
                    return "D";

                return "H";
            }
            if (pvalue <= 8) {
                return "H";
            }
        }

        //Returns null if nothing is returned so errors can be caught
        return null;
    }
}
