import org.scalatest.{FlatSpec, Matchers}

final class ElevatorSpec extends FlatSpec with Matchers {

  behavior of s"${classOf[Elevator]}"

  it should "give priority to all the requests that are in the current direction" in {
    // Objective: get to the 4th (3) floor and the go back to the second and first floors.

    val elevator: Elevator = new Elevator(id = 0, initialFloor = 0)

    elevator.addStop(floor = 3)
    elevator.addStop(floor = 1)

    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 0
        currentGoalFloor shouldBe Some(1)
        currentDirection shouldBe Some(Upwards)
    }

    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 1
        currentGoalFloor shouldBe Some(3)
        currentDirection shouldBe Some(Upwards)
    }

    // We add 2 stops.
    elevator.addStop(floor = 0)
    elevator.addStop(floor = 1)

    // We are in the third floor (2). Next we want to go to the second floor (1).
    // Note that the elevator gave priority to the stops already in the direction.
    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 2
        currentGoalFloor shouldBe Some(3)
        currentDirection shouldBe Some(Upwards)
    }

    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 3
        currentGoalFloor shouldBe Some(1)
        currentDirection shouldBe Some(Downwards)
    }

    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 2
        currentGoalFloor shouldBe Some(1)
        currentDirection shouldBe Some(Downwards)
    }

    // We are in the first floor. Next we want to go to the first floor (0).
    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 1
        currentGoalFloor shouldBe Some(0)
        currentDirection shouldBe Some(Downwards)
    }

    // First floor.
    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 0
        currentGoalFloor shouldBe None
        currentDirection shouldBe None
    }
  }

  it should "use first come first serve when elevator is in standby" in {
    val elevator: Elevator = new Elevator(id = 0, initialFloor = 3)

    elevator.state match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 3
        currentGoalFloor shouldBe None
        currentDirection shouldBe None
    }

    // Add two stops.
    elevator.addStop(floor = 2)

    elevator.addStop(floor = 4)

    // Note that we gave priority to the first stop added.
    elevator.step() match {
      case ElevatorState(currentFloor, currentGoalFloor, currentDirection) =>
        currentFloor shouldBe 3
        currentGoalFloor shouldBe Some(2)
        currentDirection shouldBe Some(Downwards)
    }
  }
}
