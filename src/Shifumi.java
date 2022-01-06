class Shifumi extends Maths {
    
    Epreuve initShiFuMi() {
        return newEpreuve(5, "ShiFuMi", -1, "Choisis soit la pierre, le papier ou les ciseaux. La pierre bat les ciseaux, les ciseaux battent le papier et le papier bat la pierre. Le premier qui gagne 3 manches remporte la victoire !", GameState.CONSEIL);
    }

    boolean startShiFuMi(Epreuve epreuve) {
        int[] winLose = new int[]{0, 0};
        int res; // joueur
        int r; // maitre
        while(!shiFuMiFinish(winLose)) {
            myClearScreen();
            println("Manches gagnées par toi : " + winLose[0] + " | Manches gagnées par le maître : " + winLose[1]);
            printCoups();
            do {
                res = enterNumber();
            } while(!isBetween(res, 1, 3));
            r = randInt(1, 3);
            println("Tu as joué " + getCoups(res));
            println("Le maître a joué " + getCoups(r));
            updateWinLose(winLose, res, r);
            delay(2000);
        }
        printWinner(winLose);
        return winLose[0] > winLose[1];
    }

    void printWinner(int[] winLose) {
        if(winLose[0] > winLose[1]) {
            println("Bien jouer tu remportes la partie !");
        } else {
            println("Dommage ! Le maître remporte la partie");
        }
    }

    boolean shiFuMiFinish(int[] winLose) {
        return winLose[0] == 3 || winLose[1] == 3;
    }

    String getCoups(int n) {
        String res;
        if(n == 1) res = "Pierre";
        else if(n == 2) res = "Ciseaux";
        else res = "Papier";
        return res;
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
            println("Le maître prend 1 point !");
        }
    }

    void printCoups() {
        println("Quel coup choisis-tu ?");
        println("1 - Pierre");
        println("2 - Ciseaux");
        println("3 - Papier");
    }
}
