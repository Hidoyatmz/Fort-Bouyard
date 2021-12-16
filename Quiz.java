import extensions.*;

class Quiz extends EpreuvesCreator {
    final String CHARADECSV = "charades.csv";
    Epreuve initQuiz() {
        return newEpreuve("Quiz", -1, "Ya pas", GameState.KEYS);
    }

    void startQuiz(Epreuve quiz){
        String[] charade = getRandomCharade(CHARADECSV);
        String answer;
        int trys = 3;
        do{
            printIntro(quiz, charade);
            println("Il te reste " + trys + " essais !");
            println("Attention ! Tu as " + quiz.timer + " secondes pour trouver la réponse.");
            println("Entrez votre réponse : ");
            answer = enterText();
            trys = trys - 1;
        } while(!isCharadeValidAnswer(answer, charade) && trys > 0);
        if(trys > 0){
            println("Bravo ! Tu as gagné une clef pour ton équipe !");
        } else {
            println("C'est perdu.. Quel dommage ahah !");
        }
        delay(3000);
    }

    void printIntro(Epreuve quiz, String[] charade) {
        myClearScreen();
        println("Bonjour et bienvenue dans le QUIQUIQUIZZZ !");
        println("Voici les règles : " + quiz.rules);
        printCharade(charade);
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