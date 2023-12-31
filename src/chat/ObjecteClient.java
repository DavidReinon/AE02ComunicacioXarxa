package chat;

import java.net.Socket;

public class ObjecteClient {
	private String nom;
	private int index;
	private Socket socketConexio;

	public ObjecteClient(String nom, int index, Socket socketConexio) {
		this.nom = nom;
		this.index = index;
		this.socketConexio = socketConexio;
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
