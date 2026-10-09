/**
 * Representa un token individual de una expresión aritmética:
 * un número literal, una variable numérica, un operador (+ - * / ^)
 * o un paréntesis (izquierdo o derecho).
 */
public class Token {
    public enum Tipo { NUMERO, VARIABLE, OPERADOR, PARENTESIS_IZQ, PARENTESIS_DER }

    private final Tipo tipo;
    private final String texto;

    public Token(Tipo tipo, String texto) {
        this.tipo = tipo;
        this.texto = texto;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getTexto() {
        return texto;
    }

    public static int precedencia(String operador) {
        switch (operador) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    public static boolean esOperador(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^");
    }

    @Override
    public String toString() {
        return texto;
    }
}
