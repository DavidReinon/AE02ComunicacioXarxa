package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ReadOnlyBufferException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		try {
			Socket socketConnexio = new Socket("localhost", 1234);

			// Escriure
			OutputStream os = socketConnexio.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);

			// Llegir
			InputStream is = socketConnexio.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			autenticacio(teclado, pw, br);

			// Lanzar fil llegir
			GestioFilsRebreClient rebreClient = new GestioFilsRebreClient(br);
			Thread fil = new Thread(rebreClient);
			fil.start();

			enviarMensatge(teclado, pw);

			/*
			 * // Clase auxiliar para manejar el cliente ObjecteClient clientHandler = new
			 * ObjecteClient("client", 1, socketConnexio, br); Thread envioThread = new
			 * Thread(() -> enviarMensajes(pw)); Thread recepcionThread = new
			 * Thread(clientHandler::recibirMensajes);
			 * 
			 * // Iniciar hilos envioThread.start(); recepcionThread.start();
			 * 
			 * // Esperar hasta que ambos hilos terminen envioThread.join();
			 * recepcionThread.join();
			 */

			socketConnexio.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		teclado.close();
	}

	private static void autenticacio(Scanner teclado, PrintWriter pw, BufferedReader br) {
	    try {
	        while (true) {
	            System.out.print("Usuario: ");
	            String usuario = teclado.nextLine();
	            System.out.print("Contrasenya: ");
	            String contrasenya = teclado.nextLine();

	            pw.println(usuario);
	            pw.println(contrasenya);

	            String respostaString = br.readLine();
	            System.out.println("SERVIDOR >>> " + respostaString);

	            Boolean respostaBoolean = Boolean.parseBoolean(br.readLine());
	            if (respostaBoolean) {
	                break; // Sale del bucle si la autenticación es correcta
	            } else {
	                System.out.println("Autenticación incorrecta. Inténtalo de nuevo.");
	            }
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	private static void enviarMensatge(Scanner teclado, PrintWriter pw) {

		while (true) {
			System.out.println("Mensatje: ");
			String mensaje = teclado.nextLine();
			if (mensaje.equalsIgnoreCase("exit")) {
				pw.println(mensaje);
				break;
			} else if (mensaje.equalsIgnoreCase("?")) {
				pw.println(mensaje);
			} else
				pw.println(mensaje + " - " + getTimestamp());

		}
	}

	private static String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return "[" + sdf.format(new Date()) + "]";
	}
}
