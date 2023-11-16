package cajeroautomatico;

import java.io.*;
import java.util.Scanner; 

public class CajeroAutomatico {
    private static final String NOMBRE_ADMIN = "admin";
    private static final int NIP_ADMIN = 3243;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Cajero cajero = new Cajero();
            
            Usuario usuario = ingresarUsuario(scanner);
            
            if (usuario.getNombre().equalsIgnoreCase(NOMBRE_ADMIN) && usuario.getNIP() == NIP_ADMIN) {
                // Acceso en modo administrador
                System.out.println("Acceso como administrador concedido.");
                
                boolean operacionExitosa = false;
                while (!operacionExitosa) {
                    System.out.println("\nAcciones disponibles para el administrador:");
                    System.out.println("1. Mostrar acciones guardadas en el log");
                    System.out.println("2. Mostrar cantidad de billetes restantes");
                    System.out.println("3. Salir");
                    
                    System.out.println("Seleccione una opción: ");
                    int opcion = scanner.nextInt();
                    
                    switch (opcion) {
                        case 1:
                            accionesAdministrador(cajero);
                            break;
                        case 2:
                            cajero.mostrarBilletesDisponibles();
                            break;
                        case 3:
                            operacionExitosa = true;
                            break;
                        default:
                            System.out.println("Opción inválida. Intente de nuevo.");
                            break;
                    }
                }
            } else {
                // Acceso en modo cajero
                System.out.println("Acceso como usuario normal concedido.");
                System.out.println("Saldo disponible: $" + usuario.getSaldo());
                cajero.mostrarBilletesDisponibles();
                
                boolean operacionExitosa = false;
                while (!operacionExitosa) {
                    System.out.println("\nAcciones disponibles:");
                    System.out.println("1. Consultar saldo");
                    System.out.println("2. Retirar efectivo");
                    System.out.println("3. Salir");
                    
                    System.out.println("Seleccione una opción: ");
                    int opcion = scanner.nextInt();
                    
                    switch (opcion) {
                        case 1:
                            usuario.consultarSaldo();
                            registrarAccion("Consultar", usuario, usuario.getSaldo(), "SI");
                            break;
                        case 2:
                            System.out.println("Ingrese la cantidad a retirar: ");
                            int cantidadRetiro = scanner.nextInt();
                            if (cajero.verificarSaldoSuficiente(cantidadRetiro) && usuario.retirarEfectivo(cantidadRetiro)) {
                                if (cajero.retirarDinero(cantidadRetiro)) {
                                    System.out.println("Retiro exitoso. Retiró $" + cantidadRetiro);
                                    registrarAccion("Retirar", usuario, cantidadRetiro, "SI");
                                } else {
                                    registrarAccion("Retirar", usuario, cantidadRetiro, "NO");
                                }
                                operacionExitosa = true;
                            }
                            break;
                        case 3:
                            operacionExitosa = true;
                            break;
                        default:
                            System.out.println("Opción inválida. Intente de nuevo.");
                            break;
                    }
                }
            }
        }
    }


    private static Usuario ingresarUsuario(Scanner scanner) {
        System.out.println("Ingrese su nombre: ");
        String nombre = scanner.next(); // Usar next() en lugar de nextLine()

        System.out.println("Ingrese su NIP de 4 dígitos: ");
        int nip = scanner.nextInt();

        return new Usuario(nombre, nip);
    }

    private static void accionesAdministrador(Cajero cajero) {
        // Lógica para el modo administrador (mostrar acciones guardadas en el log y cantidad de billetes restantes)
        try {
            System.out.println("Acciones guardadas en el log:");
            mostrarAccionesRegistradas();
            System.out.println("\nCantidad de billetes restantes:");
            cajero.mostrarBilletesDisponibles();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de logs.");
            e.printStackTrace();
        }
    }

    private static void mostrarAccionesRegistradas() throws IOException {
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader("logs.txt"))) {
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }

    private static void registrarAccion(String accion, Usuario usuario, int cantidad, String seRealizo) {
        try (FileWriter fw = new FileWriter("logs.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Accion: " + accion);
            pw.println("Usuario: " + usuario.getNombre());
            pw.println("Saldo: " + cantidad);
            pw.println("Se realizo: " + seRealizo);
            pw.println();
        } catch (IOException e) {
            System.out.println("Error al registrar la acción en el archivo de logs.");
            e.printStackTrace();
        }
    }
}
