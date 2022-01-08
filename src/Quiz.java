import extensions.*;

class Quiz extends SoundGame {
    final String CHARADECSV = "charades.csv";
    
    Epreuve initQuiz() {
        return newEpreuve(0, "Charades du père fouras", 60, "Dans cette épreuve, vous devez résoudre la charade que le père fouras vous énoncera en moins de 3 essais et dans le temps imparti.\nPrécision : les accents sont pris en compte.", GameState.KEYS);
    }

    boolean startQuiz(Epreuve quiz){
        String[] charade = getRandomCharade(CHARADECSV);
        String answer;
        int trys = 3;
        if(debug) {
            trys = 50;
        }
        Timer timer = newTimer(quiz.timer);
        do{
            printIntro(quiz, charade);
            printInfos(quiz, trys, timer);
            answer = enterText();
            if(!isCharadeValidAnswer(answer, charade)){
                playSound(SOUND_WRONG_ANSWER,true);
                trys = trys - 1;
            }
        } while(inTime(timer) && !isCharadeValidAnswer(answer, charade) && trys > 0);
        playSound(SOUND_CORRECT_ANSWER_2,true);
        return trys > 0 && inTime(timer);
    }

    void printIntro(Epreuve quiz, String[] charade) {
        myClearScreen();
        printCharade(charade);
    }

    void printInfos(Epreuve quiz, int trys, Timer timer){
        println("Tu as " + ANSI_RED + trys + ANSI_RESET + " essais !");
        println("Attention ! Il te reste " + getFormatRemainingTime(timer) + " secondes pour trouver la réponse.");
        println("Entrez votre réponse : ");
    }

    String getCharadeAnswer(String[] charade){
        return toLowerCase(charade[0]);
    }

    boolean isCharadeValidAnswer(String answer, String[] charade){
        return equals(toLowerCase(answer), getCharadeAnswer(charade));
    }

    String[] getRandomCharade(String filename) {
        CSVFile charades = myLoadCSV(CHARADECSV);
        String[] res = new String[rowCount(charades)-1];
        int line = randInt(1, rowCount(charades)-1);
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