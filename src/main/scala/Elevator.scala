final class Elevator(val id: Int, initialFloor: Int) {

  private var currentFloor: Int = initialFloor

  private var currentGoalFloor: Option[Int] = None

  private val requestsQueue: ElevatorStopsQueue =
    new ElevatorStopsQueue(initialLastStop = initialFloor)

  def step(): ElevatorState = {
    currentGoalFloor match {
      case Some(goalFloor) =>
        if (currentFloor < goalFloor) {
          currentFloor += 1
        }
        else {
          currentFloor -= 1
        }

        if (currentFloor == goalFloor) {
          currentGoalFloor = requestsQueue.nextStop()
        }
        state
      case None =>
        currentGoalFloor = requestsQueue.nextStop()
        state
    }
  }

  def addStop(floor: Int): Unit = {
    requestsQueue.addStop(floor)
  }

  def state: ElevatorState = {
    val currentDirection: Option[ElevatorDirection] = currentGoalFloor.map { goalFloor =>
      if (goalFloor > currentFloor) {
        Upwards
      } else {
        Downwards
      }
    }

    ElevatorState(
      currentFloor = currentFloor,
      currentGoalFloor = currentGoalFloor,
      currentDirection = currentDirection
    )
  }
}

final case class ElevatorState(currentFloor: Int,
                               currentGoalFloor: Option[Int],
                               currentDirection: Option[ElevatorDirection])

sealed trait ElevatorDirection {
}

case object Upwards extends ElevatorDirection
case object Downwards extends ElevatorDirection