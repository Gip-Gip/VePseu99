import java.awt.FileDialog;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

public class Save extends MenuItem
{
    private static final long serialVersionUID = 1L;
    
    public class SaveAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            Workspace workspace = GUI.getWorkspace();
            File savefile = workspace.getSavefile();
            String savefileName = workspace.getSavefileName();
            String savefileDir = workspace.getSavefileDir();
            
            if(savefile == null)
            {
                if(GUI.getWorkspace().getMap().getMapName() == null)
                {
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
                
                workspace.setSavefile(savefile);
                workspace.setSavefileName(savefileName);
                workspace.setSavefileDir(savefileDir);
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
    
    public ActionListener getSaveAction()
    {
        return new SaveAction();
    }
}
