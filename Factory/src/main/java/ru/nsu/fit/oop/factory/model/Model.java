package ru.nsu.fit.oop.factory.model;

import javafx.fxml.LoadException;
import ru.nsu.fit.oop.factory.model.assembly.Assembly;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creation.ISupplier;
import ru.nsu.fit.oop.factory.model.supplies.suppliers_creation.ISupplierCreator;
import ru.nsu.fit.oop.factory.model.supplies.warehouses.IWarehouse;
import ru.nsu.fit.oop.factory.model.sale.warehouses.FinishedProductsWarehouse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;

public class Model {
    private final ArrayList<ISupplierCreator> supplyFactories = new ArrayList<>();
    private final ArrayList<ISupplier> suppliers = new ArrayList<>();
    private final ArrayList<IWarehouse> suppliesWarehouses = new ArrayList<>();
    private final Assembly assembly;
    private Properties properties = new Properties();
    private final Sale sale;
    private final FinishedProductsWarehouseController finishedProductsWarehouseController;

    private void setProperties(String configName) throws LoadException {
        InputStream config = Assembly.class.getResourceAsStream(configName);
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

    public Model() throws LoadException, ClassNotFoundException {
        setProperties("config.txt");
        assembly = new Assembly(5, Integer.parseInt(properties.getProperty("Assemblers number")));
        registerSupplyFactories();
        arrangeSupplies();
        sale = new Sale(Integer.parseInt(properties.getProperty("Dealers number")));
        finishedProductsWarehouseController = new FinishedProductsWarehouseController();
    }

    private void registerSupplyFactories() throws LoadException, ClassNotFoundException {
        InputStream supplyFactoriesConfig = Assembly.class.getResourceAsStream("supplier_creators.txt");
        if (supplyFactoriesConfig == null) {
            throw new NullPointerException("supplier_creators.txt is not found");
        }
        Properties supplyFactoriesClassNames = new Properties();
        try {
            supplyFactoriesClassNames.load(supplyFactoriesConfig);
        } catch (IOException e) {
            throw new LoadException("Failed to load config");
        }

        for (Object supplyFactoryName : supplyFactoriesClassNames.values()) {
            try {
                ISupplierCreator supplyFactory = (ISupplierCreator) Class.forName((String) supplyFactoryName).getConstructor().newInstance();
                this.supplyFactories.add(supplyFactory);
            } catch (ClassNotFoundException e) {
                System.err.println("Error while searching shape class: " + e.getLocalizedMessage() + " This shape will be skipped");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        supplyFactories.remove(null);

        if (this.supplyFactories.isEmpty()) {
            throw new ClassNotFoundException("No shape classes from config.txt found");
        }
    }

    private void arrangeSupplies() {
        for (ISupplierCreator supplyFactory : supplyFactories) {
            ISupplier supplier = supplyFactory.createSupplier();
//            IWarehouse warehouse = supplyFactory.createWarehouse();
            suppliers.add(supplier);
            suppliesWarehouses.add(supplier.getWarehouse());
        }
    }

    public void start() throws LoadException, ClassNotFoundException {

    }
}
