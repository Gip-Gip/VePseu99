import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Properties extends MenuItem
{
    private static final long serialVersionUID = 1L;
    
    private class PropertiesAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            new PropertiesDialog(GUI.getFrame());
        }
        
    }
    
    public Properties()
    {
        super("Properties...");
        addActionListener(new PropertiesAction());
    }
    
    public void run()
    {
        PropertiesAction action = new PropertiesAction();
        action.actionPerformed(null);
    }
}