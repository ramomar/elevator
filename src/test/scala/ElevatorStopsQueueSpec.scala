import org.scalatest.{FlatSpec, Matchers}

final class ElevatorStopsQueueSpec extends FlatSpec with Matchers {
  behavior of s"${classOf[ElevatorStopsQueue]}"

  it should "correctly return the next floor when picking the elevator from the first floor" in {
    val queue = new ElevatorStopsQueue(initialLastStop = 0)

    queue.addStop(floor = 3)
    queue.nextStop() shouldBe Some(3)
    queue.addStop(floor = 4)
    queue.nextStop() shouldBe Some(4)
    queue.addStop(floor = 6)
    queue.nextStop() shouldBe Some(6)
    queue.addStop(floor = 3)
    queue.nextStop() shouldBe Some(3)
    queue.nextStop() shouldBe None
  }

  it should "correctly return the next floor the elevator from the 7th floor" in {
    val queue = new ElevatorStopsQueue(initialLastStop = 6)

    queue.addStop(floor = 5)
    queue.nextStop() shouldBe Some(5)
    queue.addStop(floor = 4)
    queue.nextStop() shouldBe Some(4)
    queue.addStop(floor = 1)
    queue.addStop(floor = 2)
    queue.nextStop() shouldBe Some(2)
    queue.nextStop() shouldBe Some(1)
  }

  it should "use first come first serve when queue is empty" in {
    val queue = new ElevatorStopsQueue(initialLastStop = 3)

    queue.addStop(floor = 2)
    queue.addStop(floor = 4)

    queue.nextStop() shouldBe Some(2)
    queue.nextStop() shouldBe Some(4)
  }
}
