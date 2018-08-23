package main.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WaitingArea {
    public int ID;
    public int waitingAreaSize;
    public Semaphore slots;
    public ArrayList<Customer> customers = new ArrayList<>();
    public TicketScanner ticketScanner = new TicketScanner();


    public Bus bus = null;

    public WaitingArea(int ID, int waitingAreaSize) {
        this.ID = ID;
        this.waitingAreaSize = waitingAreaSize;
        this.slots = new Semaphore(waitingAreaSize);
    }

    public void enter(Customer customer) {
        System.out.println("Customer [" + customer.ID + "] is trying to enter waiting area [" + this.ID + "]..");
        this.ticketScanner.checkTicket(this, customer);
        if (slots.availablePermits() == 0) {
            try {
                System.out.println("Customer [" + customer.ID + "] is going to wait for waiting area [" + this.ID + "] because there is no available permits..");
                synchronized (customer) {
                    customer.wait();
                }
                System.out.println("Customer [" + customer.ID + "] is proceeding to enter in waiting area [" + this.ID + "]..");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            slots.acquire();
            System.out.println("Customer [" + customer.ID + "] did enter the waiting area waiting area [" + this.ID + "] .. available permits [" + slots.availablePermits() + "]");
            customers.add(customer);
//            synchronized (customer) {
//                customer.notifyAll();
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void exit(Customer customer) {
        slots.release();
        customers.remove(customer);
        System.out.println("Customer [" + customer.ID + "] did leave the waiting area [" + this.ID + "]..");

        synchronized (customer) {
            customer.notifyAll();
        }
    }


    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }


}
