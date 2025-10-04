import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteTienda {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5050);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner sc = new Scanner(System.in)) {

            boolean activo = true;
            while (activo) {
                System.out.println("\n=== MENÚ TIENDA ONLINE ===");
                System.out.println("1. Listar artículos");
                System.out.println("2. Buscar por nombre o marca");
                System.out.println("3. Agregar al carrito");
                System.out.println("4. Ver carrito");
                System.out.println("5. Finalizar compra");
                System.out.print("Selecciona una opción: ");
                String opcion = sc.nextLine();

                switch (opcion) {
                    case "1":
                        out.writeObject("LISTAR");
                        out.flush();
                        mostrarArticulos((List<Articulo>) in.readObject());
                        break;
                    case "2":
                        out.writeObject("BUSCAR");
                        System.out.print("Introduce nombre o marca: ");
                        out.writeObject(sc.nextLine());
                        out.flush();
                        mostrarArticulos((List<Articulo>) in.readObject());
                        break;
                    case "3":
                        out.writeObject("AGREGAR");
                        System.out.print("Nombre del artículo: ");
                        out.writeObject(sc.nextLine());
                        out.flush();
                        System.out.println((String) in.readObject());
                        break;
                    case "4":
                        out.writeObject("CARRITO");
                        out.flush();
                        mostrarArticulos((List<Articulo>) in.readObject());
                        break;
                    case "5":
                        out.writeObject("FINALIZAR");
                        out.flush();
                        System.out.println((String) in.readObject());
                        activo = false;
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mostrarArticulos(List<Articulo> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("No hay artículos para mostrar.");
        } else {
            lista.forEach(System.out::println);
        }
    }
}
