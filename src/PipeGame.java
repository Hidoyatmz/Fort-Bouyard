import extensions.*;

class PipeGame extends MemoGame {

    final int MAPSPLAYCOUNT = 2;
    final Direction[] dirs = new Direction[]{Direction.N, Direction.E, Direction.S, Direction.W};

    Epreuve initPipeGame() {
        return newEpreuve(1, "PipeGame", 40, "Pour résoudre ce puzzle vous devez relier les extrémités à la source avec l'aide des tuyaux pour faire circuler l'énergie avant que le temps ne soit écoulé.", GameState.KEYS);
    }

    // DECODAGE MAP
    // 1er ligne : hauteur largeur
    // 0 -> VIDE
    // 1 -> START  ⯌
    // 2 -> FIN    ⊔ ⊏ ⊓ ⊐
    // 3 -> L      ╚ ╔ ╗ ╝             ┗ ┏ ┓ ┛
    // 4 -> DROIT  ═ ║                 ━ ┃
    // 5 -> CROIX  ╬                   ╋
    // 6 -> T      ╩ ╠ ╦ ╣             ┻ ┣ ┳ ┫
    //             N E S W

    // Boucle de l'épreuve
    boolean startPipeGame(Epreuve epreuve, Game game) {
        File[] maps = randomMaps();
        boolean win = false;
        int imaps = 0;

        PipePlateau plateau;
        Coordonnees pos;

        while(imaps < length(maps) && !win) {
            plateau = initPlateau(maps[imaps]);
            afficherMap(plateau.plateau);
            while(!allEnergie(plateau)) {
                // demander les coordonnees de la case a tourner au joueur
                pos = askCoordonnees(plateau);
                // tourner la pièce
                rotate(plateau, pos);
                updateEnergie(plateau);
                delay(200);
                afficherMap(plateau.plateau);
            }
            println("Bien joué !");
            delay(3000);
            win = true;
            imaps++;
        }

        return win;
    }

    void updateEnergie(PipePlateau plateau) {
        for(int i=0; i<length(plateau.plateau, 1); i++) {
            for(int j=0; j<length(plateau.plateau, 2); j++) {
                if(plateau.plateau[i][j].pipeType != PipeType.START) {
                    plateau.plateau[i][j].energie = false;
                }
            }
        }
        updateEnergieCase(plateau.plateau, plateau.start.l, plateau.start.c);
    }

    // Update l'énergie d'une case ainsi que les cases qui suivent celle ci
    void updateEnergieCase(Pipe[][] plateau, int l, int c) {
        Coordonnees[] voisins = pipeVoisins(plateau, l, c);
        int vl;
        int vc;
        for(int i=0; i<length(voisins); i++) {
            vl = voisins[i].l;
            vc = voisins[i].c;
            if(!plateau[vl][vc].energie && canReceiveEnergie(plateau, vl, vc, dirFromCoos(l, c, vl, vc))) {
                plateau[vl][vc].energie = true;
                updateEnergieCase(plateau, vl, vc);
            }
        }
    }

    // Direction de la case 2 par rapport a la case 1
    Direction dirFromCoos(int l1, int c1, int l2, int c2) {
        Direction res = Direction.NULL;
        if(l2 < l1) res = Direction.N;
        else if(l2 > l1) res = Direction.S;
        else if(c2 > c1) res = Direction.E;
        else if(c2 < c1) res = Direction.W;
        return res;
    }

    Coordonnees[] pipeVoisins(Pipe[][] plateau, int l, int c) {
        Coordonnees[] coordonnees = new Coordonnees[0];

        PipeType pipeType;
        for(int i=-1; i<=1; i=i+2) {
            if(l+i >= 0 && l+i < length(plateau, 1)){
                if(contains(plateau[l][c].connexions, dirFromCoos(l, c, l+i, c))) {
                    pipeType = plateau[l+i][c].pipeType;
                    if(pipeType != PipeType.VOID && pipeType != PipeType.START) {
                        coordonnees = add(coordonnees, newCoordonnees(l+i, c));
                    }
                }
            } 
            if(c+i >= 0 && c+i < length(plateau, 2)) {
                if(contains(plateau[l][c].connexions, dirFromCoos(l, c, l, c+i))) {
                    pipeType = plateau[l][c+i].pipeType;
                    if(pipeType != PipeType.VOID && pipeType != PipeType.START) {
                        coordonnees = add(coordonnees, newCoordonnees(l, c+i));
                    }
                }
            }
        }

        return coordonnees;
    }

