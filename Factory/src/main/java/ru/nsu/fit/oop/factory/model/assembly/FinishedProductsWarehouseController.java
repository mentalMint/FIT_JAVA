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
            outerLoop:
            while (true) {
//                System.err.println("Controller: new iteration. Products sum: " +
//                        (assembly.getFinishedProductsWarehouse().getProductsNumber() + assembly.getProductsAssemblingNumber()));
                while (assembly.getFinishedProductsWarehouse().getProductsNumber() + assembly.getProductsAssemblingNumber()
                        > assembly.getFinishedProductsWarehouse().getSize() / 2) {
//                    System.err.println("Controller: need wait");
                    try {
                        assembly.getFinishedProductsWarehouse().getMutex().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break outerLoop;
                    }
                }
                System.err.println("Controller: is notified");
                for (int i = 0; i < (assembly.getFinishedProductsWarehouse().getSize() + 1) / 2; i++) {
                    assembly.assemble();
                }
                System.err.println("Controller: delegated tasks");
            }
        }
    }
}
