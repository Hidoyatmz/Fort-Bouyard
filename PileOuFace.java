class PileOuFace extends SoundGame {

    Epreuve initPileOuFace() {
        return newEpreuve(2, "Pile Ou Face", -1, "Epreuve de chance : choisis de quel côté la pièce tombera.", GameState.JUGEMENT);
    }

    boolean startPileOuFace(Epreuve epreuve, Game game) {
        println("De quel coté veux-tu que la pièce tombe ?");
        println("1 - Pile");
        println("2 - Face");
        int res;
        do {
            res = enterNumber();
        } while(!isBetween(res, 1, 2));
        int r = randInt(1, 2);
        if(r == res) {
            wonEpreuve(game);
        } else {
            lostEpreuve(game);
        }
        delay(5000);
        return true;
    }

    void wonEpreuve(Game game){
        game.nbKeys = game.nbKeys + 1;
        println("Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !");
    }

    void lostEpreuve(Game game){
        println("C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (4-game.nbKeys) + " clefs");
    }
    
}
