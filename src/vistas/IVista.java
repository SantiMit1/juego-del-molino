package vistas;

import modelo.Posicion;

public interface IVista {
    public void colocarFicha();

    public void moverFicha();

    public void eliminarFicha();

    public void mostrarMensaje(String mensaje);

    void mostrarTablero(Posicion[][] posiciones);
}
