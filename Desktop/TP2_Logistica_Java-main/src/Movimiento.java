import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private Estado estado; //para usar el Enum 
    private Sucursal sucursal;// para no usar string descripcion y que sea objeto 
    private LocalDateTime fecha;

    public Movimiento(Estado estado, Sucursal sucursal) {
        this.estado=estado;
        this.sucursal=sucursal;
        this.fecha=LocalDateTime.now();
    }
    //metodos que necesito (getters) para que el envio pueda recuperar los datos
    public Sucursal getSucursal(){
        return this.sucursal;
    }
    public Estado getEstado(){
        return this.estado;
    }
    public LocalDateTime getFecha(){
        return this.fecha;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String lugar = (sucursal != null) ? " en sucursal " + sucursal.getNombre() : "";
        return "Fecha: " + fecha.format(formatter) + " - Estado del envío: " + estado.getDescripcion() + lugar;
    }
    
}
