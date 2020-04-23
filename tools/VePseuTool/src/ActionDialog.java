import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ActionDialog extends Dialog implements ActionListener, WindowListener
{
    TextField nameField = null;
    TextField labelField = null;
    Scene.Action[] actions;
    int actionNum = 0;
    
    public ActionDialog(Frame αframe, Scene.Action[] αactions, int αactionNum)
    {
        super(αframe);
        
        actions = αactions;
        actionNum = αactionNum;
        Scene.Action action = actions[actionNum];
        
        String name = new String();
        String label = new String();
        
        if(action != null)
        {
            name = action.getName();
            label = action.getLabel();
        }
        
        Label nameLabel = new Label("Name:");
        nameLabel.setBounds(10, 25, 50, 25);
        Label labelLabel = new Label("Label:");
        labelLabel.setBounds(120, 25, 50, 25);
        
        nameField = new TextField(name, 6);
        
        nameField.setBounds(60, 25, 50, 25);
        nameField.addActionListener(this);
        
        labelField = new TextField(label, 6);
        
        labelField.setBounds(180, 25, 50, 25);
        labelField.addActionListener(this);
        
        add(nameLabel);
        add(labelLabel);
        add(nameField);
        add(labelField);
        addWindowListener(this);
        setSize(300, 125);
        setLayout(null);
        setVisible(true);
    }

    private static final long serialVersionUID = 1L;
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        actions[actionNum] = new Scene.Action
       (
           nameField.getText().toUpperCase(),
           labelField.getText().toUpperCase()
       );
    }
    
    @Override
    public void windowActivated(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosed(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosing(WindowEvent arg0)
    {
        dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeiconified(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowIconified(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowOpened(WindowEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }
}