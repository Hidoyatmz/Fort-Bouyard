/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Passer la saisie de askUserAnswer en readString pour éviter les plantages en cas de non entées
 *                    + Utiliser des symboles genre ⯌ ⯍ ⯁ ⯀ au lieu des lettres ?
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class MemoGame extends Mastermind {
    final int SIZE = 8;
    Epreuve initMemoGame() {
        return newEpreuve(7, "MemoGame", -1, "Une combinaison de lettres s'affiche à l'écran, vous avez 10 secondes pour la mémoriser et la redonner dans l'ordre.\nSi le maître en mémorise plus que vous, vous perdez la manche.\nComptabilisez un score de 3/5 pour gagner la partie.", GameState.KEYS);
    }

    boolean startMemoGame(Epreuve epreuve) {
        MemoSymbol[] map = new MemoSymbol[SIZE];
        String userRes;
        /* Temps pour mémoriser les symboles (en secondes) */
        final int TIME = 10;
        final int MAXROUNDS = 5;
        final int ROUNDSTOWIN = 3;
        int tour = 1;
        int points = 0;
        int iaScore;
        int uScore;
        initMap(map);
        do {
            myClearScreen();
            println("MANCHE #"+tour);
            shuffleMap(map);
            println(getMap(map));
            delay(TIME*1000);
            myClearScreen();
            userRes = askUserAnswer();
            iaScore = randInt(randInt(2,3), SIZE);
            uScore = getGoodSymbols(userRes, map);
            points = (iaScore <= uScore) ? points+1 : points;
            myClearScreen();
            printUserWon(uScore, iaScore);
            println("Vous avez " + points + "/"+ROUNDSTOWIN+" points");
            tour++;
            delay(5000);
        } while((tour <= MAXROUNDS) && (points < 3) && (points + (MAXROUNDS - (tour - 1 )) >= ROUNDSTOWIN));
        return points >= ROUNDSTOWIN;
    }

    /**
     * Display who won the round depending on the score
     * @param uScore Score of the user
     * @param iaScore Score of the IA
     */

    void printUserWon(int uScore, int iaScore) {
        println("Vous avez trouvé : " + ANSI_PURPLE + uScore + ANSI_RESET + " bon symboles.");
        println("Le maître a trouvé : " + ANSI_PURPLE + iaScore + ANSI_RESET + " bon symboles.");
        String winner = (iaScore <= uScore) ? "Vous" : "Le maître";
        println(winner + " gagne la manche !");
    }

    /**
     * Returns how many good symbol are placed on the board at the given time.
     * @param userRes   String with all the answers of the user.
     * @param map       Board containing the good answers
     * @return          integer being the number of user's good answers
     */

    int getGoodSymbols(String userRes, MemoSymbol[] map) {
        int res = 0;
        MemoSymbol symbol;
        char c;
        for(int i = 0; i < SIZE; i++) {
            c = charAt(userRes, i);
            symbol = map[i];
            if(c == symbol.car){
                res++;
            }
        }
        return res;
    }

    String askUserAnswer() {
        int i = 1;
        char c;
        String res = "";
        char[] chars = new char[]{'A', 'T', 'G', 'C'};
        do {
            print("Quel était la lettre n°" + i +" : ");
            do {
                c = readChar();
            } while(!inArray(chars, c));
            res += c;
            i++;
        } while(i <= SIZE);
        return res;
    }

    /**
     * Init the board with random symbols
     * @param map Map not initialized yet.
     */

    void initMap(MemoSymbol[] map) {
        String sColor = ANSI_BLUE;
        char sCar = 'A';
        for(int i = 0; i < length(map); i++) {
            MemoSymbol symbol = new MemoSymbol();
            if(i >= 2 && i < 4) {
                sColor = ANSI_GREEN;
                sCar = 'T';
            } else if(i >= 4 && i < 6){
                sColor = ANSI_PURPLE;
                sCar = 'C';
            } else if(i >= 6) {
                sColor = ANSI_YELLOW;
                sCar = 'G';
            }
            symbol.car = sCar;
            symbol.color = sColor;
            map[i] = symbol;
        }
    }

    /**
     * Format a symbol character to be displayed.
     * @param symbol
     * @return String formated with the symbol and his color.
     */

    String getSymbol(MemoSymbol symbol) {
        return symbol.color + symbol.car + ANSI_RESET;
    }

    /**
     * Format a Map of Symbols to be displayed
     * @param symbol
     * @return String formated with all the symbols and his color.
     */

    String getMap(MemoSymbol[] map) {
        String res = "";
        for(int i = 0; i < length(map); i++){
            res += getSymbol(map[i]) + " ";
        }
        return res;
    }

    /**
     * Shuffle the given array of Symbols
     * @param map Array of Symbols.
     */

    void shuffleMap(MemoSymbol[] map) {
        MemoSymbol temp;
        int r;
        for(int i = 0; i < length(map); i++) {
            r = randInt(0, length(map)-1);
            temp = map[r];
            map[r] = map[i];
            map[i] = temp;
        }
    }
}
