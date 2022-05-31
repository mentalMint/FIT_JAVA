package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public interface ISupplier {
    IWarehouse getWarehouse();

//    void supplyProduct(IProduct product) throws InterruptedException;
}
