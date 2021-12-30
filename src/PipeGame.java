import extensions.*;

class PipeGame extends Fakir {

    final int MAPSPLAYCOUNT = 2;

    Epreuve initPipeGame() {
        return newEpreuve(1, "PipeGame", 40, "Pour résoudre ce puzzle vous devez relier les 2 extrémités des tuyaux pour faire circuler l'énergie avant que le temps ne soit écoulé.", GameState.KEYS);
    }

    boolean startPipeGame(Epreuve epreuve, Game game) {
        File[] maps = randomMaps();
        //Pipe[][] plateau = initPlateau(maps[0]);
        String[][] map = loadMapFromFile(maps[0]);
        for(int i=0; i<length(map, 1); i++) {
            for(int j=0; j<length(map, 2); j++) {
                print(map[i][j]);
            }
            print("\n");
        }
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

    Pipe[][] initPlateau(File file) {
        // DECODAGE MAP
        // 1er ligne : hauteur largeur
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
        Pipe[][] res = new Pipe[0][0];
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
