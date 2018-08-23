package main.Helpers;

import main.Models.Customer;
import main.Models.Foyer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomersGenerationThread extends Thread{
    private Foyer foyer;
    public CustomersGenerationThread(Foyer foyer){
        this.foyer = foyer;
    }
    @Override
    public void run() {
        int counter = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(150);
        while (true){
            executorService.submit(new Customer(counter++,foyer));
            try {
                Thread.sleep((new Random().nextInt(4) + 1) * 1000); //customer generation rate in millisecond[? 1-2-3-4 ? sec]
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
