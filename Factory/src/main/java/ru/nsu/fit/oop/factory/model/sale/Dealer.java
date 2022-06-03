package ru.nsu.fit.oop.factory.model.sale;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import ru.nsu.fit.oop.factory.model.assembly.Auto;
import ru.nsu.fit.oop.factory.model.warehouses.FinishedProductsWarehouse;

public class Dealer extends Thread {
    private static final Logger logger = Logger.getLogger(Dealer.class.getName());
    private final FinishedProductsWarehouse finishedProductWarehouse;
    private boolean loggingEnabled;
    private long delay;

    public Dealer(long delay, boolean loggingEnabled, FinishedProductsWarehouse finishedProductWarehouse) {
        super();
        this.finishedProductWarehouse = finishedProductWarehouse;
        this.delay = delay;
        this.loggingEnabled = loggingEnabled;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (!Thread.interrupted()) {
                Thread.sleep(delay);

//            System.err.println(Thread.currentThread().getName() + ": wait to come in");
                synchronized (finishedProductWarehouse) {
//                System.err.println(Thread.currentThread().getName( + ": in");
                    Auto auto = (Auto) finishedProductWarehouse.takeProduct();
                    if (loggingEnabled) {
                        logger.info(
                                LocalDateTime.now() + " "
                                        + Thread.currentThread().getName() + ": Auto "
                                        + auto.getId() + " (Body: " + auto.getBody().getId()
                                        + " Engine: " + auto.getEngine().getId()
                                        + " Accessory: " + auto.getAccessory().getId() + ")");
                    }
//                System.err.println(Thread.currentThread().getName() + ": cars in warehouse: " + finishedProductWarehouse.getProductsNumber());

                }
//            System.err.println(Thread.currentThread().getName() + ": out");
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        System.err.println(Thread.currentThread().getName() + " has stopped");
    }
}
