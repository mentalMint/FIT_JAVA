package ru.nsu.fit.oop.factory.model.supplies;

import ru.nsu.fit.oop.factory.model.supplies.suppliers.Supplier;

import java.util.ArrayList;

public class SuppliersAssociation {
    public final ArrayList<Supplier> suppliers = new ArrayList<>();
    private long delay;

    public SuppliersAssociation() {
    }

    public void setDelay(long delay) {
        this.delay = delay;
        suppliers.forEach(supplier -> supplier.setDelay(delay));
    }

    public long getDelay() {
        return delay;
    }

    public void start() {
        suppliers.forEach(Thread::start);
    }

    public void stop() {
        suppliers.forEach(Thread::interrupt);
    }
}
