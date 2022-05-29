package ru.nsu.fit.oop.factory.model.assembly;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.factory.model.supplies.details.Accessory;
import ru.nsu.fit.oop.factory.model.supplies.details.Body;
import ru.nsu.fit.oop.factory.model.supplies.details.Engine;
import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyTest {

    @Test
    void assemble() throws InterruptedException {
        Assembly assembly = new Assembly(5, 2);
        IWarehouse warehouse = new Warehouse(5);
        IWarehouse warehouse2 = new Warehouse(5);
        IWarehouse warehouse3 = new Warehouse(5);

        IProduct engine = new Engine(0);
        warehouse.putProduct(engine);
        IProduct body = new Body(0);
        warehouse2.putProduct(body);
        IProduct accessory = new Accessory(0);
        warehouse3.putProduct(accessory);

        assembly.setEngineSupplyWarehouse(warehouse);
        assembly.setAccessorySupplyWarehouse(warehouse2);
        assembly.setBodySupplyWarehouse(warehouse3);

        assembly.assemble();

        synchronized (assembly.getFinishedProductsWarehouse()) {
            while (assembly.getFinishedProductsWarehouse().isEmpty()) {
                assembly.getFinishedProductsWarehouse().wait();
            }
            assertEquals(engine, ((Auto) assembly.getFinishedProductsWarehouse().takeProduct()).getEngine());
        }
    }
}