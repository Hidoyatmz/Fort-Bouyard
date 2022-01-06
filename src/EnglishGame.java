/**
 * @STATUS          : 0% COMPLETED;
 * @TODO            : Tout
 * @OPTIMIZATION    : NOT DONE
 */

import extensions.*;
class EnglishGame extends BouyardCard {
    final String ENGLISHGAMECSV = "englishgame.csv";

    Epreuve initEnglishGame() {
        return newEpreuve(11, "EnglishGame", -1, "Jouez au jeu.", GameState.KEYS);
    }

    boolean startEnglishGame(Epreuve epreuve) {
        return false;
    }
}