fun main(){
    println("=== ZADATAK 1 ===")

    val name = "Bozana"
    val lastName = "Sumic"

    var email:String? = null

    var age: Int? = 20

    println("\nDuljina emaila: ${email?.length}")

    email = "test.random@gmail.com"

    println("Update-an email: ${email.length}")


    println("\n=== ZADATAK 2 ===")

    var code = 4
    var price = 2
    var money = 3.0

    val pice = when(code){
        1 -> "voda"
        2 -> "cola"
        3 -> "kava"
        4 -> "mlijeko"
        else -> "Nepoznato pice"
    }

    if(money>=price){
        println("\nToci se $pice. Ostatak: ${money-price}")
    }else{
        println("Nedostaje još ${price-money}")
    }

    println("\n=== ZADATAK 3 ===")

    val koraci = listOf(4500, 12000, 8000, 15000, 3000, 11000, 9500)

    var sum = 0
    for(k in koraci){
        sum+=k
    }

    println("\nUkupan broj koraka u tjednu: $sum")

    var i=0

    while (i<koraci.size){
        if(koraci[i] > 10000){
            println("Prvi dan s više od 10k koraka: dan ${i + 1}")
            break
        }
        i++
    }

    println("\n=== ZADATAK 4 ===")

    print("Unesite korisničko ime: ")
    val input = readln()

    val formated = formatUsername(input)
    println("Obrađeni username: $formated")

    val valid = validateUsername(formated)
    println("Je li username valjan: $valid")


    println("\n=== ZADATAK 5 ===")

    val acc1 = BankAccount("HR001")
    val acc2 = BankAccount("HR002")

    acc1.deposit(100.0)
    acc1.withdraw(30.0)

    acc2.deposit(50.0)
    acc2.withdraw(100.0)

    println("Stanje acc1: ${acc1.balance}")
    println("Stanje acc2: ${acc2.balance}")

    println("Ukupno računa: ${BankAccount.totalAccounts}")
}

fun formatUsername(input: String): String{
    return input.trim().lowercase()
}

fun validateUsername(username: String): Boolean {
    if (username.isBlank()) return false
    if (username.length !in 5..15) return false
    if (!username[0].isLetter()) return false
    if (!username.all { it.isLetterOrDigit() || it == '_' }) return false
    if (username.contains(" ")) return false

    return true
}

object TransactionLogger {
    fun log(message: String) {
        println("LOG: $message")
    }
}

class BankAccount(val brojRacuna: String) {

    var balance: Double = 0.0

    init {
        totalAccounts++
        println("Kreiran račun: $brojRacuna")
    }

    fun deposit(amount: Double) {
        if (amount > 0) {
            balance += amount
            TransactionLogger.log("Uplata $amount na račun $brojRacuna")
        }
    }

    fun withdraw(amount: Double) {
        if (amount > balance) {
            TransactionLogger.log("Neuspješna isplata $amount s računa $brojRacuna")
        } else {
            balance -= amount
            TransactionLogger.log("Isplata $amount s računa $brojRacuna")
        }
    }

    companion object {
        var totalAccounts = 0
    }
}