interface Repairable {
    fun repairCar(car: RaceCar): Boolean
}

data class RaceCar(
    val model: String,
    var fuelLevel: Int,
    var isDamaged: Boolean
)

open class Department(
    val name: String,
    val responsibility: String
) {
    protected val maxBudget = 1_000_000
}

abstract class TeamMember(
    val name: String,
    var salary: Double,
    val department: Department
) {
    abstract fun doWork(): String
}

class Driver(
    name: String,
    salary: Double,
    department: Department,
    val carNumber: Int,
    var nickname: String? = null
) : TeamMember(name, salary, department) {

    lateinit var raceEngineer: Engineer

    override fun doWork(): String {
        return "$name is driving car number $carNumber."
    }

    fun introduce(): String {
        return if (nickname != null) {
            "$name is also known as $nickname."
        } else {
            "$name has no nickname."
        }
    }
}

class Engineer(
    name: String,
    salary: Double,
    department: Department,
    private val specialty: String
) : TeamMember(name, salary, department), Repairable {

    override fun doWork(): String {
        return "$name is working as a $specialty engineer."
    }

    override fun repairCar(car: RaceCar): Boolean {
        return if (car.isDamaged) {
            car.isDamaged = false
            true
        } else {
            false
        }
    }
}

class FerrariTeam(
    val teamName: String,
    val teamPrincipal: String
) {
    private val members = mutableListOf<TeamMember>()
    private val cars = mutableListOf<RaceCar>()

    fun addMember(member: TeamMember) {
        members.add(member)
    }

    fun addCar(car: RaceCar) {
        cars.add(car)
    }

    fun showAllMembers() {
        for (member in members) {
            println(member.doWork())
        }
    }

    fun prepareForRace(engineer: Engineer, car: RaceCar): Boolean {
        if (car.fuelLevel < 20) {
            car.fuelLevel = 100
        }

        if (car.isDamaged) {
            return engineer.repairCar(car)
        }

        return true
    }

    fun showDrivers(action: (Driver) -> Unit) {
        for (member in members) {
            if (member is Driver) {
                action(member)
            }
        }
    }
}

fun main() {
    val raceDept = Department("Race Department", "Race performance")
    val techDept = Department("Technical Department", "Car repairs")

    val leclerc = Driver("Charles Leclerc", 12000000.0, raceDept, 16, "Il Predestinato")
    val hamilton = Driver("Lewis Hamilton", 15000000.0, raceDept, 44, null)

    val engineer = Engineer("Riccardo", 250000.0, techDept, "race")

    leclerc.raceEngineer = engineer

    val ferrariCar = RaceCar("SF-25", 10, true)

    val ferrari = FerrariTeam("Scuderia Ferrari", "Fred Vasseur")

    ferrari.addMember(leclerc)
    ferrari.addMember(hamilton)
    ferrari.addMember(engineer)
    ferrari.addCar(ferrariCar)

    println("=== TEAM MEMBERS ===")
    ferrari.showAllMembers()

    println()
    println(leclerc.introduce())
    println(hamilton.introduce())

    println()
    val ready = ferrari.prepareForRace(engineer, ferrariCar)
    println("Car ready for race: $ready")

    println()
    ferrari.showDrivers { driver ->
        println(driver.name)
    }
}