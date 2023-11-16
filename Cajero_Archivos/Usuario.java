package cajeroautomatico;

import java.util.Random;

public class Usuario {
    private static final int SALDO_MINIMO = 1000;
    private static final int SALDO_MAXIMO = 50000;

    private String nombre;
    private int nip;
    private int saldo;

    public Usuario(String nombre, int nip) {
        this.nombre = nombre;
        this.nip = nip;
        this.saldo = generarSaldoAleatorio();
    }

    public String getNombre() {
        return nombre;
    }

    public int getNIP() {
        return nip;
    }

    public int getSaldo() {
        return saldo;
    }

    private int generarSaldoAleatorio() {
        Random random = new Random();
        return random.nextInt(SALDO_MAXIMO - SALDO_MINIMO + 1) + SALDO_MINIMO;
    }

    public void consultarSaldo() {
        System.out.println("Saldo actual: $" + saldo);
    }

    public boolean retirarEfectivo(int cantidad) {
        if (cantidad <= 0) {
            System.out.println("Por favor, ingrese una cantidad válida para retirar.");
            return false;
        } else if (cantidad > saldo) {
            System.out.println("Saldo insuficiente para realizar el retiro.");
            return false;
        } else {
            saldo -= cantidad;
            System.out.println("Retiro exitoso. Retiró $" + cantidad);
            return true;
        }
    }
}
