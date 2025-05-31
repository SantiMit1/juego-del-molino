package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    protected List<Observer> observers = new ArrayList<>();

    public void agregarObservador(Observer observer){
        observers.add(observer);
    }

    public void eliminarObservador(Observer observer){
        observers.remove(observer);
    }

    public void notificarObservadores(Notificaciones notificacion) {
        for (Observer observer : observers) {
            observer.notificar(notificacion);
        }
    };
}
