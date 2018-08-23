package main.Models;

import java.util.Random;

public class Customer extends Thread {
    public int ID;
    private Foyer foyer;
    public Ticket ticket;

    public Customer(int ID, Foyer foyer) {
        this.ID = ID;
        this.foyer = foyer;
    }

    @Override
    public void run() {
        /*Customer Life Cycle*/

        this.foyer.enter(this);

        int ticketPurchaseDicision = new Random().nextInt(3); //range

        TicketGenerator ticketGenerator; //

        if (ticketPurchaseDicision == 0) {
            ticketGenerator = this.foyer.ticketBooth1;
        } else if (ticketPurchaseDicision == 1) {
            ticketGenerator = this.foyer.ticketBooth2;
        } else {
            ticketGenerator = this.foyer.ticketMachine;
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.ticket = ticketGenerator.takeTicket(this); //abstraction

        WaitingArea waitingArea = null;
        if (this.ticket.waitingAreaNumber == 1) {
            waitingArea = this.foyer.waitingArea1;
        } else if (this.ticket.waitingAreaNumber == 2) {
            waitingArea = this.foyer.waitingArea2;
        } else {
            waitingArea = this.foyer.waitingArea3;
        }


        synchronized (this) { //customer
            waitingArea.enter(this);
            System.out.println("Customer Entered waiting Area " + this.ticket.waitingAreaNumber);

            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            waitingArea.exit(this);
            this.foyer.exit(this);

            waitingArea.bus.enter(this);
        }


    }
}

