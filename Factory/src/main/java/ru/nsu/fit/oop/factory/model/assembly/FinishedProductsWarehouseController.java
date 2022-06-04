package ru.nsu.fit.oop.factory.model.assembly;

public class FinishedProductsWarehouseController extends Thread {
    private final Assembly assembly;

    public FinishedProductsWarehouseController(Assembly assembly) {
        this.assembly = assembly;
    }

    @Override
    public void run() {
        super.run();
        synchronized (assembly.getFinishedProductsWarehouse().getMutex()) {
            try {
                while (!Thread.interrupted()) {
                    while (assembly.getFinishedProductsWarehouse().getProductsNumber() + assembly.getProductsAssemblingNumber()
                            > assembly.getFinishedProductsWarehouse().getSize() / 2) {
                        assembly.getFinishedProductsWarehouse().getMutex().wait();
                    }
                    for (int i = 0; i < (assembly.getFinishedProductsWarehouse().getSize() + 1) / 2; i++) {
                        assembly.assemble();
                    }
                }
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " has stopped");
            }
        }
    }
}
