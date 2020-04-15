import java.util.ArrayList;

public class LZ
{
    private ArrayList<Byte> lzData = null;
    private ArrayList<ArrayList<Byte>> lzDictionary = null;
    private ArrayList<Byte> lzWord = null;
    public static final byte LZWORD = 4;
    
    public class YouDontKnowWhatThisDoes extends Exception
    {
        private static final long serialVersionUID = 1L;

        public YouDontKnowWhatThisDoes()
        {
            super("You don't know what this does!");
        }
    }
    
    // This constructor creates an LZ instance from previously compressed data
    // Pass true through αyesIknowWhatThisDoes to confirm you know what you're
    // doing
    
    public LZ(byte[] αcompressedData, boolean αyesIknowWhatThisDoes)
    throws YouDontKnowWhatThisDoes
    {
        byte[] data = αcompressedData; // Put this in a shorter variable name
        if(!αyesIknowWhatThisDoes) throw new YouDontKnowWhatThisDoes();
        
        lzData = new ArrayList<Byte>();
        lzDictionary = new ArrayList<ArrayList<Byte>>();
        
        // Load dictionary
        int ι = 0;
        int κ = data[ι++];
        int λ = 0;
        lzWord = new ArrayList<Byte>();
        while(κ > 0)
        {
            lzWord.add(data[ι++]);
            
            λ++;
            
            if(λ == LZWORD)
            {
                lzDictionary.add(lzWord);
                lzWord = new ArrayList<Byte>();
                κ--;
                λ = 0;
            }
        }
        
        // Load data
        κ = data[ι++];
        while(κ > 0)
        {
            lzData.add(data[ι++]);
            κ--;
        }
    }
    
    public LZ(byte[] αdata)
    {
        lzData = new ArrayList<Byte>();
        lzDictionary = new ArrayList<ArrayList<Byte>>();
        
        // Load dictionary
        int ι = 0;
        lzWord = new ArrayList<Byte>();
        for(byte data : αdata)
        {
            lzWord.add(data);
            ι++;
            if(ι == LZWORD)
            {
                lzDictionary.add(lzWord);
                ι = 0;
                lzWord = new ArrayList<Byte>();
            }
        }
        
        // Remove redundant dictionary entries
        ι = 0;
        int κ = 0;
        while(ι < lzDictionary.size())
        {
            ArrayList<Byte> entry = lzDictionary.get(ι);
            while((κ = lzDictionary.lastIndexOf(entry)) != ι)
                lzDictionary.remove(κ);
            ι++;
        }
        
        // Compress data
        ι = 0;
        lzWord = new ArrayList<Byte>();
        for(byte data : αdata)
        {
            lzWord.add(data);
            ι++;
            if(ι == LZWORD)
            {
                lzData.add((byte)lzDictionary.indexOf(lzWord));
                ι = 0;
                lzWord = new ArrayList<Byte>();
            }
        }
    }
    
    public byte[] getCompressedData()
    {
        byte[] data =
            new byte[lzDictionary.size() * LZWORD + lzData.size() + 2];
        
        // Add the dictionary length
        int ι = 0;
        data[ι++] = (byte)lzDictionary.size();
        
        // Add the dictionary
        for(ArrayList<Byte> lzWord : lzDictionary)
            for(Byte unit : lzWord) data[ι++] = unit;
        
        // Add the compressed data length
        data[ι++] = (byte)lzData.size();
        
        // Add the compressed data
        for(Byte unit : lzData) data[ι++] = unit;
        
        return data;
    }
    
    public byte[] getUncompressedData()
    {
        byte[] data = new byte[lzData.size() * LZWORD];
        
        int ι = 0;
        
        for(byte κ : lzData)
        {
            for(Byte unit : lzDictionary.get(κ))
                    data[ι++] = unit;
        }
        
        return data;
    }
}
