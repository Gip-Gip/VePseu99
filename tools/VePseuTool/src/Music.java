import java.util.ArrayList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Music
{
    private Line[] lines = null;
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int NOTE_BASE = 33;
    public static final int OCT_SIZE = 12;
    public static final int TI_MAXVOL = 0x0F;
    
    public static final int TI_C0VOR = 0xF0;
    public static final int TI_C1VOR = 0x90;
    public static final int TI_C2VOR = 0xB0;
    
    int c1 = 0;
    int c2 = 0;
    int c3 = 0;
    
    public class Line
    {
        byte d = 1;
        byte c0v = (byte)(TI_MAXVOL | TI_C0VOR);
        byte c1v = (byte)(TI_MAXVOL | TI_C1VOR);
        byte c2v = (byte)(TI_MAXVOL | TI_C2VOR);
        byte c1n = 0;
        byte c2n = 0;
        
        public Line()
        {
        }
        
        public Line(int αd, int αc0v, int αc1v, int αc1n, int αc2v, int αc2n)
        {
            d = (byte)αd;
            c0v = (byte)αc0v;
            c1v = (byte)αc1v;
            c2v = (byte)αc2v;
            c1n = (byte)αc1n;
            c2n = (byte)αc2n;
        }
        
        public byte[] getData()
        {
            return new byte[] {d, c0v, c1n, c1v, c2n, c2v};
        }
        
        // Set channel takes midi values and converts them to TI values
        // Keep that in mind
        public void setChannel(int αnum, int αn, int αv)
        {
            // Convert note to TI note
            int tiNote = αn - NOTE_BASE;
            // In case the note is below A2...
            if(tiNote < 0)
            {
                tiNote %= OCT_SIZE;
                tiNote += OCT_SIZE;
            }
            
            // Convert volume to TI attenuation
            // The volume range of the TI is 1/8th that of MIDI
            int tiAttenuation = Math.min(Math.round((float)αv/8.0f), 0x0F);
            // And attenuation is the inverse of volume
            tiAttenuation = TI_MAXVOL - tiAttenuation;
            
            switch(αnum)
            {
                case(0):
                    // Make the attenuation formated for the TI
                    tiAttenuation |= TI_C0VOR;
                    c0v = (byte)tiAttenuation;
                    break;
                case(1):
                    tiAttenuation |= TI_C1VOR;
                    c1v = (byte)tiAttenuation;
                    c1n = (byte)tiNote;
                    break;
                case(2):
                    tiAttenuation |= TI_C2VOR;
                    c2v = (byte)tiAttenuation;
                    c2n = (byte)tiNote;
                    break;
                default:
                    System.err.println
                    (
                        "Note in channel " +
                        αnum +
                        ", only channels 0-2 are supported!"
                    );
                    break;
            }
        }
        
        public void incD()
        {
            d ++;
        }
        
        public boolean equals(Line αline)
        {
            byte[] thisData = getData();
            byte[] thatData = αline.getData();
            
            for(int ι = 1; ι < thisData.length; ι ++)
                if(thisData[ι] != thatData[ι]) return false;
            
            return true;
        }
        
        public Line clone()
        {
            return new Line(d, c0v, c1v, c1n, c2v, c2n);
        }
    }
    
    public Music()
    {
        lines = new Line[0];
    }
    
    public Music(AsmData data)
    {
        ArrayList<Line> lineList = new ArrayList<Line>();
        
        int d, c0v, c1v, c1n, c2v, c2n;
        while((d = data.getByte()) != 0)
        {
            c0v = data.getByte();
            c1v = data.getByte();
            c1n = data.getByte();
            c2v = data.getByte();
            c2n = data.getByte();
            lineList.add(new Line(d, c0v, c1v, c1n, c2v, c2n));
        }
        
        lines = new Line[lineList.size()];
        
        for(int ι = 0; ι < lineList.size(); ι ++)
        {
            lines[ι] = lineList.get(ι);
        }
    }
    
    public Music(Sequence αsequence)
    {
        lines = new Line[(int)αsequence.getTickLength()];
        
        for(int ι = 0; ι < lines.length; ι++) lines[ι] = new Line();
        
        for(Track track : αsequence.getTracks())
        {
            for(int ι = 0; ι < track.size(); ι++)
            {
                MidiMessage message = track.get(ι).getMessage();
                int tick = (int)track.get(ι).getTick();
                
                if(message instanceof ShortMessage)
                {
                    ShortMessage sMessage = (ShortMessage)message;
                    int channel = sMessage.getChannel();
                    
                    switch(sMessage.getCommand())
                    {
                        case(NOTE_ON):
                            int note = sMessage.getData1();
                            int vol = sMessage.getData2();
                            
                            // Ensures notes sustain correctly, not quick by any means
                            // But it sure as hell is simple
                            for(int κ = tick; κ < lines.length; κ++)
                                lines[κ].setChannel(channel, note, vol);
                            break;
                        case(NOTE_OFF):
                            for(int κ = tick; κ < lines.length; κ++)
                                lines[κ].setChannel(channel, 0, 0);
                            break;
                    }
                }
            }
        }
    }
    
    public AsmData getData()
    {
        AsmData data = new AsmData();
        
        ArrayList<Line> lineList = new ArrayList<Line>();
        
        for(Line line : lines)
        {
            lineList.add(line.clone());
        }
        
        for(int ι = 0; ι < lineList.size(); ι++)
        {
            if(ι > 0 && lineList.get(ι).equals(lineList.get(ι-1)))
            {
                lineList.remove(ι);
                lineList.get(ι-1).incD();
            }
        }
        
        for(Line line : lineList)
        {
            data.addData(line.getData());
        }
        
        data.addByte((byte)0);
        
        return data;
    }
}
