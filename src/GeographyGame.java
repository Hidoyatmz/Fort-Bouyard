/**
 * @STATUS          : 0% COMPLETED;
 * @TODO            : Rien ?
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;

class GeographyGame extends BouyardCard {
    final String GEOGRAPHYCSV = "geography.csv";

    Epreuve initGeographyGame(){
        return newEpreuve(12, "GeographyGame", -1, "Jouez au jeu.", GameState.KEYS);
    }

    boolean startGeographyGame(Epreuve epreuve){
        CSVFile geography = myLoadCSV(GEOGRAPHYCSV);
        final int MAX_WORLDS = rowCount(geography) - 1;
        String[] answer = loadNewWorld(geography, MAX_WORLDS);
        String worldName = "world"+answer[0]+".txt";
        boolean res = true;
        int i = 1;
        String uRes;
        do {
            myClearScreen();
            printTxt("../ressources/geography/"+worldName);
            println("Quel est le continent au point : " + i);
            uRes = enterText();
            res = equals(uRes, answer[i]) ? true : false;
            if(res){
                info("Bravo ! C'est la bonne réponse :)");
            } else {
                info("Quel dommage.. la bonne réponse était : " + ANSI_PURPLE + answer[i] + ANSI_RESET);
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
            res[i] = getCell(geography, r, i);
        }
        return res;
    }
}