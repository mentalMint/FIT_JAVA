module ru.nsu.fit.oop.chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.fit.oop.chat to javafx.fxml;
    exports ru.nsu.fit.oop.chat.client;
    opens ru.nsu.fit.oop.chat.client to javafx.fxml;
    exports ru.nsu.fit.oop.chat;
    exports ru.nsu.fit.oop.chat.packets;
    opens ru.nsu.fit.oop.chat.packets to javafx.fxml;
}