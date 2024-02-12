package pistochat;

import java.io.*;
import java.net.*;

public class Server {
    static final int Max = 5; // Máximo de conexiones permitidas

    public static void main(String args[]) throws IOException {
        int Puerto = 22222; // Puerto en el que escucha el servidor

        ServerSocket servidor = new ServerSocket(Puerto); // Crear un socket de servidor
        System.out.println("Server iniciado"); // Imprimir un mensaje indicando que el servidor se ha iniciado

        Socket tabla[] = new Socket[Max]; // Tabla para controlar las conexiones
        GestorConexiones comun = new GestorConexiones(Max, 0, 0, tabla); // Crear un gestor de conexiones

        // Esperar conexiones hasta alcanzar el máximo de conexiones permitidas
        while (comun.getConexiones() < Max) {
            Socket socket = new Socket();
            socket = servidor.accept(); // Esperar a que un cliente se conecte

            comun.addTabla(socket, comun.getConexiones()); // Agregar el socket a la tabla de conexiones
            comun.setActuales(comun.getActuales() + 1); // Incrementar el número de conexiones actuales
            comun.setConexiones(comun.getConexiones() + 1); // Incrementar el número total de conexiones

            ServerHilo hilo = new ServerHilo(socket, comun); // Crear un hilo de servidor para el nuevo cliente
            hilo.start(); // Iniciar el hilo
        }
        servidor.close(); // Cerrar el socket del servidor una vez alcanzado el máximo de conexiones
    }
}
