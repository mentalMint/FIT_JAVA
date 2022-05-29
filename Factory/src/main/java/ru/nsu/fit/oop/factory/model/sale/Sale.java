package ru.nsu.fit.oop.factory.model.sale;

import ru.nsu.fit.oop.factory.model.warehouses.FinishedProductsWarehouse;

import java.util.ArrayList;

public class Sale {
    private final ArrayList<Dealer> dealers = new ArrayList<>();
    private final FinishedProductsWarehouse warehouse;
    private long delay = 100;
    private boolean loggingEnabled = false;

    public Sale(int dealersNumber, FinishedProductsWarehouse warehouse, long delay, boolean loggingEnabled) {
        this.delay = delay;
        this.loggingEnabled = loggingEnabled;
        this.warehouse = warehouse;
        for (int i = 0; i < dealersNumber; i++) {
            Dealer dealer = new Dealer(delay, loggingEnabled, warehouse);
            dealer.setName("Dealer " + i);
            dealers.add(dealer);
        }
    }

    public void start() {
        dealers.forEach(Thread::start);
    }

    public void stop() {
        dealers.forEach(Thread::interrupt);
    }
}
