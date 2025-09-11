package modelo.ranking;

import java.io.Serializable;

public class RegistroRanking implements Serializable {
    private String nombreJugador;
    private int victorias;

    public RegistroRanking(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.victorias = 0;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getVictorias() {
        return victorias;
    }

    public void incrementarVictorias() {
        victorias++;
    }
}
