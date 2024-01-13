package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class GestioFilsRebreClient implements Runnable {

	private BufferedReader brClient;

	public GestioFilsRebreClient(BufferedReader brClient) {
		this.brClient = brClient;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String mensatge = brClient.readLine();
				// El mensatge ya diu el usuari que el envia, no fa falta el >>>
				System.out.println(mensatge);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");

		}

	}

}
