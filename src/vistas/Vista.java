package vistas;

import controlador.Controlador;

public abstract class Vista {
    protected final Controlador controlador;
    protected String nombreJugador;

    public Vista(Controlador controlador) {
        this.controlador = controlador;
        controlador.setVista(this);
    }

    public abstract void colocarFicha();

    public abstract void moverFicha();

    public abstract void eliminarFicha();

    public abstract void mostrarMensaje(String mensaje);

    public abstract void mostrarTablero(String posiciones);

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
}
