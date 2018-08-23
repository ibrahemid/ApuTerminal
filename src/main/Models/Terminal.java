package main.Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Terminal extends Thread {

    private Queue<Bus> queue = new LinkedList<>();
    private Foyer foyer;
    private int maximumBuses;
    private Semaphore busesSlots;
    private ArrayList<Bus> buses = new ArrayList<>();

    public Terminal(Foyer foyer, int maximumBuses) {
        this.maximumBuses = maximumBuses;
        this.busesSlots = new Semaphore(maximumBuses);
        this.foyer = foyer;
    }

    //desptch
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        int counter = 1;
        while (true) {
            executorService.submit(new Bus(counter++, this, getBusType()));//id is counter starts from 1
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enter(Bus bus) {
        synchronized (bus) {
            bus.notifyAll();
        }
        System.out.println("Bus [" + bus.ID + "] is attempting to enter, available permits = " + this.busesSlots.availablePermits());
        while (this.busesSlots.availablePermits() == 0) {

            System.out.println("Bus [" + bus.ID + "] is going to wait outside the terminal");
            synchronized (bus) {
                try {
                    queue.add(bus);
                    bus.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Bus [" + bus.ID + "] is going to enter the terminal");

        try {
            this.busesSlots.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.buses.add(bus);
        System.out.println("Bus [" + bus.ID + "] did enter the terminal, available permits = " + this.busesSlots.availablePermits());

        for (WaitingArea waitingArea : foyer.getWaitingAreas()) {
            if (waitingArea.bus == null) {
                System.out.println("Bus [" + bus.ID + "] is going to be assigned to waiting area [" + waitingArea.ID + "]");
                waitingArea.setBus(bus);
                bus.setWaitingArea(waitingArea);
                break;
            }
        }


    }

    public void leave(Bus bus) {
        this.busesSlots.release();
        this.buses.remove(bus);
        bus.waitingArea.setBus(null);
        bus.setWaitingArea(null);
        System.out.println("Bus [" + bus.ID + "] did leave the terminal, available permits = " + this.busesSlots.availablePermits());

        if (queue.size() != 0) {
            Bus busToBeNotified = queue.poll();
            synchronized (busToBeNotified) {
                busToBeNotified.notify();
            }
        } else {
            synchronized (bus) {
                bus.notifyAll();
            }
        }
    }



    /**
     * @return A random BusType each Time called
     */
    private BusType getBusType() {
        return (new Random().nextBoolean()) ? BusType.FAST_BUS : BusType.NORMAL_BUS;

    }
}



