package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.KNNClassifier;
import it.unipi.ing.mim.deep.tools.Output;
import it.unipi.ing.mim.img.lucene.LucImageSearch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * Created by utente on 09/06/2017.
 */
public class RecognitionServlet extends HttpServlet {
   private boolean isMultipart;
   private String filePath = "uploaded/";
   private int maxFileSize = 50 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer=response.getWriter();
        writer.write("<!DOCTYPE html>"+
                            "<html lang='en'> <head> <meta charset='UTF-8'>"+
                            "<title>Search results</title> </head>"+
                            "<body> <h2>Best hypothesis for this image: </h2>");
        response.setStatus(200);
        
        //create the filePath if not exists
        File dir = new File(filePath);
        if (!dir.exists()){
        	dir.mkdir();
        }
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File(filePath));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        //upload.setSizeMax( maxFileSize );

        try{ 
        // Parse the request to get file items.
        List fileItems = upload.parseRequest(request);
  	
        // Process the uploaded file items
        Iterator i = fileItems.iterator();

        while ( i.hasNext () ) 
        {
           FileItem fi = (FileItem)i.next();
           if ( !fi.isFormField () )	
           {
              // Get the uploaded file parameters
              String fieldName = fi.getFieldName();
              String fileName = fi.getName();
              String contentType = fi.getContentType();
              boolean isInMemory = fi.isInMemory();
              long sizeInBytes = fi.getSize();
              
              // Write the file
              if( fileName.lastIndexOf("\\") >= 0 ){
                 file = new File( filePath + 
                 fileName.substring( fileName.lastIndexOf("\\"))) ;
              }else{
                 file = new File( filePath + 
                 fileName.substring(fileName.lastIndexOf("\\")+1)) ;
              }
              System.out.println(file.getAbsolutePath());
              fi.write( file ) ;
              
              LucImageSearch l = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
              List<ImgDescriptor> foundImages = l.recognizeImage(file);
              String classification = KNNClassifier.classify(foundImages);
              String htmlResultTable = Output.generateHtmlResultsTable(foundImages, Parameters.BASE_URI);
              writer.write("<p>"+classification+"</p><h2>Most similar images: </h2>" + htmlResultTable);
           }
        }
        writer.write("<a href='index.html'>Clicca qui per tornare al pannello principale</a>"+
                     "</body> </html>");
     }catch(Exception ex) {
         System.out.println(ex);
     }
    }
        

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
