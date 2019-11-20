/*ASSIGNMENT NUMERO 6 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : HTTPBasedFileTransfer

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un server HTTP che gestisce richieste di diverso tipo
provenienti da un browser web
*/
package httpbasedfiletransfer;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Scanner;

/*
    @CLASS FileHandler
    @OVERVIEW Classe che implementa la gestione dell'invio di file a partire
    da una richiesta HTTP ricevuta dal browser.
*/
public class FileHandler
{
    private final File file;
    private final String filepath;
    private Date date;
    
    public BufferedWriter wr;
    public DataOutputStream dt;

    /*
        @METHOD FileHandler
        @OVERVIEW Costruttore di un generico FileHandler.
    
        @PAR Path Locazione (pathname) del file.
        @PAR w Buffer di scrittura della connessione da cui derivano le richieste.

        @THROWS FileNotFoundException se il file non esiste.
    */
    public FileHandler(String path,BufferedWriter w,DataOutputStream dat) throws FileNotFoundException, IOException
    {
        filepath = path;
        file = new File(path);

        wr = w;
        dt = dat;
    }
    
    /*
        @METHOD Handle
        @OVERVIEW Metodo di gestione delle richieste di trasferimento file
    */
    public synchronized void Handle() throws IOException
    {
       //Ricava il formato del file dal suo percorso
       String format = filepath.substring(filepath.lastIndexOf(".")+1).trim();

       //Assicurandosi prima che il file esista, gestisce la richiesta in base
       //al tipo di file indicato dalla richiesta HTTP del browser
       if(file.exists())
       { 
        switch(format)
        {
            case "txt":
            {
                SendText(file);
                
                break;
            }
            case "jpg":
             {
                SendFile(file);
                
                break;
             }
            case "gif":
            {
                SendFile(file);
            }
            case "ico":
            {
                SendFile(file);
                break;
            }
            default:
            {
                System.out.println("ERRORE: Formato di file non supportato\n");
                
                wr.write("HTTP/1.1 400 BAD REQUEST\r\n");
                wr.write("Date: "+date);
                
                Thread.dumpStack();
                throw new IllegalArgumentException();
            }
        }
       }
       else//NEL CASO IL FILE NON VENGA TROVATO, VIENE MANDATA UNA RISPOSTA DI ERRORE AL BROWSER
        {
            wr.write("HTTP/1.1 404 NOT FOUND\r\n");
            wr.write("\r\n");
        }
    }
    
    /*
        @METHOD SendText
        @OVERVIEW Metodo che implementa l'invio di un file di testo al browser.
    
        @PAR t File di testo da inviare
    
        @THROWS IOException nel caso la scrittura fallisca
    */
    public void SendText(File t) throws IOException
    {
      System.out.println("RICHIESTO INVIO DI UN FILE DI TESTO: "+t.getName());
     
      date = new Date();
      
      wr.write("HTTP/1.1 201 CREATED\r\n");
      wr.write("Date: "+date);

      FileReader Content = new FileReader(t);
      
        try (Scanner textscan = new Scanner(Content)) 
        {
            String ts;
            
            wr.write("Content-type: text/plain\r\n");
            wr.write("Content-length:"+file.length()+"\r\n");
            wr.write("\r\n");

            while(textscan.hasNext())
            {
                ts = textscan.nextLine();
                wr.write(ts);
            }
        }  
    }
    
    /*
        @METHOD SendFile
        @OVERVIEW Metodo che implementa l'invio di un file immagine in formato
        jpeg o gif al browser.
    
        @PAR f File da inviare
        
        @THROWS FileNotFoundException nel caso il file non venga trovato
        @THROWS IOException in caso la scrittura fallisca.
    */
    public void SendFile(File f) throws FileNotFoundException, IOException
    {
        System.out.println("RICHIESTO INVIO DI UN'IMMAGINE: "+f.getName()); 
                       
        date = new Date(); 
        
        /*
            PER OTTENERE RICHIESTE VALIDE, OGNI STRINGA DELLA RISPOSTA
            HTTP VIENE CONVERTITA IN ARRAY DI BYTE ED INVIATA AL BROWSER
            MEDIANTE IL CANALE DI TRASFERIMENTO BUFFERIZZAO DEI DATI.
        */
        String t = "HTTP/1.1 201 CREATED\r\n";
        byte[] b = t.getBytes();
        dt.write(b);

        t = "HTTP/1.1 201 CREATED\r\n";
        b = t.getBytes();
        dt.write(b);

        t = ("Date: "+date);
        b = t.getBytes();
        dt.write(b);

        t = "Content-type: image/jpeg\r\n";
        b = t.getBytes();
        dt.write(b);

        t = "Content-length:"+file.length()+"\r\n";
        b = t.getBytes();
        dt.write(b);

        t = "\r\n";
        b = t.getBytes();
        dt.write(b);
        
        FileInputStream Fstream = new FileInputStream(f);
        
        byte[] data = new byte[(int)f.length()];
        
        int x;
        
        while((x = Fstream.read(data)) >= 0)
        {
            dt.write(data,0,x);
        }
    }
}
