class TimerMethods extends Utils {
    long getElapsedTime(Timer timer){
        long initTime = timer.initTime;
        return (getTime() - initTime)/1000;
    }

    boolean inTime(Timer timer){
        return getRemainingTime(timer) > 0;
    }

    long getRemainingTime(Timer timer) {
        long remainingTime = (timer.maxTime) - getElapsedTime(timer);
        return remainingTime > 0 ? remainingTime : 0;
    }

    String getFormatRemainingTime(Timer timer){
        String color = ANSI_GREEN;
        if(!inTime(timer)){
            color = ANSI_RED;
        } else if(getRemainingTime(timer) < (timer.maxTime / 2)) {
            color = ANSI_YELLOW;
        }
        return "" + color + getRemainingTime(timer) + ANSI_RESET;
    }

    Timer newTimer(int maxTime){
        Timer timer = new Timer();
        timer.initTime = getTime();
        timer.maxTime = maxTime;
        timer.inTime = inTime(timer);
        return timer;
    }
}