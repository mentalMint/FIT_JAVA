package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.details.Accessory;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public class AccessorySupplier extends Supplier {
    public AccessorySupplier(IWarehouse warehouse) {
        super(warehouse);
    }

    @Override
    protected IProduct createDetail() {
        Accessory accessory = new Accessory(getDetailId());
        incrementDetailId();
        return accessory;
    }
}
