import org.scalatest.{FlatSpec, Matchers}

class ElevatorSystemSpec extends FlatSpec with Matchers {
  behavior of s"${classOf[ElevatorSystem]}"

  it should "give the best elevator after a pickup request" in {
    val system = new ElevatorSystemController(elevatorCount = 20)

    val elevatorA = 1

    val elevatorB = 2

    system.addStopToElevator(elevator = elevatorA, floor = 3)

    system.addStopToElevator(elevator = elevatorB, floor = 4)

    // Need to do a first step in order to "fetch" the goal floor.
    system.step()

    system.step()

    system.step()

    system.step()

    system.elevatorState(elevator = elevatorA) shouldBe ElevatorState(3, None, None)

    system.elevatorState(elevator = elevatorB) shouldBe ElevatorState(3, Some(4), Some(Upwards))

    system.step()

    system.elevatorState(elevator = elevatorB) shouldBe ElevatorState(4, None, None)

    // Elevator is in floor 3
    system.bestElevatorToScheduleForPickup(currentFloor = 2) shouldBe elevatorA

    // Elevator is in floor 4
    system.bestElevatorToScheduleForPickup(currentFloor = 6) shouldBe elevatorB
  }
}
