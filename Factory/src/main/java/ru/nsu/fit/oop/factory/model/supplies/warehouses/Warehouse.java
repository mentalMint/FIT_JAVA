package ru.nsu.fit.oop.factory.model.supplies.warehouses;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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
    public synchronized void putProduct(IProduct product) {
        products.add(product);
    }

    @Override
    public synchronized IProduct takeProduct() {
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products");
        }
        return products.remove(0);
    }

    public synchronized int getDetailsNumber() {
        return products.size();
    }

    public synchronized boolean isEmpty() {
        return products.isEmpty();
    }

    @Override
    public boolean isFull() {
        return products.size() == size;
    }
}
