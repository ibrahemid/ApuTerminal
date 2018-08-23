package main.Models;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class TicketMachine implements TicketGenerator {
    public int ID;
    public Semaphore slots = new Semaphore(1);

    public TicketMachine(int ID) {
        this.ID = ID;
    }

    @Override
    public synchronized Ticket takeTicket(Customer customer) {
        System.out.println("Customer [" + customer.ID + "] is attempting to take a ticket from ticket machine");
        boolean isBusy = new Random().nextBoolean();

        if (slots.availablePermits() != 1) {
            synchronized (customer) {
                try {
                    System.out.println("Customer [" + customer.ID + "] is going to wait because the machine is acquired by somebody else");

                    customer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isBusy) {
            try {
                System.out.println("Customer [" + customer.ID + "] is going to wait because machine is out of service currently");
                synchronized (customer) {
                    customer.wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            slots.acquire();
            System.out.println("Customer [" + customer.ID + "] acquired the machine");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Ticket ticket = new Ticket();
        System.out.println("New Ticket [" + ticket + "] Generated For [" + customer.ID + "] to Room [" + ticket.waitingAreaNumber + "] ");


        slots.release();
        return ticket;
    }

    //payForTicket and printTicket

//    synchronized  boolean payForTicket (int cusID){
//    }

}