    boolean contains(Direction[] directions, Direction direction) {
        boolean res = false;
        int i = 0;
        while(i<length(directions) && !res) {
            if(directions[i] == direction) res = true;
            i++;
        }
        return res;
    }

    Coordonnees[] add(Coordonnees[] coos, Coordonnees coo) {
        Coordonnees[] newCoos = new Coordonnees[length(coos)+1];
        for(int i=0; i<length(coos); i++) {
            newCoos[i] = coos[i];
        }
        newCoos[length(newCoos)-1] = coo;
        return newCoos;
    }

    boolean canReceiveEnergie(Pipe[][] plateau, int l, int c, Direction dir) {
        Pipe pipe = plateau[l][c];
        int dirInt = getIntFromDir(pipe.direction);
        boolean res = false;
        if(pipe.pipeType == PipeType.L) {
            if(dirs[(dirInt+2) % length(dirs)] == dir || dirs[(dirInt+3) % length(dirs)] == dir) {
                res = true;
            }
        }
        else if(pipe.pipeType == PipeType.T) {
            if(pipe.direction != dir) {
                res = true;
            }
        }
        else if(pipe.pipeType == PipeType.DROIT) {
            if(pipe.direction == dir || dirs[(dirInt+2) % length(dirs)] == dir) {
                res = true;
            }
        }
        else if(pipe.pipeType == PipeType.CROIX) {
            res = true;
        }
        else if(pipe.pipeType == PipeType.END) {
            if(pipe.direction == dir || dirs[(dirInt+2) % length(dirs)] == dir) {
                res = true;
            }
        }
        return res;
    }

    // Tourner une piece d'un crang vers la droite sur le plateau aux coordonnees pos
    void rotate(PipePlateau plateau, Coordonnees pos) {
        Pipe piece = plateau.plateau[pos.l][pos.c];
        if(piece.direction != Direction.NULL) {
            int dir = getIntFromDir(piece.direction) + 1;
            piece.direction = dirs[dir % length(dirs)];
            for(int i=0; i<length(piece.connexions); i++) {
                piece.connexions[i] = dirs[(getIntFromDir(piece.connexions[i])+1)%4];
            }
        }
    }

    // Donne un entier selon une Direction
    int getIntFromDir(Direction dir) {
        int res = 0;
        if(dir == Direction.E) res = 1;
        else if(dir == Direction.S) res = 2;
        else if(dir == Direction.W) res = 3;
        return res;
    }

    // Demande des coordonnees de la case a tourner (jusqu'elle soit valide) au joueur
    Coordonnees askCoordonnees(PipePlateau plateau) {
        String pos;
        Coordonnees coordonnees = newCoordonnees(-1, -1);
        do {
            println("Quel case veux tu tourner ? (A1 par exemple)");
            pos = toUpperCase(readString());
            if(length(pos) > 1) {
                coordonnees.l = charAt(pos, 0)-'A';
                coordonnees.c = charAt(pos, 1)-'0'-1;
            }
        } while(!validPos(plateau.plateau, coordonnees));
        return coordonnees;
    }

    // Renvoie true si les coordonnees d'une case peut etre tourner sinon false
    boolean validPos(Pipe[][] plateau, Coordonnees coordonnees) {
        boolean res = false;
        if(coordonnees.l >= 0 && coordonnees.l < length(plateau, 1) && coordonnees.c >= 0 && coordonnees.c < length(plateau, 2)) {
            Pipe pipe = plateau[coordonnees.l][coordonnees.c];
            if(pipe.pipeType == PipeType.VOID) {
                println("Cette case est vide tu ne peux pas la tourner !");
            }
            else if(pipe.pipeType == PipeType.START || pipe.pipeType == PipeType.END || pipe.bloquer) {
                println("Cette case ne peut pas être tourner !");
            }
            else {
                res = true;
            }
        }
        return res;
    }

