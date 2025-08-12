package servidor;

import ar.edu.unlu.rmimvc.servidor.Servidor;
import modelo.IJuego;
import modelo.Juego;
import modelo.Tablero;

public class AppServidor {
    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int portServidor = 8888;

        IJuego juego = new Juego(new Tablero());
        Servidor servidor = new Servidor(ipServidor, portServidor);

        try {
            servidor.iniciar(juego);
            System.out.println("Servidor iniciado en " + ipServidor + ":" + portServidor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
