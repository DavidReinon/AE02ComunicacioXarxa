package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ObjecteClient {
    private String nom;
    private int index;
    private Socket socketConexio;
    private BufferedReader br;

    public ObjecteClient(String nom, int index, Socket socketConexio, BufferedReader br) {
        this.nom = nom;
        this.index = index;
        this.socketConexio = socketConexio;
        this.br = br;
    }

    public void recibirMensajes() {
        try {
            while (true) {
                String mensaje = br.readLine();
                System.out.println("SERVIDOR >>> " + mensaje);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Agrega getters y setters según sea necesario

    public String getNom() {
        return nom;
    }

    public int getIndex() {
        return index;
    }

    public Socket getSocket() {
        return socketConexio;
    }
}

