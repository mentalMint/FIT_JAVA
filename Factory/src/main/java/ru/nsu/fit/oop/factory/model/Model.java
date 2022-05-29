package ru.nsu.fit.oop.factory.model;

import javafx.fxml.LoadException;
import ru.nsu.fit.oop.factory.model.assembly.Assembly;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.assembly.FinishedProductsWarehouseController;
import ru.nsu.fit.oop.factory.model.supplies.supplier_associations.SuppliersAssociation;
import ru.nsu.fit.oop.factory.model.supplies.suppliers.AccessorySupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creators.ISupplierCreator;
import ru.nsu.fit.oop.factory.model.warehouses.IWarehouse;
import ru.nsu.fit.oop.factory.model.warehouses.Warehouse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Model {
    private final ArrayList<ISupplierCreator> supplyFactories = new ArrayList<>();
    private final ArrayList<SuppliersAssociation> suppliersAssociations = new ArrayList<>();
    private final ArrayList<IWarehouse> suppliesWarehouses = new ArrayList<>();
    private final Assembly assembly;
    private Properties properties = new Properties();
    private final Sale sale;
    private final FinishedProductsWarehouseController finishedProductsWarehouseController;

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
        sale = new Sale(Integer.parseInt(properties.getProperty("Dealers")), assembly.getFinishedProductsWarehouse(),
                100, Boolean.parseBoolean(properties.getProperty("LogSale")));
        finishedProductsWarehouseController = new FinishedProductsWarehouseController(assembly);
    }

//    private void registerSupplyCreators() throws LoadException, ClassNotFoundException {
//        InputStream supplyCreatorsConfig = Assembly.class.getResourceAsStream("supplier_creators.txt");
//        if (supplyCreatorsConfig == null) {
//            throw new NullPointerException("supplier_creators.txt is not found");
//        }
//        Properties supplierCreatorsClassNames = new Properties();
//        try {
//            supplierCreatorsClassNames.load(supplyCreatorsConfig);
//        } catch (IOException e) {
//            throw new LoadException("Failed to load config");
//        }
//
//        for (Object supplierCreatorClassName : supplierCreatorsClassNames.values()) {
//            try {
//                ISupplierCreator supplierCreator = (ISupplierCreator) Class.forName((String) supplierCreatorClassName).getConstructor().newInstance();
//                this.supplyFactories.add(supplierCreator);
//            } catch (ClassNotFoundException e) {
//                System.err.println("Error while searching shape class: " + e.getLocalizedMessage() + " This shape will be skipped");
//            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
//        supplyFactories.remove(null);
//
//        if (this.supplyFactories.isEmpty()) {
//            throw new ClassNotFoundException("No shape classes from config.txt found");
//        }
//    }

    private void arrangeSupplies() {
        IWarehouse accessoryWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("AccessoryWarehouseSize")));
        SuppliersAssociation accessorySuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("AccessorySuppliers")); i++) {
            accessorySuppliersAssociation.suppliers.add(new AccessorySupplier(accessoryWarehouse));
        }
        suppliersAssociations.add(accessorySuppliersAssociation);
        suppliesWarehouses.add(accessoryWarehouse);

        IWarehouse bodyWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("BodyWarehouseSize")));
        SuppliersAssociation bodySuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("BodySuppliers")); i++) {
            bodySuppliersAssociation.suppliers.add(new AccessorySupplier(bodyWarehouse));
        }
        suppliersAssociations.add(bodySuppliersAssociation);
        suppliesWarehouses.add(bodyWarehouse);

        IWarehouse engineWarehouse = new Warehouse(Integer.parseInt(properties.getProperty("EngineWarehouseSize")));
        SuppliersAssociation engineSuppliersAssociation = new SuppliersAssociation();
        for (int i = 0; i < Integer.parseInt(properties.getProperty("EngineSuppliers")); i++) {
            engineSuppliersAssociation.suppliers.add(new AccessorySupplier(engineWarehouse));
        }
        suppliersAssociations.add(engineSuppliersAssociation);
        suppliesWarehouses.add(engineWarehouse);
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
