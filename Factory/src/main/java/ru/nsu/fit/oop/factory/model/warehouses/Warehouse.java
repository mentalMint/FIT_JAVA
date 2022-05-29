package ru.nsu.fit.oop.factory.model.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;

import java.util.ArrayList;

public class Warehouse implements IWarehouse {
    private final ArrayList<IProduct> products = new ArrayList<>();
    private final int size;

    @Override
    public int getSize() {
        return size;
    }

    public Warehouse(int size) {
        this.size = size;
    }

    @Override
    public synchronized void putProduct(IProduct product) throws InterruptedException {
        while (isFull()) {
//                System.err.println(Thread.currentThread().getName() + ": wait to put");
            wait();
        }
//        System.err.println(Thread.currentThread().getName() + ": put product");

        if (isEmpty()) {
            notify();
        }
        products.add(product);
    }

    @Override
    public synchronized IProduct takeProduct() throws InterruptedException {
        while (isEmpty()) {
//                System.err.println(Thread.currentThread().getName() + ": wait to take");
            wait();
        }
//        System.err.println(Thread.currentThread().getName() + ": take product");
        if (isFull()) {
            notify();
        }

        synchronized (products) {
            return products.remove(0);
        }
    }

    public int getProductsNumber() {
        synchronized (products) {
            return products.size();
        }
    }

    public boolean isEmpty() {
        synchronized (products) {
            return products.isEmpty();
        }
    }

    @Override
    public boolean isFull() {
        synchronized (products) {
            return products.size() == size;
        }
    }
}
