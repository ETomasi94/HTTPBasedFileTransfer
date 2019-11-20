/*ASSIGNMENT NUMERO 6 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : HTTPBasedFileTransfer

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un server HTTP che gestisce richieste di diverso tipo
provenienti da un browser web
*/
package httpbasedfiletransfer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    @CLASS MainClass
    @OVERVIEW Classe che implementa il ciclo di vita principale del programma
*/
public class MainClass
{
    String hostname = "localhost";
    public static final int PORT = 6789;

    /*
    @METHOD main
    @OVERVIEW Metodo principale che implementa la creazione di un server che gestisce
    le richieste HTTP di un genrico browser tramite la porta logica PORT.
    */
    public static void main(String[] args) throws IOException 
    { 
        //Richieste gestite tramite Threadpool
        ExecutorService ReqHandle = null;
        
        try(ServerSocket NewConnection = new ServerSocket(PORT))
        {
            //Creazione di un threadpool per gestire le richieste
            ReqHandle = Executors.newFixedThreadPool(4);
            
            //Ciclo di vita della connessione
            while(true)
            {
                try
                {
                    Socket ClientConnection = NewConnection.accept();
                    
                    RequestHandler HandlerTask = new RequestHandler(ClientConnection);
                    
                    ReqHandle.execute(HandlerTask);
                }
                catch(IOException e)
                {
                    System.out.println("FALLIMENTO: Errore di IO lato client\n");
                    ReqHandle.shutdownNow();
                }
            }
        }
        catch(UnknownHostException e)
        {
            System.out.println("FALLIMENTO: Indirizzo host non valido\n");
            e.printStackTrace();
            ReqHandle.shutdownNow();
        }
        catch(IOException e)
        {
            System.out.println("FALLIMENTO: Errore di IO lato server\n");
            e.printStackTrace();
            ReqHandle.shutdownNow();
        }
        finally
        {
            if(ReqHandle != null)
            {
                //Fine della connessione
                ReqHandle.shutdown();
            }
        }
    }
}
