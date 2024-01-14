package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

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

			socketConnexio.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		teclado.close();
	}

	private static void autenticacio(Scanner teclado, PrintWriter pw, BufferedReader br) {
		boolean autenticacionCorrecta = false;
		try {
			while (!autenticacionCorrecta) {
				System.out.print("Usuario: ");
				String usuario = teclado.nextLine();
				System.out.print("Contrasenya: ");
				String contrasenya = teclado.nextLine();

				pw.println(usuario);
				pw.println(contrasenya);

				String respostaString = br.readLine();
				System.out.println("SERVIDOR >>> " + respostaString);

				Boolean respostaBoolean = Boolean.parseBoolean(br.readLine());
				autenticacionCorrecta = respostaBoolean;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void enviarMensatge(Scanner teclado, PrintWriter pw) {

		System.out.println("Click ESPAI y ENTER per enviar missatge: ");
		while (true) {
			String enter = teclado.nextLine();
			if (enter.equals(" ")) {
				
				String mensatje = JOptionPane.showInputDialog("Escriu el mensatge aci:");
				if (mensatje != null) {
					
					if (mensatje.equalsIgnoreCase("exit")) {
						pw.println(mensatje);
						break;
					} else if (mensatje.equalsIgnoreCase("?")) {
						pw.println(mensatje);
					} else
						pw.println(mensatje + " - " + getTimestamp());
				}
			}
		}
	}

	private static String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return "[" + sdf.format(new Date()) + "]";
	}
}
