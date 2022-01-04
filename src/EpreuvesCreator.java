// import extensions.*;

class EpreuvesCreator extends TimerMethods {

    Epreuve newEpreuve(int id, String name, int timer, String rules, GameState gameState) {
        Epreuve epreuve = new Epreuve();
        epreuve.id = id;
        epreuve.name = name;
        epreuve.timer = timer;
        epreuve.rules = rules;
        epreuve.gameState = gameState;
        return epreuve;
    }

}