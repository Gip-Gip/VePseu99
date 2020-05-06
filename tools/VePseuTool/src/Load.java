import java.awt.FileDialog;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

public class Load extends MenuItem
{
    private static final long serialVersionUID = 1L;
    
    
    private class LoadAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            Workspace workspace = GUI.getWorkspace();
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
                    FileInputStream loadfileStream = 
                            new FileInputStream(loadfile);
                    byte[] loadfileBuffer = new byte[(int)loadfile.length()];
                    
                    loadfileStream.read(loadfileBuffer);
                    
                    GUI.getWorkspace().getMap().loadMap(loadfileBuffer);
                    loadfileStream.close();
                }
                catch (Exception ε)
                {
                    GUI.errorMessage(ε);
                }
                
                workspace.setSavefile(loadfile);
                workspace.setSavefileName(loadfileName);
                workspace.setSavefileDir(loadfileDir);
            }
        }
    }
        
    public Load()
    {
        super("Load");
        addActionListener(new LoadAction());
    }
        
    public void run()
    {
        LoadAction action = new LoadAction();
        action.actionPerformed(null);
    }
    
}
