import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapNameDialog extends Dialog implements ActionListener
{
    TextField textField = null;
    public MapNameDialog(Frame αframe)
    {
        super(αframe);
        Label label = new Label("Type the name of your map");
        label.setBounds(10, 25, 190, 50);
        
        textField = new TextField("", 6);
        
        textField.setBounds(125, 80, 50, 25);
        textField.addActionListener(this);
        
        add(label);
        add(textField);
        setSize(300, 125);
        setLayout(null);
        setVisible(true);
    }

    private static final long serialVersionUID = 1L;
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        GUI.getWorkspace().getMap().setMapName(textField.getText());
        GUI.getWorkspace().getSave().run();
        dispose();
    }

}