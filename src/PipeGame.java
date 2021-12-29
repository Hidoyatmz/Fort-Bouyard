class PipeGame extends Fakir {

    

    Epreuve initPipeGame() {
        return newEpreuve(1, "PipeGame", 40, "Pour résoudre ce puzzle vous devez relier les 2 extrémités des tuyaux pour faire circuler l'énergie avant que le temps ne soit écoulé.", GameState.KEYS);
    }

    boolean startPipeGame(Epreuve epreuve, Game game) {

        Pipe[][] plateau = new Pipe[4][4];

        return true;
    }
    
    // DECODAGE MAP
    // 0 -> VIDE
    // 1 -> START
    // 2 -> FIN
    // 3 -> L
    // 4 -> DROIT
    // 5 -> CROIX
    // 6 -> T

    // N
    // E
    // S
    // W
}
