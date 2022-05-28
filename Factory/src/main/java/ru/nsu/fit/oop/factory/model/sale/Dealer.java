package ru.nsu.fit.oop.factory.model.sale;

import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;

public class Dealer extends Thread {
    private final IWarehouse finishedProductWarehouse;

    public Dealer(IWarehouse finishedProductWarehouse) {
        super();
        this.finishedProductWarehouse = finishedProductWarehouse;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (finishedProductWarehouse) {
                if (finishedProductWarehouse.isEmpty()) {
                    try {
                        finishedProductWarehouse.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
