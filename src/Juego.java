
import java.util.Scanner;
//Clase que maneja toda la logica del juego
public class Juego
{
    private Menu menu;
    private Tablero tablero = new Tablero();
    private Scanner in;
    private Jugador[] jugadores;
    private boolean activo;
    private boolean tableroCreado;
    private boolean jugadoresAgregados;
    private boolean juegoActivo;
    private RankingABB arbol;
    private MazoCartas mazoDeCartas;
    
    //Constructor recibe parametros y los inicializa
    public Juego(){
        this.menu = new Menu();
        this.in = new Scanner(System.in);
        this.activo = true;
        this.tableroCreado = false;
        this.jugadoresAgregados = false;
        this.juegoActivo = false;
        this.mazoDeCartas = new MazoCartas();
        Carta c1 = new Carta("Gana $50","premio", 50);
        Carta c2 = new Carta("Pierde $100","impuesto", 100);
        Carta c3 = new Carta("Avanzar 2 casillas","avanzar", 2);
        Carta c4 = new Carta("retroceder 4 casillas","retroceder", 4);
        Carta c5 = new Carta("Pierde 1 turno","pierdeTurno", 1);
        this.mazoDeCartas.agregarCarta(c1);
        this.mazoDeCartas.agregarCarta(c2);
        this.mazoDeCartas.agregarCarta(c3);
        this.mazoDeCartas.agregarCarta(c4);
        this.mazoDeCartas.agregarCarta(c5);
    }
    
    //Metodo iniciar muestra el menu en un bucle hasta que el usuario decida salir o hasta que termine el juego
    public void iniciar() {
        mostrarMenu();
    }
    
    //Metodo que usa iniciar() para mostrar el menu inicial, recibe input del usuario y lo utiliza para el metodo recibirInput()
    public void mostrarMenu(){
        while (this.activo) {
            //Metodo muestra las opciones del menu (Cargar tablero, agregar jugadores, iniciar partida y salir)
            this.menu.mostrarMenu();
            String entrada = in.nextLine();
            recibirInput(entrada);
        }
    }
    
    //Maneja el input inicial del usuario para el menu 
    public void recibirInput(String input) {
        
        switch(input) {
            //Opcion para cargar el tablero con metodo aparte
            case "1":
                cargarTablero(); 
                break;
            //Opcion para registrar jugadores con metodo aparte
            case "2": 
                registrarJugadores();
                break;
                
            //Opcion para iniciar partida con metodo aparte 
            case "3":
                iniciarPartida();
                break;
                
            //Opcion para salir            
            case "4":
                //Esto hace que el bucle principal termine
                this.activo = false;
                this.menu.dibujarLineaBase();
                this.menu.dibujarGeneral("Hasta pronto");
                this.menu.dibujarLineaTope();
                
                System.out.println("");
                break;
            //Cualquier otro input
            default:
                System.out.println("Opcion invalida");
                break;
        
        }
    }
    
