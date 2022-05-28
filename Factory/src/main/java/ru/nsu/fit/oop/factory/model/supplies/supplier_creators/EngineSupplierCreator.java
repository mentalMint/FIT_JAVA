package ru.nsu.fit.oop.factory.model.supplies.supplier_creators;

import ru.nsu.fit.oop.factory.model.supplies.suppliers.EngineSupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creation.ISupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creation.ISupplierCreator;

public class EngineSupplierCreator implements ISupplierCreator {
    private int warehouseSize = 100;

    @Override
    public ISupplier createSupplier() {
        return new EngineSupplier(warehouseSize);
    }

}
