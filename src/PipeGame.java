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
    // 3 -> L -> NE
    // 4 -> L -> SE
    // 5 -> L -> SW
    // 6 -> L -> NW
    // 7 -> DROIT -> horizontale
    // 8 -> DROIT -> verticale
    // 9 -> CROIX
    // 10 -> T -> N
    // 11 -> T -> E
    // 12 -> T -> S
    // 13 -> T -> W
}
