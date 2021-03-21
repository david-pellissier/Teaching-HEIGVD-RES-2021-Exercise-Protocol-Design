import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import picocli.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.concurrent.Callable

@CommandLine.Command(name = "connect", description = ["Se connecter à un serveur"], mixinStandardHelpOptions = true)
class Client() : Callable<Int> {

    private val BUFFER_SIZE = 256
    private val border = "\n-------------------------------------------------------------------------\n"
    private val menu = "Votre calcul (\"quit\" pour terminer la session):"

    @CommandLine.Parameters(index = "0", description = ["Adresse IP du serveur"])
    lateinit var ip: String

    @CommandLine.Option(names = ["-p", "--port"], description = ["Port distant"], paramLabel = "PORT", defaultValue = "1337")
    var port = 0

    private fun send(type: Type, data : String, outputStream: OutputStream){
        val req = prepareReq(type, data)

        outputStream.write(req)
        outputStream.flush()
    }


    private fun receive(instream: InputStream) : String{
        val responseBuffer = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var newBytes: Int
        while ((instream.read(buffer).also { newBytes = it }) != -1) {
            responseBuffer.write(buffer, 0, newBytes)
        }

        return responseBuffer.toString()
    }


    private fun init(instream: InputStream){

        // Attente du statut "AVAILABLE" du serveur
        while(true) {

            val response = receive(instream);
            if(getType(response) == Type.AVAILABLE){
                break
            }
        }
    }


    private fun closeConnection(instream: InputStream, outstream: OutputStream){
        // envoi de la requête
        send(Type.QUIT, "", outstream)

        // attente d'une réposne "Terminated"
        while(true){
            val response = receive(instream)
            if(getType(response) == Type.TERMINATED)
                break
        }
    }


    private fun communicate(instream:InputStream, outstream: OutputStream){
        while(true){
            print(border)
            println(menu)
            val input = readLine().toString()

            if(input == "quit" )
                break

            // envoi de la requête
            send(Type.CALC, input, outstream)
            val response = receive(instream)

            val responseCode = getType(response)
            val responseValue = getValue(response)

            if(responseCode == Type.RESULT){
                println("Réponse: $responseValue")
            }
            else if(responseCode == Type.ERROR){
                println("Erreur: $responseValue")
            }
            else {
                println("Erreur inconnue ($response")
                break;
            }

        }
    }


    override fun call(): Int {

        try {
            print("En attente de connexion à $ip:$port...")
            val socket = Socket(ip, port)
            val outstream = socket.getOutputStream()
            val instream = socket.getInputStream()
            println(" Connexion établie !")

            print("Attente du serveur...")
            init(instream)
            print("Le serveur est prêt !")

            // Communication avec le serveur
            communicate(instream, outstream)

            print("Fermeture de la session...")
            closeConnection(instream, outstream)
            println("Session terminée !")

            outstream.close()
            instream.close()
            socket.close()

        }
        catch(e: Exception){
            print("\nerreur: ${e.message}")
        }

        return 0
    }

}