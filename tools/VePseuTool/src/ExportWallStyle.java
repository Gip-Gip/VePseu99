import java.awt.FileDialog;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class ExportWallStyle implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        File exportfile = null;
        String exportfileName = null;
        String exportfileDir = null;
        
        FileDialog fileDialog = new FileDialog(GUI.getFrame());
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setVisible(true);
        
        if(fileDialog.getFile() != null) try
        {
            exportfileName = fileDialog.getFile();
            exportfileDir = fileDialog.getDirectory();
            exportfile = new File(exportfileDir + exportfileName);
            if(exportfile.exists()) exportfile.delete();
            exportfile.createNewFile();
        }
        catch (Exception ε)
        {
            GUI.errorMessage(ε);
        }
        
        if(exportfile != null)
        {
            try
            {
                FileOutputStream exportStream = new FileOutputStream(exportfile);
                AsmData exportAsm = new AsmData();
                WallStyle wallStyle = GUI.getWorkspace().getWallStyle();
                LZ compressedTextures = new LZ(wallStyle.getTextures());
                
                exportAsm.setName("\r\nCLRTBL");
                for(byte unit : wallStyle.getPallet())
                {
                    exportAsm.addByte(unit);
                }
                
                exportStream.write(exportAsm.getAsmData());
                
                exportAsm = new AsmData();
                exportAsm.setName("\r\nTEXTBL");
                
                for(byte unit : wallStyle.getTextures())
                {
                    exportAsm.addByte(unit);
                }
                
                exportStream.write(exportAsm.getAsmData());
                
                exportStream.close();
            }
            catch (Exception ε)
            {
                GUI.errorMessage(ε);
            } 
        }
    }    
    
        
    public ExportWallStyle()
    {
    }
        
    public void run()
    {
        actionPerformed(null);
    }
    
}