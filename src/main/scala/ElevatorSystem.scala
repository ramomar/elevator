import math.abs

trait ElevatorSystem {
  protected[this] val elevatorCount: Int

  def elevatorState(elevator: Int): ElevatorState

  def elevatorsState: Map[Int, ElevatorState]

  def addStopToElevator(elevator: Int, floor: Int): Unit

  def bestElevatorToScheduleForPickup(floor: Int): Int

  def step(): Unit
}

class ElevatorSystemController(protected val elevatorCount: Int) extends ElevatorSystem {
  private val elevators: Map[Int, Elevator] =
    Map((0 until elevatorCount).map(n => n -> new Elevator(id = n, initialFloor = 0)): _*)

  def elevatorState(elevator: Int): ElevatorState =
    elevators(elevator).state

  def elevatorsState: Map[Int, ElevatorState] =
    elevators.mapValues(_.state)

  // May be a good idea to return current stops for this in elevator in order to update an UI.
  def addStopToElevator(elevator: Int, floor: Int): Unit =
    elevators(elevator).addStop(floor)

  def bestElevatorToScheduleForPickup(currentFloor: Int): Int =
    findNearestElevator(currentFloor)

  def step(): Unit =
    elevators.values
      .foreach(_.step())

  /** Try to find nearest elevator to the current pickup floor.
    * We sort the elevators taking in account current goal floor, current floor, and pickup floor. Then we take the first elevator of the sorted sequence.
    * We have four cases when comparing two elevators.
    * Case 1: both elevators have a goal floor; pick the elevator that minimizes the distance from the pickup floor and the goal floor.
    * Case 2 and 3: one elevator has a goal floor while the other elevator has not; pick the closest elevator using these distances. e.g. abs(e1GoalFloor - pickupFloor) < abs(e2CurrentFloor - pickupFloor).
    * Case 4: both elevators in standby; pick the closest elevator according to its current floor.
    */
  private def findNearestElevator(pickupFloor: Int): Int = {
    elevators.values.toSeq
      .map(elevator => elevator.id -> elevator.state)
      .sortWith { case ((_, e1State), (_, e2State)) =>
        (e1State.currentGoalFloor, e2State.currentGoalFloor) match {
          case (Some(e1GoalFloor), Some(e2GoalFloor)) =>
            abs(e1GoalFloor - pickupFloor) < abs(e2GoalFloor - pickupFloor)
          case (Some(e1GoalFloor), None) =>
            abs(e1GoalFloor - pickupFloor) < abs(e2State.currentFloor - pickupFloor)
          case (None, Some(e2GoalFloor)) =>
            abs(e1State.currentFloor - pickupFloor) < abs(e2GoalFloor - pickupFloor)
          case (None, None) =>
            abs(e1State.currentFloor - pickupFloor) < abs(e2State.currentFloor - pickupFloor)
        }
      }.head._1
  }
}
