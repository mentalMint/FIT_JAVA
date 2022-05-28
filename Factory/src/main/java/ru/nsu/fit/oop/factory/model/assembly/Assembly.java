package ru.nsu.fit.oop.factory.model.assembly;

import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.Warehouse;
import ru.nsu.fit.oop.factory.threadpool.ThreadPool;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;

import java.util.ArrayList;

public class Assembly {
    private final ArrayList<IWarehouse> supplyWarehouses = new ArrayList<>();
    private final Warehouse finishedProductsWarehouse;
    private final ThreadPool assemblers;
    private final Runnable task;

     public Assembly(int warehouseSize, int assemblersNumber) {
        if (assemblersNumber <= 0) {
            throw new IllegalArgumentException("Not positive assemblersNumber");
        }
        assemblers = new ThreadPool(assemblersNumber);
        finishedProductsWarehouse = new Warehouse(warehouseSize);
         task = () -> {
             ArrayList<IProduct> finishedProductDetails = new ArrayList<>();
//             for (int i = 0; i < supplyWarehouses.size(); i++) {
             for (IWarehouse supplyWarehouse : supplyWarehouses) {
                 synchronized (supplyWarehouse) {
                     if (supplyWarehouse.isEmpty()) {
                         try {
                             supplyWarehouse.wait();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                     finishedProductDetails.add(supplyWarehouse.takeProduct());
                 }
             }

             synchronized (finishedProductsWarehouse) {
                 if (finishedProductsWarehouse.isFull()) {
                     try {
                         finishedProductsWarehouse.wait();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                 finishedProductsWarehouse.putProduct(new FinishedProduct(finishedProductDetails));
                 finishedProductsWarehouse.notify();
             }
         };
    }

    public IWarehouse getFinishedProductsWarehouse() {
        return finishedProductsWarehouse;
    }

    public void addWarehouse(IWarehouse warehouse) {
        supplyWarehouses.add(warehouse);
    }

    public void assemble() {
        assemblers.execute(task);
    }


}
