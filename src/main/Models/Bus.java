package main.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class Bus extends Thread {
    public int ID;

    public WaitingArea waitingArea;
    public Terminal terminal;
    public Semaphore slots = new Semaphore(10);
    public ArrayList<Customer> customers = new ArrayList<>();

    public Bus(int ID, Terminal terminal){
        this.ID = ID;
        this.terminal = terminal;
    }

    @Override
    public void run() {
        this.terminal.enter(this);
        System.out.println("Bus ["+this.ID+"] notifying customers in waiting area ["+this.waitingArea.ID+"] ");
        while (this.slots.availablePermits() != 0){
            if (this.waitingArea.customers.size() > 0){
                int counter = 0;
                ArrayList<Customer> copyCustomers = new ArrayList<>(waitingArea.customers);

                for (Iterator<Customer> iterator = copyCustomers.iterator(); iterator.hasNext();){
                    Customer customer = iterator.next();
                    System.out.println("Bus ["+this.ID+"] is notifying customer ["+customer.ID+"]");
                    synchronized (customer) {
                        customer.notify();
                    }
                    counter++;
                    if (counter == 10) break;
                }
            }else{
                System.out.println("Bus ["+this.waitingArea.ID+"] is empty .. waiting for someone to come");
                try {
                    synchronized (this) {
                        this.wait(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("Bus ["+this.ID+"] is full and ready to go..");

        this.terminal.leave(this);
    }

    public void enter(Customer customer){
        System.out.println("Customer ["+customer.ID+"] is attempting to enter the bus");
        if (slots.availablePermits() == 0){
            synchronized (customer){
                try {
                    System.out.println("Customer ["+customer.ID+"] is going to wait because the bus is full");

                    customer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            slots.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        customers.add(customer);
        System.out.println("Customer ["+customer.ID+"] did enter the bus");

    }


    public WaitingArea getWaitingArea() {
        return waitingArea;
    }

    public void setWaitingArea(WaitingArea waitingArea) {
        this.waitingArea = waitingArea;
    }
}

