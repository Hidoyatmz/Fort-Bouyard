import extensions.*;

class Mists extends PileOuFace {
    final int[] DIMENSIONS = new int[]{20,30};
    final int NB_PLAYERS = 2;
    final String PLAYER_SYMBOL = "⅄";
    final String PYLONNE_SYMBOL = "•";
    final String PYLONNE_COLOR = ANSI_GREEN_BG + ANSI_WHITE;
    final String PYLONNE_COLOR_UNACTIVE = ANSI_RED_BG + ANSI_WHITE;
    final String WALL_COLOR = ANSI_WHITE_BG;
    final String[] PLAYERS_COLORS = new String[]{ANSI_RED,ANSI_BLUE};
    int[][] POS_PYLONNES = new int[][]{{2,0}, {0,8}, {3,9}, {9,7}, {19,4}, {18,19}, {12,16}, {1,28}, {18,29}, {10,15}, {16,24}, {19,26}};
    final int NB_PYLONNES = length(POS_PYLONNES, 1);
    final String[] PYLONNE_HIDDENS = new String[]{"correct-sound-2","correct-sound","mario-coin","mario-death","minecraft-damage","wrong-sound"};
    Pylonne[] pylonnesPlayers = new Pylonne[2];
    GameMists game;
    long lastSoundPlay = getTime()-1000;

    Epreuve initMists() {
        return newEpreuve(13, "Mists", 180, "Trouez les pairs de sons. Pylonne vert = actif, pylonne rouge = non actif. Le but est de désactiver tous les pylonnes.", GameState.INDICES);
    }

    boolean startMists() {
        long initTime = getTime();
        String test;
        int score = 0;
        shuffleArray(POS_PYLONNES);
        if(!checkEnoughSounds()){
            printlnRed("Jeu corrompu.. Il n'y a pas assez de sons !");
            return false;
        }
        if(!checkPylonnesCountEven()){
            printlnRed("Jeu corrompu.. Le nombre de pylonnes sur la map n'est pas pair.");
            return false;
        }
        game = newGame(DIMENSIONS, NB_PLAYERS, NB_PYLONNES);
        PlayerMists player1 = game.players[0];
        PlayerMists player2 = game.players[1];
        hide();
        while(getElapsedTime(initTime) < 180 && !allUnactivePylonnes(game)){
            clearScreen();
            cursor(0,0);
            enableKeyTypedInConsole(false);
            println("Score: " + score);
            getPylonnesAroundPlayers(game);
            if(checkPlayersPylonnes(game)){
                score++;
            }
            displayMap(game.map);
            enableKeyTypedInConsole(true);
            delay(100);
        }
        show();
        enableKeyTypedInConsole(false);
        return allUnactivePylonnes(game);
    }

    boolean allUnactivePylonnes(GameMists game){
        return getUnactivePylonnes(game) == NB_PYLONNES;
    }

    int getUnactivePylonnes(GameMists game){
        Pylonne[] pylonnes = game.pylonnes;
        int cpt = 0;
        for(int i = 0; i < length(pylonnes); i++){
            if(!pylonnes[i].active){
                cpt++;
            }
        }
        return cpt;
    }

    boolean checkEnoughSounds(){
        return (length(POS_PYLONNES,1)/2) <= length(PYLONNE_HIDDENS);
    }

    boolean checkPylonnesCountEven(){
        return length(POS_PYLONNES,1)%2 == 0;
    }

    String stringToSoundPath(String s){
        return "sound/"+s+".mp3";
    }

    int getElapsedTime(long initTime){
        return (int) ((getTime()-initTime)/1000);
    }

    void getPylonnesAroundPlayers(GameMists g){
        PlayerMists[] players = game.players;
        for(int i = 0; i < length(players); i++){
            println(ANSI_GREEN + "[DEBUG] Infos joueur n°" + i + " : " + ANSI_RESET);
            println("X: " + players[i].posX + " | " + "Y: " + players[i].posY);
            println("Pylonne à coté :" + hasPylonneAround(players[i]));
            if(hasPylonneAround(players[i])){
                int[] pylPos = getPylonnePosAroundPlayer(players[i]);
                Pylonne pyl = getPylonneById(getPylonneIdByPos(pylPos[0], pylPos[1]));
                pylonnesPlayers[i] = pyl;
                println("Pylonne id : "+pyl.id+" hidden: " + pyl.hidden + " active: " + toString(pyl.active));
                if(getElapsedTime(lastSoundPlay) > 1 && (!pyl.triggered)){
                    playSound(stringToSoundPath(pyl.hidden), true);
                    lastSoundPlay = getTime();
                    pyl.triggered = true;
                }
            } else {
                if(pylonnesPlayers[i] != null) {
                    pylonnesPlayers[i].triggered = false;
                    pylonnesPlayers[i] = null;
                }
                println("Pylonne id : null hidden: null active: null");
            }
            println();
        }
    }

