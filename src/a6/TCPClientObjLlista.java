package a6;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TCPClientObjLlista extends Thread {
    private String hostname;
    private int port;
    private boolean acabat = false;
    private InputStream is;
    private OutputStream os;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public TCPClientObjLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket;
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            os = socket.getOutputStream();
            output = new ObjectOutputStream(os);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);

            while (!acabat) {
                Llista llista = new Llista("llista1", Arrays.asList(10,2,45,2,67,10));
                output.writeObject(llista);
                output.flush();

                llista = (Llista) input.readObject();
                printLlista(llista);
                acabat = true;
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            output.close();
            input.close();
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void printLlista(Llista llista) {
        llista.getNumberList().forEach(System.out::println);
    }

    public static void main(String[] args) {
        TCPClientObjLlista clientObjLlista  = new TCPClientObjLlista("localhost",5557);
        clientObjLlista.start();
    }
}
