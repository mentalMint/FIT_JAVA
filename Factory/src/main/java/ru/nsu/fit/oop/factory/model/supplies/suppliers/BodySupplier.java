package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.details.Body;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public class BodySupplier extends Supplier  {
    public BodySupplier(IWarehouse warehouse) {
        super(warehouse);
    }

    @Override
    protected IProduct createDetail() {
        Body body = new Body(getDetailId());
        incrementDetailId();
        return body;
    }
}
