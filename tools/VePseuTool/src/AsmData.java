import java.util.ArrayList;

public class AsmData
{
    private String asmData = null;
    private String asmName = null;
    private int index = 0;
    
    public AsmData()
    {
        asmData = new String();
        asmData += "    BYTE";
    }
    
    public AsmData(byte[] αdata)
    {
        asmName = new String();
        
        int ι = 0;
        while(!Character.isWhitespace((char)αdata[ι]))
            asmName += (char)αdata[ι++];
        
        asmData = new String();
        while(ι < αdata.length)
            asmData += (char)αdata[ι++];
    }
    
    public void setName(String name)
    {
        asmName = name;
    }
    
    public String getName()
    {
        return asmName;
    }
    
    public void addByte(byte unit)
    {
        if(index == 16)
        {
            index = 0;
            asmData = asmData.substring(0, asmData.length() - 1);
            asmData += "\r\n    BYTE";
        }
        
        asmData += " " + unit + ",";
        index ++;
    }
    
    public byte getByte()
    {
        String byteStr = new String();
        
        while
        (
            index < asmData.length() &&
            (!Character.isDigit(asmData.charAt(index)) && asmData.charAt(index) != '-')
        )
            index++;
        while
        (
            index < asmData.length() &&
            (Character.isDigit(asmData.charAt(index)) || asmData.charAt(index) == '-')
        )
            byteStr += asmData.charAt(index++);
        
        return Byte.parseByte(byteStr);
    }
    
    public byte[] getAsmData()
    {
        return
        (
            asmName +
            "\r\n" +
            asmData.substring(0, asmData.length()-1)
        ).getBytes();
    }
    
    public byte[] getData()
    {
        ArrayList<Byte> data = new ArrayList<Byte>();
        
        while(index < asmData.length())
        {
            data.add(getByte());
        }
        
        int ι = 0;
        byte[] byteData = new byte[data.size()];
        for(Byte unit : data)
        {
            byteData[ι++] = unit;
        }
        
        return byteData;
    }
}