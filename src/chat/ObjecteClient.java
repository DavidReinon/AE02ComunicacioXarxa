package chat;

import java.net.Socket;

public class ObjecteClient {
	private String nombre;
	private int index;
	private Socket socketConexio;

	public ObjecteClient(String nombre, int index, Socket socketConexio) {
		this.nombre = nombre;
		this.index = index;
		this.socketConexio = socketConexio;
	}

	// Agrega getters y setters según sea necesario

	public String getNombre() {
		return nombre;
	}

	public int getIndex() {
		return index;
	}

	public Socket getSocket() {
		return socketConexio;
	}
}
