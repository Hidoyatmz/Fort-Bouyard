import extensions.*;

class SoundGame extends Quiz {
    final String SOUNDGAMECSV = "soundgame.csv";
    Epreuve initSoundGame() {
        return newEpreuve(1, "SoundGame", -1, "Vous devez trouver 3 animaux sur les 5 joués !", GameState.KEYS);
    }

    boolean startSoundGame(Epreuve soundgame, Game game){
        CSVFile soundCSV = loadCSV(SOUNDGAMECSV);
        String[] answers = new String[rowCount(soundCSV)-1];
        Sound[] sounds = registerSounds(soundCSV, answers);
        int tour = 1;
        int goodAnswers = 0;
        introSoundGame(soundgame);
        for(int i = 0; i < length(answers); i++){
            boolean found = doSound(sounds[i], answers[i], tour);
            myClearScreen();
            if(found){
                println("Bravo ! C'était bien un(e) " + answers[i]);
                goodAnswers += 1;
            } else {
                println("Pas de chance ! La réponse était : " + answers[i]);
            }
            if(i < length(answers)-1 ){
                println("Passons au prochain animal !");
            }
            delay(1500);
            tour = tour +1;
        }
        if(userWon(goodAnswers, answers)){
            wonSoundGame(game);
        } else {
            lostSoundGame(game);
        }
        delay(3000);
        return true;
    }

    void introSoundGame(Epreuve soundgame){
        myClearScreen();
        println("Bonjour et bienvenue au " + soundgame.name);
        println("Règles : " + soundgame.rules);
        delay(3000);
    }

    void wonSoundGame(Game game){
        game.nbKeys = game.nbKeys + 1;
        println("Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !");
    }

    void lostSoundGame(Game game){
        println("C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (4-game.nbKeys) + " clefs");
    }

    boolean userWon(int goodAnswers, String[] answers){
        return goodAnswers >= ((length(answers)/2) + 1);
    }

    Sound[] registerSounds(CSVFile soundCSV, String[] res) {
        int rowCount = rowCount(soundCSV);
        int colCount = columnCount(soundCSV);
        Sound[] sounds = new Sound[rowCount-1];
        for(int i = 1; i < rowCount; i++){
            sounds[i-1] = newSound("sounds/" + getCell(soundCSV, i, 0));
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
            println("Animal n°" + tour);
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