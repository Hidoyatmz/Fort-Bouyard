import extensions.*;

class EpreuvesCreator extends Utils {

    Epreuve newEpreuve(String name, int timer, String rules, GameState gameState) {
        Epreuve epreuve = new Epreuve();
        epreuve.name = name;
        epreuve.timer = timer;
        epreuve.rules = rules;
        epreuve.gameState = gameState;
        return epreuve;
    }

}