    boolean checkPlayersPylonnes(GameMists game){
        PlayerMists[] players = game.players;
        int cpt = 0;
        for(int i = 0; i < length(players); i++){
            if(pylonnesPlayers[i] != null){
                cpt++;
            }
        }
        if(cpt == 2){
            if(pylonneAreEven(pylonnesPlayers)){
                for(int i = 0; i < length(players); i++){
                    pylonnesPlayers[i].active = false;
                    pylonnesPlayers[i].color = PYLONNE_COLOR_UNACTIVE;
                    game.map[pylonnesPlayers[i].posX][pylonnesPlayers[i].posY] = toString(pylonnesPlayers[i]);
                }
            }
            return pylonneAreEven(pylonnesPlayers);
        }
        return false;
    }

    boolean pylonneAreEven(Pylonne[] pylonnes){
        println("Debug purpose : " + pylonnes[0].hidden + " vs " + pylonnes[1].hidden);
        return equals(pylonnes[0].hidden, pylonnes[1].hidden) && (pylonnes[0].id != pylonnes[1].id);
    }

    void printlnRed(String s){
        println(ANSI_RED + s + ANSI_RESET);
    }

    void printlnBlue(String s){
        println(ANSI_BLUE + s + ANSI_RESET);
    }

    boolean isPylonne(String toTest){
        return equals(toTest, toStringPylonne(PYLONNE_SYMBOL));
    }

    boolean hasPylonneAround(PlayerMists player){
        String[] neighbours = getCellsAroundPlayer(player);
        boolean res = false;
        for(int i = 0; i < length(neighbours); i++){
            if(neighbours[i] != null && isPylonne(neighbours[i])){
                res = true;
            }
        }
        return res;
    }

    void keyTypedInConsole(char key){
        if(key == ANSI_RIGHT){
            movePlayer(game, 0, 'r');
            clearLine();
        } 
        else if(key == ANSI_LEFT){
            movePlayer(game, 0, 'l');
            clearLine();
        }
        else if(key == ANSI_UP){
            movePlayer(game, 0, 'u');
            clearLine();
        }
        else if(key == ANSI_DOWN){
            movePlayer(game, 0, 'd');
            clearLine();
        }
        else if(key == 'D' || key == 'd'){
            movePlayer(game, 1, 'r');
            clearLine();
        }
        else if(key == 'Q' || key == 'q'){
            movePlayer(game, 1, 'l');
            clearLine();
        }
        else if(key == 'Z' || key == 'z'){
            movePlayer(game, 1, 'u');
            clearLine();
        }
        else if(key == 'S' || key == 's'){
            movePlayer(game, 1, 'd');
            clearLine();
        }
    }

    String[] getCellsAroundPlayer(PlayerMists player){
        String[] res = new String[9];
        int posX = player.posX;
        int posY = player.posY;
        String ibx = "";
        String iby = "";
        int cpt = 0;
        for(int x = posX-1; x <= posX + 1; x++){
            for(int y = posY-1; y <= posY + 1; y++){
                if(inBoundsX(x) && inBoundsY(y)){
                    res[cpt] = game.map[x][y];
                }
                cpt++;
            }
        }
        return res;
    }

    Pylonne getPylonneById(int id){
        Pylonne pylonne = game.pylonnes[0];
        for(int i = 0; i < length(game.pylonnes); i++){
            if(game.pylonnes[i].id == id){
                pylonne = game.pylonnes[i];
            }
        }
        return pylonne;
    }

    int getPylonneIdByPos(int x, int y){
        int cpt = 0;
        for(int i = 0; i < length(POS_PYLONNES); i++){
            if(POS_PYLONNES[i][0] == x && POS_PYLONNES[i][1] == y){
                return cpt;
            }
            cpt++;
        }
        return cpt;
    }

    int[] getPylonnePosAroundPlayer(PlayerMists player){
        int[] res = new int[2];
        res[0] = 0;
        res[1] = 0;
        int posX = player.posX;
        int posY = player.posY;
        for(int x = posX-1; x <= posX + 1; x++){
            for(int y = posY-1; y <= posY + 1; y++){
                if(inBoundsX(x) && inBoundsY(y)){
                    if(isPylonne(game.map[x][y])){
                        res[0] = x;
                        res[1] = y;
                    }
                }
            }
        }
        return res;
    }
    
    boolean inBoundsX(int x){
        return (x >= 0 ) && (x < length(game.map, 1));
    }

    boolean inBoundsY(int y){
        return (y >= 0) && (y < length(game.map,2));
    }

