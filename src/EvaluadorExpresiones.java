import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Evaluador de expresiones aritméticas basado en pilas.
 * Implementa el algoritmo Shunting-yard (Dijkstra) para convertir una
 * expresión infija a notación postfija usando una Pila<Token> de
 * operadores, y luego evalúa la expresión postfija usando una
 * Pila<Double> de valores. Las variables numéricas se resuelven
 * pidiéndole su valor al usuario por consola.
 */
public class EvaluadorExpresiones {

    /** Convierte el texto de la expresión en una lista de Token. */
    public List<Token> tokenizar(String expresion) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        expresion = expresion.replace(" ", "");
        while (i < expresion.length()) {
            char c = expresion.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numero = new StringBuilder();
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero.append(expresion.charAt(i));
                    i++;
                }
                tokens.add(new Token(Token.Tipo.NUMERO, numero.toString()));
            } else if (Character.isLetter(c)) {
                StringBuilder variable = new StringBuilder();
                while (i < expresion.length() && Character.isLetterOrDigit(expresion.charAt(i))) {
                    variable.append(expresion.charAt(i));
                    i++;
                }
                tokens.add(new Token(Token.Tipo.VARIABLE, variable.toString()));
            } else if (c == '(') {
                tokens.add(new Token(Token.Tipo.PARENTESIS_IZQ, "("));
                i++;
            } else if (c == ')') {
                tokens.add(new Token(Token.Tipo.PARENTESIS_DER, ")"));
                i++;
            } else if (Token.esOperador(String.valueOf(c))) {
                tokens.add(new Token(Token.Tipo.OPERADOR, String.valueOf(c)));
                i++;
            } else {
                throw new IllegalArgumentException("Carácter no reconocido en la expresión: '" + c + "'");
            }
        }
        return tokens;
    }

    /**
     * Convierte una lista de tokens en notación infija a notación postfija
     * (RPN) usando una Pila<Token> para los operadores y paréntesis.
     */
    public List<Token> infijaAPostfija(List<Token> entrada) {
        List<Token> salida = new ArrayList<>();
        Pila<Token> pilaOperadores = new Pila<>();

        for (Token t : entrada) {
            switch (t.getTipo()) {
                case NUMERO:
                case VARIABLE:
                    salida.add(t);
                    break;
                case PARENTESIS_IZQ:
                    pilaOperadores.apilar(t);
                    break;
                case PARENTESIS_DER:
                    while (!pilaOperadores.esVacia() && pilaOperadores.verTope().getTipo() != Token.Tipo.PARENTESIS_IZQ) {
                        salida.add(pilaOperadores.desapilar());
                    }
                    if (pilaOperadores.esVacia()) {
                        throw new IllegalArgumentException("Paréntesis desbalanceados en la expresión.");
                    }
                    pilaOperadores.desapilar(); // descarta el '('
                    break;
                case OPERADOR:
                    while (!pilaOperadores.esVacia()
                            && pilaOperadores.verTope().getTipo() == Token.Tipo.OPERADOR
                            && Token.precedencia(pilaOperadores.verTope().getTexto()) >= Token.precedencia(t.getTexto())) {
                        salida.add(pilaOperadores.desapilar());
                    }
                    pilaOperadores.apilar(t);
                    break;
            }
        }
        while (!pilaOperadores.esVacia()) {
            Token restante = pilaOperadores.desapilar();
            if (restante.getTipo() == Token.Tipo.PARENTESIS_IZQ) {
                throw new IllegalArgumentException("Paréntesis desbalanceados en la expresión.");
            }
            salida.add(restante);
        }
        return salida;
    }

    /**
     * Evalúa una expresión en notación postfija usando una Pila<Double>.
     * Si encuentra una variable, le pide su valor al usuario por consola
     * (y lo recuerda si la misma variable vuelve a aparecer).
     */
    public double evaluarPostfija(List<Token> postfija, Scanner entrada) {
        Pila<Double> pilaValores = new Pila<>();
        java.util.Map<String, Double> valoresVariables = new java.util.HashMap<>();

        for (Token t : postfija) {
            if (t.getTipo() == Token.Tipo.NUMERO) {
                pilaValores.apilar(Double.parseDouble(t.getTexto()));
            } else if (t.getTipo() == Token.Tipo.VARIABLE) {
                Double valor = valoresVariables.get(t.getTexto());
                if (valor == null) {
                    System.out.print("Ingrese el valor numérico de la variable '" + t.getTexto() + "': ");
                    valor = Double.parseDouble(entrada.nextLine().trim());
                    valoresVariables.put(t.getTexto(), valor);
                }
                pilaValores.apilar(valor);
            } else if (t.getTipo() == Token.Tipo.OPERADOR) {
                double b = pilaValores.desapilar();
                double a = pilaValores.desapilar();
                double resultado;
                switch (t.getTexto()) {
                    case "+": resultado = a + b; break;
                    case "-": resultado = a - b; break;
                    case "*": resultado = a * b; break;
                    case "/":
                        if (b == 0) throw new ArithmeticException("División entre cero.");
                        resultado = a / b;
                        break;
                    case "^": resultado = Math.pow(a, b); break;
                    default: throw new IllegalStateException("Operador no soportado: " + t.getTexto());
                }
                pilaValores.apilar(resultado);
            }
        }
        if (pilaValores.tamanio() != 1) {
            throw new IllegalArgumentException("La expresión no es válida (sobran o faltan operandos).");
        }
        return pilaValores.desapilar();
    }
}
