package pistochat;

import java.net.Socket;

public class GestorConexiones {
    int Conexion; // Número total de conexiones permitidas
    int Actual; // Número actual de conexiones establecidas
    int Max; // Máximo número de conexiones permitidas
    Socket tabla[]; // Tabla para almacenar los sockets de las conexiones
    String mensajes; // Mensajes recibidos en el gestor de conexiones

    // Constructor con parámetros
    public GestorConexiones(int maximo, int actuales, int conexiones, Socket[] tabla) {
        Max = maximo;
        Actual = actuales;
        Conexion = conexiones;
        this.tabla = tabla;
        mensajes = "";
    }

    // Constructor por defecto
    public GestorConexiones() { super(); }

    // Métodos getter y setter para el atributo Max
    public int getMax() { return Max; }
    public void setMax(int maximo) { Max = maximo; }

    // Métodos getter y setter para el atributo Conexion
    public int getConexiones() { return Conexion; }
    public synchronized void setConexiones(int conexiones) { Conexion = conexiones; }

    // Métodos getter y setter para el atributo mensajes
    public String getMensajes() { return mensajes; }
    public synchronized void setMensajes(String mensajes) { this.mensajes = mensajes; }

    // Métodos getter y setter para el atributo Actual
    public int getActuales() { return Actual; }
    public synchronized void setActuales(int actuales) { Actual = actuales; }

    // Método para agregar un socket a la tabla de conexiones
    public synchronized void addTabla(Socket s, int i) {
        tabla[i] = s;
    }

    // Método para obtener un elemento de la tabla de conexiones
    public Socket getElementoTabla(int i) { return tabla[i]; }

}

