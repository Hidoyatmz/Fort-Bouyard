/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Ptet utiliser checkMastermind dans le while du jeu & Passer les couleurs en variable pour les changer facilement.
 * @OPTIMIZATION    : NOT DONE
 */

// import extensions.*;

class Mastermind extends Roulette {
    final int SIZE = 5;
    final int BLUE = 3;

    Epreuve initMastermind() {
        return newEpreuve(9, "Mastermind", -1, "Dans ce défi, vous devez reconstituer une combinaison de 5 balles bleues ou rouges. Pour ce faire, vous disposez de 4 essais.\nAprès chaque déplacement, le score de balles bleus bien placés s'affiche.", GameState.KEYS);
    }   

    boolean startMastermind(Epreuve epreuve) {
        int health = 4;
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
        return health >= 0;
    }

    /**
     * Swap the given stars entered by their positions in the array.
     * @param usermap
     * @param move
     */

    void makeMove(String[] usermap, String move) {
        String temp;
        int first = (charToInt(charAt(move, 0))-1);
        int second = (charToInt(charAt(move, 1))-1);
        temp = usermap[first];
        usermap[first] = usermap[second];
        usermap[second] = temp;
    }

    /**
     * Returns if the move entered by the user can be done or not.
     * @param move
     * @return true if it can be done, false otherwise.
     */

    boolean isValideMove(String move){
        int lenMove = length(move);
        boolean allDigits = true;
        int i = 0;
        if(lenMove >= 2 && allDigits(move)){
            int first = charToInt(charAt(move, 0));
            int second = charToInt(charAt(move, 1));
            return ((first >= 1 && first <= 5) && (second >= 1 && second <= 5) && (second != first));
        }
        return false;
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

    /**
     * Returns the amount of good blue stars in the user array.
     * @param map
     * @param usermap
     * @return number of placed blue stars.
     */

    int getGoodBlues(String[] map, String[] usermap) {
        int res = 0;
        for(int i = 0; i < length(map); i++){
            if((equals(map[i], ANSI_BLUE)) && equals(map[i], usermap[i])){
                res++;
            }
        }
        return res;
    }

    /**
     * Init the board with blue & red stars.
     * @param map
     */

    void initMastermind(String[] map) {
        for(int i = 0; i < BLUE; i++){
            map[i] = ANSI_BLUE;
        }
        for(int i = BLUE; i < SIZE; i++) {
            map[i] = ANSI_RED;
        }
    }

    /**
     * Display the stars of the map on the screen with their colors.
     * @param mastermind
     */

    void printMastermind(String[] mastermind) {
        println("1 2 3 4 5");
        for(int i = 0; i < length(mastermind); i++){
            print(mastermind[i] + "★"+ ANSI_RESET + " ");
        }
        println();
    }
}