    File[] randomMaps() {
        String path = "../ressources/pipegame/";
        String[] maps = getAllFilesFromDirectory(path);
        shuffleMaps(maps);
        File[] files = new File[MAPSPLAYCOUNT];
        for(int i=0; i<MAPSPLAYCOUNT; i++) {
            files[i] = newFile(path + maps[i]);
        }
        return files;
    }

    boolean allEnergie(PipePlateau plateau) {
        boolean all = true;
        int i = 0;
        Coordonnees pos;
        while(i < length(plateau.fins) && all) {
            pos = plateau.fins[i];
            if(!plateau.plateau[pos.l][pos.c].energie) all = false;
            i++;
        }
        return all;
    }

    void afficherMap(Pipe[][] plateau) {
        myClearScreen();
        String res = "  ";
        for(int i=1; i<=length(plateau, 2); i++) {
            res = res + i + " ";
        }
        res = res + "\n";
        char cLine = 'A';
        for(int i=0; i<length(plateau, 1); i++) {
            res = res + cLine + " ";
            for(int j=0; j<length(plateau, 2); j++) {
                if(plateau[i][j].bloquer) res = res + ANSI_RED_BG;
                if(plateau[i][j].energie) res = res + ANSI_YELLOW;
                res = res + getStringPipe(plateau[i][j]) + ANSI_RESET + " ";
            }
            res = res + "\n";
            cLine = (char) (cLine + 1);
        }
        println(res);
    }

    String getStringPipe(Pipe pipe) {
        String res;
        if(pipe.pipeType == PipeType.START) res = "⯌";
        else if(pipe.pipeType == PipeType.END) {
            if(pipe.direction == Direction.N) res = "⊔";
            else if(pipe.direction == Direction.E) res = "⊏";
            else if(pipe.direction == Direction.S) res = "⊓";
            else res = "⊐";
        }
        else if(pipe.pipeType == PipeType.L) {
            if(pipe.direction == Direction.N) res = "┗";
            else if(pipe.direction == Direction.E) res = "┏";
            else if(pipe.direction == Direction.S) res = "┓";
            else res = "┛";
        }
        else if(pipe.pipeType == PipeType.DROIT) {
            if(pipe.direction == Direction.N || pipe.direction == Direction.S) res = "┃";
            else res = "━";
        }
        else if(pipe.pipeType == PipeType.CROIX) {
            res = "╋";
        }
        else if(pipe.pipeType == PipeType.T) {
            if(pipe.direction == Direction.N) res = "┻";
            else if(pipe.direction == Direction.E) res = "┣";
            else if(pipe.direction == Direction.S) res = "┳";
            else res = "┫";
        }
        else {
            res = " ";
        }
        return res;
    }

    PipePlateau initPlateau(File file) {
        String[][] map = loadMapFromFile(file);
        Pipe[][] plateau = new Pipe[length(map, 1)][length(map, 2)];
        for(int i=0; i<length(map, 1); i++) {
            for(int j=0; j<length(map, 2); j++) {
                initPipe(plateau, i, j, map[i][j]);
                
            }
        }
        return newPipePlateau(plateau, getStart(plateau), getFins(plateau));
    }

    Coordonnees newCoordonnees(int l, int c) {
        Coordonnees coordonnees = new Coordonnees();
        coordonnees.l = l;
        coordonnees.c = c;
        return coordonnees;
    }

    Coordonnees getStart(Pipe[][] plateau) {
        Coordonnees res = newCoordonnees(0, 0);
        int i = 0;
        int j;
        boolean found = false;
        while(i < length(plateau, 1) && !found) {
            j = 0;
            while(j < length(plateau, 2) && !found) {
                if(plateau[i][j].pipeType == PipeType.START) {
                    res.l = i;
                    res.c = j;
                }
                j++;
            }
            i++;
        }
        return res;
    }

