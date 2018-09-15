import scala.math.Ordering
import scala.collection.mutable

final class ElevatorStopsQueue(initialLastStop: Int) {
  private val ascendingStops: mutable.PriorityQueue[Int] =
    new mutable.PriorityQueue[Int]()(Ordering.fromLessThan[Int] { case (a, b) => a > b })

  private val descendingStops: mutable.PriorityQueue[Int] =
    new mutable.PriorityQueue[Int]()(Ordering.fromLessThan[Int] { case (a, b) => a < b })

  private var lastStop: Int = initialLastStop

  private var isGoingUpwards: Boolean = false

  def addStop(floor: Int): Unit = {
    if (floor > lastStop) {
      if (descendingStops.isEmpty) {
        isGoingUpwards = true
      }

      ascendingStops.enqueue(floor)
    }

    if (floor < lastStop) {
      if (ascendingStops.isEmpty) {
        isGoingUpwards = false
      }

      descendingStops.enqueue(floor)
    }
  }

  def nextStop(): Option[Int] = {
    var result: Option[Int] = None

    if (isGoingUpwards && ascendingStops.nonEmpty) {
      lastStop = ascendingStops.dequeue()
      result = Some(lastStop)
      if (ascendingStops.isEmpty) {
        isGoingUpwards = false
      }
    }

    if (!isGoingUpwards && descendingStops.nonEmpty) {
      lastStop = descendingStops.dequeue()
      result = Some(lastStop)
      if (descendingStops.isEmpty) {
        isGoingUpwards = true
      }
    }

    result
  }

  def isEmpty: Boolean = {
    ascendingStops.isEmpty && descendingStops.isEmpty
  }
}

