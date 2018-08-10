package main.Models;

public class TicketScanner {
    public Boolean checkTicket(WaitingArea waitingArea,Customer customer){
        System.out.println("Customer [" + customer.ID + "] is scanning the ticket with scanner in waiting area ["+waitingArea.ID+"]..");
        boolean result = waitingArea.ID == customer.ticket.waitingAreaNumber;
        if (result){
            System.out.println("Customer [" + customer.ID + "] ticket's ["+customer.ticket+"] is valid proceed to room ["+waitingArea.ID+"]..");
        }
        return result;
    }
}