    Coordonnees[] getFins(Pipe[][] plateau) {
        Coordonnees[] fins = new Coordonnees[0];

        for(int i=0; i<length(plateau, 1); i++) {
            for(int j=0; j<length(plateau, 2); j++) {
                if(plateau[i][j].pipeType == PipeType.END) fins = add(fins, newCoordonnees(i, j));
            }
        }

        return fins;
    }

    PipePlateau newPipePlateau(Pipe[][] plateau, Coordonnees start, Coordonnees[] fins) {
        PipePlateau pipePlateau = new PipePlateau();
        pipePlateau.plateau = plateau;
        pipePlateau.start = start;
        pipePlateau.fins = fins;
        return pipePlateau;   
    }

    Pipe newPipe(PipeType pipeType, Direction direction, Direction[] connexions, boolean energie, boolean bloquer) {
        Pipe pipe = new Pipe();
        pipe.pipeType = pipeType;
        pipe.direction = direction;
        pipe.connexions = connexions;
        pipe.energie = energie;
        pipe.bloquer = bloquer;
        return pipe;
    }

    void initPipe(Pipe[][] plateau, int l, int c, String code) {
        int pipeTypeInt = getPipeType(code);
        char dir = length(code) > 1 ? getDir(code) : ' ';
        boolean energie = pipeTypeInt == 1 ? true : false;
        boolean bloquer = length(code) == 3 ? true : false;
        PipeType pipeType = pipeFromInt(pipeTypeInt);
        Direction direction = dirFromCar(dir);
        plateau[l][c] = newPipe(pipeType, direction, createConnexions(pipeType, direction), energie, bloquer);
    }

    char getDir(String s) {
        return charAt(s, 1);
    }

    int getPipeType(String s) {
        return charAt(s, 0) - '0';
    }

    PipeType pipeFromInt(int n) {
        PipeType res = PipeType.VOID;
        if(n == 1) res = PipeType.START;
        else if(n == 2) res = PipeType.END;
        else if(n == 3) res = PipeType.L;
        else if(n == 4) res = PipeType.DROIT;
        else if(n == 5) res = PipeType.CROIX;
        else if(n == 6) res = PipeType.T;
        return res;
    }

    Direction dirFromCar(char c) {
        Direction res = Direction.NULL;
        if(c == 'N') res = Direction.N;
        else if(c == 'E') res = Direction.E;
        else if(c == 'S') res = Direction.S;
        else if(c == 'W') res = Direction.W;
        return res;
    }

    Direction[] createConnexions(PipeType pipeType, Direction direction) {
        Direction[] connexions;
        if(pipeType == pipeType.VOID) connexions = new Direction[]{Direction.NULL};
        else if(pipeType == PipeType.START || pipeType == PipeType.CROIX) connexions = new Direction[]{Direction.N, Direction.E, Direction.S, Direction.W};
        else if(pipeType == PipeType.END) connexions = new Direction[]{direction};
        else if(pipeType == PipeType.DROIT) connexions = new Direction[]{direction, dirs[(getIntFromDir(direction)+2)%length(dirs)]};
        else if(pipeType == PipeType.L) connexions = new Direction[]{direction, dirs[(getIntFromDir(direction)+1)%length(dirs)]};
        else connexions = new Direction[]{direction, dirs[(getIntFromDir(direction)+1)%length(dirs)], dirs[(getIntFromDir(direction)+3)%length(dirs)]};
        return connexions;
    }

    String[][] loadMapFromFile(File file) {
        String[] strSize = split(readLine(file), ' ');
        int[] size = new int[]{stringToInt(strSize[0]), stringToInt(strSize[1])};
        String[][] res = new String[size[0]][size[1]];
        String[] line;
        for(int i=0; i<size[0]; i++) {
            line = split(readLine(file), ' ');
            for(int j=0; j<size[1]; j++) {
                res[i][j] = line[j];
            }
        }
        return res;
    }

    void shuffleMaps(String[] maps) {
        int randomPosition;
        String temp;
        for(int i=0; i<length(maps); i++) {
            randomPosition = (int) (random()*length(maps));
            temp = maps[i];
            maps[i] = maps[randomPosition];
            maps[randomPosition] = temp;
        }
    }
    
}
