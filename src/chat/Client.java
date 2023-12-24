package chat;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		try {
			Socket connexio = new Socket("localhost", 1234);

			OutputStream os = connexio.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			Scanner teclat = new Scanner(System.in);
			// while() {
			System.out.print("Usuario: ");
			String usuario = teclat.nextLine();
			System.out.print("Contrasenya: ");
			String contrasenya = teclat.nextLine();
			
			pw.write(usuario + "\n");
			pw.write(contrasenya + "\n");
			pw.flush();
			System.out.println("Credencials enviades a servidor.");
			// }
			teclat.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
