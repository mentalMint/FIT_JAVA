package ru.nsu.fit.oop.factory.model.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.observer.Observable;

import java.util.ArrayList;

public class Warehouse extends Observable implements IWarehouse {
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
            notifyAll();
        }
        products.add(product);
        notifyObservers();
    }

    @Override
    public synchronized IProduct takeProduct() throws InterruptedException {
        while (isEmpty()) {
//                System.err.println(Thread.currentThread().getName() + ": wait to take");
            wait();
        }
//        System.err.println(Thread.currentThread().getName() + ": take product");
        if (isFull()) {
            notifyAll();
        }

        IProduct product;
        synchronized (products) {
            product = products.remove(0);
        }
        notifyObservers();
        return product;
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
