package ru.nsu.fit.oop.factory.model.supplies.suppliers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineSupplierTest {

    @Test
    void supplierBlockingTest() {
        EngineSupplier supplier = new EngineSupplier(10);
        Thread thread = new Thread(supplier);

        thread.start();

        while(!thread.getState().equals(Thread.State.WAITING)) {

        }

        synchronized (supplier.getWarehouse()) {
            assertEquals(supplier.getWarehouse().getSize(), supplier.getWarehouse().getDetailsNumber());
        }
    }
}