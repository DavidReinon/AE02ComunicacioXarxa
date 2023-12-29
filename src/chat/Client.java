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
			Socket socketConnexio = new Socket("localhost", 1234);

			// Escriure
			OutputStream os = socketConnexio.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true); // true per indicar que es faça el pw.flush() directament

			// Llegir
			InputStream is = socketConnexio.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			Scanner teclat = new Scanner(System.in);
			
			boolean autenticacioCorrecta = false;
			while (!autenticacioCorrecta) {
				System.out.print("Usuario: ");
				String usuario = teclat.nextLine();
				System.out.print("Contrasenya: ");
				String contrasenya = teclat.nextLine();

				pw.println(usuario);
				pw.println(contrasenya);
				// pw.flush();
				System.out.println("Credencials enviades a servidor.");

				String mensaje = br.readLine();
				autenticacioCorrecta = Boolean.getBoolean(br.readLine());
				System.out.println("SERVIDOR >>> " + mensaje);
			}

			teclat.close();
			socketConnexio.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
