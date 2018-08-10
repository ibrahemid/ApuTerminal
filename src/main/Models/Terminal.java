package main.Models;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Terminal extends Thread {

    private Foyer foyer;
    private int maximumBuses;
    public Semaphore busesSlots;
    public ArrayList<Bus> buses = new ArrayList<>();

    public Terminal(Foyer foyer,int maximumBuses){
        this.maximumBuses = maximumBuses;
        this.busesSlots = new Semaphore(maximumBuses);
        this.foyer = foyer;
    }

    @Override
    public void run() {
        int counter = 1;
        while (true){
            new Bus(counter++,this).start();

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enter(Bus bus){
        System.out.println("Bus ["+bus.ID+"] is attempting to enter");
        if (this.busesSlots.availablePermits() == 0){
            System.out.println("Bus ["+bus.ID+"] is going to wait outside from the terminal");
            synchronized (bus){
                try {
                    bus.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Bus ["+bus.ID+"] is going to enter the terminal");

        try {
            this.busesSlots.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.buses.add(bus);
        System.out.println("Bus ["+bus.ID+"] did enter the terminal");

        for (WaitingArea waitingArea : foyer.getWaitingAreas()){
            if (waitingArea.bus == null){
                System.out.println("Bus ["+bus.ID+"] is going to be assigned to waiting area ["+waitingArea.ID+"]");
                waitingArea.setBus(bus);
                bus.setWaitingArea(waitingArea);
                break;
            }
        }


    }

    public void leave(Bus bus){
        this.busesSlots.release();
        this.buses.remove(bus);
        bus.waitingArea.setBus(null);
        bus.setWaitingArea(null);
        System.out.println("Bus ["+bus.ID+"] did leave the terminal");

    }

}
