package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.details.Engine;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

abstract public class Supplier extends Thread implements ISupplier {
    private final IWarehouse warehouse;
    private int detailId = 0;
    private long delay = 100;

    public Supplier(IWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    protected int getDetailId() {
        return detailId;
    }

    protected void incrementDetailId() {
        detailId++;
    }

    protected abstract IProduct createDetail();

    @Override
    public IWarehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public void supplyProduct(IProduct detail) throws InterruptedException {
        warehouse.putProduct(detail);
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(delay);
                supplyProduct(createDetail());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
