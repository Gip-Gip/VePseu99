import java.awt.Point;
import java.util.ArrayList;

public class Map
{
    private byte[][] map = null;
    private String mapName = null;
    public static final byte MAPWIDTH = 16;
    public static final byte MAPHEIGHT = 16;
    
    public Map()
    {
        map = new byte[MAPWIDTH][MAPHEIGHT];
    }
    
    public void placeWall(int x, int y, byte type)
    {
        if(x < 16 && x > -1 && y < 16  && y > -1)
            map[y][x] = type;
    }
    
    public void removeWall(int x, int y)
    {
        if(x < 15 && x > -1 && y < 15  && y > -1)
            map[y][x] = 0x00;
    }
    
    public byte[] get1dMap()
    {
        byte[] bytes = new byte[MAPWIDTH * MAPHEIGHT];
        
        int ι = 0;
        
        for(byte[] row : map)
            for(byte wall : row)
                bytes[ι++] = wall;
        
        return bytes;
    }
    
    public String getMapName()
    {
        return mapName;
    }
    
    public void setMapName(String αname)
    {
        mapName = αname;
    }
    
    public byte[] makeMapFile()
    {
        String mapFile = new String();
        
        mapFile += mapName + "\r\n";
        
        for(byte[] row : map)
        {
            mapFile += "    BYTE";
            for(byte wall : row)
                mapFile += " " + wall + ",";
            // Remove trailing comma
            mapFile = mapFile.substring(0, mapFile.length() - 1);
            // Use windows newline
            mapFile += "\r\n";
        }
        
        
        
        GUI.getWorkspace().setNotification("Saved map " + mapName + "!");
        
        return mapFile.getBytes();
    }
    
    public void loadMap(byte[] inFile)
    {
        int ι = 0;
        mapName = new String();
        
        while(!Character.isWhitespace((char)inFile[ι]))
            mapName += (char)inFile[ι++];
        
        int y = 0;
        
        while(y < MAPHEIGHT &&  ι < inFile.length)
        {
            int x = 0;
            while(x < MAPWIDTH)
            {
                String wallValStr = new String();
                
                while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
                    ι++;
                while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
                    wallValStr += (char)inFile[ι++];
                
                if(ι < inFile.length)
                    placeWall(x, y, Byte.parseByte(wallValStr));
                x ++;
            }
            
            y ++;
        }
        
        GUI.getWorkspace().setNotification("Loaded map " + mapName + "!");
        
        GUI.getWorkspace().repaint();
    }
    
    public ArrayList<Point> getPoints()
    {
        ArrayList<Point> points = new ArrayList<Point>(); 
        
        for(int κ = 0; κ < MAPHEIGHT; κ++)
            for(int ι = 0; ι < MAPWIDTH; ι++)
            if(map[κ][ι] > 0)
                points.add(new Point(ι, κ)); 
        
        return points;
    }
}
