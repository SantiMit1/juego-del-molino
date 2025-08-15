package cliente;

import ar.edu.unlu.rmimvc.cliente.Cliente;
import controlador.Controlador;
import vistas.Vista;
import vistas.terminal.VistaTerminal;
import vistas.grafica.VistaGrafica;

public class AppCliente {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        String ipServidor = "127.0.0.1";
        int portServidor = 8888;
        int port = 9999;

        Controlador controlador = new Controlador();
        Cliente cliente = new Cliente(ip, port, ipServidor, portServidor);
        try {
            cliente.iniciar(controlador);
            Vista vista = new VistaGrafica(controlador);
            controlador.setVista(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
