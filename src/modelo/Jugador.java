package modelo;

import modelo.enums.Color;

import java.util.ArrayList;
import java.util.List;

import modelo.enums.EstadoFicha;

public class Jugador {
    private String nombre;
    private Color color;
    private final List<Ficha> fichas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.fichas = new ArrayList<>();
    }

    public void agregarFicha(Ficha ficha) {
        if (ficha.getColor() != this.color) {
            throw new IllegalArgumentException("El color de la ficha no coincide con el jugador.");
        }
        if (fichas.size() >= 9) {
            throw new IllegalStateException("El jugador ya tiene el máximo permitido de fichas.");
        }
        fichas.add(ficha);
    }

    public List<Ficha> obtenerFichasPorEstado(EstadoFicha estado) {
        List<Ficha> fichasPorEstado = new ArrayList<>();
        for (Ficha ficha : fichas) {
            if (ficha.getEstado() == estado) {
                fichasPorEstado.add(ficha);
            }
        }
        return fichasPorEstado;
    }

    public int contarFichasEnMano() {
        return obtenerFichasPorEstado(EstadoFicha.EN_MANO).size();
    }

    public int contarFichasEnTablero() {
        return obtenerFichasPorEstado(EstadoFicha.EN_TABLERO).size();
    }

    public boolean puedeColocarFicha() {
        return contarFichasEnMano() > 0;
    }

    public boolean puedeMoverFicha() {
        return contarFichasEnTablero() >= 3 && contarFichasEnMano() == 0;
    }

    public boolean puedeSaltarFicha() {
        return contarFichasEnTablero() <= 3 && contarFichasEnMano() == 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }
}

