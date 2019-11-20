/*ASSIGNMENT NUMERO 6 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : HTTPBasedFileTransfer

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un server HTTP che gestisce richieste di diverso tipo
provenienti da un browser web
*/
package httpbasedfiletransfer;

/*
    @CLASS URIParser
    @OVERVIEW Classe che implementa un parser delle stringhe rappresentanti
    le richieste HTTP di un browser finalizzato a trovare il path del file
    da trasferire
*/
public class URIParser 
{
    String Request;
    
    /*
        @METHOD URIParser
        @OVERVIEW Costruttore di un generico URIParser
    
        @PAR R Stringa rappresentante la richiesta da analizzare
    */
    public URIParser(String R)
    {
        Request = R;
    }
    
    /*
        @METHOD Parse
        @OVERVIEW Analisi di una richiesta di trasferimento HTTP (ovvero richiesta
        di tipo GET) finalizzata al reperimento di un file
    */
    public synchronized String Parse() throws NullPointerException
    {
            //RIMOZIONE DELLA SOTTOSTRINGA CONTENENTE IL METODO UTILIZZATO
            String[] URI = Request.split("GET /");
            
            Request = "";
            
            //IL RISULTATO E' LA STRINGA CONTENENTE URI DEL FILE E PROTOCOLLO UTILIZZATO
            for (String URI1 : URI) 
            {
                Request = Request.concat(URI1);
            }

            //RIMOZIONE DELLA SOTTOSTRINGA CONTENENTE IL PROTOCOLLO UTILIZZATO
            URI = Request.split("HTTP/",2);
            
            //STRINGA CONTENENTE LA RICHIESTA
            Request = URI[0];
 
            return Request;
    }
}
