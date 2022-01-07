/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Système de timer pour répondre
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class Maths extends EnglishGame {
    final String MATHEMATIXCSV = "mathematix.csv";

    Epreuve initMathematix() {
        return newEpreuve(6, "Mathematix", -1, "Résolvez cette suite de 6 calculs mentaux pour gagner !", GameState.KEYS);
    }
    boolean startMathematix(Epreuve mathematix) {
        CSVFile mathematixCSV = myLoadCSV(MATHEMATIXCSV);
        final int MAXCALC = rowCount(mathematixCSV) - 1;
        final int solveToWin = 6;
        // Check if the game can be run (If there is enough uniq questions.);
        if(solveToWin > MAXCALC){
            erreur(solveToWin + ">" + MAXCALC + " couldnt start " + mathematix.name + "...");
            info("Votre jeu est corrompu, veuillez le télécharger à nouveau.");
            delay(15000);
            return false;
        }
        int tour = 1;
        int[] played = new int[MAXCALC];
        boolean alive = true;
        String[] calc;
        do {
            calc = getNextCalc(mathematixCSV, MAXCALC, played, tour);
            alive = doCalc(calc, tour);
            if(alive){
                println(ANSI_GREEN + "Bravo ! "+ ANSI_RESET + getRandomCongrats());
                if(tour+1 < MAXCALC){
                    println("Passons au prochain calcul !");
                }
            } else {
                println(ANSI_RED + "Oh non... " + ANSI_RESET + "\nTu as perdu...");
            }
            tour++;
            delay(3000);
        } while(tour <= solveToWin && alive);
        return alive;
    }

    String getRandomCongrats() {
        final String[] congrats = new String[]{"Tu es très bon en calcul mentaux !", "Tu maîtrises les chiffres !", "Ca se voit que tu es un bon élève !"};
        return congrats[(randInt(1,length(congrats))-1)];
    }

    /**
     * Display the next question and return if the user has the correct answers.
     * @param calc
     * @param tour
     * @return true if user answered good, false otherwise.
     */

    boolean doCalc(String[] calc, int tour) {
        myClearScreen();
        println("Calcul n°" + tour + " !");
        println(calc[0]);
        int uChoice = enterNumber();
        return uChoice == stringToInt(calc[1]);
    }

    /**
     * Return the next question from the given CSVFile that has not been done yet.
     * @param mathematixCSV
     * @param MAXCALC
     * @param played
     * @param tour
     * @return A table of String being the next question with it answers.
     */

    String[] getNextCalc(CSVFile mathematixCSV, int MAXCALC, int[] played, int tour) {
        String[] res = new String[2];
        int r;
        do {
            r = randInt(1,MAXCALC);
        } while(inArray(played, r));
        played[(tour-1)] = r;
        res[0] = getCell(mathematixCSV, r, 0);
        res[1] = getCell(mathematixCSV, r, 1);
        return res;
    }
}
    