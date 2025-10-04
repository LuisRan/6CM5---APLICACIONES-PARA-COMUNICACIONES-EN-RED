import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorTienda {

    private static List<Articulo> inventario = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5050)) {
            System.out.println("🛒 Servidor de tienda en línea iniciado en el puerto 5000...");
            inicializarInventario();

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                new Thread(new ManejadorCliente(cliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void inicializarInventario() {
        inventario.add(new Articulo("Laptop", "Dell", "Electrónica", 15000, 5));
        inventario.add(new Articulo("Mouse", "Logitech", "Accesorio", 400, 10));
        inventario.add(new Articulo("Celular", "Samsung", "Electrónica", 12000, 3));
        inventario.add(new Articulo("Audífonos", "Sony", "Accesorio", 800, 8));
    }

    private static class ManejadorCliente implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private List<Articulo> carrito = new ArrayList<>();

        ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                boolean activo = true;
                while (activo) {
                    String opcion = (String) in.readObject();

                    switch (opcion) {
                        case "LISTAR":
                            out.writeObject(inventario);
                            break;
                        case "BUSCAR":
                            String termino = (String) in.readObject();
                            List<Articulo> resultado = new ArrayList<>();
                            for (Articulo a : inventario) {
                                if (a.getNombre().equalsIgnoreCase(termino)
                                        || a.getMarca().equalsIgnoreCase(termino)) {
                                    resultado.add(a);
                                }
                            }
                            out.writeObject(resultado);
                            break;
                        case "AGREGAR":
                            String nombre = (String) in.readObject();
                            for (Articulo a : inventario) {
                                if (a.getNombre().equalsIgnoreCase(nombre) && a.getStock() > 0) {
                                    carrito.add(a);
                                    a.setStock(a.getStock() - 1);
                                    out.writeObject("Artículo agregado al carrito.");
                                    nombre = null;
                                    break;
                                }
                            }
                            if (nombre != null) out.writeObject("No se encontró o sin stock.");
                            break;
                        case "CARRITO":
                            out.writeObject(carrito);
                            break;
                        case "FINALIZAR":
                            double total = 0;
                            for (Articulo a : carrito) total += a.getPrecio();
                            out.writeObject("Compra finalizada. Total: $" + total);
                            activo = false;
                            break;
                    }
                    out.flush();
                }

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
