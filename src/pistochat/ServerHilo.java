package pistochat;


import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import pistochat.GestorConexiones;

public class ServerHilo extends Thread {
    private final DataInputStream entrada;
    private final Socket socket;
    private final GestorConexiones comun;

    public ServerHilo(Socket s, GestorConexiones comun) throws IOException {
        this.socket = s;
        this.comun = comun;
        this.entrada = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        System.out.println("Conexiones actuales: " + comun.getActuales());
        String texto = comun.getMensajes();
        EnviarMensajesaTodos(texto);

        while (true) {
            try {
                String cadena = entrada.readUTF();

                if (cadena.trim().equals("*")) {
                    comun.setActuales(comun.getActuales() - 1);
                    System.out.println("Conexiones actuales: " + comun.getActuales());
                    break;
                }

                comun.setMensajes(comun.getMensajes() + cadena + "\n");
                guardarMensaje(cadena);
                EnviarMensajesaTodos(comun.getMensajes());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void EnviarMensajesaTodos(String texto) {
        for (int i = 0; i < comun.getConexiones(); i++) {
            Socket s1 = comun.getElementoTabla(i);
            if (!s1.isClosed()) {
                try {
                    DataOutputStream salida = new DataOutputStream(s1.getOutputStream());
                    salida.writeUTF(texto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void guardarMensaje(String mensaje) {
        String rutaArchivo = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\SuperChat\\src\\superchat\\mensajes.txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            escritor.write(mensaje + "\n");
            escritor.flush(); // Flushing para asegurarnos de que todos los datos se escriban en el archivo
        } catch (IOException e) {
            e.printStackTrace();
            // Podríamos agregar un registro aquí o notificar al usuario sobre el error de escritura
        }
    }
}
