import java.util.ArrayList;

public abstract class Envio {
    protected ArrayList<Paquete> paquetes;
    protected int diasTranscurridos;
    protected boolean codigoEscaneado;
    protected Cliente cliente;
    protected int codigoSeguimiento;
    private ArrayList<Movimiento> historial;

    public Envio(Cliente cliente, int codigoSeguimiento) {
        if(cliente == null){
            throw new IllegalArgumentException(" Disculpe, un envio debe estar asociado a un cliente");
        }
        this.paquetes = new ArrayList<>();
        this.diasTranscurridos = 0; 
        this.codigoEscaneado = false; 
        this.cliente = cliente;
        this.codigoSeguimiento=codigoSeguimiento;
        this.historial = new ArrayList<>();
    }
    private boolean hayLugar(){
        return paquetes.size()<3;
    }
    private boolean todaviaNoSalio() {
        for(Movimiento mov: historial){
            if(mov.getEstado()== Estado.EN_REPARTO || mov.getEstado()==Estado.ENTREGADO){
                return false;
            }
        }
        return true;
    }
    private boolean estaEntregado(){
        if(historial.isEmpty()){
            return false;
        }
        return obtenerUltimoMovimiento().getEstado()==Estado.ENTREGADO;
    }
    private boolean estaEnAlgunaSucursal(){
        return getSucursalActual() !=null;
    }
    private void ponerPaquetesEnViaje(){
        for(Paquete paq:paquetes){
            paq.salirAReparto();
        }
    }
    public void agregarPaquete(Paquete paq) {
        if(paq == null){
            throw new IllegalArgumentException("No se pueden agregar un paquete que no existe ");
        }
    if (!hayLugar()) {
        throw new IllegalStateException(" No se pueden enviar más de 3 paquetes en el mismo envio."); 
    }
    if (!todaviaNoSalio()) {
        throw new IllegalStateException("No se pueden agregar paquetes a un envio que ya salió  "); 
    }
    paquetes.add(paq);
    paq.iniciarPreparacion();
    historial.add(new Movimiento(Estado.EN_PREPARACION, null));
    }
    public void pasarDias(int dias) {
        this.diasTranscurridos += dias;
    }

    public void escanearCodigoEntrega() {
        this.codigoEscaneado = true;
    }
    public void recibir(Sucursal sucursal){
        if(sucursal== null){
            throw new IllegalArgumentException(" Hay que indicar que sucursal recibe el envio ");
        }
        if (paquetes.isEmpty()){
            throw new IllegalStateException("No se puede recibir un envio que no tiene paquetes ");
        }
        if(estaEntregado()){
            throw new IllegalStateException("El envio" + codigoSeguimiento + "ya fue entregado. ");
        }
        if(estaEnAlgunaSucursal()){
            throw new IllegalStateException ("El envio " + codigoSeguimiento + " ya esta en la sucursal"+getSucursalActual().getNombre() + " y todavia no fue despachado ");
        }
        for (Paquete paq:paquetes){
            paq.recibir();
        }
        historial.add(new Movimiento(Estado.RECIBIDO,sucursal));
    }
    public void despachar(Sucursal sucursal){
        if(estaEntregado()){
            throw new IllegalStateException("No se puede despachar el envio"+ codigoSeguimiento + "porque ya fue entregado .");
        }
        if(!estaEnSucursal(sucursal)){
            throw new IllegalStateException("El envio "+codigoEscaneado+" no esta en esa sucursal, por lo tanto no se puede despachar desde acá ");
        }
        ponerPaquetesEnViaje();
        historial.add(new Movimiento(Estado.EN_REPARTO,sucursal));
    }
    public void entregarPaquete(){
        if(paquetes.isEmpty()){
            throw new IllegalStateException("No se puede entregar un envio que no tiene paquetes");
        }
        if(!codigoEscaneado){
            throw new IllegalStateException("Falta escanear el código de entrega");
        }
        if(!validarTiempoEscaneo()){
            throw new IllegalStateException("El envio no se puede entregar: á fuera del plazo permitido para su tipo de envio "); 
        }
        for(Paquete paq:paquetes){
            paq.registrarEntrega();
        }
        historial.add(new Movimiento(Estado.ENTREGADO,null));
    }
    public Sucursal getSucursalActual() {
        for (int i = historial.size() - 1; i >= 0; i--) {
            Movimiento mov = historial.get(i);
            if (mov.getEstado() == Estado.RECIBIDO) {
                return mov.getSucursal();
            }
            if (mov.getEstado() == Estado.EN_REPARTO || mov.getEstado() == Estado.ENTREGADO) {
                return null;
            }
        }
        return null;
    }
    public boolean estaEnSucursal(Sucursal sucursal){
        Sucursal actual=getSucursalActual();
        if(actual==null || sucursal==null){
            return false;
        }
        return actual.equals(sucursal);
    }
    public ArrayList<Movimiento> getHistorial(){
        return new ArrayList<>(this.historial);
    }
    public ArrayList<Sucursal> obtenerSucursalesRecorridas(){
        ArrayList<Sucursal> recorridas= new ArrayList<>();
        for(Movimiento mov:historial){
            Sucursal sucursal=mov.getSucursal();
            if(sucursal != null && !recorridas.contains(sucursal)){
                recorridas.add(sucursal);
            }
        }
        return recorridas;
    }
    public Movimiento obtenerUltimoMovimiento(){
        if(historial.isEmpty()){
            throw new IllegalStateException("El envio "+ codigoSeguimiento+" todavia no tiene Movimientos.");
        }
        return historial.get(historial.size()-1);
    }
    public int getCodigoSeguimiento() {
        return this.codigoSeguimiento;
    }

    public abstract double calcularCostoEnvio();
    public abstract boolean validarTiempoEscaneo();
}
