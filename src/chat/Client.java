package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		try {
			Socket connexio = new Socket("localhost", 1234);

			// Llegir
			InputStream is = connexio.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			// Escriure
			OutputStream os = connexio.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);

			Scanner teclat = new Scanner(System.in);
			boolean eixir = false;
			while (!eixir) {
				System.out.print("Usuario: ");
				String usuario = teclat.nextLine();
				System.out.print("Contrasenya: ");
				String contrasenya = teclat.nextLine();

				pw.println(usuario);
				pw.println(contrasenya);
				pw.flush();
				System.out.println("Credencials enviades a servidor.");

				String mensaje = br.readLine();
				System.out.println(mensaje);
			}
			teclat.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
