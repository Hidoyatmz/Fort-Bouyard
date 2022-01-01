/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Passer la saisie de askUserAnswer en readString pour éviter les plantages en cas de non entées
 *                    + Utiliser des symboles genre ⯌ ⯍ ⯁ ⯀ au lieu des lettres ?
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class MemoGame extends Fakir {
    final int SIZE = 8;
    Epreuve initMemoGame() {
        return newEpreuve(7, "MemoGame", -1, "Vous devez trouver 3 animaux sur les 5 joués !", GameState.KEYS);
    }

    boolean startMemoGame(Epreuve epreuve, Game game) {
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

    void printUserWon(int uScore, int iaScore) {
        println("Vous avez trouvé : " + ANSI_PURPLE + uScore + ANSI_RESET + " bon symboles.");
        println("Le maître a trouvé : " + ANSI_PURPLE + iaScore + ANSI_RESET + " bon symboles.");
        String winner = (iaScore <= uScore) ? "Vous" : "Le maître";
        println(winner + " gagne la manche !");
    }

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

    String getSymbol(MemoSymbol symbol) {
        return symbol.color + symbol.car + ANSI_RESET;
    }

    String getMap(MemoSymbol[] map) {
        String res = "";
        for(int i = 0; i < length(map); i++){
            res += getSymbol(map[i]) + " ";
        }
        return res;
    }

    void println(MemoSymbol[] map){
        for(int i = 0; i < length(map); i++){
            print(getSymbol(map[i]) + " ");
        }
        println();
    }

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
