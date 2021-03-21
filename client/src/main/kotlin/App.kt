import picocli.CommandLine
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@CommandLine.Command(
        name = "ez", version = ["0.1 Ultimate Deluxe (RES edition)" ],
        description = ["Calculatrice réseau"],
        mixinStandardHelpOptions = true,
        subcommands = [Client::class])
class Ez() : Callable<Int> {

    override fun call(): Int {
        CommandLine(Ez()).execute("-h")
        return 0
    }

}

// ne pas modifier
fun main(args: Array<String>) : Unit = exitProcess(CommandLine(Ez()).execute(*args))