import java.io.Serializable;

public class Articulo implements Serializable {
    private String nombre;
    private String marca;
    private String tipo;
    private double precio;
    private int stock;

    public Articulo(String nombre, String marca, String tipo, double precio, int stock) {
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public String getTipo() { return tipo; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre + " (" + marca + ") - " + tipo + " - $" + precio + " [" + stock + " disponibles]";
    }
}
