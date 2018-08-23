package main;


import main.Helpers.CustomersGenerationThread;
import main.Models.Foyer;
import main.Models.Terminal;
import main.Models.TicketInspector;

public class Main {


    public static void main(String[] args) {

        /**
         * TEST
         * */
        Foyer foyer = new Foyer(150);//foyer size
        CustomersGenerationThread customersGenerationThread = new CustomersGenerationThread(foyer);
        customersGenerationThread.start();

        TicketInspector ticketInspector = new TicketInspector(foyer);
        ticketInspector.start();

        Terminal terminal = new Terminal(foyer, 3);
        terminal.start();
    }
}
