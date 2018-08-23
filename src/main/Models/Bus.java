package main.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class Bus extends Thread {
    public int ID;
    BusType busType;
    public WaitingArea waitingArea;
    public Terminal terminal;
    public Semaphore slots = new Semaphore(10);
    public ArrayList<Customer> customers = new ArrayList<>();

    public Bus(int ID, Terminal terminal, BusType busType) {
        this.ID = ID;
        this.terminal = terminal;
        this.busType = busType;
    }

    @Override
    public void run() {
        this.terminal.enter(this);
        System.out.println("Bus [" + this.ID + "] notifying customers in waiting area [" + this.waitingArea.ID + "] ");

        while (this.slots.availablePermits() != 0) {//loop to reach no slot
            int counter = 0; // init counter
            ArrayList<Customer> customersWaiting = new ArrayList<>(waitingArea.customers);//temp array (in Java we cant alter An array while we loop trough it


            for (Iterator<Customer> iterator = customersWaiting.iterator(); iterator.hasNext(); ) {

                Customer customer = iterator.next();//filling the bus until 10
                System.out.println("Waiting Area has " + this.waitingArea.customers.size());
                System.out.println("Bus [" + this.ID + "] is notifying customer [" + customer.ID + "] in Waiting Area " + this.waitingArea.ID); //waking up the customer waiting

                synchronized (customer) {
                    customer.notify();
                }
                if (counter == 10) {
                    break;
                }
                counter++;
            }

            System.out.println("Bus [" + this.ID + "] has " + this.slots.availablePermits() + " free seats .. ");
            if (busType == BusType.NORMAL_BUS) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                synchronized (this) {
                    try {
                        System.out.println("Bus [" + this.ID + "] is fast type going to leave after 5sec");
                        this.wait(5000);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        System.out.println("Bus [" + this.ID + "] is leaving || bus type: " + busType.toString() + " ||Number of Passenger:" + this.customers.size());
        this.terminal.leave(this);
    }

    public void enter(Customer customer) {
        synchronized (customer) {

            System.out.println("Customer [" + customer.ID + "] is attempting to enter the bus"); //

            try {
                slots.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            customers.add(customer);
            System.out.println("Customer [" + customer.ID + "] did enter the bus[ " + this.ID + " ]");
        }
    }


    public void setWaitingArea(WaitingArea waitingArea) {
        this.waitingArea = waitingArea;
    }
}

//4:28 save