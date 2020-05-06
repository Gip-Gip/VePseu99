import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotSavedDialog extends Dialog implements ActionListener
{
    public class SaveAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            GUI.getWorkspace().getSave().run();
            System.exit(0);
        }
    }
    
    public class CloseAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            System.exit(0);
        }
    }
    
    public NotSavedDialog(Frame αframe)
    {
        super(αframe);
        Label errLabel = new Label("You have unsaved changes!");
        errLabel.setBounds(10, 25, 190, 50);
        
        Button saveButton = new Button("Save");
        saveButton.setBounds(50, 80, 50, 25);
        saveButton.addActionListener(new SaveAction());
        
        Button closeButton = new Button("Exit");
        closeButton.setBounds(125, 80, 50, 25);
        closeButton.addActionListener(new CloseAction());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setBounds(200, 80, 50, 25);
        cancelButton.addActionListener(this);
        
        add(errLabel);
        add(saveButton);
        add(closeButton);
        add(cancelButton);
        setSize(300, 125);
        setLayout(null);
        setVisible(true);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        dispose();
    }

}