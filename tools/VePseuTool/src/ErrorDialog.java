import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorDialog extends Dialog implements ActionListener
{
    public ErrorDialog(Frame αframe, Exception αε)
    {
        super(αframe);
        Label errLabel = new Label("Error! " + αε.toString());
        errLabel.setBounds(10, 25, 190, 50);
        
        Button errButton = new Button("OK");
        errButton.setBounds(125, 80, 50, 25);
        
        errButton.addActionListener(this);
        
        add(errLabel);
        add(errButton);
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
