# Juego del Molino

## Reglas básicas del juego del molino

El juego del molino es un juego de mesa para dos jugadores. El objetivo es formar "molinos" (líneas de tres fichas propias en línea recta) y eliminar las fichas del oponente. El juego se desarrolla en dos fases principales:

1. **Colocación de fichas:**  
   Los jugadores colocan sus fichas por turnos en los posiciones vacíos del tablero.  
   Cada vez que un jugador forma un molino (tres fichas alineadas), puede eliminar una ficha del oponente que no forme parte de un molino.

2. **Movimiento de fichas:**  
   Cuando ambos jugadores han colocado todas sus fichas, comienzan a moverlas a posiciones adyacentes vacíos.  
   Si un jugador queda con solo tres fichas, puede moverlas a cualquier posicion vacío.  
   El juego termina cuando un jugador no puede mover o le quedan solo dos fichas.

## Responsabilidades de cada clase

### `Tablero`
- Representa el tablero del juego
- Guarda las fichas y en que posicion está cada una
- Revisa al colocar o mover una ficha si el movimiento es legal

### `Nodo`
- Representa una posición en el tablero.
- Puede contener una ficha o estar vacío.

### `Ficha`
- Representa una ficha de un jugador.
- Puede tener diferentes estados (en mano, en tablero, eliminada).

### `Jugador`
- Representa a un jugador.
- Gestiona sus fichas y su nombre.

### `Juego`
- Controla la lógica principal del juego.
- Gestiona los turnos, verifica molinos y determina el ganador.

### `Controlador`
- Actúa como intermediario entre la vista y el modelo.
- Recibe acciones de la vista y las traduce en operaciones sobre el modelo.
- Notifica a la vista sobre cambios y eventos del juego.

---

Este proyecto implementa el juego del molino siguiendo el patrón MVC (Modelo-Vista-Controlador) y el patrón observador para la comunicación entre el modelo y la vista.