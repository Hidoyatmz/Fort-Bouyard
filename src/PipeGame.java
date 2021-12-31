import extensions.*;

class PipeGame extends Fakir {

    final int MAPSPLAYCOUNT = 2;

    Epreuve initPipeGame() {
        return newEpreuve(1, "PipeGame", 40, "Pour résoudre ce puzzle vous devez relier les 2 extrémités des tuyaux pour faire circuler l'énergie avant que le temps ne soit écoulé.", GameState.KEYS);
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

    boolean startPipeGame(Epreuve epreuve, Game game) {
        File[] maps = randomMaps();
        Pipe[][] plateau = initPlateau(maps[0]);
        afficherMap(plateau);
        readString();
        return true;
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

    void afficherMap(Pipe[][] plateau) {
        String res = "";
        for(int i=0; i<length(plateau, 1); i++) {
            for(int j=0; j<length(plateau, 2); j++) {
                if(plateau[i][j].bloquer) res = res + ANSI_RED_BG;
                if(plateau[i][j].energie) res = res + ANSI_YELLOW;
                res = res + getStringPipe(plateau[i][j]) + ANSI_RESET;
            }
            res = res + "\n";
        }
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

    Pipe[][] initPlateau(File file) {
        String[][] map = loadMapFromFile(file);
        Pipe[][] res = new Pipe[length(map, 1)][length(map, 2)];
        for(int i=0; i<length(map, 1); i++) {
            for(int j=0; j<length(map, 2); j++) {
                initPipe(res, i, j, map[i][j]);
            }
        }
        return res;
    }

    Pipe newPipe(PipeType pipeType, Direction direction, boolean energie, boolean bloquer) {
        Pipe pipe = new Pipe();
        pipe.pipeType = pipeType;
        pipe.direction = direction;
        pipe.energie = energie;
        pipe.bloquer = bloquer;
        return pipe;
    }

    void initPipe(Pipe[][] plateau, int l, int c, String code) {
        int pipeTypeInt = getPipeType(code);
        char dir = length(code) > 1 ? getDir(code) : ' ';
        boolean energie = pipeTypeInt == 1 ? true : false;
        boolean bloquer = length(code) == 3 ? true : false;
        plateau[l][c] = newPipe(pipeFromInt(pipeTypeInt), dirFromCar(dir), energie, bloquer);
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
