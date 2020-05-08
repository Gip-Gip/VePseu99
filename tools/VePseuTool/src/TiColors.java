
public class TiColors
{
    public class TiColor
    {
        public int red;
        public int green;
        public int blue;
        public int tiID;
        public String name;
        
        public TiColor(int αred, int αgreen, int αblue, int αtiID, String αname)
        {
            red = αred;
            green = αgreen;
            blue = αblue;
            tiID = αtiID;
            name = αname;
        }
    }
    
    public final TiColor TI_BLACK = new TiColor(0x01, 0x01, 0x01, 1, "BLACK");
    public final TiColor TI_GREEN = new TiColor(0x21, 0xc8, 0x42, 2, "GREEN");
    public final TiColor TI_LGREEN = new TiColor(0x5e, 0xdc, 0x78, 3, "LGREEN");
    public final TiColor TI_BLUE = new TiColor(0x54, 0x55, 0xed, 4, "BLUE");
    public final TiColor TI_LBLUE = new TiColor(0x7d, 0x76, 0xfc, 5, "LBLUE");
    public final TiColor TI_DRED = new TiColor(0xd4, 0x52, 0x4d, 6, "DRED");
    public final TiColor TI_CYAN = new TiColor(0x42, 0xeb, 0xf5, 7, "CYAN");
    public final TiColor TI_RED = new TiColor(0xfc, 0x55, 0x54, 8, "RED");
    public final TiColor TI_LRED = new TiColor(0xff, 0x79, 0x78, 9, "LRED");
    public final TiColor TI_DYELLOW = new TiColor(0xd4, 0xc1, 0x54, 10, "DYLW");
    public final TiColor TI_LYELLOW = new TiColor(0xe6, 0xce, 0x80, 11, "LYLW");
    public final TiColor TI_DGREEN = new TiColor(0x21, 0xb0, 0x3b, 12, "DGREEN");
    public final TiColor TI_MAGENTA = new TiColor(0xc9, 0x5b, 0xba, 13, "MGNTA");
    public final TiColor TI_GRAY = new TiColor(0xcc, 0xcc, 0xcc, 14, "GRAY");
    public final TiColor TI_WHITE = new TiColor(0xff, 0xff, 0xff, 15, "WHITE");
    
    public final TiColor tiColors[] =
    {
        TI_BLACK, TI_GREEN, TI_LGREEN, TI_BLUE, TI_LBLUE, TI_DRED, TI_CYAN,
        TI_RED, TI_LRED, TI_LRED, TI_DYELLOW, TI_LYELLOW, TI_DGREEN, TI_MAGENTA,
        TI_GRAY, TI_WHITE
    };
    
    public byte RGBtoTI(int αrgb)
    {
        int red = (αrgb & 0x00FF0000) >> 16;
        int green = (αrgb & 0x0000FF00) >> 8;
        int blue = (αrgb & 0x000000FF);
        long difference = Long.MAX_VALUE;
        int selColor = 0;
        
        for(TiColor color : tiColors)
        {
            long currDifference =
                Math.abs(color.red - red) +
                Math.abs(color.green - green) +
                Math.abs(color.blue - blue);
            
            if(difference > currDifference)
            {
                selColor = color.tiID;
                difference = currDifference;
            }
        }
        
        return (byte)selColor;
    }
    
    public int TItoRGB(byte αtiID)
    {
        
        for(TiColor color : tiColors)
        {
            if(color.tiID == αtiID)
            {
                return (color.red << 16) + (color.green << 8) + color.blue;
            }
        }
        
       return 0;
    }
    
    public String getName(int αtiID)
    {
        for(TiColor color : tiColors)
        {
            if(color.tiID == αtiID)
            {
                return color.name;
            }
        }
        
        return null;
    }
}