    //Cargar el tablero
    public void cargarTablero(){
        if(!this.tableroCreado) {
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("Introduzca cada casilla con:");
            this.menu.dibujarGeneral("Nombre,tipo,precio,alquiler,precio de casa");
            this.menu.dibujarGeneral("Separado por comas");
            this.menu.dibujarGeneral(("Se sugiere crear al menos 15 casillas"));
            this.menu.dibujarGeneral("Para finalizar el tablero escriba \"FIN\"");
            this.menu.dibujarLineaTope();
    
            String[] entrada;
            boolean creado = false;
            Casilla casillaNueva = null;
             
            while(!this.tableroCreado) {
                
                entrada = in.nextLine().split(",");
                if (entrada.length > 0) { 
                    //Deja de esperar casillas si el usuario escribe "fin"
                    if(entrada[0].equalsIgnoreCase("FIN")){
                        tableroCreado = true;
                        this.menu.dibujarLineaBase();
                        this.menu.dibujarGeneral("Tablero creado exitosamente");
                        this.menu.dibujarLineaTope(); 
                        //Se crean casillas si la entrada al dividir con comas tiene tamano 5
                    } else if(entrada.length == 5){
                        String nombre = entrada[0];
                        String tipo = entrada[1];
                        int precio = Integer.parseInt(entrada[2]);
                        int alquiler = Integer.parseInt(entrada[3]);
                        int precioCasa = Integer.parseInt(entrada[4]);
                        //Se crea la casilla nueva
                        casillaNueva = new Casilla(nombre,tipo,precio,alquiler,precioCasa);
                        //Se agrega la casiila al tablero
                        this.tablero.agregarAlFinal(casillaNueva);
                    } else {
                        
                        //Casilla invalida:
                        this.menu.dibujarLineaBase();
                        this.menu.dibujarGeneral("Casilla invalida, debe seguir formato:");
                        this.menu.dibujarGeneral("Nombre,tipo,precio,alquiler,precio de casa");
                        this.menu.dibujarLineaTope();
                    }
                }
            }
            
        } else {
            //Si ya se creo el tablero se muestra esto si el usuario repite la opcion crear tablero
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("El tablero ya ha sido creado");
            this.menu.dibujarLineaTope();
        
        }   
    }
    
    //Cargar los jugadores
    public void registrarJugadores(){
        boolean jugadoresContados = false;
        //Se ejecuta si los jugadores no han sido creados
        if(!this.jugadoresAgregados) {
            while(!jugadoresContados) {
                this.menu.dibujarLineaBase();
                this.menu.dibujarGeneral("Introduzca cantidad de jugadores(2-4)");
                this.menu.dibujarLineaTope();
                int cantJugadores = 0;
                //Se evalua que la entrada sea numerica
                if(in.hasNextInt()) {
                    cantJugadores += in.nextInt();
                    in.nextLine();
                    //Si se reciben numeros menores a 2 o mayores a 4 se imprime error
                    if(cantJugadores < 2 || cantJugadores > 4){
                    System.out.println("La cantidad ingresada es invalida");
                
                    } else{
                        //Se crea la lista de jugadores
                        this.jugadores = new Jugador[cantJugadores];
                        //Se crea el dinero inicial segun la cnatidad de jugadores
                        int dineroInicial = 1500;
                            if(cantJugadores == 3) {
                                dineroInicial = 1700;
                            } else if (cantJugadores == 4){
                                dineroInicial = 1900;
                            }
                        
                        
                        System.out.println();
                        
                        //Se agregan jugadores a  la lista de jugadores
                        for (int i = 0; i < cantJugadores; i++) {
                            System.out.println("Introduzca el nombre del Jugador " + (i + 1));
                            String nom = in.nextLine();
                            this.jugadores[i] = new Jugador(nom,dineroInicial, tablero.getSalida());
                            this.menu.dibujarLineaBase();
                            this.menu.dibujarGeneral("Jugador agregado exitosamente");
                            this.menu.dibujarLineaTope();
                        }
                        this.menu.dibujarLineaBase();
                        this.menu.dibujarGeneral("Todos los jugadores han sido agregados");
                        this.menu.dibujarLineaTope();
                        jugadoresContados = true;
                    }
                    
                    
                } else {
                    System.out.println("Error: Se introdujo un valor no numerico");
                    in.nextLine();
                }
            }
            this.jugadoresAgregados = true;
        } else {
            //Se imprime si el usuario vuelve a usar la opcion agregar jugadores
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("Ya se agregaron los jugadores");
            this.menu.dibujarLineaTope();
        }
    }
   
