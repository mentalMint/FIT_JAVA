package ru.nsu.fit.oop.factory.model.assembly;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.factory.model.supplies.products.Engine;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.EngineWarehouse;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyTest {

    @Test
    void assemble() throws InterruptedException {
        Assembly assembly = new Assembly(5, 2);
        IWarehouse warehouse = new EngineWarehouse(5);
        IProduct engine = new Engine(0);
        warehouse.putProduct(engine);
        assembly.addWarehouse(warehouse);

        assembly.assemble();

        synchronized (assembly.getFinishedProductsWarehouse()) {
            if (assembly.getFinishedProductsWarehouse().isEmpty()) {
                assembly.getFinishedProductsWarehouse().wait();
            }
            assertEquals(engine, assembly.getFinishedProductsWarehouse().takeProduct().getDetails().get(0));
        }
    }
}