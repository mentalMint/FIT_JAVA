package ru.nsu.fit.oop.factory.model.supplies.supplier_associations;

import ru.nsu.fit.oop.factory.model.supplies.suppliers.Supplier;

import java.util.ArrayList;

public class SuppliersAssociation {
    public ArrayList<Supplier> suppliers = new ArrayList<>();

    public SuppliersAssociation() {
    }

    public void start() {
        suppliers.forEach(Thread::start);
    }

    public void stop() {
        suppliers.forEach(Thread::interrupt);
    }
}
