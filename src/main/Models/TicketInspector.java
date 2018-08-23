package main.Models;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;

public class TicketInspector extends Thread {
    Foyer foyer;

    public TicketInspector(Foyer foyer) {
        this.foyer = foyer;
    }

    @Override
    public void run() {

        while (true) {
            ArrayList<WaitingArea> waitingAreas = new ArrayList<>(this.foyer.getWaitingAreas());

            for (WaitingArea waitingArea : waitingAreas) {

                for (Customer customer : new ArrayList<Customer>(waitingArea.customers)) {

                    if (!customer.ticket.isValidated()) {
                        System.out.println("Inspector is checking customer [" + customer.ID + "] in room [" + waitingArea.ID + "]");
                        customer.ticket.setValidated(true);
                    }

                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
