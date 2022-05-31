package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import ru.nsu.fit.oop.factory.model.supplies.IdGenerator;
import ru.nsu.fit.oop.factory.model.supplies.details.Engine;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;

public class EngineSupplier extends Supplier {
    public EngineSupplier(IWarehouse warehouse, IdGenerator idGenerator) {
        super(warehouse, idGenerator);
    }

    @Override
    protected IProduct createDetail() {
        Engine engine = new Engine(getDetailId());
        return engine;
    }
}