package ru.nsu.fit.oop.factory.model;

import javafx.fxml.LoadException;
import ru.nsu.fit.oop.factory.model.assembly.Assembly;
import ru.nsu.fit.oop.factory.model.sale.Sale;
import ru.nsu.fit.oop.factory.model.supply_abstract_factory.ISupplier;
import ru.nsu.fit.oop.factory.model.supply_abstract_factory.ISupplyFactory;
import ru.nsu.fit.oop.factory.model.supply_abstract_factory.IWarehouse;
import ru.nsu.fit.oop.factory.model.warehouses.FinishedProductsWarehouse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;

public class Model {
    private final ArrayList<ISupplyFactory> supplyFactories = new ArrayList<>();
    private final ArrayList<ISupplier> suppliers = new ArrayList<>();
    private final ArrayList<IWarehouse> suppliesWarehouses = new ArrayList<>();
    private final Assembly assembly;
    private Properties properties = new Properties();
    private final IWarehouse finishedProductsWarehouse;
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
        assembly = new Assembly(Integer.parseInt(properties.getProperty("Assemblers number")));
        registerSupplyFactories();
        arrangeSupplies();
        finishedProductsWarehouse = new FinishedProductsWarehouse();
        sale = new Sale(Integer.parseInt(properties.getProperty("Dealers number")));
        finishedProductsWarehouseController = new FinishedProductsWarehouseController();
    }

    private void registerSupplyFactories() throws LoadException, ClassNotFoundException {
        InputStream supplyFactoriesConfig = Assembly.class.getResourceAsStream("supply_factories.txt");
        if (supplyFactoriesConfig == null) {
            throw new NullPointerException("supply_factories.txt is not found");
        }
        Properties supplyFactoriesClassNames = new Properties();
        try {
            supplyFactoriesClassNames.load(supplyFactoriesConfig);
        } catch (IOException e) {
            throw new LoadException("Failed to load config");
        }

        for (Object supplyFactoryName : supplyFactoriesClassNames.values()) {
            try {
                ISupplyFactory supplyFactory = (ISupplyFactory) Class.forName((String) supplyFactoryName).getConstructor().newInstance();
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
        for (ISupplyFactory supplyFactory : supplyFactories) {
            ISupplier supplier = supplyFactory.createSupplier();
            IWarehouse warehouse = supplyFactory.createWarehouse();
            suppliers.add(supplier);
            suppliesWarehouses.add(warehouse);
            supplier.setWarehouse(warehouse);
        }
    }

    public void start() throws LoadException, ClassNotFoundException {

    }
}
