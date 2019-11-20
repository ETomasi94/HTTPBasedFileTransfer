/*ASSIGNMENT NUMERO 6 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : HTTPBasedFileTransfer

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un server HTTP che gestisce richieste di diverso tipo
provenienti da un browser web
*/
package httpbasedfiletransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    @CLASS RequestHandler
    @OVERVIEW Classe che implementa un gestore di richieste HTTP che legge, analizza
    e prova a soddisfare le richieste del browser.
*/
public class RequestHandler implements Runnable
{
    Socket Client;
    URIParser Parser;
    
    /*
        @METHOD RequestHandler
        @OVERVIEW Costruttore di un generico RequestHandler
        @PAR connection Socket della connessione da cui provengono le richieste
        HTTP da dover gestire.
    */
    public RequestHandler(Socket connection)
    {
        this.Client = connection;
    }

    
    /*
        @METHOD run
        @OVERVIEW Ciclo di vita di un generico thread del Threadpool
    */
    @Override
    public synchronized void run() 
    {
        /*APERTURA DI DUE STREAM DI CARATTERI PER RICHIESTE HTTP E TESTO
        ED UNO STREAM DI BYTE PER I FILE*/
        try(BufferedReader rd = new BufferedReader(new InputStreamReader(this.Client.getInputStream()));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(this.Client.getOutputStream()));
            DataOutputStream dt = new DataOutputStream(this.Client.getOutputStream());)
        {   
            /*
              LETTURA E PARSING DELLE RICHIESTE PROVENIENTI DAL BROWSER
            */
            
            //LETTURA DELLA PRIMA LINEA DELLA RICHIESTA
            String request;

            while((request = rd.readLine()) != null)
            {
                if(request.contains("GET /"))
                {
                    break;
                }
            }
            
            if(request != null)
            {
                Parser = new URIParser(request);

                request = Parser.Parse();

                //COSTRUZIONE DEL FILEHANDLER PER LA RICHIESTA  
                FileHandler FH = new FileHandler(request,wr,dt);

                //ESECUZIONE DEL FILEHANDLER
                FH.Handle();
            }

            //SVUOTIAMO I BUFFER DI SCRITTURA E LETTURA
            wr.flush();
        } 
        catch (IOException ex) 
        {
        }        
        finally
        {
            try 
            {
                this.Client.close();
            } 
            catch (IOException ex) 
            {
                System.out.println("FALLIMENTO: Impossibile chiudere la connessione"
                        + "con il client in maniera corretta\n");
            }
        }
    }
}
