package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.IdGenerator;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

abstract public class Supplier extends Thread implements ISupplier {
    private final IWarehouse warehouse;
    private long delay = 100;
    private final IdGenerator idGenerator;

    public Supplier(IWarehouse warehouse, IdGenerator idGenerator) {
        this.warehouse = warehouse;
        this.idGenerator = idGenerator;
    }

    protected int getDetailId() {
        return idGenerator.getNewId();
    }

    protected abstract IProduct createDetail();

    public IWarehouse getWarehouse() {
        return warehouse;
    }

    protected void supplyProduct(IProduct detail) throws InterruptedException {
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
            while (!Thread.interrupted()) {
                Thread.sleep(delay);
                supplyProduct(createDetail());
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        System.err.println(Thread.currentThread().getName() + " has stopped");
    }
}
