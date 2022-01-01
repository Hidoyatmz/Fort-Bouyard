/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : C'passe des trucs bressons avec le comptage des espaces sur le cursor Kappa
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class Roulette extends Fakir {
    final int SIZE = 8;
    Epreuve initRoulette() {
        return newEpreuve(8, "Roulette", -1, "Devinez sur quelle couleur s'arretera le curseur ! +1 clef bonus si vert.", GameState.KEYS);
    }

    boolean startRoulette(Epreuve epreuve, Game game) {
        String[] roulette = new String[15];
        String cursor = "^" + ANSI_CURSOR_HIDE;
        boolean res = false;
        int tour = 1;
        int tourTodo = randInt(32,80);
        int[] posBlue = new int[]{1,5,9,13,17,21,25,29};
        fillRoulette(roulette);
        String userChoice = askUserChoice();
        myClearScreen();
        println("Voyons voir si vous aviez raison !");
        printRoulette(roulette);
        cusp();
        do {
            if(tour > 1){
                curp();
                clearEOL();
            }
            print(cursor);
            cursor = moveCursor(cursor,tour);
            delay(getDelay(tour, tourTodo));
            tour = tour + 1;
        } while (tour <= tourTodo);
        println();
        if(inArray(posBlue,(countSpaceInString(cursor)+1)) || countSpaceInString(cursor) == -2){
            res = equals(userChoice, ANSI_BLUE) ? true : false;
        } else {
            res = equals(userChoice, ANSI_RED) ? true : false;
        }
        if(countSpaceInString(cursor) == 14){
            res = equals(userChoice, ANSI_GREEN) ? true : false;
        }
        if(res){
            println(ANSI_CURSOR_SHOW + ANSI_GREEN + "Bravo ! " + ANSI_RESET + " C'est gagné.");
        } else {
            println(ANSI_CURSOR_SHOW + ANSI_RED + "Dommage ! " + ANSI_RESET + " C'est perdu.");
        }
        delay(2000);
        return res;
    }

    void fillRoulette(String[] roulette) {
        for(int i = 0; i < length(roulette); i++){
            if(i%2 == 0){
                roulette[i] = ANSI_BLUE;
            } else {
                roulette[i] = ANSI_RED;
            }
            if(i == 7) {
                roulette[i] = ANSI_GREEN;
            }
        }
    }

    int getDelay(int tour, int tourTodo){
        if(tour < (tourTodo/3)){
            return 100;
        } else if(tour < (tourTodo/2)){
            return 200;
        } else if(tour < (tourTodo/1.5)){
            return 250;
        } else if(tour < (tourTodo/1.2)){
            return 350;
        } else {
            return 500;
        }
    }

    String askUserChoice(){
        String choice;
        myClearScreen();
        cursor(0,0);
        int n;
        println("Vous avez le choix entre ces 3 couleurs :\n1. Bleu\n2. Rouge\n3. Vert");
        print("Quelle vouleur voulez vous :");
        cursor(6,0);
        do {
            n = enterNumber();
        } while(!isBetween(n, 1, 3));
        if(n == 1) {
            choice = ANSI_BLUE;
        } else if(n ==2){
            choice = ANSI_RED;
        } else {
            choice = ANSI_GREEN;
        }
        return choice;
    }

    void printRoulette(String[] roulette) {
        for(int i = 0; i < length(roulette); i++){
            print(roulette[i] + "•"+ ANSI_RESET + " ");
        }
        println();
    }

    String moveCursor(String cursor, int tour){
        if(tour%15 == 0){
            cursor = "^";
        } else {
            cursor = "  " + cursor;
            // if(tour%2 == 0){
            //     cursor = " " + cursor;
            // } else {
            //     cursor = "  " + cursor;
            // }
        }
        return cursor;
    }
    
    int countSpaceInString(String cursor){
        int res = 0;
        for(int i = 0; i < length(cursor); i++){
            if(charAt(cursor, i) == ' '){
                res++;
            }
        }
        return res-2;
    }
    
}
