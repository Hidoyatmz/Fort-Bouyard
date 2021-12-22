import extensions.*;

class Quiz extends EpreuvesCreator {
    final String CHARADECSV = "charades.csv";
    
    Epreuve initQuiz() {
        return newEpreuve(0, "Quiz", -1, "Ya pas", GameState.KEYS);
    }

    boolean startQuiz(Epreuve quiz, Game game){
        String[] charade = getRandomCharade(CHARADECSV);
        String answer;
        int trys = 3;
        do{
            printIntro(quiz, charade);
            printInfos(quiz, trys);
            answer = enterText();
            if(!isCharadeValidAnswer(answer, charade)){
                trys = trys - 1;
            }
        } while(!isCharadeValidAnswer(answer, charade) && trys > 0);
        if(trys > 0){
            wonQuiz(game);
        } else {
            lostQuiz(game);
        }
        delay(3000);
        return true;
    }

    void wonQuiz(Game game){
        game.nbKeys = game.nbKeys + 1;
        println("Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !");
    }

    void lostQuiz(Game game){
        println("C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (4-game.nbKeys) + " clefs");
    }

    void printIntro(Epreuve quiz, String[] charade) {
        myClearScreen();
        println("Bonjour et bienvenue dans le QUIQUIQUIZZZ !");
        println("Voici les règles : " + quiz.rules);
        printCharade(charade);
    }

    void printInfos(Epreuve quiz, int trys){
        println("Tu as " + ANSI_RED + trys + ANSI_RESET + " essais !");
        println("Attention ! Tu as " + quiz.timer + " secondes pour trouver la réponse.");
        println("Entrez votre réponse : ");
    }

    String getCharadeAnswer(String[] charade){
        return toLowerCase(charade[0]);
    }

    boolean isCharadeValidAnswer(String answer, String[] charade){
        return equals(toLowerCase(answer), getCharadeAnswer(charade));
    }

    String[] getRandomCharade(String filename) {
        CSVFile charades = loadCSV(CHARADECSV);
        String[] res = new String[rowCount(charades)-1];
        int line = randInt(1, rowCount(charades));
        for(int i = 0; i < length(res); i++){
            res[i] = getCell(charades, line, i);
        }
        return res;
    }

    void printCharade(String[] charade){
        for(int i = 1; i < length(charade); i++){
            println(ANSI_BLUE + charade[i] + ANSI_RESET);
        }
    }

}