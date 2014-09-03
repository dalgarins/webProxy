package com.poxy;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author NoaD
 */
public class StringYokenizer implements Enumeration<Object> {

    private String str;
    private String delim;
    private int maxPosition;
    private int posTokens;
    private int posActToken;
    private String nueva;
    private ArrayList<String> tokens = new ArrayList<>();

    /**
     * Contructor de la clase, esta clase sirve para manejar token por palabras
     * completas
     *
     * @param str es la String que se desea evaluar
     * @param delim es el delimitador del cual dependen los tokens pueden ser
     * palabras
     * 
     */
    public StringYokenizer(String str, String delim) {
        this.str = str;
        this.delim = delim;
        maxPosition = str.length();
        posActToken = 0;
        posTokens = 0;
        nueva = str;
        initTokens();
    }

    /**
     * Agrega totos los tokens que tenga la cadena a evaluar dentro del array de tokens
     */
    private void initTokens() {
        while (nueva.contains(delim)) {
            int currentMaxPosicion = nueva.length();
            if (posTokens < currentMaxPosicion) {
                nueva = nueva.substring(scanToken(posTokens), currentMaxPosicion);
                tokens.add(nueva);
            }
        }
    }

    /**
     * Este metodo escanea la cadena en busca de tokens
     *
     * @param startPos posicion desde la que se quiere escanear los tokens
     * @return int con la posicion despues del token
     */
    private int scanToken(int startPos) {
        int currentMaxPosicion = nueva.length();
        if (startPos < currentMaxPosicion) {
            nueva = nueva.substring(startPos, currentMaxPosicion);
            if (nueva.contains(delim)) {
                return nueva.indexOf(delim) + delim.length();
            }
        }
        return -1;
    }

    /**
     * Este metodo sirve para contar los tokens que tiene la cadena
     *
     * @return int con la cantidad de tokens
     */
    public int countTokens() {
        return (tokens.size() - posActToken);
    }

    /**
     * Este metodo obtiene el siguiente token
     *
     * @return String que es el nuevo token
     */
    public String nextToken() {
        if (posActToken < tokens.size()) {
            String resp = tokens.get(posActToken);
            posActToken++;
            return resp;
        }
        return null;
    }

    /**
     * Este metodo verifica si existen mas tokens dentro de la cadena
     *
     * @return true si encuentra mas tokens
     */
    public boolean hasMoreTokens() {
        return (tokens.size() - posActToken != 0);
    }

    /**
     * Este metodo verifica si existen mas elementos o tokens en la cadena
     * @return true si encuentra mas tokens
     */
    @Override
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     * Este metodo obtiene el siguiente token
     * @return Object del siguiente token
     */
    @Override
    public Object nextElement() {
        return nextToken();
    }
}
