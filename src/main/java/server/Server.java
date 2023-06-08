package server;


import IO.ConsoleManager;
import commands.Command;
import exceptions.PropertiesException;
import utilities.*;
import utilities.Module;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import static utilities.PropHelper.*;

public class Server {
    private int port;
    private Socket socket;
    private ServerSocket server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InputStream stream;
    private final int DEFAULT_PORT = 2023;
    private Command command;

    public Server() throws PropertiesException {
        this.port = DEFAULT_PORT;
        boolean connect = false;
        while (!connect) {
            try {
                server = new ServerSocket(port);
                connect = true;
                ConsoleManager.printInfoPurple("The server is up and accessible by port " + port);
            } catch (Exception e) {
                port = (int) (Math.random() * 20000 + 10000);
            }
        }
        stream = System.in;
        getProperties();
        DataBaseHandler dataBaseHandler = new DataBaseHandler(PropHelper.getHost(), PropHelper.getPort(), PropHelper.getUser(), PropHelper.getPassword(), PropHelper.getBaseName());
        DataBaseUserManager dataBaseUserManager = new DataBaseUserManager(dataBaseHandler);
        DataBaseCollectionManager dataBaseCollectionManager = new DataBaseCollectionManager(dataBaseHandler, dataBaseUserManager);
        CollectionManager collectionManager = new CollectionManager(dataBaseCollectionManager);
        Module.setCollectionManager(collectionManager);
    }

    public void runServer() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ForkJoinTask<Object>() {
            @Override
            public Object getRawResult() {
                return null;
            }

            @Override
            protected void setRawResult(Object value) {

            }

            @Override
            protected boolean exec() {

                Object o = null;
                try {
                    connect();
                    while (o == null) {
                        o = getObject();
                        command = (Command) o;
                    }
                    B b = getB();
                    new Thread(b).start();
                    return true;
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    return true;
                }
            }
        });

    }

    private B getB() {
        return new B();
    }

    class B implements Runnable {

        @Override
        public void run() {
            try {
                while (command == null) {
                    try {
                        command = (Command) getObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                boolean result = Module.runningCmd(command);
                if (result) {
                    Module.addMessage("Execution is successful");

                } else {
                    Module.addMessage("The command could not be executed ((");
                }
                ExecutorService service = Executors.newCachedThreadPool();
                C c = getC();
                service.execute(c);
                service.shutdown();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public C getC() {
            return new C();
        }

        class C implements Runnable {

            @Override
            public void run() {
                try {
                    sendObject(Module.messageFlush());
                } catch (IOException ignore) {
                    //ignore
                }
            }
        }
    }


    private void connect() throws IOException {
        socket = server.accept();
    }

    private Object getObject() throws IOException, ClassNotFoundException {
        inputStream = new ObjectInputStream(socket.getInputStream());
        return inputStream.readObject();
    }

    public void close() throws IOException {
        server.close();
    }

    private void sendObject(Serializable o) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(o);
        outputStream.flush();
    }
}
