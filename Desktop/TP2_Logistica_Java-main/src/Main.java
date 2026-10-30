import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //instancia de registro Central
        RegistroCentral registro = new RegistroCentral();

        //  Instancias de Sucursales y Cliente
        Sucursal sucursalBariloche = new Sucursal("Bariloche", "8400");
        Sucursal sucursalViedma = new Sucursal("Viedma", "8500");
        registro.registrarSucursal(Bariloche);
        registro.registrarSucursal(Viedma);

        Cliente Juan = new Cliente("Juan", "Mora", 34567843);
        Cliente Maria= new Cliente("Maria", "Peralta",28111343);
        // Clientes creados
        System.out.println("=== Clientes Registrados===");
        for(Cliente e :registro.listarClientes()){
            System.out.println(e);
        }
        
        System.out.println("=== PREPARANDO ENVÍO ===");
        Envio envio1= new EnvioEstandar(Juan, 1001);
        Envio envio2= new EnvioExpress(Ana, 1002);
        registro.registrarEnvio(envio1);
        registro.registrarEnvio(envio2);

        System.out.println("Costo total estimado: $" + envio1.calcularCostoEnvio()+" del Envio :"+ getCodigoSeguimiento);

        System.out.println("Costo total estimado: $" + envio2.calcularCostoEnvio()+"del Envio :"+ getCodigoSeguimiento);

        System.out.println("\n=== Llegan a Bariloche ===");
        bariloche.recibirEnvio(registro.buscarEnvio(1001));
        bariloche.recibirEnvio(registro.buscarEnvio(1002));
        System.out.println("Envios esperando en Bariloche: " + bariloche.cantidadPendientes());

        System.out.println("\n=== DESPACHO DESDE BARILOCHE (capacidad 1) ===");
        ArrayList<Envio> salieron = bariloche.despachar(1);
        for (Envio e : salieron) {
            System.out.println("Salió el envio " + e.getCodigoSeguimiento());
        }
        System.out.println("Quedan esperando: " + bariloche.cantidadPendientes());

        System.out.println("\n=== EL ENVIO LLEGA A VIEDMA Y SE ENTREGA ===");  
        Envio enViaje = registro.buscarEnvio(1001);
        viedma.recibirEnvio(enViaje);
        viedma.despachar(1);
        enViaje.escanearCodigoEntrega();
        enViaje.pasarDias(6);
        enViaje.entregarPaquete();
        System.out.println("Envio entregado con éxito");

        System.out.println("\n=== HISTORIAL DEL ENVIO ===");
        for (Movimiento mov : enViaje.getHistorial()) {
            System.out.println(mov);
        }
         System.out.println("\n=== SUCURSALES POR LAS QUE PASÓ EL ENVIO ===");
        for (Sucursal s : enViaje.obtenerSucursalesRecorridas()) {
            System.out.println("- " + s.getNombre());
        }

        System.out.println("\n=== ÚLTIMO MOVIMIENTO DEL ENVIO ===");
        System.out.println(enViaje.obtenerUltimoMovimiento());

        System.out.println("\n=== BÚSQUEDA SECUENCIAL DE CLIENTE ===");
        System.out.println("Encontrado: " + registro.buscarClienteSecuencial(28111222));

        System.out.println("\n=== DESPACHO CON MENOS ENVIOS QUE LA CAPACIDAD ===");
        ArrayList<Envio> restantes = bariloche.despachar(5); // pedimos 5,pero  solo hay 1
        System.out.println("Salieron " + restantes.size() + " envio(s)");

        System.out.println("\n=== PRUEBAS DE OPERACIONES NO VÁLIDAS ===");

        try {
            registro.registrarEnvio(envio1); // código repetido
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            registro.buscarEnvio(9999); // no existe
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            registro.buscarClienteSecuencial(1); // no existe
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bariloche.despachar(1); // cola vacía
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            envio1.agregarPaquete(new Paquete(103, 2.0, "Buenos Aires")); // ya salió
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bariloche.recibirEnvio(envio1); // ya fue entregado
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            viedma.despachar(1); // no hay nada en cola
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}