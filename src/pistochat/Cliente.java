package pistochat;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

// Clase Cliente que representa el cliente del chat
public class Cliente extends JFrame implements ActionListener, Runnable {

    private static final long serialVersionUID = 1L;
    Socket socket = null; // Socket para la conexión con el servidor

    DataInputStream entrada; // Stream de entrada para recibir mensajes del servidor
    DataOutputStream salida; // Stream de salida para enviar mensajes al servidor
    String nombre; // Nombre del usuario del cliente
    static JTextField mensaje = new JTextField(); // Campo de texto para ingresar mensajes

    private JScrollPane scrollpane1; // JScrollPane para el área de texto del chat
    static JTextArea textarea1; // Área de texto para mostrar los mensajes del chat
    JButton botonEnviarMensaje = new JButton("Enviar"); // Botón para enviar mensajes
    JButton botonSalirChat = new JButton("Salir"); // Botón para salir del chat
    boolean repetir = true; // Bandera para controlar la ejecución del hilo de recepción de mensajes

    // Constructor de la clase Cliente
    public Cliente(Socket s, String nombre) {
        super(" PIST😊CHAT "); // Título de la ventana del cliente
        setLayout(null); // Configuración del layout

        mensaje.setBounds(10, 10, 400, 30); // Configuración del campo de texto para mensajes
        add(mensaje); // Agregar el campo de texto a la ventana

        textarea1 = new JTextArea(); // Inicializar el área de texto para el chat
        scrollpane1 = new JScrollPane(textarea1); // Inicializar el JScrollPane con el área de texto
        scrollpane1.setBounds(10, 50, 400, 300); // Configuración del tamaño y posición del JScrollPane
        add(scrollpane1); // Agregar el JScrollPane a la ventana

        // Configuración de los botones y su posición en la ventana
        botonEnviarMensaje.setBounds(420, 10, 100, 30);
        add(botonEnviarMensaje);
        botonSalirChat.setBounds(420, 50, 100, 30);
        add(botonSalirChat);

        textarea1.setEditable(false); // Configuración para que el área de texto no sea editable
        botonEnviarMensaje.addActionListener(this); // Agregar un ActionListener al botón de enviar mensaje
        botonSalirChat.addActionListener(this); // Agregar un ActionListener al botón de salir del chat
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Configuración para manejar el cierre de la ventana

        socket = s; // Asignar el socket recibido como argumento
        this.nombre = nombre; // Asignar el nombre del usuario recibido como argumento
        try {
            entrada = new DataInputStream(socket.getInputStream()); // Inicializar el stream de entrada
            salida = new DataOutputStream(socket.getOutputStream()); // Inicializar el stream de salida
            String texto ="Entra en la sala A: "+ nombre; // Mensaje de entrada al chat
            salida.writeUTF(texto); // Enviar el mensaje al servidor
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0); // En caso de error, salir del programa
        }
    }

    // Método actionPerformed para manejar eventos de los botones
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonEnviarMensaje) { // Si se pulsa el botón de enviar mensaje
            if (mensaje.getText().trim().length() == 0) // Si el campo de texto está vacío, retornar
                return;
            String texto = nombre + "~> " + mensaje.getText(); // Obtener el mensaje del campo de texto
            try {
                mensaje.setText(""); // Limpiar el campo de texto
                salida.writeUTF(texto); // Enviar el mensaje al servidor
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == botonSalirChat) { // Si se pulsa el botón de salir del chat
            String texto ="Sale de la sala A: " + nombre; // Mensaje de salida del chat
            try {
                salida.writeUTF(texto); // Enviar el mensaje de salida al servidor
                salida.writeUTF("*"); // Enviar un marcador para indicar el cierre del chat
                repetir = false; // Cambiar la bandera para detener el hilo de recepción de mensajes
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // Método run para el hilo de recepción de mensajes del servidor
    public void run() {
        String texto = ""; // Variable para almacenar los mensajes recibidos
        while (repetir) { // Mientras la bandera de repetición sea verdadera
            try {
                texto = entrada.readUTF(); // Leer un mensaje del servidor
                textarea1.setText(texto); // Mostrar el mensaje en el área de texto del chat
            } catch (IOException e) {
                // Manejar el error en caso de que se pierda la conexión con el servidor
                JOptionPane.showMessageDialog(null,
                        "No ha sido posible conectarse con el servidor\n" + e.getMessage(), "ERROR 404",
                        JOptionPane.ERROR_MESSAGE);
                repetir = false; // Cambiar la bandera para detener el hilo de recepción de mensajes
            }
        }

        try {
            socket.close(); // Cerrar el socket
            System.exit(0); // Salir del programa
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método principal que inicia la aplicación cliente
    public static void main(String args[]) {
        int puerto = 22222; // Puerto del servidor
        Socket s = null; // Socket para la conexión con el servidor

        // Solicitar al usuario que ingrese su nombre de usuario
        String nombre = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");

        // Validar si el nombre de usuario está vacío
        if (nombre.trim().length() == 0) {
            System.out.println("El nombre está vacío"); // Imprimir un mensaje en la consola
            return; // Salir del método main
        }

        try {
            s = new Socket("localhost", puerto); // Intentar conectar con el servidor en localhost
            Cliente cliente = new Cliente(s, nombre); // Crear una instancia de Cliente
            cliente.setBounds(0, 0, 540, 400); // Configurar el tamaño y posición de la ventana del cliente
            cliente.setVisible(true); // Hacer visible la ventana del cliente
            new Thread(cliente).start(); // Iniciar un nuevo hilo para el cliente
        } catch (IOException e) {
            // Manejar el error en caso de que no se pueda conectar con el servidor
            JOptionPane.showMessageDialog(null,
                    "No ha sido posible conectarse con el servidor\n" + e.getMessage(), "ERROR 404",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
