package chat;

import java.io.BufferedReader;

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

		}

	}

}
