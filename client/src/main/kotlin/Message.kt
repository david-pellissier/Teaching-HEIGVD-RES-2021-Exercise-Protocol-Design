
enum class Type {
    AVAILABLE, RESULT, ERROR, TERMINATED, // server
    CALC, QUIT, //client
    NONE
}

const val SEPARATOR = ';'

fun getType(data: String): Type {
    val typeInt = data.substringBefore(SEPARATOR).toInt()

    // Remarque : Pas trouvé d'autre moyen de récupérer un Type depuis un Int
    var type = Type.NONE
    for(t in Type.values()) {
        if (typeInt == t.ordinal){
            type = t
            break
        }
    }

    return type
}

fun getValue(data: String): String {
    val value = data.substringAfter(SEPARATOR)

    return value
}

fun prepareReq(type: Type, data: String) : ByteArray {
    val req = "${type.ordinal}$SEPARATOR$data"

    return req.toByteArray()

}