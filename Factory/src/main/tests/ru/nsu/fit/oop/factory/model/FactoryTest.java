package ru.nsu.fit.oop.factory.model;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.factory.model.assembly.Assembly;
import ru.nsu.fit.oop.factory.model.assembly.FinishedProductsWarehouseController;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.AccessorySupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.BodySupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.EngineSupplier;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

public class FactoryTest {
    @Test
    void carProducing() throws InterruptedException {
        Assembly assembly = new Assembly(5, 5);
        EngineSupplier supplier = new EngineSupplier(new Warehouse(10));
        BodySupplier supplier2 = new BodySupplier(new Warehouse(10));
        AccessorySupplier supplier3 = new AccessorySupplier(new Warehouse(10));
        assembly.setEngineSupplyWarehouse(supplier.getWarehouse());
        assembly.setBodySupplyWarehouse(supplier2.getWarehouse());
        assembly.setAccessorySupplyWarehouse(supplier3.getWarehouse());
        Sale sale = new Sale(5, assembly.getFinishedProductsWarehouse(), 100, true);
        FinishedProductsWarehouseController finishedProductsWarehouseController = new FinishedProductsWarehouseController(assembly);

        supplier.start();
        supplier2.start();
        supplier3.start();
        finishedProductsWarehouseController.start();
        sale.start();

        Thread.sleep(10000);
        sale.stop();
        assembly.stop();
        finishedProductsWarehouseController.interrupt();
        supplier.interrupt();
        supplier2.interrupt();
        supplier3.interrupt();
    }
}