    //método para inciar la partida, lleva la logica mas compleja
    public void iniciarPartida(){
        //Se imprime si falta el tablero o si falta agrega jugadores
        if(!this.tableroCreado && !this.jugadoresAgregados) {
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("Debe ingresar el tablero y jugadores para comenzar");
            this.menu.dibujarLineaTope();
        } else if (!this.tableroCreado) {
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("Debe ingresar el tablero para comenzar");
            this.menu.dibujarLineaTope();
        } else if (!this.jugadoresAgregados) {
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("Debe ingresar los jugadores para comenzar");
            this.menu.dibujarLineaTope();
        } else {
            
            //Si ya esta todo agregado comienza el juego
            juegoActivo = true;
            int contadorDeRondas = 1;
            int rondaMaxima = 15; //Rondas maximas establecidas para el juego
            int jugadorEnTurno = 0; // Indice para recorrer arreglo de jugadores
            boolean jugando = true; // booleano para indicar que le juego esta activo y repetir bucle de juego
                
                
            //Se ejecuta mientras se siga jugando y la ronda maxima sea mayor o igual al contador de Rondas
            while(jugando && rondaMaxima >= contadorDeRondas){
                    
                    this.menu.dibujarLineaBase();
                    this.menu.dibujarGeneral(" ===== Ronda " + contadorDeRondas + " =====");
                    this.menu.dibujarLineas(1);
                    this.menu.dibujarLineaTope();
                    boolean rondaTerminada = false;
                    
                    
                    
                //Todos los jugadores juegan hasta llegar al ultimo, ahi termina la ronda y se repite el primer while con la siguiente ronda
                    while(!rondaTerminada) {
                        //Se asigna el jugador actual del turno
                        Jugador actual = jugadores[jugadorEnTurno];
                        //Se muestra el ranking ABB al comienzo de cada ronda
                        rankingABB();
                        
                        //El juego termina si solo hahy un jugador activo
                        if(contarJugadoresActivos() == 1) {
                            this.menu.dibujarLineaBase();
                            this.menu.dibujarGeneral("Solo queda 1 jugador activo");                
                            this.menu.dibujarLineaTope();
                            rondaTerminada = true;
                            jugando = false;
                            this.activo = false;
                            
                        } else {
                            //Si hay un jugador eliminado o con estado inactivo se salta su turno
                            if (!actual.getEstado()) {
                                if (jugadorEnTurno == this.jugadores.length - 1) {
                                    rondaTerminada = true;
                                    contadorDeRondas++;
                                    jugadorEnTurno = 0;
                                } else {
                                    jugadorEnTurno++;
                                    
                                }
                                //Si el jugador tiene turnos perdidos se salta su turno y se indica
                            } else if (actual.getTurnosPerdidos() > 0) {
                                this.menu.dibujarLineaBase();
                                this.menu.dibujarGeneral(actual.getNombre() + " pierde este turno.");
                                this.menu.dibujarLineaTope();
                                //Se le quita un turno perdido
                                actual.agregarTurnosPerdidos(-1); 
                                
                                //Se devuelve al comeinzo de la lista de jugadores si el actual es el ultimo de la lista
                                if (jugadorEnTurno == this.jugadores.length - 1) {
                                    rondaTerminada = true; // Termina la ronda
                                    contadorDeRondas++; //Avanza ronda
                                    jugadorEnTurno = 0;
                                } else {
                                    jugadorEnTurno++; //Avanza al siguiente jugador si no es el ultimo
                                }
                            } else{
                                
                                //Si no se ha lanzado el dado el jugador tiene las siguientes opciones:
                                if(!actual.getDadoLanzado()) {
                                        this.menu.dibujarLineaBase();
                                        this.menu.dibujarGeneral("Turno de " + actual.getNombre() + " | " + actual.getDinero());
                                    
                                        
                                        this.menu.dibujarGeneral("1. Lanzar dado | 2. Ver propiedades ");
                                        this.menu.dibujarLineas(1);
                                        this.menu.dibujarGeneral("3. Ver ranking | 4 Ver estado General");
                                        this.menu.dibujarLineas(1);
                                        this.menu.dibujarGeneral("Seleccione una opcion");
                                        this.menu.dibujarLineaTope();
                                        
                                        String entrada = in.nextLine();
                                        
                                        
                                        switch(entrada) {
                                            //Lanzar dado
                                            case "1":
                                                
                                                lanzarDado(actual);
                                                actual.setDadoLanzado("t");
                                                
                                                break;
                                                
                                            //Ver propiedades
                                            case "2":
                                                this.menu.dibujarLineaBase();
                                                actual.getPropiedades().mostrarPropiedades();
                                                this.menu.dibujarLineaTope();
                                                break;
                                           //mostrar ranking
                                            case "3":
                                                mostrarRanking();
                                                break;
                                            //Mostrar el reporte general
                                            case "4":
                                                mostrarActivos();
                                                break;
                                            default:
                                                //Cualquier otra opcion
                                                this.menu.dibujarLineaBase();
                                                this.menu.dibujarGeneral("Opcion invalida");
                                                this.menu.dibujarLineaTope();
                                                break;
                                        }
                                        
                                        
                                } else {
                                    //Si el jugador ya tiro el dado se le muestra este otro menu:
                                        if(actual.getTurnosPerdidos() <= 0 && actual.getEstado() != false) {
                                            this.menu.dibujarLineaBase();
                                            this.menu.dibujarGeneral("Turno de " + actual.getNombre() + " | " + actual.getDinero());
                                    
                                            this.menu.dibujarGeneral("1. Ver propiedades | 2. Comprar propiedad");
                                            this.menu.dibujarGeneral("3. Construir casa | 4. Terminar turno");
                                            this.menu.dibujarLineas(1);
                                            this.menu.dibujarGeneral("Seleccione una opcion");
                                            this.menu.dibujarLineaTope();
                                            
                                            String entrada2 = "";
                                            //Termina el turno del jugador si esta en la carcel
                                            if(actual.getPosicion().getCasilla().getTipo().equalsIgnoreCase("carcel")){
                                                entrada2 = "4";
                                            }else{
                                                entrada2 = in.nextLine(); //Si no recibe el texto del usuario como opcion
                                            }
                                            
                                            switch(entrada2) {
                                                //Ver propiedades
                                                case "1":
                                                    
                                                    this.menu.dibujarLineaBase();
                                                    actual.getPropiedades().mostrarPropiedades();
                                                    this.menu.dibujarLineaTope();
                                                    
                                                    break;
                                                    
                                                //Comprar propiedad
                                                case "2":
                                                    //Se evalua si la casilla es una propiedad comprable
                                                    if(!actual.getPosicion().getCasilla().getTipo().equalsIgnoreCase("propiedad")){
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral("Esta casilla no se puede comprar");
                                                        this.menu.dibujarLineaTope();
                                                        //Se evalua si la casilla ya tiene dueno y es el jugador actual
                                                    } else if(this.tablero.buscarCasilla(actual.getPosicion()).getCasilla().getDueno() != null) {
                                                        if(this.tablero.buscarCasilla(actual.getPosicion()).getCasilla().getDueno().getNombre().equalsIgnoreCase(actual.getNombre())){
                                                            this.menu.dibujarLineaBase();
                                                            this.menu.dibujarGeneral(actual.getNombre() + " ya compro esta casilla");
                                                            this.menu.dibujarLineaTope(); 
                                                        } else {
                                                            //Se evualua si la casilla ya tiene dueno
                                                            this.menu.dibujarLineaBase();
                                                            this.menu.dibujarGeneral(this.tablero.buscarCasilla(actual.getPosicion()).getCasilla().getDueno().getNombre() + " ya compro esta casilla");
                                                            this.menu.dibujarLineaTope(); 
                                                            
                                                        }
                                                        //Se compra la propiedad si e l jugador actual tiene el dinero suficiente
                                                    }else if(actual.getDinero() >= actual.getPosicion().getCasilla().getPrecio()) {
                                                        actual.cobrarDinero(actual.getPosicion().getCasilla().getPrecio());
                                                        actual.agregarPropiedades(actual.getPosicion().getCasilla().getPropiedad());
                                                        this.tablero.buscarCasilla(actual.getPosicion()).getCasilla().setDueno(actual);
                                                        
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral(actual.getNombre() + " compro " + actual.getPosicion().getCasilla().getNombre());
                                                        this.menu.dibujarLineaTope();
                                                    } else {
                                                        //Se indica que el jugador no tiene dinero para comprar la propiedad
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral(actual.getNombre() + " no tiene dinero suficiente");
                                                        this.menu.dibujarGeneral("para comprar la propiedad");
                                                        this.menu.dibujarLineaTope();
                                                    }
                                                    break;
                                                
                                                //Construir casa
                                                case "3":
                                                    //Se indica si no se puede construir en la casilla actual
                                                    if(!actual.getPosicion().getCasilla().getTipo().equalsIgnoreCase("propiedad")){
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral("No se puede construir en esta casilla");
                                                        this.menu.dibujarLineaTope();
                                                        
                                                        //Se indica si no tiene dinero suficiente para comprar
                                                    } else if (actual.getDinero() < actual.getPosicion().getCasilla().getPrecioCasa()) {
                                                        
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral(actual.getNombre() + " no tiene dinero suficiente");
                                                        this.menu.dibujarLineaTope();              
                                                    //Se construye la casa si el jugador actual tiene la propiedad comprada en su lista de propiedades
                                                    }else if(actual.getPropiedades().buscarPropiedad(actual.getPosicion().getCasilla().getNombre())) {
                                                        if(actual.getPosicion().getCasilla().getPropiedad().getListaDeCasas().getContador()<3){
                                                            actual.cobrarDinero(actual.getPosicion().getCasilla().getPrecioCasa());
                                                            actual.construirCasa(actual.getPosicion());
                                                            this.menu.dibujarLineaBase();
                                                            this.menu.dibujarGeneral(actual.getNombre() + " construyo casa en " + actual.getPosicion().getCasilla().getNombre());
                                                            this.menu.dibujarLineaTope();
                                                        }else{
                                                            //Si intenta construir mas de 3 casas en la propiedad
                                                            this.menu.dibujarLineaBase();
                                                            this.menu.dibujarGeneral("Máximo de casas acanzado");
                                                            this.menu.dibujarLineaTope();
                                                        }
                                                    } else {
                                                        //Si el jugador no es propietario se indica
                                                        this.menu.dibujarLineaBase();
                                                        this.menu.dibujarGeneral("Debe ser el propietario");
                                                        this.menu.dibujarGeneral("Para construir casas");
                                                        this.menu.dibujarLineaTope();
                                                    }
                                                    
                                     
                                                    break;
                                                    
                                                //Terminar turno
                                                case "4":
                                                    this.menu.dibujarLineaBase();
                                                    this.menu.dibujarGeneral(actual.getNombre() + " termino su turno");
                                                    this.menu.dibujarLineaTope();
                                                    actual.setDadoLanzado("f");
                                                    
                                                    if (jugadorEnTurno == this.jugadores.length - 1) {
                                                            rondaTerminada = true;
                                                    }else{
                                                        jugadorEnTurno++;
                                                    }
                                                    break;    
                                                     
                                                    
                                            
                                                default:
                                                    this.menu.dibujarLineaBase();
                                                    this.menu.dibujarGeneral("Opcion invalida");
                                                    this.menu.dibujarLineaTope();
                                                    break;
                                            
                                            
                                            
                                            }
                                    } else {
                                        //Si el jugador actual tiene turnos perdidos y es el ultimo jugador en la lista se termina la ronda
                                        if (jugadorEnTurno == this.jugadores.length - 1) {
                                            rondaTerminada = true;
                                        }   
                                        //Si el jguador actual es el ultimo, se devuelve el jugador EnTurno al siugiente para terminar ronda y comenzar la siguiente con el primero en lista de jugadores
                                        if (jugadorEnTurno + 1 > this.jugadores.length - 1) {
                                            jugadorEnTurno = 0;
                                        } else {
                                            jugadorEnTurno++; // Se pasa al siguiente jugador
                                        }
                                        this.menu.dibujarLineaBase();
                                        this.menu.dibujarGeneral(actual.getNombre() + " termino su turno");
                                        this.menu.dibujarLineaTope();
                                        actual.setDadoLanzado("f");
                                        
                                    }
                                }
                            //Se indica si un jugador pierde por estar en banca rota
                                if( actual.getDinero() <= 0) {
                                    actual.cambiarEstado();
                                    this.menu.dibujarLineaBase();
                                    this.menu.dibujarGeneral(actual.getNombre() + " ha quedado en banca rota");
                                    this.menu.dibujarGeneral("Ha perdido el juego");
                                    this.menu.dibujarLineaTope();
                                }
                            }
                        }
                }
                
                
                
                this.menu.dibujarLineaBase();
                this.menu.dibujarGeneral(" ==== Fin de la ronda  " + contadorDeRondas +  " ====");
                this.menu.dibujarLineaTope();
                //Se muestra el ranking al final de cada ronda si se sigue jugando y no es la ronda final
                if(contadorDeRondas < rondaMaxima && jugando) {
                    mostrarRanking();
                }
                contadorDeRondas++; // Se avanza de ronda
                jugadorEnTurno = 0; // Se asegura de volver al jugador inicial
            }
            //Fin del bucle princial, termino el juego
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral("==== Fin del juego ====");
            
            //Se indica si el juego termino por limite de rondas
            if(contadorDeRondas > rondaMaxima && jugando) {
                    this.menu.dibujarGeneral("Limite de rondas alcanzado");
                    this.menu.dibujarGeneral("La partida termino");
                    jugando = false;
                    this.activo = false;
                    
            }
            this.menu.dibujarLineaTope();
            //Se muestra el reporte general y el ranking al final del juego
            reporteABB();
            mostrarRanking();
            
        
        }
    }
    //lanzar el dado, recibe jugador para avanzar su posicion
    public void lanzarDado(Jugador j){
        
        //Solo puede lanzar el dado si no tiene turnos perdidos
        if (j.getTurnosPerdidos() == 0) {
            
            int dado = (int) ((Math.random() * 6)+1); // dado es un numero al azar entre 1 y 6
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral(j.getNombre() + " lanzo el dado:  --> " + dado);
            NodoCasilla posicionActual = j.getPosicion();
            NodoCasilla destino = this.tablero.avanzar(posicionActual, dado);
            
            
            j.setPosicionNueva(destino); // se avanza al jugador dado posiciones y se indica
            this.menu.dibujarGeneral("Avanza a ---> " + j.getPosicion().getCasilla().getNombre());
            this.tablero.buscarCasilla(j.getPosicion()).getCasilla().mostrarInfo();
            
            //Se recorre desde la posicion original al destino para ver si el jugador paso por salida
            for(int avance = 0; avance < dado; avance++) {
                posicionActual = posicionActual.getSiguiente();
                if(posicionActual.getCasilla().getNombre().equalsIgnoreCase("salida")){
                    //Se agrega dinero si el jugador paso por salida
                    j.agregarDinero(200);
                    this.menu.dibujarGeneral("El jugador paso por la salida");
                    this.menu.dibujarGeneral("Recibe $200");
                
                }
            }
            
            //Se analiza el destino donde cayo el jugador y se actua segun el tipo de casilla
            analizarDestino(destino, j);
            
            
            this.menu.dibujarLineaTope();
            
        }else {
            // Se imprime si el jugador tiene turnos perdidos
            this.menu.dibujarLineaBase();
            this.menu.dibujarGeneral(j.getNombre() + " tiene que esperar " + (j.getTurnosPerdidos()) + " turnos para jugar");
            this.menu.dibujarLineaTope();
            j.agregarTurnosPerdidos(-1);
            
        }
    }
    
