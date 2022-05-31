package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.IdGenerator;
import ru.nsu.fit.oop.factory.model.supplies.details.Accessory;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public class AccessorySupplier extends Supplier {
    public AccessorySupplier(IWarehouse warehouse, IdGenerator idGenerator) {
        super(warehouse, idGenerator);
    }

    @Override
    protected IProduct createDetail() {
        return new Accessory(getDetailId());
    }
}
