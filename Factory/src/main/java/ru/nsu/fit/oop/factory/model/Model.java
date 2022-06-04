package ru.nsu.fit.oop.factory.model;

import javafx.fxml.LoadException;
import ru.nsu.fit.oop.factory.model.assembly.Assembly;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.assembly.FinishedProductsWarehouseController;
import ru.nsu.fit.oop.factory.model.supplies.IdGenerator;
import ru.nsu.fit.oop.factory.model.supplies.SuppliersAssociation;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.AccessorySupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.BodySupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.EngineSupplier;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Flow;

public class Model {
    private final ArrayList<SuppliersAssociation> suppliersAssociations = new ArrayList<>();
    private final Assembly assembly;
    private Properties properties = new Properties();
    private final Sale sale;
    private final FinishedProductsWarehouseController finishedProductsWarehouseController;
    private Warehouse accessoryWarehouse;
    private Warehouse bodyWarehouse;
    private Warehouse engineWarehouse;
    private final IdGenerator accessoryIdGenerator = new IdGenerator();
    private final IdGenerator bodyIdGenerator = new IdGenerator();
    private final IdGenerator engineIdGenerator = new IdGenerator();

    public void subscribe(Flow.Subscriber<Boolean> subscriber) {
        accessoryIdGenerator.subscribe(subscriber);
        bodyIdGenerator.subscribe(subscriber);
        engineIdGenerator.subscribe(subscriber);
        accessoryWarehouse.subscribe(subscriber);
        bodyWarehouse.subscribe(subscriber);
        engineWarehouse.subscribe(subscriber);
        assembly.subscribe(subscriber);
        sale.subscribe(subscriber);
    }

    public void setBodySupplierDelay(long delay) {
        suppliersAssociations.get(1).setDelay(delay);
    }

    public void setEngineSupplierDelay(long delay) {
        suppliersAssociations.get(2).setDelay(delay);
    }

    public void setAccessorySupplierDelay(long delay) {
        suppliersAssociations.get(0).setDelay(delay);
    }

    public void setDealerDelay(long delay) {
        sale.setDelay(delay);
    }

    public int getAutosNumber() {
        return assembly.getProductId();
    }

    public int getAccessoryId() {
        return accessoryIdGenerator.getCurrentId();
    }

    public int getBodyId() {
        return bodyIdGenerator.getCurrentId();
    }

    public int getEngineId() {
        return engineIdGenerator.getCurrentId();
    }

    public int getAccessoryWarehouseWorkload() {
        return accessoryWarehouse.getProductsNumber();
    }

    public int getBodyWarehouseWorkload() {
        return bodyWarehouse.getProductsNumber();
    }

    public int getEngineWarehouseWorkload() {
        return engineWarehouse.getProductsNumber();
    }

    public int getAutoWarehouseWorkload() {
        return assembly.getFinishedProductsWarehouse().getProductsNumber();
    }

    private void setProperties(String configName) throws LoadException {
        InputStream config = Model.class.getResourceAsStream(configName);
        if (config == null) {
            throw new NullPointerException(configName);
        }
        properties = new Properties();
        try {
            properties.load(config);
        } catch (IOException e) {
            throw new LoadException("Failed to load config");
        }
    }

    public Model() throws LoadException {
        setProperties("config.txt");
        assembly = new Assembly(Integer.parseInt(properties.getProperty("AutoWarehouseSize")), Integer.parseInt(properties.getProperty("Assemblers")));
        arrangeSupplies();
        assembly.setAccessorySupplyWarehouse(accessoryWarehouse);
        assembly.setBodySupplyWarehouse(bodyWarehouse);
        assembly.setEngineSupplyWarehouse(engineWarehouse);
        sale = new Sale(Integer.parseInt(properties.getProperty("Dealers")), assembly.getFinishedProductsWarehouse(),
                5000, Boolean.parseBoolean(properties.getProperty("LogSale")));
        finishedProductsWarehouseController = new FinishedProductsWarehouseController(assembly);
        finishedProductsWarehouseController.setName("Controller");
    }

    private void arrangeSupplies() {
        accessoryWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("AccessoryWarehouseSize")));
        SuppliersAssociation accessorySuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("AccessorySuppliers")); i++) {
            AccessorySupplier supplier = new AccessorySupplier(accessoryWarehouse, accessoryIdGenerator);
            supplier.setName("AccessorySupplier " + i);
            accessorySuppliersAssociation.suppliers.add(supplier);
        }
        suppliersAssociations.add(accessorySuppliersAssociation);

        bodyWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("BodyWarehouseSize")));
        SuppliersAssociation bodySuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("BodySuppliers")); i++) {
            BodySupplier supplier = new BodySupplier(bodyWarehouse, bodyIdGenerator);
            supplier.setName("BodySupplier " + i);
            bodySuppliersAssociation.suppliers.add(supplier);
        }
        suppliersAssociations.add(bodySuppliersAssociation);

        engineWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("EngineWarehouseSize")));
        SuppliersAssociation engineSuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("EngineSuppliers")); i++) {
            EngineSupplier supplier = new EngineSupplier(engineWarehouse, engineIdGenerator);
            supplier.setName("EngineSupplier " + i);
            engineSuppliersAssociation.suppliers.add(supplier);
        }
        suppliersAssociations.add(engineSuppliersAssociation);
    }

    public void start() {
        suppliersAssociations.forEach(SuppliersAssociation::start);
        finishedProductsWarehouseController.start();
        sale.start();
    }

    public void stop() {
        sale.stop();
        assembly.stop();
        finishedProductsWarehouseController.interrupt();
        suppliersAssociations.forEach(SuppliersAssociation::stop);
    }
}