    //Metodo analiza la casilla actual para agregar efecto al jugadro segun el tipo de casilla
    //Recibe NodoCasilla destino y el Jugador
    public void analizarDestino(NodoCasilla destino, Jugador j) {
        //Se cobra si la casilla es impuesto
        if(destino.getCasilla().getTipo().equalsIgnoreCase("impuesto")){
                j.cobrarDinero(100);
                this.menu.dibujarGeneral(j.getNombre() + " pierde $100");
                //Se pierde 1 turno si es carcel
            }else if (destino.getCasilla().getTipo().equalsIgnoreCase("carcel")){
                j.agregarTurnosPerdidos(1);
                this.menu.dibujarGeneral(j.getNombre() + " pierde 1 turno");
                //gana 50 si es premio
            } else if(destino.getCasilla().getTipo().equalsIgnoreCase("premio")){
                j.agregarDinero(50);
                this.menu.dibujarGeneral(j.getNombre() + " recibe $50");
                //Toma carta si es carta
            } else if (destino.getCasilla().getTipo().equalsIgnoreCase("carta")) {
                Carta cartaTomada = this.mazoDeCartas.tomarCarta();
                //Si la carta es:
                //impuesto: se le cobra el valor de la carta
                if(cartaTomada.getAccion().equalsIgnoreCase("impuesto")){
                    j.cobrarDinero(cartaTomada.getValor());
                    this.menu.dibujarGeneral(j.getNombre() + " pierde $" + cartaTomada.getValor());
                    //premio: recibe el valor de la carta
                } else if(cartaTomada.getAccion().equalsIgnoreCase("premio")){
                    j.agregarDinero(cartaTomada.getValor());
                    this.menu.dibujarGeneral(j.getNombre() + " gana $" + cartaTomada.getValor());
                    //Avanzar: avanza n casilllas donde n = valor de la carta
                } else if(cartaTomada.getAccion().equalsIgnoreCase("avanzar")){
                    NodoCasilla posActual = j.getPosicion();
                    j.setPosicionNueva(this.tablero.avanzar(j.getPosicion(), cartaTomada.getValor()));
                    this.menu.dibujarGeneral(j.getNombre() + " avanza " + cartaTomada.getValor() + " casillas");
                    this.menu.dibujarGeneral(posActual.getCasilla().getNombre() + " ---> " + j.getPosicion().getCasilla().getNombre());
                    analizarDestino(j.getPosicion(), j);
                    if(j.getPosicion().getCasilla().getTipo().equalsIgnoreCase("propiedad")){
                        this.menu.dibujarLineas(1);
                        j.getPosicion().getCasilla().mostrarInfo();
                    }
                    
                }
                //Retroceder: retrocede n casilllas donde n = valor de la carta
                else if(cartaTomada.getAccion().equalsIgnoreCase("retroceder")){
                    NodoCasilla posActual = j.getPosicion();
                    j.setPosicionNueva(this.tablero.retroceder(j.getPosicion(), cartaTomada.getValor()));
                    this.menu.dibujarGeneral(j.getNombre() + " retrocede " + cartaTomada.getValor() + " casillas");
                    this.menu.dibujarGeneral(j.getPosicion().getCasilla().getNombre() + " <--- " + posActual.getCasilla().getNombre());
                    analizarDestino(j.getPosicion(), j);
                    //pierdeTurno: pierde un turno
                } else if(cartaTomada.getAccion().equalsIgnoreCase("pierdeTurno")){
                    j.agregarTurnosPerdidos(1);
                    this.menu.dibujarGeneral(j.getNombre() + " pierde " + cartaTomada.getValor() + " turnos");
                
                }
        }
        //Se le cobra alquiler al jugador si la casilla tiene dueno y no es el jugador actual
        if(destino.getCasilla().getDueno() != null && !destino.getCasilla().getDueno().getNombre().equals(j.getNombre()) && destino.getCasilla().getDueno().getEstado() != false){
                    int alquiler = 0;
                    alquiler += destino.getCasilla().getAlquilerBase()+destino.getCasilla().getPropiedad().getListaDeCasas().calcAumentoAlquiler();
                    
                    if(destino.getCasilla().getPropiedad() != null && destino.getCasilla().getPropiedad().getListaDeCasas().contarCasas() > 0) {
                       destino.getCasilla().getPropiedad().getListaDeCasas().calcAumentoAlquiler(); 
                    }
                    j.cobrarDinero(alquiler);
                    destino.getCasilla().getDueno().agregarDinero(alquiler);
                    
                    this.menu.dibujarGeneral(j.getNombre() + " paga $" + alquiler + " de alquiler a " +  destino.getCasilla().getDueno().getNombre());
                    
                }
    }
    //Se crea el ranking a partir de los jugadores ordenados, se llama constantemente para ordenarlos al final o inicio de cada ronda
    public void rankingABB(){
        Jugador[] jugadoresOrdenados = ordenarSeleccion();
        RankingABB arbol = new RankingABB();
        for(int i = 0; i<jugadores.length; i++){
            arbol.agregarAlArbol(jugadoresOrdenados[i]);
        }
        
    }
    
