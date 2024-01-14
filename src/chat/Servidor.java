package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

	public static void main(String[] args) throws IOException {
		System.err.println("SERVIDOR >>> Arranca el servidor, espera petición");
		ServerSocket socketEscolta = null;
		ArrayList<ObjecteClient> listaClients = new ArrayList<>();

		try {
			socketEscolta = new ServerSocket(1234);
		} catch (IOException e) {
			System.err.println("SERVIDOR >>> Error");
			return;
		}

		int contadorClients = 0;
		while (true) {
			Socket connexio = socketEscolta.accept();

			ObjecteClient objecteClient = new ObjecteClient("client" + (contadorClients + 1), contadorClients,
					connexio);
			listaClients.add(objecteClient);

			System.err
					.println("SERVIDOR >>> Conexión recibida --> Lanza fil per a gestionar: " + objecteClient.getNom());
			GestioFilsServidor filServidor = new GestioFilsServidor(listaClients, objecteClient);
			Thread fil = new Thread(filServidor);
			fil.start();

			contadorClients++;
		}
	}
}
