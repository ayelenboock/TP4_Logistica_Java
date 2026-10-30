import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Sucursal {
    private String nombreCiudad;
    private String codigoPostal;
    // Cola FIFO , donde el primer envio que entra el es el primero que sale
    private Queue<Envio> colaDespacho;
    
    public Sucursal (String nombreCiudad, String codigoPostal){
        this.nombreCiudad = nombreCiudad;
        this.codigoPostal = codigoPostal;
        this.colaDespacho=new LinkedList<>();
    }

    public String getNombre() {
        return this.nombreCiudad;
    }

    public String getCodigoPostal() {
        return this.codigoPostal;
    }
    public int cantidadPendiente(){
        return colaDespacho.size();
    }

    public void recibirEnvio(Envio envio){
        if(envio == null){
            throw new IllegalArgumentException("No se puede recibir un envio que no exitse");
        }
        envio.recibir(this); // si falla, muestra la excepcion y no agrega a cola
        colaDespacho.add(envio);
    }
    //salen segun el maximo de cantidad envios, segun el orden de llegada
    public ArrayList<Envio> despachar(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a despachar debe ser mayor que cero.");
        }
        if (colaDespacho.isEmpty()) {
            throw new IllegalStateException("No hay envios esperando para despachar en la sucursal " + nombreCiudad + ".");
        }
        ArrayList<Envio> despachados = new ArrayList<>();
        while (despachados.size() < cantidad && !colaDespacho.isEmpty()) {
            Envio envio = colaDespacho.peek(); // mira el primero sin sacarlo
            envio.despachar(this);             // si falla, el envio sigue en la cola
            colaDespacho.poll();               //  acá lo saca
            despachados.add(envio);
        }
        return despachados;
    }