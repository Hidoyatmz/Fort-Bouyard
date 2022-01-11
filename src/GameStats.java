class GameStats extends TimerMethods {

    // get stats prisonniers
    int getNbJails(Game g){
        return g.stats[0];
    }

    // ajouter stat prisonnier
    void increaseNbJails(Game g){
        g.stats[0] = g.stats[0] + 1;
    }

    // get stat bonne réponse
    int getGoodAnswers(Game g){
        return g.stats[1];
    }
    
    // ajouter stat bonne réponse
    void increaseGoodAnswers(Game g){
        g.stats[1] = g.stats[1] + 1;
    }

    // get stat mauvaise réponse
    int getBadAnswers(Game g){
        return g.stats[2];
    }

    // ajouter stat mauvaise réponse
    void increaseBadAnswers(Game g){
        g.stats[2] = g.stats[2] + 1;
    }

    // get stat temps total
    int getFinaltime(Game g){
        return g.stats[3];
    }

    // ajouter stat temps total
    void setFinalTime(Game g){
        g.stats[3] = (int) getElapsedTime(g.timer);
    }

    // get stat conseil
    int getConseilTimeWon(Game g){
        return g.stats[4];
    }

    // ajouter stat conseil
    void increaseConseilTimeWon(Game g){
        g.stats[4] = g.stats[4] + 30;
    }

}
