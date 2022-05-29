package ru.nsu.fit.oop.factory.model.assembly;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.supplies.details.Engine;
import ru.nsu.fit.oop.factory.model.warehouses.FinishedProductsWarehouse;

class FinishedProductsWarehouseTest {

    @Test
    void takeProduct() throws InterruptedException {
        FinishedProductsWarehouse warehouse = new FinishedProductsWarehouse(5);
        Sale sale = new Sale(5, warehouse, 100, true);
        for (int i = 0; i < 5; i++) {
            warehouse.putProduct(new Auto(i));
        }

        sale.start();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(50);
            System.err.println(warehouse.getProductsNumber());
        }
    }
}