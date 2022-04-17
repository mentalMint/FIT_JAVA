package ru.nsu.fit.oop;

public class Controller {
    public void run() throws Exception {
        Model model = new Model();
        View view = new View(model);
        model.start();
    }
}
