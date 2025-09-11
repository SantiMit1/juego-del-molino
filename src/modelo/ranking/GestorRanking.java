package modelo.ranking;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorRanking {
    private static final String ARCHIVO_RANKING = "ranking.dat";
    private List<RegistroRanking> registros;

    public GestorRanking() {
        cargarRanking();
    }

    private void cargarRanking() {
        File archivo = new File(ARCHIVO_RANKING);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
                registros = (List<RegistroRanking>) entrada.readObject();
            } catch (Exception e) {
                System.out.println("Error al cargar el ranking: " + e.getMessage());
                registros = new ArrayList<>();
            }
        } else {
            registros = new ArrayList<>();
        }
    }

    private void guardarRanking() {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RANKING))) {
            salida.writeObject(registros);
        } catch (Exception e) {
            System.out.println("Error al guardar el ranking: " + e.getMessage());
        }
    }

    public void registrarVictoria(String nombreJugador) {
        RegistroRanking registro = buscarRegistro(nombreJugador);
        if (registro == null) {
            registro = new RegistroRanking(nombreJugador);
            registros.add(registro);
        }
        registro.incrementarVictorias();
        guardarRanking();
    }

    private RegistroRanking buscarRegistro(String nombreJugador) {
        for (RegistroRanking registro : registros) {
            if (registro.getNombreJugador().equals(nombreJugador)) {
                return registro;
            }
        }
        return null;
    }

    public List<RegistroRanking> obtenerTop(int n) {
        List<RegistroRanking> ranking = new ArrayList<>(registros);
        ranking.sort(Comparator.comparing(RegistroRanking::getVictorias).reversed());

        if (ranking.size() > n) {
            return ranking.subList(0, n);
        } else {
            return ranking;
        }
    }
}

