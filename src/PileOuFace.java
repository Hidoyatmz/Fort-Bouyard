class PileOuFace extends Shifumi {

    Epreuve initPileOuFace() {
        return newEpreuve(4, "Pile Ou Face", -1, "Epreuve de chance : choisis de quel côté la pièce tombera.", GameState.JUGEMENT);
    }

    boolean startPileOuFace(Epreuve epreuve) {
        println("De quel coté veux-tu que la pièce tombe ?");
        println("1 - Pile");
        println("2 - Face");
        int res;
        do {
            res = enterNumber();
        } while(!isBetween(res, 1, 2));
        int r = randInt(1, 2);
        println("La pièce est tombé sur " + getFace(r));
        return r == res;
    }

    String getFace(int n) {
        String res = "Face";
        if(n == 1) res = "Pile";
        return res;
    }
    
}
