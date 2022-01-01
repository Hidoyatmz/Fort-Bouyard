/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Système de joker
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class Maths extends EpreuvesCreator {
    final String MATHEMATIXCSV = "mathematix.csv";

    Epreuve initMathematix() {
        return newEpreuve(6, "Mathematix", -1, "Résolvez cette suite de 6 calculs mentaux pour gagner ! (Vous pouvez utiliser 1 fois le mot Joker pour changer de calcul)", GameState.KEYS);
    }
    boolean startMathematix(Epreuve mathematix, Game game) {
        CSVFile mathematixCSV = myLoadCSV(MATHEMATIXCSV);
        final int MAXCALC = rowCount(mathematixCSV) - 1;
        final int solveToWin = 6;
        if(solveToWin > MAXCALC){
            erreur(solveToWin + ">" + MAXCALC + " couldnt start " + mathematix.name + "...");
            info("Votre jeu est corrompu, veuillez le télécharger à nouveau.");
            delay(15000);
            return false;
        }
        // int joker = 1;
        int tour = 1;
        int[] played = new int[MAXCALC];
        boolean alive = true;
        String[] calc;
        do {
            calc = getNextCalc(mathematixCSV, MAXCALC, played, tour);
            alive = doCalc(calc, tour);
            if(alive){
                println(ANSI_GREEN + "Bravo ! "+ ANSI_RESET + getRandomCompliment());
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

    String getRandomCompliment() {
        final String[] compliments = new String[]{"Tu es très bon en calcul mentaux !", "Tu maîtrises les chiffres !", "Ca se voit que tu es un bon élève !"};
        return compliments[(randInt(1,length(compliments))-1)];
    }

    boolean doCalc(String[] calc, int tour) {
        myClearScreen();
        println("Calcul n°" + tour + " !");
        println(calc[0]);
        int uChoice = enterNumber();
        return uChoice == stringToInt(calc[1]);
    }

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
    