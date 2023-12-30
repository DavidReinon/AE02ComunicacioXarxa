package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

	public static void main(String[] args) throws IOException {
		System.err.println("SERVIDOR >>> Arranca el servidor, espera peticio");
		ServerSocket socketEscolta = null;
		ArrayList<String> llistaClients = new ArrayList<String>();

		try {
			socketEscolta = new ServerSocket(1234);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			return;
		}

		int contadorClients = 1;
		while (true) {
			Socket connexio = socketEscolta.accept();
			llistaClients.add("client" + contadorClients);
			System.err.println("SERVIDOR >>> Connexio rebuda --> Llança fil per gestionar: "
					+ llistaClients.get(contadorClients - 1));
			GestioFilsServidor filServidor = new GestioFilsServidor(llistaClients, contadorClients, connexio);
			Thread fil = new Thread(filServidor);
			fil.start();
		}
	}

}