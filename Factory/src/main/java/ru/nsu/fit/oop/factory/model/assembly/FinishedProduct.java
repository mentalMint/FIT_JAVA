package ru.nsu.fit.oop.factory.model.assembly;

import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;

import java.util.List;

public class FinishedProduct implements IFinishedProduct {
    private final List<IProduct> details;

    public FinishedProduct(List<IProduct> details) {
        this.details = details;
    }


    @Override
    public List<IProduct> getDetails() {
        return details;
    }
}
