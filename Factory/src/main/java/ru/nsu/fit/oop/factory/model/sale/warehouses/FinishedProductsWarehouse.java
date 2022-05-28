package ru.nsu.fit.oop.factory.model.sale.warehouses;

import ru.nsu.fit.oop.factory.model.supplies.products.IDetail;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;

public class FinishedProductsWarehouse implements IWarehouse {
    @Override
    public void putProduct(IProduct detail) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public IDetail takeProduct() {
        return null;
    }

    @Override
    public int getDetailsNumber() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFull() {
        return false;
    }
}
