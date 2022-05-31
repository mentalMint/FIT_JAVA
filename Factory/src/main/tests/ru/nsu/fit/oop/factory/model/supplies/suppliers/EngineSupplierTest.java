package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.factory.model.supplies.IdGenerator;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

import static org.junit.jupiter.api.Assertions.*;

class EngineSupplierTest {

    @Test
    void supplierBlocking() {
        EngineSupplier supplier = new EngineSupplier(new Warehouse(10), new IdGenerator());
        Thread thread = new Thread(supplier);

        thread.start();

        while(!thread.getState().equals(Thread.State.WAITING)) {

        }

        synchronized (supplier.getWarehouse()) {
            assertEquals(supplier.getWarehouse().getSize(), supplier.getWarehouse().getProductsNumber());
        }
    }
}