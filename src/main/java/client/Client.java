package client;


import IO.ConsoleManager;
import commands.Connect;
import data.User;
import exceptions.Disconnect;

import static config.ConfigData.CAPACITY_BUFFER;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    private String host;
    private int port;
    private SocketChannel client;
    private Serializer serializer;
    private Deserializer deserializer;
    private ByteBuffer buffer;
    private User user;

    public Client(String h, int p, User user) throws Disconnect, IOException {
        this.host = h;
        this.port = p;
        this.user = user;
        serializer = new Serializer();
        deserializer = new Deserializer();
        buffer = ByteBuffer.allocate(CAPACITY_BUFFER);
        findServer();

    }

    public String run(Object o1) {
        String out = "";
        try {
            connect();
            sendObject(o1);
            out = (String) getObject();
            close();
        } catch (IOException e) {
            return "ohh(( No connection with the server";
        }
        return out;
    }

    private void connect() throws IOException {
        client = SocketChannel.open(new InetSocketAddress(host, port));
        client.configureBlocking(false);
    }

    private void sendObject(Object object) throws IOException {
        client.write(serializer.serialize(object));
    }

    private Object getObject() {
        while (true) {
            try {
                client.read(buffer);
                Object o = deserializer.deserialize(buffer);
                buffer = ByteBuffer.allocate(CAPACITY_BUFFER);
                return o;
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }
    }

    private void close() throws IOException {
        client.close();
    }

    private void findServer() throws Disconnect {
        ConsoleManager.printInfoPurple("Connecting to the server...");
        String result = run(new Connect(user));
        System.out.println(result);
        if (!(result.equals("Registration and authorization succeeded\nExecution is successful\n") || result.equals("Authorization succeeded\nExecution is successful\n"))) {
            ConsoleManager.printInfoPurple(result);
            throw new Disconnect("No connection");
        }
        ConsoleManager.printInfoPurple(result);
    }


}