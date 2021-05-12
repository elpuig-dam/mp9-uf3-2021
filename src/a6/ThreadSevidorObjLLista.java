package a6;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class ThreadSevidorObjLLista implements Runnable{

    private Socket clientSocket;
    private OutputStream os;
    private ObjectOutputStream output;
    private InputStream is;
    private ObjectInputStream input;
    private boolean acabat = false;


    public ThreadSevidorObjLLista(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            is = clientSocket.getInputStream();
            input = new ObjectInputStream(is);
            os = clientSocket.getOutputStream();
            output = new ObjectOutputStream(os);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!acabat) {
                //LLegim l'objecte LLista del stream input
                Llista llista = (Llista) input.readObject();

                //ordenem la llista i eliminem duplicats
                ordenarInetejar(llista);
                printLlista(llista);

                //tornem la llista al client per l'output
                output.writeObject(llista);
                output.flush();

                acabat = true;
            }
        }catch (ClassNotFoundException | IOException e) {
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

    private Llista ordenarInetejar(Llista ll) {
        Collections.sort(ll.getNumberList());
        Set<Integer> unics = new HashSet<>(ll.getNumberList());
        ll.setNumberList(unics.stream().toList());
        return ll;
    }

    private void printLlista(Llista llista) {
        llista.getNumberList().forEach(System.out::println);
    }
}
