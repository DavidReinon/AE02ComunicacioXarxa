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

public class Client {

    private static boolean autenticacionCorrecta = false;

    public static void main(String[] args) {
        try {
            Socket socketConnexio = new Socket("localhost", 1234);

            // Escriure
            OutputStream os = socketConnexio.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);

            // Llegir
            InputStream is = socketConnexio.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            // Clase auxiliar para manejar el cliente
            ObjecteClient clientHandler = new ObjecteClient("client", 1, socketConnexio, br);
            Thread envioThread = new Thread(() -> enviarMensajes(pw));
            Thread recepcionThread = new Thread(clientHandler::recibirMensajes);

            // Iniciar hilos
            envioThread.start();
            recepcionThread.start();

            // Esperar hasta que ambos hilos terminen
            envioThread.join();
            recepcionThread.join();

            socketConnexio.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enviarMensajes(PrintWriter pw) {
        try {
            Scanner teclado = new Scanner(System.in);
            while (!autenticacionCorrecta) {
                System.out.print("Usuario: ");
                String usuario = teclado.nextLine();
                System.out.print("Contrasenya: ");
                String contrasenya = teclado.nextLine();

                pw.println(usuario);
                pw.println(contrasenya);

                String respuesta = br.readLine(); // Cambiado de pw a br
                autenticacionCorrecta = Boolean.parseBoolean(respuesta);

                if (!autenticacionCorrecta) {
                    System.out.println("SERVIDOR >>> " + br.readLine());
                }
            }

            while (true) {
                System.out.print("Mensaje: ");
                String mensaje = teclado.nextLine();
                pw.println(getTimestamp() + " - " + mensaje);

                if (mensaje.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            teclado.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return "[" + sdf.format(new Date()) + "]";
    }
}

