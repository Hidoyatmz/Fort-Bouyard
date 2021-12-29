import extensions.*;

class SoundGame extends PipeGame {
    final String SOUNDGAMECSV = "soundgame.csv";
    Epreuve initSoundGame() {
        return newEpreuve(2, "SoundGame", -1, "Vous devez trouver 3 animaux sur les 5 joués !", GameState.KEYS);
    }

    boolean startSoundGame(Epreuve soundgame, Game game){
        CSVFile soundCSV = myLoadCSV(SOUNDGAMECSV);
        String[] answers = new String[rowCount(soundCSV)-1];
        Sound[] sounds = registerSounds(soundCSV, answers);
        int tour = 0;
        int goodAnswers = 0;
        introSoundGame(soundgame);
        /* @TODO :
            -- Shuffle les sounds au début du lancement
            -- En jouer que 5.
        */
        do {
            boolean found = doSound(sounds[tour], answers[tour], tour);
            myClearScreen();
            if(found){
                println("Bravo ! C'était bien un(e) " + answers[tour]);
                goodAnswers += 1;
            } else {
                println("Pas de chance ! La réponse était : " + answers[tour]);
            }
            if(tour < length(answers)-1 ){
                println("Passons au prochain animal !");
            }
            delay(1500);
            tour = tour +1;
        } while(!userWon(goodAnswers, answers) && tour < length(answers));
        return userWon(goodAnswers, answers);
    }

    void introSoundGame(Epreuve soundgame){
        myClearScreen();
        println("Bonjour et bienvenue au " + soundgame.name);
        println("Règles : " + soundgame.rules);
        delay(3000);
    }

    boolean userWon(int goodAnswers, String[] answers){
        return goodAnswers >= 3;
    }

    Sound[] registerSounds(CSVFile soundCSV, String[] res) {
        int rowCount = rowCount(soundCSV);
        // int colCount = columnCount(soundCSV);
        Sound[] sounds = new Sound[rowCount-1];
        for(int i = 1; i < rowCount; i++){
            sounds[i-1] = newSound("../ressources/sounds/" + getCell(soundCSV, i, 0));
            res[i-1] = toLowerCase(getCell(soundCSV, i, 1));
        }
        return sounds;
    }

    boolean doSound(Sound sound, String answer, int tour){
        int trys = 2;
        String toCheck;
        boolean res = false;
        debug("Sound is running...");
        play(sound);
        do{
            myClearScreen();
            println("Animal n°" + (tour + 1));
            println("De quel animal sagit-il ?");
            println("Tu as " + ANSI_RED + trys + ANSI_RESET + " essais !");
            println("Entrez votre réponse : ");
            toCheck = toLowerCase(enterText());
            if(!equals(answer, toCheck)){
                trys = trys - 1;
            }
        } while(!equals(answer, toCheck) && trys > 0);
        if(trys > 0){
            res = true;
        }
        return res;
    }

}