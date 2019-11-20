# HTTPBasedFileTransfer
A program that replies HTTP request given by a browser on Local Host

OVERVIEW : This program replies to the HTTP requests of a browser finding the file in the specified path and displaying it via browser
Request must be sent via Browser's search bar specifying the port number 6789 (EG: Http://localhost:6789/<File_Path>)

HOW-TO-USE IT: Open the program in a terminal and write the request into a browser, there are some examples uploaded in a folder, so you could begin by typing Http://localhost:6789/Examples/Ciao.txt (The textfile given in the example)

FILES: 
-MainClass.java : The main class implementing program's lifecycle.
-FileHandler.java : Class implementing file searching and response to browser sending.
-RequestHandler.java : Class implementing the creation of the connection necessary in order for the browser to communicate with this program
-URIParser.java : Class implementing the parsing of an HTTP request in order to extract the Uniform Resource Identifier of the desired file

WRITTEN BY : Enrico Tomasi ON DATE : 20/11/2019
