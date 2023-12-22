package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileReader;


public class Servidor {

	public static void main(String[] args) {
		try {
			ServerSocket socketEscolta = new ServerSocket(1234);	
			Socket connexio = socketEscolta.accept();
			InputStream is = connexio.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String missatge = br.readLine();
			System.out.println(missatge);
			BufferedReader br2 = new BufferedReader(isr);
			String contrasenya = br.readLine();
			System.out.println(contrasenya);
			if(AutenticacioUsuari(missatge, contrasenya)) {
				System.out.println("Usuari y contrasenya correctes");
			}else {
				System.out.println("Usuari o contrasenya incorrectes");
				
			}
			OutputStream os = connexio.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(missatge.toString() + "\n");
			pw.flush();
			System.err.println("SERVIDOR >>> Espera nova peticio");
		}catch (Exception e) {
			System.out.println(e);

		}

	}
	
	private static boolean AutenticacioUsuari(String usuario, String Contrasenya) {
		File autenticacio = new File("autenticacio.txt");
		boolean resultat = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(autenticacio));
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
