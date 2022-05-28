package ru.nsu.fit.oop.factory.model.supplies.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.products.IDetail;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class EngineWarehouse implements IWarehouse {
    private final ArrayList<IProduct> details = new ArrayList<>();
    private final int size;

    @Override
    public int getSize() {
        return size;
    }

    public EngineWarehouse(int size) {
        this.size = size;
    }

    @Override
    public synchronized void putProduct(IProduct detail) {
        details.add(detail);
    }

    @Override
    public synchronized IProduct takeProduct() {
        if (details.isEmpty()) {
            throw new NoSuchElementException("No details");
        }
        return details.remove(0);
    }

    public synchronized int getDetailsNumber() {
        return details.size();
    }

    public synchronized boolean isEmpty() {
        return details.isEmpty();
    }

    @Override
    public boolean isFull() {
        return details.size() == size;
    }
}