import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import picocli.*
import java.lang.Exception
import java.util.concurrent.Callable



@CommandLine.Command(name = "connect", description = ["Se connecter à un serveur"], mixinStandardHelpOptions = true)
class Client() : Callable<Int> {

    @CommandLine.Parameters(index = "0", description = ["Adresse IP du serveur"])
    lateinit var ip: String

    @CommandLine.Option(names = ["-p", "--port"], description = ["Port (défaut = 1337)"], paramLabel = "PORT")
    var port: Int = 1337

    override fun call(): Int {

        try {
            val socket = Socket(ip, port)
            val outsream = socket.getOutputStream()
            val instream = socket.getInputStream()



        }
        catch(e: Exception){
            print(e.message)
        }




        return 0
    }

}

fun main(args: Array<String>){
    val client = Client()
    CommandLine(client).parseArgs(*args)
}