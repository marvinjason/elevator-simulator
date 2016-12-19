
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Floor
{
    private int x1;
    private int x2;
    private int x3;
    private int x4;
    private int y;
    private int l;
    private int floor;
    private List<Passenger> passengers;
    private List<Passenger> departing;
    
    public Floor(int x1, int x2, int x3, int x4, int y, int l, int floor)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y = y;
        this.l = l;
        this.floor = floor;
        this.passengers = Collections.synchronizedList(new ArrayList());
        this.departing = Collections.synchronizedList(new ArrayList());
    }
    
    public void draw(Graphics g)
    {
        g.drawLine(0, y, x1, y);
        g.drawLine(x2, y, x3, y);
        g.drawLine(x4, y, l, y);
        
        g.setColor(Color.LIGHT_GRAY);
        
        g.fillRect(222, y - 50, 8, 10);
        g.fillRect(372, y - 50, 8, 10);
        
        g.drawLine(x1, y, x2, y);
        g.drawLine(x3, y, x4, y);
        
        g.setColor(Color.BLACK);
        
        g.drawString("Floor " + (floor + 1), 30, y - 45);
        
        for (int i = 0, j = 400; i < passengers.size(); i++, j += 50)
        {
            passengers.get(i).setDestPosX(j);
            passengers.get(i).draw(g);
        }
        
        Iterator iterator = departing.iterator();
        
        while (iterator.hasNext())
        {
            Passenger temp = (Passenger) iterator.next();
            
            temp.setDestPosX(775);
            temp.setDirection(Passenger.Mode.RIGHT);
            temp.draw(g);
            
            if (temp.getX() == temp.getDestPosX())
            {
                iterator.remove();
            }
        }
    }
    
    public void addPassenger(Passenger passenger)
    {
        int k = (!passengers.isEmpty()) ? passengers.get(passengers.size() - 1).getX() + 750 : 750;
        
        passenger.setX(k);
        passengers.add(passenger);
    }
    
    public int getFloor()
    {
        return this.floor;
    }
    
    public List<Passenger> getPassengers()
    {
        return this.passengers;
    }
    
    public List<Passenger> getDeparting()
    {
        return this.departing;
    }
}