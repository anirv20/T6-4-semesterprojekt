package worldofzuul.domain;

import java.util.Set;
import java.util.HashMap;


public class Room 
{
    private String description; // om stedet
    private String info;// om stedert
    private String name;// hvad hedder den stedet
    private HashMap<String, Room> exits;

    public Room(String description)
    {
        this.description = description;
        this.exits = new HashMap<String, Room>();
    }
    public Room(String description, String name)
    {
        this.description = description;
        this.name = name;
        this.exits = new HashMap<String, Room>();
    }

    public void setExit(String direction, Room neighbor) 
    {
        this.exits.put(direction, neighbor);
    }

    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return name;
    }
}

