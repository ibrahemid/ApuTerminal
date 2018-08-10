package main.Models;

import java.util.Random;

public class Ticket {

    private boolean isValidated;
    public int waitingAreaNumber = new Random().nextInt(3) + 1;

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
