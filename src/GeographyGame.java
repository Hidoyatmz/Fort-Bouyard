/**
 * @STATUS          : 90% COMPLETED;
 * @TODO            : Ajouter un timer par questions
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;

class GeographyGame extends BouyardCard {
    final String GEOGRAPHYCSV = "geography.csv";

    Epreuve initGeographyGame(){
        return newEpreuve(12, "Géograsquiz", -1, "Dans cette épreuve, une carte du monde ayant 3 continents numérotés s'affiche.\nRetrouvez le nom de ces 3 continents pour remporter la victoire.", GameState.KEYS);
    }

    boolean startGeographyGame(Epreuve epreuve, Game g){
        CSVFile geography = myLoadCSV(GEOGRAPHYCSV);
        final int MAX_WORLDS = rowCount(geography) - 1;
        String[] answer = loadNewWorld(geography, MAX_WORLDS);
        String worldName = "world"+answer[0]+".txt";
        boolean res = true;
        int i = 1;
        String uRes;
        Timer qTimer;
        do {
            myClearScreen();
            qTimer = newTimer(20);
            printTxt("../ressources/geography/"+worldName);
            println("Vous avez " + getFormatRemainingTime(qTimer) + " secondes pour répondre à la question.");
            println("Quel est le continent au point : " + ANSI_LIGHT_PINK + i + ANSI_RESET);
            uRes = toLowerCase(enterText());
            res = (equals(uRes, answer[i]) || equals(uRes, "l'"+answer[i])) && inTime(qTimer) ? true : false;
            if(res){
                increaseGoodAnswers(g);
                info(ANSI_LIGHT_GREEN + "Bravo !" + ANSI_RESET + " C'est la bonne réponse :)");
            } else {
                increaseBadAnswers(g);
                if(!inTime(qTimer)){
                    erreur("Vous avez dépassé le temps imparti de " + ANSI_LIGHT_PINK + (getElapsedTime(qTimer) - qTimer.maxTime) + ANSI_RESET + " secondes...");
                } else {
                    erreur("Quel dommage.. la bonne réponse était : " + ANSI_PURPLE + answer[i] + ANSI_RESET);
                }
            }   
            i++;
            delay(1000);
        } while(i < length(answer) && res);
        return res;
    }

    String[] loadNewWorld(CSVFile geography, int MAX_WORLDS) {
        int r = randInt(1,MAX_WORLDS);
        int cCount = columnCount(geography);
        String[] res = new String[cCount];
        for(int i = 0; i < cCount; i++){
            res[i] = toLowerCase(getCell(geography, r, i));
        }
        return res;
    }
}