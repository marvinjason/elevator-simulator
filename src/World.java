
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class World extends JPanel
{
    private Timer timer;
    private boolean isRunning;
    private int counter;
    private int timeElapsedInSecs;
    
    private Elevator elevator1;
    private Elevator elevator2;
    private List<Floor> floors;
    
    private Random random;
    
    public World(int refreshRate)
    {
        this.isRunning = false;
        this.counter = 0;
        this.timeElapsedInSecs = 0;
        
        elevator1 = new Elevator(100, 400);
        elevator2 = new Elevator(250, 400);

        floors = Collections.synchronizedList(new ArrayList());
        
        for (int i = 0; i < 5; i++)
        {
            floors.add(new Floor(100, 200, 250, 350, 100 * (5 - i), 750, i));
        }
        
        random = new Random();
        
        timer = new Timer(refreshRate, (e) -> {
            
            counter += refreshRate;
            
            if (counter / 1000 == 1)
            {
                counter = 0;
                ++timeElapsedInSecs;
                
                if (timeElapsedInSecs % 5 == 0)
                {
                    spawn();
                }
            }
            
            if (isRunning)
            {
                repaint();
            }
        });
        
        timer.start();
    }
    
    public World()
    {
        this(10);
    }
    
    public void start()
    {
        if (!isRunning)
        {
            isRunning = true;
        }
    }
    
    public void stop()
    {
        if (isRunning)
        {
            isRunning = false;
        }
    }
    
    public void spawn()
    {
        int numOfPassenger = random.nextInt(3) + 1;

        for (int i = 0; i < numOfPassenger; i++)
        {
            int srcFloor = random.nextInt(5);
            int destFloor;

            do
            {
                destFloor = random.nextInt(5);
            }
            while (destFloor == srcFloor);

            int weight = random.nextInt(120) + 60;

            floors.get(srcFloor).addPassenger(new Passenger(0, 100 * (5 - srcFloor) - 50, weight, srcFloor, destFloor));
        }
    }
    
    public void maneuver(Elevator elevator)
    {
        // maneuever() is being called every paint session.
        if (elevator.test1)
        {
            System.out.println("init elev");
            for (Floor floor : floors)
            {
                if (floor.getPassengers().isEmpty())
                {
                    continue;
                }
                
                if (floor.getPassengers().get(0).getX() == floor.getPassengers().get(0).getDestPosX())
                {
                    elevator.setMode(Elevator.Mode.UP);
                    elevator.test1 = false;
                    elevator.test2 = true;
                    
                    break;
                }
            }
        }
        
        if (elevator.test2)
        {
            System.out.println("open elev");
            if (elevator.getY() % 100 == 0)
            {
                boolean alightCheck = false;
                
                for (Passenger passenger : elevator.getPassengers())
                {
                    if (passenger.getDestinationFloor() == elevator.getFloor())
                    {
                        alightCheck = true;
                    }
                }
                
                if (floors.get(elevator.getFloor()).getPassengers().isEmpty() && !alightCheck)
                {
                    elevator.setMode(elevator.getDirection() == Elevator.Mode.UP ? Elevator.Mode.UP : Elevator.Mode.DOWN);
                }
                else
                {
                    elevator.setMode(Elevator.Mode.OPEN);
                    elevator.test2 = false;
                    elevator.test3 = true;
                }
            }
        }
        
        if (elevator.isOpen() && elevator.test3)
        {
            System.out.println("alight & board");
            
            elevator.alight(floors.get(elevator.getFloor()).getDeparting());
            elevator.board(floors.get(elevator.getFloor()).getPassengers());

            if (!elevator.getPassengers().isEmpty())
            {
                elevator.test3 = false;
                elevator.test4 = true;
            }
            else
            {
                elevator.test3 = false;
                elevator.test7 = true;
            }
        }
        
        if (elevator.test7)
        {
            elevator.setMode(Elevator.Mode.CLOSE);
            
            if (elevator.isClose())
            {
                elevator.test7 = false;
                elevator.test1 = true;
            }
        }
        
        if (elevator.test4)
        {
            System.out.println("check if boarders positioned");
            List<Passenger> temp = elevator.getPassengers();
            
            if (!temp.isEmpty())
            {
                if (temp.get(temp.size() - 1).getX() == temp.get(temp.size() - 1).getDestPosX())
                {
                    elevator.setMode(Elevator.Mode.CLOSE);
                    elevator.test4 = false;
                    elevator.test5 = true;
                }
                else //here
                {
                    Iterator i = temp.iterator();
                    
                    while (i.hasNext())
                    {
                        Passenger p = (Passenger) i.next();
                        
                        if (p.getX() < elevator.getX())
                        {
                            i.remove();
                        }   
                    }
                }
            }
            else
            {
                elevator.test4 = false;
                elevator.test7 = true;
            }
        }
        
        if (elevator.isClose() && elevator.test5)
        {
            System.out.println("closed & re-init");

            elevator.setMode(elevator.getDirection() == Elevator.Mode.UP ? Elevator.Mode.UP : Elevator.Mode.DOWN);
            
            elevator.test5 = false;
            elevator.test6 = true;
        }
        
        if (elevator.test6)
        {
            System.out.println("transition to cond 2");
            elevator.test6 = false;
            elevator.test2 = true;
            System.out.println("working fine til here");
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        
        for (Floor floor : floors)
        {
            floor.draw(g);
        }
        
        elevator1.draw(g);
        elevator2.draw(g);
        
        maneuver(elevator1);
        maneuver(elevator2);
    }
}