    void movePlayer(GameMists game, int id, char direction){
        PlayerMists playerToMove = getPlayerById(game, id);
        int posX = playerToMove.posX;
        int posY = playerToMove.posY;
        if(direction == 'r'){
            if(((posY+1) < length(game.map,2)) && isVoid(game.map, posX, posY+1)){
                game.map[posX][posY+1] = toString(playerToMove);
                game.map[posX][posY] = " ";
                playerToMove.posY += 1;
            }
        }
        else if(direction == 'l'){
            if(((posY-1) >= 0) && isVoid(game.map, posX, posY-1)){
                game.map[posX][posY-1] = toString(playerToMove);
                game.map[posX][posY] = " ";
                playerToMove.posY -= 1;
            }
        }
        else if(direction == 'u'){
            if(((posX-1) >= 0) && isVoid(game.map, posX-1, posY)){
                game.map[posX-1][posY] = toString(playerToMove);
                game.map[posX][posY] = " ";
                playerToMove.posX -= 1;
            }
        }
        else if(direction == 'd'){
            if(((posX+1) < length(game.map,1)) && isVoid(game.map, posX+1, posY)){
                game.map[posX+1][posY] = toString(playerToMove);
                game.map[posX][posY] = " ";
                playerToMove.posX += 1;
            }
        }
    }

    boolean isVoid(String[][] map, int x, int y){
        return equals(map[x][y], " ");
    }

    PlayerMists getPlayerById(GameMists game, int id){
        PlayerMists[] playersArray = game.players;
        PlayerMists player = playersArray[0];
        boolean res = false;
        int i = 0;
        do {
            if(playersArray[i].id == id){
                res = true;
                player = playersArray[i];
                res = true;
            }
            i++;
        } while(i < length(playersArray) && !res);
        return player;
    }

    GameMists newGame(int[] dimensions, int nbPlayers, int nbPylonnes) {
        int cptPylonnes = 0;
        int idxHiddens = 0;
        GameMists game = new GameMists();
        game.map = new String[dimensions[0]][dimensions[1]];
        initMap(game.map);
        game.players = new PlayerMists[nbPlayers];
        game.pylonnes = new Pylonne[nbPylonnes];
        for(int i = 0; i < NB_PLAYERS; i++){
            game.players[i] = newPlayer(i, length(game.map, 1)-1,i);
        }
        for(int i = 0; i < nbPylonnes; i++){
            game.pylonnes[i] = newPylonne(i);
        }
        for(int i = 0; i < length(game.pylonnes); i++){
            if(cptPylonnes > 0 && ((cptPylonnes % 2) == 0)){
                idxHiddens++;
            }
            Pylonne pyl = game.pylonnes[i];
            pyl.hidden = PYLONNE_HIDDENS[idxHiddens];
            game.map[pyl.posX][pyl.posY] = toString(pyl);
            cptPylonnes++;
        }
        for(int i = 0; i < length(game.players); i++){
            PlayerMists p = game.players[i];
            game.map[p.posX][p.posY] = toString(p);
        }
        return game;
    }

    PlayerMists newPlayer(int id, int posX, int posY){
        PlayerMists player = new PlayerMists();
        player.id = id;
        player.posX = posX;
        player.posY = posY;
        player.color = PLAYERS_COLORS[id];
        return player;
    }

    Pylonne newPylonne(int id){
        Pylonne pylonne = new Pylonne();
        pylonne.id = id;
        pylonne.color = PYLONNE_COLOR;
        pylonne.posX = POS_PYLONNES[id][0];
        pylonne.posY = POS_PYLONNES[id][1];
        pylonne.triggered = false;
        pylonne.active = true;
        return pylonne;
    }

    void initMap(String[][] gameMap){
        for(int x = 0; x < length(gameMap, 1); x++){
            for(int y = 0; y < length(gameMap, 2); y++){
                gameMap[x][y] = " ";
            }
        }
    }

    void displayMap(String[][] gameMap){
        displayWall(gameMap);
        for(int x = 0; x < length(gameMap, 1); x++){
            displayWallPiece();
            for(int y = 0; y < length(gameMap, 2); y++){
                if(!equals(gameMap[x][y], " ")){
                    print(gameMap[x][y]);
                } else {
                    print(" ");
                }
            }
            displayWallPiece();
            println();
        }
        displayWall(gameMap);
    }

    void displayWallPiece(){
        print(WALL_COLOR+" "+ANSI_RESET);
    }

    void displayWall(String[][] gameMap){
        for(int i = 0; i <= length(gameMap, 2)+1; i++){
            displayWallPiece();
        }
        println();
    }

    String toString(PlayerMists player){
        return player.color + PLAYER_SYMBOL + ANSI_RESET;   
    }

    String toString(Pylonne pylonne){
        return pylonne.color + PYLONNE_SYMBOL + ANSI_RESET;
    }

    String toString(boolean b){
        return b ? "true" : "false";
    }

    String toStringPylonne(String pylonne){
        return PYLONNE_COLOR + PYLONNE_SYMBOL + ANSI_RESET;
    }

    void println(String[] array){
        for(int i = 0; i < length(array); i++){
            print(array[i] + " ");
        }
        println();
    }

    int randInt(int min, int max){
        return (int) (random()*((max+1)-min)+min);
    }

    void shuffleArray(int[][] array){
        int[] temp;
        int r;
        for(int j = 0; j < 5; j++){
            for(int i = 0; i < length(array,1); i++){
                temp = array[i];
                r = randInt(0,length(array,1)-1);
                array[i] = array[r];
                array[r] = temp;
            }
        }
    }

}