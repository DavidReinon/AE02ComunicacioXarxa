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
	private static List<ObjecteClient> llistaClients;
	private static List<String> llistaUsuarisAutenticacio = new ArrayList<String>();
	private ObjecteClient objecteClient;
	private String nom;
	private BufferedReader br;
	private PrintWriter pw;

	public GestioFilsServidor(List<ObjecteClient> llistaClients, ObjecteClient objecteClient) {
		GestioFilsServidor.llistaClients = llistaClients;
		this.objecteClient = objecteClient;
	}

	@Override
	public void run() {
		nom = objecteClient.getNom();
		try {
			// Recibir
			InputStream is = objecteClient.getSocket().getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			// Enviar
			OutputStream os = objecteClient.getSocket().getOutputStream();
			pw = new PrintWriter(os, true);

			autenticacioClient();

			processEnviuMissatges();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error en " + nom);

		}

	}

	/**
	 * Bucle que s'encarrega de autenticar al client y li torna un booleano depenent
	 * del resultat
	 */
	private void autenticacioClient() {
		boolean autenticacioCorrecta = false;
		try {
			while (!autenticacioCorrecta) {

				String usuari = br.readLine();
				String contrasenya = br.readLine();

				if (comprobarCredencials(usuari, contrasenya)) {

					if (!llistaUsuarisAutenticacio.isEmpty() && llistaUsuarisAutenticacio.contains(usuari)) {
						System.out.println("SERVIDOR >>> Autenticacio del " + nom + " incorrecta.");
						pw.println("Ya existeix un client conectat amb aquest usuari.");
						pw.println(false);
					} else {
						System.out.println("SERVIDOR >>> Autenticacio del " + nom + " correcta.");
						llistaUsuarisAutenticacio.add(usuari);
						pw.println("Ok");
						pw.println(true);
						autenticacioCorrecta = true;
					}

				} else {
					System.out.println("SERVIDOR >>> Autenticacio del " + nom + " incorrecta.");
					pw.println("Usuari o contrasenya incorrectes");
					pw.println(false);
				}
			}
		} catch (Exception e) {
			System.err.println("SERVIDOR >>> Error en autenticacio de " + nom);
		}

	}

	/**
	 * Comproba les credencials introduides per el client
	 * 
	 * @param usuari
	 * @param Contrasenya
	 * @return true si son correctes, false si no.
	 */
	private boolean comprobarCredencials(String usuari, String Contrasenya) {
		File autenticacio = new File("autenticacio.txt");
		boolean resultat = false;
		try (BufferedReader br = new BufferedReader(new FileReader(autenticacio))) {
			String linea;
			String[] usuariInfo = new String[2];
			while ((linea = br.readLine()) != null) {
				usuariInfo = linea.split(";");
				if (usuariInfo[0].equals(usuari) && usuariInfo[1].equals(Contrasenya)) {
					resultat = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	/**
	 * Gestiona el bucle d'enviu de missatges
	 */
	private void processEnviuMissatges() {
		boolean seguir = true;
		try {
			while (seguir) {
				String missatge = br.readLine();
				if (missatge == null) {
					seguir = false; // Se detectó el cierre del BufferedReader
				} else if (!missatge.isBlank()) {
					seguir = executarMissatge(missatge);
				}
			}
		} catch (Exception e) {
			System.err.println("SERVIDOR >>> Error en enviu de " + nom);
		}

	}

	/**
	 * Executa la opcio del missatge que es convenient
	 * 
	 * @param missatge
	 * @return false en la opcio 'exit', true en les de mes.
	 */
	private boolean executarMissatge(String missatge) {
		if (missatge.equals("exit")) {
			return exitClient();

		} else if (missatge.equals("?")) {
			return mostrarLListaClientsDisponibles();

		} else if (missatge.startsWith("@")) {
			return mensatgePersonal(missatge);

		} else {
			// Mensaje para todos
			return mensatgeGlobal(missatge);
		}
	}

	/**
	 * Executa la eixida del client
	 * 
	 * @return false, per finalitzar el bucle de lectura
	 */
	private boolean exitClient() {
		llistaClients.remove(objecteClient);
		try {
			objecteClient.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error al tancar socket client.");
		}
		// Client tanca la seva terminal
		return false;
	}

	/**
	 * Envia al client la llista de tots els client que estan disponibles
	 * 
	 * @return true
	 */
	private boolean mostrarLListaClientsDisponibles() {
		pw.print("Clients disponibles:");
		for (ObjecteClient client : llistaClients) {
			pw.print(" | " + client.getNom());
		}
		pw.println();
		return true;
	}

	/**
	 * Envia el misstage del client a altre client en concret. Concretat per @ al
	 * principi del misstage.
	 * 
	 * @param missatge
	 * @return true
	 */
	private boolean mensatgePersonal(String missatge) {
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

				// Per no enviar el '@usuari' comença per 1
				for (int i = 1; i < missatgeArrayStrings.length; i++) {
					mensatgeSenseTagString += missatgeArrayStrings[i] + " ";
				}

				pwThisClient.println(nom + " >>> " + mensatgeSenseTagString);
				pwThisClient.println();
				break;
			}
		}
		return true;
	}

	/**
	 * Envia el misstage del client a tots els clients conectats
	 * 
	 * @param missatge
	 * @return
	 */
	private boolean mensatgeGlobal(String missatge) {
		for (ObjecteClient client : llistaClients) {
			// Menos a ell mateix
			if (!client.getNom().equals(nom)) {
				OutputStream osThisClient = null;
				try {
					osThisClient = client.getSocket().getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}

				PrintWriter pwThisClient = new PrintWriter(osThisClient, true);
				pwThisClient.println(nom + " >>> " + missatge);
			}
		}
		return false;
	}

}
