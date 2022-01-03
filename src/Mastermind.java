/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Ptet utiliser checkMastermind dans la boucle.
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;

class Mastermind extends Roulette {
    final int SIZE = 5;
    final int BLUE = 3;

    Epreuve initMastermind() {
        return newEpreuve(9, "Mastermind", -1, "Retrouve la combinaison !", GameState.KEYS);
    }

    boolean startMastermind(Epreuve epreuve) {
        int health = 3;
        String move;
        String[] map = new String[SIZE];
        String[] usermap = new String[SIZE];
        initMastermind(usermap);
        initMastermind(map);
        do {
            shuffleArray(map);
        } while(getGoodBlues(map, usermap) > 1);
        do{
            myClearScreen();
            println("Voici votre plateau : ");
            if(debug){
                debug("For debug :");
                printMastermind(map);
            }
            printMastermind(usermap);
            println("Vous avez " + ANSI_BLUE + getGoodBlues(map, usermap) + ANSI_RESET + " bonnes balles bleues et il vous reste " + ANSI_RED + health + ANSI_RESET + " essais");
            println("Quelles balles souhaitez vous intervetir ? (ex: 14)");
            cusp();
            do {
                curp();
                clearLine(); 
                move = readString();
            } while(!isValideMove(move));
            health--;
            makeMove(usermap, move);
        } while(health > 0 && getGoodBlues(map, usermap) != 3);
        return health > 0;
    }

    void makeMove(String[] usermap, String move) {
        String temp;
        int first = (charToInt(charAt(move, 0))-1);
        int second = (charToInt(charAt(move, 1))-1);
        temp = usermap[first];
        usermap[first] = usermap[second];
        usermap[second] = temp;
    }

    boolean isValideMove(String move){
        int first = charToInt(charAt(move, 0));
        int second = charToInt(charAt(move, 1));
        return ((first >= 1 && first <= 5) && (second >= 1 && second <= 5) && (second != first) && (length(move) == 2));
    }

    /*boolean checkMastermind(String[] map, String[] usermap) {
        boolean res = true;
        int i = 0;
        do {
            if(!equals(map[i], usermap[i])){
                res = false;
            }
        } while(res && i < length(map));
        return false;
    }*/

    int getGoodBlues(String[] map, String[] usermap) {
        int res = 0;
        for(int i = 0; i < length(map); i++){
            if((equals(map[i], ANSI_BLUE)) && equals(map[i], usermap[i])){
                res++;
            }
        }
        return res;
    }

    void initMastermind(String[] map) {
        for(int i = 0; i < BLUE; i++){
            map[i] = ANSI_BLUE;
        }
        for(int i = BLUE; i < SIZE; i++) {
            map[i] = ANSI_RED;
        }
    }

    void printMastermind(String[] mastermind) {
        println("1 2 3 4 5");
        for(int i = 0; i < length(mastermind); i++){
            print(mastermind[i] + "•"+ ANSI_RESET + " ");
        }
        println();
    }
}
