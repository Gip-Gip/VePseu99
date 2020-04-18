import java.awt.FileDialog;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class LoadSprite
{
    
    private Scene scene = null; 
    
        
    public LoadSprite(Scene αscene)
    {
        scene = αscene;
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
                Image image = ImageIO.read(loadfile);
                scene.addSprite(image);
                scene.repaint();
            }
            catch (Exception ε)
            {
                GUI.errorMessage(ε);
            } 
        }
        
        scene.notLoadingSprite();
    }
}