package chat;

import java.net.Socket;
import java.util.ArrayList;

public class FilsServidor implements Runnable {
	private ArrayList<String> clientsList;
	private Socket socketConnexio;

	public FilsServidor(ArrayList<String> clientsList, Socket socketConnexio) {
		super();
		this.clientsList = clientsList;
		this.socketConnexio = socketConnexio;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
