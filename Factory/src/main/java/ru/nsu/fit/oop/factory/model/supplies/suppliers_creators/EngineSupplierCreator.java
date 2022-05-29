package ru.nsu.fit.oop.factory.model.supplies.suppliers_creators;

import ru.nsu.fit.oop.factory.model.supplies.suppliers.EngineSupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.ISupplier;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

public class EngineSupplierCreator implements ISupplierCreator {
    private IWarehouse warehouse;

    public EngineSupplierCreator(int warehouseSize) {
        warehouse = new Warehouse(warehouseSize);
    }

    @Override
    public ISupplier createSupplier() {
        return new EngineSupplier(warehouse);
    }

}
