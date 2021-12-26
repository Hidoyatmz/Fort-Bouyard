class Fakir extends PileOuFace {
    
    Epreuve initFakir() {
        return newEpreuve(3, "Le Fakir", -1, "Choisi la couleur où la bille va tomber et met là où tu as envie qu'elle tombe.", GameState.JUGEMENT);
    }

    boolean startFakir(Epreuve epreuve, Game game) {
        FakirItems[][] grille = new FakirItems[6][8];
        // initialisation grille
        // 
        return true;
    }

    enum FakirItems {
        VIDE, BILLE, BARRE, TROU_BLANC, TROU_BLEU;
    }
}