package me.siyum.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        try {
            //Creating server socket
            ServerSocket serverSocket = new ServerSocket(5001);
            //accept and create new socket
            Socket socket = serverSocket.accept();

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            new Thread(() -> {
                while (true) {
                    String message;
                    try {
                        message = dataInputStream.readUTF();
                        System.out.println("Client : " + message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    String reply;
                    try {
                        reply = bufferedReader.readLine();
                        if (reply.equals("end")) break;

                        dataOutputStream.writeUTF(reply);
                        dataOutputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    dataOutputStream.close();
                    dataInputStream.close();
                    bufferedReader.close();
                    socket.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}