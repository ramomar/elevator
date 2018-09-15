# Elevator


### Challenge

Design and implement an elevator control system.

### Scheduling algorithm

My approach is a really simple one:
- The first person on the elevator who presses the floor button first is the one who sets the direction.
- We give priority to the stops that are already in the direction.
- When a person outside wants to use the elevator we try to find the closest elevator to the pickup floor according to the goal floor.

#### Example

- Two persons enter an standby elevator:
  - The first person that presses the button sets the direction. Example: both persons are on the third floor.
    - Person A presses the button 1
    - Person B presses the button 4
    - Result: Elevator first goes to floor 1, then to floor 4.

- Elevator already has people:
  - Case 1:
    - Goal of elevator is floor 6.
    - Person A on floor 2 enters and presses button 3.
    - Elevator stops on floor 3 and continues to floor 6.
  - Case 2.1:
    - Goal of elevator is floor 6. 
    - Person A enters on floor 2 enters and presses button 3.
    - Person B enters on floor 3 and presses button 1.
    - Person C enters on floor 3 and presses button 4.
    - Elevator goes to floor 3, then goes to floor 4, then goes to floor 6 and finally goes to floor 1.
  - Case 2.2:
    - Goal of elevator is floor 1.
    - Person A enters on floor 5 and presses button 4.
    - Person B enters on floor 4 and presses button 5.
    - Elevator goes to floor 4, then to floor 1, then to floor 5.
    
- Person is in floor 3. Many elevators.
  - Elevator A has as goal to reach floor 0 and is currently on floor 2. 
  - Elevator B has as goal to reach floor 1 and is currently on floor 4.
  - Algorithm selects elevator B.
  - Explanation: [we pick the elevator that minimizes distance between pickup floor and elevator goal floor.](https://github.com/ramomar/elevator/blob/master/src/main/scala/ElevatorSystem.scala#L73)
    

### How it is implemented?

| Entity             | Description | API  |
|--------------------|-------------| ---- |
| [Elevator](https://github.com/ramomar/elevator/blob/master/src/main/scala/Elevator.scala)           | A class that represents an elevator. It holds the elevator id, and current state such as current floor, current goal floor, requests queue, and current direction. | `step()`, `addStop()`, `state()` |
| [ElevatorStopsQueue](https://github.com/ramomar/elevator/blob/master/src/main/scala/ElevatorStopsQueue.scala) | A special queue that gives priority to stops already in the direction. It is backed by two priority queues. [It works as follows:  1) Add a stop to the queue: if the elevator is ascending then add to the stop to the ascending queue, else add the stop to the descending queue. 2) Consuming the queue: if the elevator is already going upwards, then dequeue from the asceding queue, else dequeue from the descending queue.](https://github.com/ramomar/elevator/blob/master/src/main/scala/ElevatorStopsQueue.scala) | `addStop()`, `nextStop()`, `isEmpty()` |
| [ElevatorSystem](https://github.com/ramomar/elevator/blob/master/src/main/scala/ElevatorSystem.scala)     | This class controls and holds all the elevators. `bestElevatorToScheduleForPickup()`, is used to request an elevator for any floor. It tries to find the elevator that is closest to the pickup floor.  | `elevatorState()`, `elevatorsState()`, `addStopToElevator()`, `bestElevatorToScheduleForPickup()`, `step()` |

### Tests
In order to run tests run `sbt test`.

#### Included tests
- [ElevatorStopsQueueSpec](https://github.com/ramomar/elevator/blob/master/src/test/scala/ElevatorStopsQueueSpec.scala)
- [ElevatorSpec](https://github.com/ramomar/elevator/blob/master/src/test/scala/ElevatorSpec.scala)
- [ElevatorSystemSpec](https://github.com/ramomar/elevator/blob/master/src/test/scala/ElevatorSystemSpec.scala)
