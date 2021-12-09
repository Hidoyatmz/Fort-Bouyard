import extensions.*;

class Utils extends Program {

    void testIsBetween() {
        assertTrue(isBetween(5, 1, 10));
        assertFalse(isBetween(15, 1, 10));
    }

    boolean isBetween(int entry, int min, int max) {
        return ((entry >= min) && (entry <= max));
    }

    void testIsDigit(){
        assertTrue(isDigit('9'));
        assertFalse(isDigit('a'));
    }

    boolean isDigit(char letter){
        return ((letter >= '0') && (letter <= '9'));
    }

    void testRandInt(){
        assertEquals(true, isBetween(randInt(1,100),1,100));
        assertEquals(false, isBetween(randInt(101,150),1,100));
    }

    int randInt(int min, int max){
        return (int) (random()*(max-min)+min);
    }

    void debug(String s){
        println(ANSI_GREEN + "[Fort Bouyard 1.0 DEBUGGER] : " + ANSI_RESET + s);
    }

    void info(String s){
        println(ANSI_CYAN + s + ANSI_RESET);
    }

    String readTxt(String path) {
        File txt = newFile(path);
        String res = "";

        while(ready(txt)) {
            res += readLine(txt) + "\n";
        }

        return res;
    }


    void printTxt(String path) {
        println(readTxt(path));
    }

    void myClearScreen(){
        clearScreen();
        cursor(0,0);
    }

}