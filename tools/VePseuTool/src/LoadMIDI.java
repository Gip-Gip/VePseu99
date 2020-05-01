import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class LoadMIDI implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        File loadfile = null;
        String loadfileName = null;
        String loadfileDir = null;
        
        FileDialog fileDialog = new FileDialog(GUI.getFrame());
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        
        if(fileDialog.getFile() != null) try
        {
            loadfileName = fileDialog.getFile();
            loadfileDir = fileDialog.getDirectory();
            loadfile = new File(loadfileDir + loadfileName);
        }
        catch (Exception ε)
        {
            GUI.errorMessage(ε);
        }
        
        if(loadfile != null)
        {
            try
            {
                Sequence sequence = MidiSystem.getSequence(loadfile);
                GUI.getWorkspace().setMusic(sequence);
            }
            catch (Exception ε)
            {
                GUI.errorMessage(ε);
            } 
        }
    }    
    
        
    public LoadMIDI()
    {
    }
        
    public void run()
    {
        actionPerformed(null);
    }
    
}