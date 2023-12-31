package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GestioFilsServidor implements Runnable {
	private List<ObjecteClient> llistaClients;
	private ObjecteClient objecteClient;

	public GestioFilsServidor(List<ObjecteClient> llistaClients, ObjecteClient objecteClient) {
		this.llistaClients = llistaClients;
		this.objecteClient = objecteClient;
	}

	@Override
	public void run() {
		try {
			// Recibir
			InputStream is = objecteClient.getSocket().getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			// Enviar
			OutputStream os = objecteClient.getSocket().getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);

			boolean autenticacioCorrecta = false;
			while (!autenticacioCorrecta) {

				String usuari = br.readLine();
				String contrasenya = br.readLine();

				if (AutenticacioUsuari(usuari, contrasenya)) {
					System.out.println("SERVIDOR >>> Autenticacio correcta.");
					pw.println("Ok");
					pw.println(true);
					autenticacioCorrecta = true;
				} else {
					System.out.println("SERVIDOR >>> Autenticacio incorrecta.");
					pw.println("Usuari o contrasenya incorrectes");
					pw.println(false);
				}
			}

			// br.readline();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");

		}

	}

	private void ExecutarMisstage(String misstage) {
		if (misstage.equals("exit")) {
			llistaClients.remove(client);
			// Client tanca la seva terminal
			return;
		}
		if (misstage.equals("?")) {
			System.out.print("Clients disponibles:");
			for (String clientString : llistaClients) {
				System.out.print(" | " + clientString);
			}
			return;
		}
		if (misstage.startsWith("@")) {

		}
	}

	private boolean AutenticacioUsuari(String usuario, String Contrasenya) {
		File autenticacio = new File("autenticacio.txt");
		boolean resultat = false;
		try (BufferedReader br = new BufferedReader(new FileReader(autenticacio))) {
			String linea;
			String[] usuariInfo = new String[2];
			while ((linea = br.readLine()) != null) {
				usuariInfo = linea.split(";");
				if (usuariInfo[0].equals(usuario) && usuariInfo[1].equals(Contrasenya)) {
					resultat = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultat;
	}

}
