import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WallStyle
{
    private ArrayList<ArrayList<Boolean>> textures = null;
    private ArrayList<Boolean> texture = null;
    private ArrayList<Byte> pallet = null;
    private TiColors tiColors = null;
    private Boolean[] defTexture =
    {
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true,
        true, true, true, true, true, true, true, true
    };
    
    public static final int TEXWIDTH = 8;
    public static final int TEXHEIGHT = 8;
    public static final int TEXCNT = 8;
    public static final int COLORCNT = 16;
    
    public WallStyle()
    {
        textures = new ArrayList<ArrayList<Boolean>>();
        pallet = new ArrayList<Byte>();
        
        int ι = 0;
        
        while(ι++ < TEXCNT)
        {
            texture = new ArrayList<Boolean>();
            
            for(boolean pixel : defTexture)
            {
                texture.add(pixel);
            }
            
            textures.add(texture);
        }
        
        ι = 0;
        
        while(ι++ < COLORCNT)
        {
            pallet.add((byte)0xF1);
        }
    }
    
    public WallStyle(byte[] data)
    {
        textures = new ArrayList<ArrayList<Boolean>>();
        pallet = new ArrayList<Byte>();
        
        int ι = 0;
        while(ι < COLORCNT)
        {
            pallet.add(data[ι++]);
        }
        
        int κ = 0;
        int x = 0;
        int y = 0;
        
        while(κ < TEXCNT)
        {
            ArrayList<Boolean> texture = new ArrayList<Boolean>();
            while(y < TEXHEIGHT)
            {
                while(x < TEXWIDTH)
                {
                    texture.add((data[ι] & 0b10000000) > 0);
                    data[ι] <<= 1;
                    x ++;
                }
                
                ι ++;
                x = 0;
                y ++;
            }
            κ ++;
            y = 0;
            textures.add(texture);
        }
        
    }
    
    public WallStyle(BufferedImage αimage)
    {
        textures = new ArrayList<ArrayList<Boolean>>();
        tiColors = new TiColors();
        pallet = new ArrayList<Byte>();
        
        int ι = 0;
        int κ = 0;
        int x = 0;
        int y = 0;
        while(ι++ < TEXCNT)
        {
            texture = new ArrayList<Boolean>();
            
            while(κ < TEXHEIGHT)
            {
                while(x < TEXWIDTH)
                {
                    // Remove alpha
                    int pixel = αimage.getRGB(x, y) & 0x00FFFFFF;
                    if(pixel > 0) texture.add(true);
                    else texture.add(false);
                    x ++;
                }
                
                x = 0;
                y ++;
                κ ++;
            }
            
            κ = 0;
            textures.add(texture);
        }
        
        ι = 0;
        while(ι < COLORCNT)
        {
            while(x < TEXWIDTH)
            {
                int color = tiColors.RGBtoTI(αimage.getRGB(x, y)) << 4;
                color |= tiColors.RGBtoTI(αimage.getRGB(x + 1, y));
                pallet.add((byte)color);
                x += 2;
                ι ++;
            }
            x = 0;
            y ++;
        }
        
        GUI.getWorkspace().repaint();
    }
    
    public ArrayList<Point> getPointsFromTexture(int textureNum)
    {
        ArrayList<Point> points = new ArrayList<Point>();
        
        int x = 0;
        int y = 0;
        
        for(Boolean pixel : textures.get(textureNum))
        {
            if(pixel) points.add(new Point(x, y));
            x ++;
            if(x == TEXWIDTH)
            {
                x = 0;
                y ++;
            }
        }
        
        return points;
    }
    
    public byte getColor(int αcolor)
    {
        return pallet.get(αcolor);
    }
    
    public byte[] getPallet()
    {
        byte[] retPallet = new byte[pallet.size()];
        
        int ι = 0;
        for(Byte color : pallet)
        {
            retPallet[ι++] = color;
        }
        
        return retPallet;
    }
    
    public byte[] getTextures()
    {
        byte[] retTextures = new byte[textures.size() * (TEXHEIGHT)];
        
        int ι = 0;
        String byteStr = new String();
        
        for(ArrayList<Boolean> texture : textures)
        {
            for(Boolean pixel : texture)
            {
                byteStr += (pixel ? '1' : '0');
                if(byteStr.length() == TEXWIDTH)
                {
                    retTextures[ι++] = (byte)Integer.parseInt(byteStr, 2);
                    byteStr = new String();
                }
            }
        }
        
        return retTextures;
    }
    
    public byte[] getAll()
    {
        byte[] palData = getPallet();
        byte[] texData = getTextures();
        byte[] retData = new byte[palData.length + texData.length];
        
        int ι = 0;
        
        for(byte unit : palData)
        {
            retData[ι++] = unit;
        }
        
        for(byte unit : texData)
        {
            retData[ι++] = unit;
        }
        
        return retData;
    }
}
