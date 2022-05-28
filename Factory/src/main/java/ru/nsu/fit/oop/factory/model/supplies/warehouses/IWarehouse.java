package ru.nsu.fit.oop.factory.model.supplies.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.products.IDetail;

public interface IWarehouse {
    void putProduct(IProduct product);

    int getSize();

    IProduct takeProduct();

    int getDetailsNumber();

    boolean isEmpty();

    boolean isFull();
}
