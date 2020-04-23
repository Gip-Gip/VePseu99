import java.util.ArrayList;

public class AsmData
{
    private String asmData = null;
    private String asmName = null;
    private int index = 0;
    
    public AsmData()
    {
        asmData = new String();
        index = -1;
    }
    
    public AsmData(byte[] αdata)
    {
        asmName = "EVEN";
        
        int ι = 0;
        while(asmName.equals("EVEN"))
        {
            asmName = new String();
            while(ι < αdata.length && Character.isWhitespace((char)αdata[ι]))
                ι++;
            while(ι < αdata.length && !Character.isWhitespace((char)αdata[ι]))
                asmName += (char)αdata[ι++];
        }
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
            index = -1;
            asmData = asmData.substring(0, asmData.length() - 1);
        }
        
        if(index == -1)
        {
            index ++;
            asmData += "\r\n    BYTE";
        }
        
        asmData += " " + unit + ",";
        index ++;
    }
    
    public void addRef(String unit)
    {
        if(index > 0) asmData = asmData.substring(0, asmData.length() - 1);
        index = -1;
        asmData += "\r\n    DATA " + unit;
    }
    
    public void addText(String unit)
    {
        if(index > 0) asmData = asmData.substring(0, asmData.length() - 1);
        index = -1;
        asmData += "\r\n    TEXT '" + unit + "'";
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
        
        return byteStr.length() > 0 ? Byte.parseByte(byteStr) : 0;
    }
    
    public String getRef()
    {
        String tokenStr = new String();
        while(index < asmData.length() && !tokenStr.equals("DATA"))
        {
            tokenStr = new String();
            while
            (
                index < asmData.length() &&
                (Character.isWhitespace(asmData.charAt(index)))
            ) index ++;
            
            while
            (
                index < asmData.length() &&
                !Character.isWhitespace(asmData.charAt(index))
            )
                tokenStr += asmData.charAt(index++);
        }
        
        String refStr = new String();
        while
        (
            index < asmData.length() &&
            (Character.isWhitespace(asmData.charAt(index++)))
        );
        
        while
        (
            index < asmData.length() &&
            !Character.isWhitespace(asmData.charAt(index))
        )
            refStr += asmData.charAt(index++);
        return refStr;
    }
    
    public String getText()
    {
        String tokenStr = new String();
        while(index < asmData.length() && !tokenStr.equals("TEXT"))
        {
            tokenStr = new String();
            while
            (
                index < asmData.length() &&
                (Character.isWhitespace(asmData.charAt(index)))
            ) index ++;
            
            while
            (
                index < asmData.length() &&
                !Character.isWhitespace(asmData.charAt(index))
            )
                tokenStr += asmData.charAt(index++);
        }
        
        String textStr = new String();
        while
        (
            index < asmData.length() &&
            (Character.isWhitespace(asmData.charAt(index++)))
        );
        
        while
        (
            index < asmData.length() &&
            !Character.isWhitespace(asmData.charAt(index))
        )
            if(asmData.charAt(index) != '\'')
                textStr += asmData.charAt(index++);
        return textStr;
    }
    
    public byte[] getAsmData()
    {
        return
        (
            "    EVEN\r\n" +
            asmName +
            "\r\n" +
            asmData.substring(0, asmData.length()-1) + 
            "\r\n"
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
