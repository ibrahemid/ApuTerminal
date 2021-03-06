package main.Models;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class TicketBooth implements TicketGenerator {
    public int ID;
    public Semaphore slots = new Semaphore(1);

    public TicketBooth(int ID) {
        this.ID = ID;
    }

    @Override
    public Ticket takeTicket(Customer customer) { //the method in the TicketGenerator
        System.out.println("Customer [" + customer.ID + "] is attempting to take a ticket");
        boolean isBusy = new Random().nextBoolean();

        if (slots.availablePermits() != 1) {
            synchronized (customer) {
                try {
                    System.out.println("Customer [" + customer.ID + "] is going to wait because booth is acquired by somebody else");
                    customer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isBusy) {
            try {
                System.out.println("Customer [" + customer.ID + "] is going to wait because the employer in Booth " + this.ID + " is busy currently");
                synchronized (customer) {
                    customer.wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            slots.acquire();
            System.out.println("Customer [" + customer.ID + "] acquired to booth " + this.ID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Ticket ticket = new Ticket();
        System.out.println("New Ticket [" + ticket + "] Generated For Customer [" + customer.ID + "] to waiting [" + ticket.waitingAreaNumber + "] ");

        synchronized (customer) {
            customer.notifyAll();
        }

        slots.release();
        return ticket;
    }
}
