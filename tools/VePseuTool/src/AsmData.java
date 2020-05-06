import java.util.ArrayList;
import java.util.Base64;

public class AsmData
{
    private String asmData = null;
    private byte[] embData = null;
    private String asmName = null;
    private int index = 0;
    
    public AsmData()
    {
        asmData = new String();
        index = -1;
        embData = new byte[0];
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
        
        while(ι < αdata.length && (char)αdata[ι] != ';')
            asmData += (char)αdata[ι++];
        
        if(ι < αdata.length && (char)αdata[ι++] == ';')
        {
            String embDataB64 = new String();
            while(ι < αdata.length && !Character.isWhitespace((char)αdata[ι]))
            {
                if((char)αdata[ι] != ';') embDataB64 += (char)αdata[ι++];
            }
            embData = Base64.getDecoder().decode(embDataB64);
        }
        else
        {
            embData = new byte[0];
        }
    }
    
    public void setName(String name)
    {
        asmName = name;
    }
    
    public void setEmbData(byte[] data)
    {
        embData = data;
    }
    
    public String getName()
    {
        return asmName;
    }
    
    public void addByte(byte unit)
    {
        if(index >= 16)
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
    
    public void addData(byte[] bytes)
    {
        for(byte unit : bytes)
        {
            addByte(unit);
        }
    }
    
    public void addRef(String unit)
    {
        if(index >= 16)
        {
            index = -1;
            asmData = asmData.substring(0, asmData.length() - 1);
        }
        
        if(index == -1)
        {
            index ++;
            asmData += "\r\n    BYTE";
        }
        
        index += 2;
        asmData += " " + unit + "/256, " + unit + ",";
    }
    
    public void addText(String unit)
    {
        if(index > 0) asmData = asmData.substring(0, asmData.length() - 1);
        index = -1;
        asmData += "\r\n    TEXT '" + unit + "'\r\n";
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
        String tokenStr = "BYTE";
        while(index < asmData.length() && tokenStr.equals("BYTE"))
        {
            tokenStr = new String();
            while
            (
                index < asmData.length() &&
                !Character.isAlphabetic(asmData.charAt(index))
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
            !Character.isAlphabetic(asmData.charAt(index))
        ) index++;
        
        while
        (
            index < asmData.length() &&
            !Character.isWhitespace(asmData.charAt(index))
        )
        {
            if(asmData.charAt(index) != ',') refStr += asmData.charAt(index);
            index ++;
        }
        
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
                !Character.isAlphabetic(asmData.charAt(index))
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
            asmData.charAt(index) != '\''
        ) index ++;
        
        while
        (
            index < asmData.length() &&
            !Character.isWhitespace(asmData.charAt(index))
        )
        {
            if(asmData.charAt(index) != '\'')
                textStr += asmData.charAt(index);
            index ++;
        }
        
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
            "\r\n" +
            (embData.length > 0 ?
            ";" +Base64.getEncoder().encodeToString(embData) :
            "")
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
    
    public byte[] getEmbData()
    {
        return embData;
    }
}
