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

			boolean eixir = false;
			while (!eixir) {
				String missatge = br.readLine();
				eixir = ExecutarMisstage(missatge, pw, br);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");

		}

	}

	private boolean ExecutarMisstage(String missatge, PrintWriter pw, BufferedReader br) {
		if (missatge.equals("exit")) {
			llistaClients.remove(objecteClient);
			try {
				objecteClient.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("SERVIDOR >>> Error al tancar socket client.");

			}
			// Client tanca la seva terminal
			return true;
		}
		if (missatge.equals("?")) {
			pw.print("Clients disponibles:");
			for (ObjecteClient client : llistaClients) {
				pw.print(" | " + client.getNom());
			}
			return false;
		}
		if (missatge.startsWith("@")) {
			String[] missatgeArrayStrings = missatge.split(" ");
			String usuariTag = missatgeArrayStrings[0].substring(1);

			for (ObjecteClient client : llistaClients) {

				if (client.getNom().equals(usuariTag)) {
					OutputStream osThisClient = null;
					try {
						osThisClient = client.getSocket().getOutputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}

					PrintWriter pwThisClient = new PrintWriter(osThisClient, true);
					String mensatgeSenseTagString = "";

					for (int i = 1; i < missatgeArrayStrings.length; i++) {
						mensatgeSenseTagString += missatgeArrayStrings[i];
					}

					pwThisClient.println(mensatgeSenseTagString);
					pwThisClient.close();
					return false;
				}
			}
			pw.println("SERVIDOR >>> Client no trobat");
			return false;
		}

		// Mensatge per a tots
		for (ObjecteClient client : llistaClients) {
			OutputStream osClient = null;
			try {
				osClient = client.getSocket().getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			PrintWriter pwCliente = new PrintWriter(osClient, true);
			pwCliente.println(objecteClient.getNom() + " >>> " + missatge);
		}
		return false;
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
