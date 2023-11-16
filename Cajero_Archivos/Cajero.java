package cajeroautomatico;

import java.io.*;

public class Cajero {
    private static final String ARCHIVO_BILLETES = "billetes.dat";
    private static final int[] VALORES_BILLETES = { 100, 200, 500, 1000 };
    private int[] cantidadBilletes;

    public Cajero() {
        cantidadBilletes = new int[VALORES_BILLETES.length];
        cargarBilletes();
    }

    public void mostrarBilletesDisponibles() {
        System.out.println("Billetes disponibles en el cajero:");
        for (int i = 0; i < VALORES_BILLETES.length; i++) {
            System.out.println("$" + VALORES_BILLETES[i] + ": " + cantidadBilletes[i] + " billetes");
        }
    }

    public boolean verificarSaldoSuficiente(int cantidad) {
        int saldoTotal = 0;
        for (int i = 0; i < VALORES_BILLETES.length; i++) {
            saldoTotal += VALORES_BILLETES[i] * cantidadBilletes[i];
        }
        return saldoTotal >= cantidad;
    }

    public boolean retirarDinero(int cantidad) {
        if (!verificarSaldoSuficiente(cantidad)) {
            System.out.println("No hay suficiente dinero en el cajero.");
            return false;
        }

        int[] cantidadBilletesTemp = cantidadBilletes.clone(); // Copia temporal de billetes

        for (int i = VALORES_BILLETES.length - 1; i >= 0; i--) {
            int billetesUsados = Math.min(cantidad / VALORES_BILLETES[i], cantidadBilletesTemp[i]);
            cantidad -= billetesUsados * VALORES_BILLETES[i];
            cantidadBilletesTemp[i] -= billetesUsados;
        }

        if (cantidad == 0) {
            // Retirar los billetes y actualizar la cantidad de billetes disponibles
            for (int i = 0; i < cantidadBilletes.length; i++) {
                cantidadBilletes[i] = cantidadBilletesTemp[i];
            }
            guardarBilletes();
            return true;
        } else {
            System.out.println("No es posible entregar la cantidad solicitada con los billetes disponibles.");
            return false;
        }
    }

    private void cargarBilletes() {
        File archivo = new File(ARCHIVO_BILLETES);
        if (archivo.exists() && archivo.isFile()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                for (int i = 0; i < cantidadBilletes.length; i++) {
                    cantidadBilletes[i] = dis.readInt();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Asignar cantidades iniciales de billetes
            cantidadBilletes = new int[]{100, 100, 20, 10};
            guardarBilletes();
        }
    }

    private void guardarBilletes() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(ARCHIVO_BILLETES))) {
            for (int cantidad : cantidadBilletes) {
                dos.writeInt(cantidad);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Resto de métodos de la clase Cajero...
}
