import java.awt.FileDialog;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

public class Save extends MenuItem
{
    private static final long serialVersionUID = 1L;
    
    File savefile = null;
    String savefileName = null;
    String savefileDir = null;
    
    private class SaveAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            if(savefile == null)
            {
                if(GUI.getWorkspace().getMap().getMapName() == null)
                {
                    //This dialog is self-setting up and self-terminating
                    @SuppressWarnings("unused")
                    MapNameDialog mapNameDialog = 
                            new MapNameDialog(GUI.getFrame());
                    return;
                }
                
                FileDialog fileDialog = new FileDialog(GUI.getFrame());
                fileDialog.setMode(FileDialog.SAVE);
                fileDialog.setVisible(true);
                
                if(fileDialog.getFile() != null) try
                {
                    savefileName = fileDialog.getFile();
                    savefileDir = fileDialog.getDirectory();
                    savefile = new File(savefileDir + savefileName);
                    if(savefile.exists()) savefile.delete();
                    savefile.createNewFile();
                }
                catch (Exception ε)
                {
                    GUI.errorMessage(ε);
                }
            }
            
            if(savefile != null)
            {
                try
                {
                    FileOutputStream savefileStream = 
                            new FileOutputStream(savefile);
                    
                    savefileStream.write(GUI.getWorkspace().getMap().makeMapFile());
                    
                    savefileStream.close();
                }
                catch (Exception ε)
                {
                    GUI.errorMessage(ε);
                } 
            }
        }
        
    }
    
    public Save()
    {
        super("Save");
        addActionListener(new SaveAction());
    }
    
    public void run()
    {
        SaveAction action = new SaveAction();
        action.actionPerformed(null);
    }
}
