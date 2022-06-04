package ru.nsu.fit.oop.factory.model.assembly;

import ru.nsu.fit.oop.factory.model.warehouses.FinishedProductsWarehouse;
import ru.nsu.fit.oop.factory.observer.Observable;
import ru.nsu.fit.oop.factory.threadpool.ThreadPool;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public class Assembly extends Observable {
    private IWarehouse accessorySupplyWarehouse;
    private IWarehouse bodySupplyWarehouse;
    private IWarehouse engineSupplyWarehouse;
    private final FinishedProductsWarehouse finishedProductsWarehouse;
    private final ThreadPool assemblers;
    private final Runnable task;
    private final Object mutex = new Object();
    private int tasksNumber = 0;
    private int productId = 0;

    public int getProductId() {
        return productId;
    }

    public Assembly(int warehouseSize, int assemblersNumber) {
        if (assemblersNumber <= 0) {
            throw new IllegalArgumentException("Not positive assemblersNumber");
        }
        assemblers = new ThreadPool(assemblersNumber);
        finishedProductsWarehouse = new FinishedProductsWarehouse(warehouseSize);
        task = () -> {
            Auto auto = new Auto(productId++);
            try {
                auto.setBody(bodySupplyWarehouse.takeProduct());
                auto.setEngine(engineSupplyWarehouse.takeProduct());
                auto.setAccessory(accessorySupplyWarehouse.takeProduct());
//            System.err.println(Thread.currentThread().getName() + ": want to put car");
                notifyObservers();
                synchronized (finishedProductsWarehouse) {
                    finishedProductsWarehouse.putProduct(auto);
//                System.err.println(Thread.currentThread().getName() + ": cars in warehouse: " + finishedProductsWarehouse.getProductsNumber());
                }

                synchronized (mutex) {
                    tasksNumber--;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                e.printStackTrace();
            }
        };
    }

    public FinishedProductsWarehouse getFinishedProductsWarehouse() {
        return finishedProductsWarehouse;
    }

    public IWarehouse getAccessorySupplyWarehouse() {
        return accessorySupplyWarehouse;
    }

    public IWarehouse getBodySupplyWarehouse() {
        return bodySupplyWarehouse;
    }

    public IWarehouse getEngineSupplyWarehouse() {
        return engineSupplyWarehouse;
    }

    public void setAccessorySupplyWarehouse(IWarehouse accessorySupplyWarehouse) {
        this.accessorySupplyWarehouse = accessorySupplyWarehouse;
    }

    public void setBodySupplyWarehouse(IWarehouse bodySupplyWarehouse) {
        this.bodySupplyWarehouse = bodySupplyWarehouse;
    }

    public void setEngineSupplyWarehouse(IWarehouse engineSupplyWarehouse) {
        this.engineSupplyWarehouse = engineSupplyWarehouse;
    }

    public void assemble() {
        assemblers.execute(task);
        synchronized (mutex) {
            tasksNumber++;
        }
    }

    public int getProductsAssemblingNumber() {
        synchronized (mutex) {
            return tasksNumber;
        }
    }

    public void stop() {
        assemblers.shutdown();
    }
}
