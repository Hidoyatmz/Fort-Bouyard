class GameManager extends Quiz {

    void startGame(Game game) {
        myClearScreen();
        println("Et tout de suite : UN NOUVEAU JEU !");
        delay(2000);
        startEpreuve(game, game.epreuves[0]);
        startEpreuve(game, game.epreuves[1]);
        startEpreuve(game, game.epreuves[2]);
        startEpreuve(game, game.epreuves[3]);
    }

    void startEpreuve(Game game, Epreuve epreuve) {
        if(epreuve.id == 0) {
            startQuiz(epreuve, game);
        }
        else if(epreuve.id == 1) {
            startSoundGame(epreuve, game);
        }
        else if(epreuve.id == 2) {
            startPileOuFace(epreuve, game);
        }
        else if(epreuve.id == 3) {
            startFakir(epreuve, game);
        }
        else if(epreuve.id == 4) {
            startShiFuMi(epreuve, game);
        }
    }
    
}