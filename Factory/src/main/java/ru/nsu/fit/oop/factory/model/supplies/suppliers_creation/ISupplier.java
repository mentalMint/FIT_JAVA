package ru.nsu.fit.oop.factory.model.supplies.suppliers_creation;

import ru.nsu.fit.oop.factory.model.supplies.products.IDetail;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;

public interface ISupplier extends Runnable {
    IWarehouse getWarehouse();

    void supplyProduct(IProduct product) throws InterruptedException;
}
