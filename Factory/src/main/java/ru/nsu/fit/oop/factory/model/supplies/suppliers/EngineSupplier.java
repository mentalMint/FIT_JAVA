package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.products.Engine;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creation.ISupplier;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.EngineWarehouse;

public class EngineSupplier implements ISupplier {
    private final IWarehouse warehouse;
    private int detailId = 0;
    private long delay = 100;

    public EngineSupplier(int warehouseSize) {
        warehouse = new EngineWarehouse(warehouseSize);
    }

    private IProduct createDetail() {
        return new Engine(detailId++);
    }

    @Override
    public IWarehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public void supplyProduct(IProduct detail) throws InterruptedException {
        synchronized (warehouse) {
            if (warehouse.isFull()) {
                warehouse.wait();
            } else if (warehouse.isEmpty()) {
                warehouse.notifyAll();
            }
            warehouse.putProduct(detail);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                supplyProduct(createDetail());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}