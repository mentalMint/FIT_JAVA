package ru.nsu.fit.oop.factory.model.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;

public interface IWarehouse {
    void putProduct(IProduct product) throws InterruptedException;

    int getSize();

    IProduct takeProduct() throws InterruptedException;

    int getProductsNumber();

    boolean isEmpty();

    boolean isFull();
}
