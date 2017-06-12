package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by utente on 09/06/2017.
 */
public class RecognitionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer=response.getWriter();
        writer.write("<!DOCTYPE html>"+
                            "<html lang='en'> <head> <meta charset='UTF-8'>"+
                            "<title>Success Creation</title> </head>"+
                            "<body> <h2>L'azione &eacute stata completata con successo!</h2>"+
                            "<a href='index.html'>Clicca qui per tornare al pannello principale</a>"+
                            "</body> </html>");
        response.setStatus(200);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
