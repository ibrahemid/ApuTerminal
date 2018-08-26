package main.Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class WaitingArea {
    public int ID;
    public int waitingAreaSize;
    public Semaphore slots;
    public ArrayList<Customer> customers = new ArrayList<>();
    public TicketScanner ticketScanner = new TicketScanner();
    private Queue<Customer> customerQueue = new LinkedList<>();



    public Bus bus = null;

    public WaitingArea(int ID, int waitingAreaSize) {
        this.ID = ID;
        this.waitingAreaSize = waitingAreaSize;
        this.slots = new Semaphore(waitingAreaSize);
    }

    public void enter(Customer customer) {
        System.out.println("Customer [" + customer.ID + "] is trying to enter waiting area [" + this.ID + "] .. available permits [" + slots.availablePermits() + "]");
        this.ticketScanner.checkTicket(this, customer);
        if (slots.availablePermits() == 0) {
            System.out.println("Customer [" + customer.ID + "] is going to wait for waiting area [" + this.ID + "] because there are no available permits..");

            synchronized (customer) {
                try {
                    customerQueue.add(customer);
                    customer.wait();
                } catch (InterruptedException e) {
                    e.getCause();

                }
            }
        }
        try {
            this.slots.acquire();
            System.out.println("Customer [" + customer.ID + "] did enter the waiting area waiting area [" + this.ID + "] .. available permits [" + slots.availablePermits() + "]");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void exit(Customer customer) {
        slots.release();
        customers.remove(customer);
        System.out.println("Customer [" + customer.ID + "] did leave the waiting area [" + this.ID + "]..");

        if (customerQueue.size() != 0) {
            Customer CustomerToBeNotified = customerQueue.peek();
            customerQueue.remove(CustomerToBeNotified);
            synchronized (CustomerToBeNotified) {
                CustomerToBeNotified.notify();
            }
        } else {
            synchronized (customer) {
                customer.notifyAll();
            }
        }
    }


    public void setBus(Bus bus) {
        this.bus = bus;
    }


}
