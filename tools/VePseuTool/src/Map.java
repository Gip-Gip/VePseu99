import java.awt.Point;
import java.util.ArrayList;

public class Map
{
    private byte[][] map = null;
    
    private String mapName = null;
    public static final byte MAPWIDTH = 16;
    public static final byte MAPHEIGHT = 16;
    
    private byte playerX = 0;
    private byte playerY = 0;
    private byte playerA = 0;
    
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
        if(x < 16 && x > -1 && y < 16  && y > -1)
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
        mapName = αname.substring(0, Math.min(αname.length(), 6));
    }
    
    public byte getPlayerX()
    {
        return playerX;
    }
    
    public byte getPlayerY()
    {
        return playerY;
    }
    
    public byte getPlayerAngle()
    {
        return playerA;
    }
    
    public void setPlayerCoords(int αx, int αy, int αangle)
    {
        // Make sure the player X and Y don't go over 15 or under 0
        playerX = (byte)Math.max(Math.min(αx, 15), 0);
        playerY = (byte)Math.max(Math.min(αy, 15), 0);
        // Make sure the player angle doesn't go over 3 or under 0
        playerA = (byte)Math.max(Math.min(αangle, 3), 0);
    }
    
    public byte[] makeMapFile()
    {
        AsmData mapFile = new AsmData();
        
        mapFile.setName(mapName);
        mapFile.addByte(playerX);
        mapFile.addByte(playerY);
        mapFile.addByte(playerA);
        
        LZ compressedMap = new LZ(get1dMap());
        
        for(byte mapByte : compressedMap.getCompressedData())
        {
            mapFile.addByte(mapByte);
        }
        
        GUI.getWorkspace().setNotification("Saved map " + mapName + "!");
        
        return mapFile.getAsmData();
    }
    
    public void loadMap(byte[] inFile) throws Exception
    {
        AsmData mapFile = new AsmData(inFile);
        mapName = mapFile.getName();
        playerX = mapFile.getByte();
        playerY = mapFile.getByte();
        playerA = mapFile.getByte();
        LZ compressedMap = new LZ(mapFile.getData(), true);
        
        
        int x = 0;
        int y = 0;
        for(byte wall : compressedMap.getUncompressedData())
        {
            map[y][x] = wall;
            x ++;
            if(x == MAPWIDTH)
            {
                x = 0;
                y ++;
            }
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
