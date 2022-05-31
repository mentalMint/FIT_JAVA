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
//                System.err.println("Controller: new iteration. Products sum: " +
//                        (assembly.getFinishedProductsWarehouse().getProductsNumber() + assembly.getProductsAssemblingNumber()));
                    while (assembly.getFinishedProductsWarehouse().getProductsNumber() + assembly.getProductsAssemblingNumber()
                            > assembly.getFinishedProductsWarehouse().getSize() / 2) {
//                    System.err.println("Controller: need wait");
                        assembly.getFinishedProductsWarehouse().getMutex().wait();
                    }
//                    System.err.println("Controller: is notified");
                    for (int i = 0; i < (assembly.getFinishedProductsWarehouse().getSize() + 1) / 2; i++) {
                        assembly.assemble();
                    }
//                    System.err.println("Controller: delegated tasks");
                }
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " has stopped");

//                e.printStackTrace();
            }
        }
    }
}
