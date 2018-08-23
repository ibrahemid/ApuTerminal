package main.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Foyer {
    private int foyerSize;
    private Semaphore slots;
    private ArrayList<Customer> customers = new ArrayList<>();

    ////

    TicketBooth ticketBooth1;
    TicketBooth ticketBooth2;
    TicketMachine ticketMachine;

    WaitingArea waitingArea1;
    WaitingArea waitingArea2;
    WaitingArea waitingArea3;


    public Foyer(int foyerSize) {
        this.foyerSize = foyerSize;
        slots = new Semaphore(foyerSize);

        ticketBooth1 = new TicketBooth(1);
        ticketBooth2 = new TicketBooth(2);
        ticketMachine = new TicketMachine(1);


        waitingArea1 = new WaitingArea(1, 12);
        waitingArea2 = new WaitingArea(2, 12);
        waitingArea3 = new WaitingArea(3, 12);


    }

    public void enter(Customer customer) {
        System.out.println("Customer [" + customer.ID + "] is trying to enter..the building");
        if (slots.availablePermits() <= foyerSize - foyerSize * 0.80) {
            try {
                System.out.println("Customer [" + customer.ID + "] is going to wait.. because the maximum capacity reached");
                synchronized (customer) {
                    customer.wait();
                }
                System.out.println("Customer [" + customer.ID + "] is proceeding to enter..");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            slots.acquire();
            System.out.println("Customer [" + customer.ID + "] did enter the building.. available permits [" + slots.availablePermits() + "]");
            customers.add(customer);
            synchronized (customer) {
                customer.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit(Customer customer) {
        slots.release();
        customers.remove(customer);
        System.out.println("Customer [" + customer.ID + "] did leave the foyer..");
    }

    public ArrayList<WaitingArea> getWaitingAreas() {

        return new ArrayList<>(List.of(this.waitingArea1, this.waitingArea2, this.waitingArea3));
    }

}
