package modelo;

import modelo.enums.Color;
import modelo.enums.EstadoFicha;

public class Ficha {
    private final Color color;
    private EstadoFicha estado;

    public Ficha(Color color) {
        this.color = color;
        this.estado = EstadoFicha.EN_MANO;
    }

    public void colocarFicha(int fila, int columna) {
        if (this.estado == EstadoFicha.ELIMINADA) throw new IllegalStateException("No se puede colocar una ficha eliminada");
        if (this.estado == EstadoFicha.EN_TABLERO) throw new IllegalStateException("La ficha ya está en el tablero");

        if (this.estado == EstadoFicha.EN_MANO) {
            this.estado = EstadoFicha.EN_TABLERO;
        }
    }


    public void eliminarFicha() {
        this.estado = EstadoFicha.ELIMINADA;
    }

    public Color getColor() {
        return color;
    }

    public EstadoFicha getEstado() {
        return estado;
    }

    public void setEstado(EstadoFicha estado) {
        this.estado = estado;
    }
}
