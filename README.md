# Estudio de caso 1 — Pila para análisis de expresiones aritméticas

Universidad CENFOTEC — Estructuras de Datos (SOFT-10) — C3-2026
Docente: Romario Salas Cerdas

Implementación propia (sin `java.util.Stack`) de una pila enlazada genérica
(`Nodo`, `Pila`) aplicada al análisis de expresiones aritméticas: tokenización,
conversión de notación infija a postfija (algoritmo Shunting-yard) y
evaluación de la expresión resultante, incluyendo soporte para variables
numéricas y paréntesis.

## Estructura

- `src/Nodo.java` — nodo genérico de la pila enlazada.
- `src/Pila.java` — pila genérica implementada a mano (apilar, desapilar, verTope, esVacia).
- `src/Token.java` — representa un token de la expresión (número, variable, operador, paréntesis).
- `src/EvaluadorExpresiones.java` — tokenización, conversión infija→postfija y evaluación.
- `src/Main.java` — menú interactivo por consola.

## Cómo ejecutar

```bash
cd src
javac *.java
java Main
```

El menú permite evaluar una expresión directamente, o en modo "paso a paso"
que imprime el estado de la pila de operadores después de cada operación
(apilar/desapilar), para evidenciar el comportamiento LIFO de la estructura.
