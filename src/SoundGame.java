/**
 * @STATUS          : 100% COMPLETED;
 * @TODO            : ---
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class SoundGame extends PipeGame {
    final String SOUNDGAMECSV = "soundgame.csv";
    final int MAXTRYS = 5;
    Epreuve initSoundGame() {
        return newEpreuve(2, "SoundGame", -1, "Vous devez trouver 3 animaux sur les 5 joués !", GameState.KEYS);
    }

    boolean startSoundGame(Epreuve soundgame){
        CSVFile soundCSV = myLoadCSV(SOUNDGAMECSV);
        String[] answers = new String[rowCount(soundCSV)-1];
        Sound[] sounds = registerSounds(soundCSV, answers);
        int tour = 0;
        int goodAnswers = 0;
        shuffleArrays(sounds,answers);
        sounds = cutArray(0,MAXTRYS-1,sounds);
        answers = cutArray(0,MAXTRYS-1,answers);
        do {
            boolean found = doSound(sounds[tour], answers[tour], tour);
            myClearScreen();
            if(found){
                playSound(SOUND_CORRECT_ANSWER, true);
                println("Bravo ! C'était bien un(e) " + answers[tour]);
                goodAnswers += 1;
            } else {
                playSound(SOUND_WRONG_ANSWER, true);
                println("Pas de chance ! La réponse était : " + answers[tour]);
            }
            if(tour < length(answers)-1 ){
                println("Passons au prochain animal !");
            }
            delay(1500);
            tour = tour +1;
        } while(!userWon(goodAnswers, answers) && tour < length(answers) && canStillWin(tour, goodAnswers));
        return userWon(goodAnswers, answers);
    }

    boolean canStillWin(int tour, int goodAnswers) {
        return ((MAXTRYS-tour) + goodAnswers) >= 3;
    }

    Sound[] cutArray(int s, int e, Sound[] sounds) {
        int size = e-s;
        Sound[] newArray = new Sound[size+1];
        for(int i = s; i <= e; i++){
            newArray[i] = sounds[i];
        }
        return newArray;
    }

    void shuffleArrays(Sound[] sounds, String[] answers) {
        int r;
        int lenSounds = length(sounds);
        Sound tempSound;
        String tempString;
        for(int i = 0; i < lenSounds; i++) {
            r = randInt(0, lenSounds-1);
            tempSound = sounds[r];
            tempString = answers[r];
            sounds[r] = sounds[i];
            answers[r] = answers[i];
            sounds[i] = tempSound;
            answers[i] = tempString;
        }
    }

    boolean userWon(int goodAnswers, String[] answers){
        return goodAnswers >= 3;
    }

    Sound[] registerSounds(CSVFile soundCSV, String[] res) {
        int rowCount = rowCount(soundCSV);
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