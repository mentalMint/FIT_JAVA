package ru.nsu.fit.oop.factory.model.assembly;

import ru.nsu.fit.oop.factory.threadpool.ThreadPool;
import ru.nsu.fit.oop.factory.model.supply_abstract_factory.IWarehouse;

import java.util.ArrayList;

public class Assembly {
    private final ArrayList<IWarehouse> warehouses = new ArrayList<>();
    private ThreadPool assemblers;

    public Assembly(int assemblersNumber) {
        assemblers = new ThreadPool(assemblersNumber);
    }

    void setWarehouses() {

    }


}
