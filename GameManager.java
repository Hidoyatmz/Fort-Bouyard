class GameManager extends SoundGame {

    void startGame(Game game) {
        myClearScreen();
        println("Et tout de suite : UN NOUVEAU JEU !");
        delay(2000);
    }

    void startEpreuve(Game game, Epreuve epreuve) {
        if(epreuve.id == 0) {
            startQuiz(epreuve, game);
        }
        else if(epreuve.id == 1) {
            startSoundGame(epreuve, game);
        }
    }
    
}