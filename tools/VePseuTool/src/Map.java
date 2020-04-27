import java.awt.Point;
import java.util.ArrayList;

public class Map
{
    private byte[][] map = null;
    private ArrayList<Scene> scenes = null;
    
    private String mapName = null;
    public static final byte MAPWIDTH = 16;
    public static final byte MAPHEIGHT = 16;
    
    private byte playerX = 0;
    private byte playerY = 0;
    private byte playerA = 0;
    
    public Map()
    {
        map = new byte[MAPWIDTH][MAPHEIGHT];
        scenes = new ArrayList<Scene>();
    }
    
    public void addScene(int αx, int αy)
    {
        scenes.add(new Scene(αx, αy));
    }
    
    public ArrayList<Scene> getScenes()
    {
        return scenes;
    }
    
    public Scene getScene(int αx, int αy)
    {
        for(Scene scene : scenes)
            if(scene.getSceneX() == αx && scene.getSceneY() == αy) return scene;
        
        return null;
    }
    
    public void removeScene(int αx, int αy)
    {
        for(Scene scene : scenes)
            if(scene.getSceneX() == αx && scene.getSceneY() == αy)
            {
                scenes.remove(scene);
                return;
            }
    }
    
    public void placeWall(int αx, int αy, byte αtype)
    {
        if(αx < 16 && αx > -1 && αy < 16  && αy > -1)
            map[αy][αx] = (byte)(αtype + 0x80);
    }
    
    public void removeWall(int αx, int αy)
    {
        if(αx < 16 && αx > -1 && αy < 16  && αy > -1)
            map[αy][αx] = 0x00;
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
    
    public byte getWall(int αx, int αy)
    {
        return (byte)(map[αy][αx] - 0x80);
    }
    
    public boolean isWall(int αx, int αy)
    {
        return map[αy][αx] != 0;
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
        ArrayList<AsmData> concat = new ArrayList<AsmData>();
        int fileLength = 0;
        int sceneCnt = scenes.size();
        String sceneName = mapName + 'S';
        
        for(int ι = 0; ι < sceneCnt; ι++)
        {
            AsmData sceneData = scenes.get(ι).getData
            (
                sceneName + ι,
                ι + 1 < sceneCnt ? sceneName + (ι + 1) : "NULL"
            );
            fileLength += sceneData.getAsmData().length;
            concat.add(sceneData);
        }
        
        mapFile.setName(mapName);
        mapFile.addRef(sceneCnt > 0 ? sceneName + '0' : "NULL");
        mapFile.addByte(playerX);
        mapFile.addByte(playerY);
        mapFile.addByte(playerA);
        
        for(byte mapByte : get1dMap())
        {
            mapFile.addByte(mapByte);
        }
        
        fileLength += mapFile.getAsmData().length;
        concat.add(mapFile);
        
        byte[] mapData = new byte[fileLength];
        for(int ι = 0; ι < fileLength; ι++)
        {
            for(AsmData data : concat)
            {
                for(byte unit : data.getAsmData())
                {
                    mapData[ι++] = unit;
                }
            }
        }
        
        GUI.getWorkspace().setNotification("Saved map " + mapName + "!");
        
        return mapData;
    }
    
    public void loadMap(byte[] inFile) throws Exception
    {
        ArrayList<AsmData> sections = new ArrayList<AsmData>();
        ArrayList<Byte> section = new ArrayList<Byte>();
        String cmpstr = new String();
        
        for(byte unit : inFile)
        {
            switch(unit)
            {
                case('E'): case('V'): case('N'):
                    cmpstr += (char)unit;
                    break;
                default:
                    cmpstr = new String();
                    break;
            }
            
            section.add(unit);
            
            if(cmpstr.length() == 4)
            {
                if(cmpstr.equals("EVEN") && section.size() > 10)
                {
                    
                    byte[] sectionData = new byte[section.size()];
                    int ι = 0;
                    for(Byte sectionUnit : section)
                    {
                        sectionData[ι++] = sectionUnit;
                    }
                    sections.add(new AsmData(sectionData));
                    section = new ArrayList<Byte>();
                }
                else cmpstr = new String();
            }
        }
        
        byte[] sectionData = new byte[section.size()];
        int ι = 0;
        for(Byte sectionUnit : section)
        {
            sectionData[ι++] = sectionUnit;
        }
        sections.add(new AsmData(sectionData));
        
        ι = 0;
        AsmData asmSection = null;
        while(ι < sections.size() - 1)
        {
            asmSection = sections.get(ι++);
            scenes.add(new Scene(asmSection));
        }
        
        AsmData mapFile = sections.get(sections.size() - 1);
        
        mapName = mapFile.getName();
        mapFile.getRef();
        playerX = mapFile.getByte();
        playerY = mapFile.getByte();
        playerA = mapFile.getByte();
        
        int x = 0;
        int y = 0;
        for(byte wall : mapFile.getData())
        {
            map[y][x] = wall;
            x ++;
            if(x == MAPWIDTH)
            {
                x = 0;
                y ++;
                if(y == MAPWIDTH) y--;
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
            if(map[κ][ι] != 0)
                points.add(new Point(ι, κ)); 
        
        return points;
    }
}
