import java.awt.FileDialog;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class LoadWallStyle implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
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
                BufferedImage loadImage = ImageIO.read(loadfile);
                GUI.getWorkspace().setWallStyle(new WallStyle(loadImage));
            }
            catch (Exception ε)
            {
                GUI.errorMessage(ε);
            } 
        }
    }    
    
        
    public LoadWallStyle()
    {
    }
        
    public void run()
    {
        actionPerformed(null);
    }
    
}