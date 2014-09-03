package com.poxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Darwin
 */
public class ManagerRequest {

    private static LinkedList<String> listaParametros = new LinkedList<String>();

    public ManagerRequest() {
        initParametros();
    }

    /**
     * Decide que hacer cuando se recibe una peticion tipo get con la URL
     *
     * @param response contiene la respuesta que se dara con el servidor
     * @param uri contiene la URL del host al que se desea acceder
     * @param request contiene todos los datos y parametros de la peticion
     *
     * @throws IOException
     */
    protected void response(HttpServletResponse response, String uri, HttpServletRequest request) throws IOException {

        try {

            URLConnection url = new URL(uri).openConnection();
            response.setContentType(url.getContentType());
            reponseContentType(url.getContentType(), url, response, request, uri);

        } catch (MalformedURLException e) {

            response.getWriter().println("Direccion Url Invalida! " + e.getMessage());
            System.out.println("Direccion Url Invalida! " + e.getMessage());
        } catch (IllegalArgumentException iae) {

            response.getWriter().println("Argumento Invalido! " + iae.getMessage());
        } catch (IllegalStateException ie) {

            response.getWriter().println("Estado Ilegal! " + ie.getMessage());
        } catch (InterruptedException ex) {

            response.getWriter().println("Interrupcion Ilegal! " + ex.getMessage());
        }
    }

    /**
     * Este metodo evalua el tipo de respuesta que dio el servidor al que se
     * accedio para poder cargarlo en el navegador
     *
     * @param contentType contiene el tipo de respuesta del servidor
     * @param url contiene la url a la que se desea acceder
     * @param response respuesta que mandaremos al cliente
     * @param request peticion realizada por el cliente
     * @throws IOException
     */
    private void reponseContentType(String contentType, URLConnection url, HttpServletResponse response, HttpServletRequest request, String uri) throws IOException, InterruptedException {

        if (contentType.contains("image")) {

            System.out.println(url.getContentType());
            DataInputStream dis = new DataInputStream(url.getInputStream());
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());

            BufferedInputStream bis = new BufferedInputStream(dis);
            BufferedOutputStream bos = new BufferedOutputStream(dos);

            int len = bis.available();
            byte[] leido = new byte[len];

            while (bis.available() > 0) {
                System.out.println("leyendo");
                bis.read(leido);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(poxy.class.getName()).log(Level.SEVERE, null, ex);
                }

                bos.write(leido);

            }
        } else {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.getInputStream()));

            PrintWriter out = response.getWriter();

            String linea = "";

            while ((linea = in.readLine()) != null) {
                linea = evaluaString(linea, request, uri);
                out.println(linea);
            }
        }
    }

    /**
     * Sobreescribe cada link de la pagina solicitada para que sean pedidos a
     * traves del proxy
     *
     * @param evalua contiene la informacion que se va a verificar
     * @return String con los links modificados en caso de tener
     */
    private String evaluaString(String evalua, HttpServletRequest request, String url) {
        for (String cmp : listaParametros) {
            evalua = remplazaTokens(evalua, cmp, request, url);
        }
        return evalua;
    }

    /**
     * Este metodo reemplaza los links de cada atributo para que sean
     * redireccionados a travez del proxy
     *
     * @param evalua es la String que contiene los links
     * @param cmp es el delimitador por el cual se van a tomar los tokens
     * @return String con los links ya modificados
     */
    private String remplazaTokens(String evalua, String cmp, HttpServletRequest request, String url) {
        StringYokenizer syk = new StringYokenizer(evalua, cmp);
        int cantidad = syk.countTokens();
        for (int i = 0; i < cantidad; i++) {
            String remplaza = syk.nextToken();
            if (isHttpOrHttps(remplaza)) {
                evalua = evalua.replace(remplaza, "http://" + request.getHeader("Host") + "/poxy?url=" + remplaza);
            } else {
                if (isDoubleSlash(remplaza)) {
                    evalua = evalua.replace(remplaza, "http://" + request.getHeader("Host") + "/poxy?url=" + "http:" + remplaza);
                } else if (isUniqueSlash(remplaza)) {
                    evalua = evalua.replace(remplaza, "http://" + request.getHeader("Host") + "/poxy?url=" + url + remplaza);
                }
            }
        }
        return evalua;
    }

    /**
     * Valida si la cadena que recibe comienza con http
     *
     * @param remplaza es la String que se va a evaluar
     * @return true si la cadena comienza con http o https
     */
    private boolean isHttpOrHttps(String remplaza) {
        return (remplaza.substring(0, 4).equalsIgnoreCase("http"));
    }

    /**
     * Valida se la cadena comienza con doble slash de division ej: //
     *
     * @param remplaza es la String que se va a evaluar
     * @return true si comienza con //
     */
    private boolean isDoubleSlash(String remplaza) {
        return (remplaza.substring(0, 2).equalsIgnoreCase("//"));
    }

    /**
     * Valida se la cadena comienza con doble slash de division ej: /
     *
     * @param remplaza es la String que se va a evaluar
     * @return true si comienza con /
     */
    private boolean isUniqueSlash(String remplaza) {
        return (remplaza.substring(0, 1).equalsIgnoreCase("/"));
    }

    /**
     * Inicializa los parametros que seran tomados en cuenta para tomar los
     * tokens por la clase StringYokenizer y poder sobreescribir el contenido de
     * las String
     */
    private void initParametros() {
        //se añaden a la lista de parametros los delimitadores para hacer las modificaciones de los links
        if (listaParametros.size() == 0) {
            listaParametros.add("href=\"");
            listaParametros.add("href='");
            listaParametros.add("content=\"");
            listaParametros.add("src=\"");
            listaParametros.add("src='");
            listaParametros.add("action=\"");
        }
    }
}
