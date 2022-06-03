package ru.nsu.fit.oop.factory.model.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;

public class FinishedProductsWarehouse extends Warehouse {
    private final Object mutex = new Object();

    public Object getMutex() {
        return mutex;
    }

    public FinishedProductsWarehouse(int size) {
        super(size);
    }

    @Override
    public IProduct takeProduct() throws InterruptedException {
//        System.err.println(Thread.currentThread().getName() + ": wait to notify");
        synchronized (mutex) {
//            System.err.println(Thread.currentThread().getName() + ": notify controller");
            mutex.notify();
        }
        return super.takeProduct();
    }
}
