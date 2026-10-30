public class Paquete {
    //atributos de la clase , privados para proteger su información.
    private int numGuia;
    private double peso;
    private String destino;
    private Estado estado;
   
   //Constructor
    public Paquete(int numGuia,double peso,String destino){
        if(peso<0){
            throw new ArithmeticException("El peso del Paquete no puede ser negativo");
        }
        this.numGuia=numGuia;
        this.peso=peso;
        this.destino=destino;
        this.estado=Estado.RECIBIDO;  // estado por defecto
    }
    private boolean estaRecibido() {
        return this.estado == Estado.RECIBIDO;
    }
    private boolean estaEnPreparacion() {
        return this.estado == Estado.EN_PREPARACION;
    }

    private boolean estaEnReparto() {
        return this.estado == Estado.EN_REPARTO;
    }

    public void iniciarPreparacion() {
        if (!estaRecibido()) {
            throw new IllegalStateException("El paquete " + numGuia + " ya fue preparado, no se puede preparar de nuevo.");
        }
        this.estado = Estado.EN_PREPARACION;
    }

    public void salirAReparto() {
        if (!estaEnPreparacion()) {
            throw new IllegalStateException("El paquete " + numGuia + " todavÃ­a no estÃ¡ preparado para salir.");
        }
        this.estado = Estado.EN_REPARTO;
    }
    public void recibir() {
        if (estaEnReparto()) {
            this.estado = Estado.EN_PREPARACION;
        } else if (!estaEnPreparacion()) {
            throw new IllegalStateException("El paquete " + numGuia + " no puede recibirse en una sucursal (estado actual: "
                    + estado.getDescripcion() + ").");
        }
    }
    public void registrarEntrega() {
        if (!estaEnReparto()) {
            throw new IllegalStateException("El paquete " + numGuia + " no se puede entregar porque todavÃ­a no saliÃ³ a reparto.");
        }
        this.estado = Estado.ENTREGADO;
    }

    public double getPeso() {
        return this.peso;
    }  
    public String getDestino() {
        return this.destino;
    }

    public int getNumGuia() {
        return this.numGuia;
    }

    public Estado getEstado() {
        return this.estado;
    }
}
