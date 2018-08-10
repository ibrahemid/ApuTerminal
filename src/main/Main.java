package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Helpers.CustomersGenerationThread;
import main.Models.Foyer;
import main.Models.Terminal;
import main.Models.TicketInspector;

public class Main  {

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello !");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }


    public static void main(String[] args) {
        Foyer foyer = new Foyer(150);
        CustomersGenerationThread customersGenerationThread = new CustomersGenerationThread(foyer);
        customersGenerationThread.start();

        TicketInspector ticketInspector = new TicketInspector(foyer);
        ticketInspector.start();

        Terminal terminal = new Terminal(foyer,3);
        terminal.start();
    }
}
