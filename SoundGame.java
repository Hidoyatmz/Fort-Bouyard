import extensions.*;

class SoundGame extends Quiz {
    final String SOUNDGAMECSV = "soundgame.csv";
    Epreuve initSoundGame() {
        return newEpreuve("SoundGame", -1, "Ya pas", GameState.KEYS);
    }

    boolean startSoundGame(Epreuve soundgame, Game game){
        CSVFile soundCSV = loadCSV(SOUNDGAMECSV);
        String[] answers = new String[rowCount(soundCSV)-1];
        Sound[] sounds = registerSounds(soundCSV, answers);
        int tour = 1;
        for(int i = 0; i < length(answers); i++){
            boolean found = doSound(sounds[i], answers[i], tour);
            if(found){
                myClearScreen();
                println("Bravo ! C'était bien un(e) " + answers[i]);
                delay(1500);
            }
            tour = tour +1;
        }
        return true;
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
        int trys = 3;
        String toCheck;
        boolean res = false;
        debug("Sound is running...");
        play(sound);
        do{
            myClearScreen();
            println("Animal n°" + tour);
            println("De quel animal sagit-il ?");
            println("Il te reste " + trys + " essais !");
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