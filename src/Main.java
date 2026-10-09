import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada del programa. Expone un menú por consola para
 * interactuar con el evaluador de expresiones aritméticas basado
 * en pilas (estudio de caso 1 — Estructuras de Datos).
 */
public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        EvaluadorExpresiones evaluador = new EvaluadorExpresiones();
        boolean continuar = true;

        System.out.println("=== Evaluador de expresiones aritméticas con Pila (Estudio de caso 1) ===");
        while (continuar) {
            menu();
            String opcion = entrada.nextLine().trim();
            switch (opcion) {
                case "1":
                    evaluarExpresion(evaluador, entrada, false);
                    break;
                case "2":
                    evaluarExpresion(evaluador, entrada, true);
                    break;
                case "3":
                    continuar = false;
                    System.out.println("Fin del programa.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.\n");
            }
        }
        entrada.close();
    }

    private static void menu() {
        System.out.println("\n----------------------------------------");
        System.out.println("1. Evaluar una expresión aritmética");
        System.out.println("2. Evaluar una expresión mostrando el detalle de la pila paso a paso");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void evaluarExpresion(EvaluadorExpresiones evaluador, Scanner entrada, boolean conTraza) {
        System.out.print("\nIngrese la expresión (ej: 3 + (4 * x) - 2 ^ y): ");
        String texto = entrada.nextLine();
        try {
            List<Token> tokens = evaluador.tokenizar(texto);
            List<Token> postfija;
            if (conTraza) {
                postfija = infijaAPostfijaConTraza(tokens);
            } else {
                postfija = evaluador.infijaAPostfija(tokens);
            }
            System.out.print("Notación postfija (RPN): ");
            for (Token t : postfija) System.out.print(t + " ");
            System.out.println();

            double resultado = evaluador.evaluarPostfija(postfija, entrada);
            System.out.println("Resultado: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al evaluar la expresión: " + e.getMessage());
        }
    }

    /**
     * Misma conversión infija -> postfija que EvaluadorExpresiones.infijaAPostfija,
     * pero imprimiendo el estado de la Pila<Token> de operadores después de cada
     * apilar()/desapilar(), para evidenciar de forma didáctica el comportamiento
     * LIFO de la estructura (requisito de "representación gráfica de las
     * operaciones fundamentales de la pila" de la consigna).
     */
    private static List<Token> infijaAPostfijaConTraza(List<Token> entradaTokens) {
        java.util.List<Token> salida = new java.util.ArrayList<>();
        Pila<Token> pilaOperadores = new Pila<>();
        int paso = 1;

        for (Token t : entradaTokens) {
            switch (t.getTipo()) {
                case NUMERO:
                case VARIABLE:
                    salida.add(t);
                    System.out.println("Paso " + (paso++) + ": token '" + t + "' -> directo a la salida. Pila operadores: " + pilaOperadores);
                    break;
                case PARENTESIS_IZQ:
                    pilaOperadores.apilar(t);
                    System.out.println("Paso " + (paso++) + ": apilar '(' -> Pila operadores: " + pilaOperadores);
                    break;
                case PARENTESIS_DER:
                    while (!pilaOperadores.esVacia() && pilaOperadores.verTope().getTipo() != Token.Tipo.PARENTESIS_IZQ) {
                        salida.add(pilaOperadores.desapilar());
                    }
                    pilaOperadores.desapilar();
                    System.out.println("Paso " + (paso++) + ": ')' desapila hasta '(' -> Pila operadores: " + pilaOperadores);
                    break;
                case OPERADOR:
                    while (!pilaOperadores.esVacia()
                            && pilaOperadores.verTope().getTipo() == Token.Tipo.OPERADOR
                            && Token.precedencia(pilaOperadores.verTope().getTexto()) >= Token.precedencia(t.getTexto())) {
                        salida.add(pilaOperadores.desapilar());
                    }
                    pilaOperadores.apilar(t);
                    System.out.println("Paso " + (paso++) + ": apilar operador '" + t + "' -> Pila operadores: " + pilaOperadores);
                    break;
            }
        }
        while (!pilaOperadores.esVacia()) {
            salida.add(pilaOperadores.desapilar());
            System.out.println("Paso " + (paso++) + ": vaciar pila restante -> Pila operadores: " + pilaOperadores);
        }
        return salida;
    }
}
