package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class GestioFilsClient implements Runnable{

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
	
	
}
