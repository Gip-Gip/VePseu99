import java.awt.Point;
import java.util.ArrayList;

public class Map
{
    private byte[][] map = null;
    private ArrayList<Byte> lzMap = null;
    private ArrayList<ArrayList> lzDictionary = null;
    public static final byte LZWORD = 4;
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
    
    public ArrayList<Byte> getCompressedMap()
    {
        byte[] map1d = get1dMap();
        lzMap = new ArrayList<Byte>();
        lzDictionary = new ArrayList<ArrayList>();
        
        int ι = 0;
        
        ArrayList<Byte> lzWord = new ArrayList<Byte>();
        
        for(byte wall : map1d)
        {
            lzWord.add(wall);
            ι++;
            if(ι == LZWORD)
            {
                lzDictionary.add(lzWord);
                lzWord = new ArrayList<Byte>();
                ι = 0;
            }
        }
        
        System.out.println();
        
        ι = 0;
        
        while(ι < lzDictionary.size())
        {
            lzWord = lzDictionary.get(ι);
            int κ = 0;
            while((κ = lzDictionary.lastIndexOf(lzWord)) != ι)
                lzDictionary.remove(κ);
            ι ++;
        }
        
        lzMap.add((byte)lzDictionary.size());
        
        for(ArrayList<Byte> entry : lzDictionary)
            for(Byte entryByte : entry)
                lzMap.add(entryByte);
        
        ι = 0;
        
        lzWord = new ArrayList<Byte>();
        
        for(byte wall : map1d)
        {
            lzWord.add(wall);
            ι++;
            if(ι == LZWORD)
            {
                lzMap.add((byte)lzDictionary.indexOf(lzWord));
                ι = 0;
                lzWord = new ArrayList<Byte>();
            }
        }
        
        return lzMap;
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
        String mapFile = new String();
        
        mapFile += mapName + "\r\n    EVEN\r\n";
        mapFile +=
            "    BYTE " + playerX + ", " + playerY + ", " + playerA + "\r\n";
        
        ArrayList<Byte> compressedMapFile = getCompressedMap();
        
        mapFile += "    BYTE";
        
        int ι = 0;
        int κ = 1;
        
        for(Byte mapByte : compressedMapFile)
        {
            mapFile += " " + mapByte + ",";
            ι ++;
            if(ι == MAPWIDTH && κ != MAPHEIGHT)
            {
                mapFile = mapFile.substring(0, mapFile.length() - 1);
                mapFile += "\r\n    BYTE";
                ι = 0;
                κ ++;
            }
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
        
        String playerXStr = new String();
        String playerYStr = new String();
        String playerAStr = new String();
        String dictLengthStr = new String();
        int dictLength = 0;
        lzDictionary = new ArrayList<ArrayList>();
        ArrayList<Byte> lzWord = new ArrayList<Byte>();
        
        // Load player X
        while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
            ι++;
        while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
            playerXStr += (char)inFile[ι++];
        
        if(ι < inFile.length)
            playerX = Byte.parseByte(playerXStr);
        
        // Load player Y
        while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
            ι++;
        while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
            playerYStr += (char)inFile[ι++];
        
        if(ι < inFile.length)
            playerY = Byte.parseByte(playerYStr);
        
        // Load player A
        while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
            ι++;
        while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
            playerAStr += (char)inFile[ι++];
        
        if(ι < inFile.length)
            playerA = Byte.parseByte(playerAStr);
        
        // Load dictionary length
        while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
            ι++;
        while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
            dictLengthStr += (char)inFile[ι++];
        
        if(ι < inFile.length)
            dictLength = Byte.parseByte(dictLengthStr);

        // Load dictionary
        int κ = 0;
        int λ = 0;
        String wordStr = new String();
        while(κ < dictLength)
        {
            while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
                ι++;
            while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
                wordStr += (char)inFile[ι++];
            
            if(ι < inFile.length)
                lzWord.add(Byte.parseByte(wordStr));
            
            wordStr = new String();
            
            λ ++;
            
            if(λ  == LZWORD)
            {
                lzDictionary.add(lzWord);
                lzWord = new ArrayList<Byte>();
                κ ++;
                λ = 0;
            }
        }
        
        // Load map
        int x = 0;
        int y = 0;
        κ = 0;
        wordStr = new String();
        
        while(y < MAPHEIGHT)
        {
            while(x < MAPWIDTH)
            {
                while(ι < inFile.length && !Character.isDigit((char)inFile[ι]))
                    ι++;
                while(ι < inFile.length && Character.isDigit((char)inFile[ι]))
                    wordStr += (char)inFile[ι++];
                
                if(ι < inFile.length)
                {
                    κ = Byte.parseByte(wordStr);
                    for(Byte wall : (ArrayList<Byte>)lzDictionary.get(κ))
                    {
                        map[y][x] = wall;
                        x ++;
                    }
                    wordStr = new String();
                }
                
                
            }
            
            x = 0;
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
