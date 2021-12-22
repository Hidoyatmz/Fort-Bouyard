class Fakir extends EpreuvesCreator {
    
    Epreuve initFakir() {
        return newEpreuve(3, "Le Fakir", -1, "Choisi la couleur où la bille va tomber et met là où tu as envie qu'elle tombe.", GameState.JUGEMENT);
    }

    boolean startFakir(Epreuve epreuve, Game game) {
        // Enum FakirObjects avec BILLE, VIDE, POTEAU, TROU
        // Type FakirCase avec un FakirObjects

        

        return true;
    }
}