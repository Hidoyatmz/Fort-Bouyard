class Shifumi extends EpreuvesCreator {
    
    Epreuve initShiFuMi() {
        return newEpreuve(4, "ShiFuMi", -1, "Choisis soit la pierre, le papier ou les ciseaux. La pierre bat les ciseaux, les ciseaux battent le papier et le papier bat la pierre. Le premier qui gagne 3 manches remporte la victoire !", GameState.JUGEMENT);
    }

    boolean startShiFuMi(Epreuve epreuve, Game game) {
        int[] winLose = new int[]{0, 0};
        int res; // joueur
        int r; // robot
        while(!shiFuMiFinish(winLose)) {
            myClearScreen();
            println("Manches gagnées par toi : " + winLose[0] + " | Manches gagnées par le robot : " + winLose[1]);
            printCoups();
            do {
                res = enterNumber();
            } while(!isBetween(res, 1, 3));
            r = randInt(1, 3);
            updateWinLose(winLose, res, r);
            delay(3000);
        }
        delay(5000);
        return true;
    }

    boolean shiFuMiFinish(int[] winLose) {
        return winLose[0] == 3 || winLose[1] == 3;
    }

    void updateWinLose(int[] winLose, int player, int robot) {
        if(player == robot) {
            println("Egalité ! Personne ne prend de points !");
        }
        else if((player == 1 && robot == 2) || (player == 2 && robot == 3) || (player == 3 && robot == 1)) {
            winLose[0] = winLose[0] + 1;
            println("Tu gagnes 1 point !");
        } else {
            winLose[1] = winLose[1] + 1;
            println("Le robot prend 1 point !");
        }
    }

    void printCoups() {
        println("Quel coup choisis-tu ?");
        println("1 - Pierre");
        println("2 - Ciseaux");
        println("3 - Papier");
    }

    void wonEpreuve(Game game){
        game.nbKeys = game.nbKeys + 1;
        println("Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !");
    }

    void lostEpreuve(Game game){
        println("C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (4-game.nbKeys) + " clefs");
    }
}