    //Se ordenan los jugadores y despues se muestra su ranking
    public void mostrarRanking(){
        Jugador[] jugadoresOrdenados = ordenarSeleccion();
        RankingABB arbol = new RankingABB();
        for(int i = 0; i<jugadores.length; i++){
            arbol.agregarAlArbol(jugadoresOrdenados[i]);
        }
        arbol.ranking();
    }
    //Se muestra el reporte ABB con los jugadores ordenados
    public void reporteABB(){
        Jugador[] jugadoresOrdenados = ordenarSeleccion();
        RankingABB arbol = new RankingABB();
        for(int i =0; i<jugadores.length; i++){
            arbol.agregarAlArbol(jugadoresOrdenados[i]);
        }
        arbol.reporte();
    }
    
    //Ordenar los jugadores, retorna un array de jugadores ordenados por Seleccion
    public Jugador[] ordenarSeleccion(){
        
        Jugador[] jugadoresOrdenados = jugadores.clone();
        for(int i = 0; i<jugadores.length; i++){
            
            int posMenor = i;
            for(int j=i+1; j<jugadores.length;j++){
                if(jugadoresOrdenados[j].calcPatrimonio()<jugadoresOrdenados[posMenor].calcPatrimonio()){
                    posMenor = j;
                }
            }
            Jugador temp = jugadoresOrdenados[i];
            jugadoresOrdenados[i] = jugadoresOrdenados[posMenor];
            jugadoresOrdenados[posMenor] = temp;
        }
        return jugadoresOrdenados;
    }
    
    //Se cuenta la cantidad de jugadores activos
    public int contarJugadoresActivos() {
        int jugadoresActivos = 0;
        
        
        for (int i = 0; i < this.jugadores.length; i++) {
            if(this.jugadores[i].getEstado() == true) {
                jugadoresActivos++;
            }
        } 
        
        return jugadoresActivos;
    
    }
    
    //Se muestran los jugadores activos para reporte general
    public void mostrarActivos(){
        for(int i = 0; i<jugadores.length; i++){
            this.menu.dibujarLineaBase();
            if(this.jugadores[i].getEstado()){
                
                this.menu.dibujarGeneral("Estado: activo");
                jugadores[i].mostrarInfo();
                
            }else{
                this.menu.dibujarGeneral("Estado inactivo");
                this.menu.dibujarGeneral(jugadores[i].toString());
                
            }
            this.menu.dibujarLineaTope();
        }
    }
    
    
    
}