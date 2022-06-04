package ru.nsu.fit.oop.chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.oop.chat.client.model.EchoClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

public class ClientMain {
        public static void main(String[] args) throws IOException {
            EchoClient client = EchoClient.start();
            Scanner input = new Scanner(System.in);
            while (true) {
//                if (input.hasNext()) {
                    String line = input.nextLine();
                    if (line.equals("")) {
                        break;
                    } else {
                        System.err.println(client.sendMessage(line));
                    }
//                }
            }
//            String resp1 = client.sendMessage("hello");
//            System.out.println(resp1);
//            String resp2 = client.sendMessage("world");
//            System.out.println(resp2);
            EchoClient.stop ();
//            launch(args);
        }

//        @Override
//        public void start(Stage stage) {
//            FXMLLoader loader = new FXMLLoader(ClientMain.class.getResource("view.fxml"));
//            try {
//                Parent root = loader.load();
//                stage.setTitle("Chat");
//                stage.setScene(new Scene(root, 480, 720));
////                Controller controller = loader.getController();
////                stage.setOnCloseRequest(windowEvent -> controller.exit());
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }