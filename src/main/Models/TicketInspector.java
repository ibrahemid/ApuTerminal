package main.Models;

public class TicketInspector extends Thread {
    Foyer foyer;
    public TicketInspector(Foyer foyer){
        this.foyer = foyer;
    }
    @Override
    public void run() {

        while (true){
            for (WaitingArea waitingArea : this.foyer.getWaitingAreas()){
                for (Customer customer : waitingArea.customers){
                    if (!customer.ticket.isValidated()){
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
