package ru.nsu.fit.oop.factory.model.supply_abstract_factory;

public interface ISupplyFactory {
    ISupplier createSupplier();

    IWarehouse createWarehouse();
}
