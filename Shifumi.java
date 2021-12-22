class Shifumi extends EpreuvesCreator {
    
    Epreuve initShiFuMi() {
        return newEpreuve(4, "ShiFuMi", -1, "Choisis soit la pierre, le papier ou les ciseaux. La pierre bat les ciseaux, les ciseaux battent le papier et le papier bat la pierre.", GameState.JUGEMENT);
    }

    // A FINIR
    boolean startShiFuMi(Epreuve epreuve, Game game) {
        println("Quel coup choisis-tu ?");
        println("1 - Pierre");
        println("2 - Ciseaux");
        println("3 - Feuille");
        int res;
        do {
            res = enterNumber();
        } while(!isBetween(res, 1, 3));
        int r = randInt(1, 3);
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
