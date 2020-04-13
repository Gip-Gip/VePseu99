import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PropertiesDialog extends Dialog
implements ActionListener, WindowListener
{
    TextField mnTextField = null;
    TextField paTextField = null;
    TextField pxTextField = null;
    TextField pyTextField = null;
    Map map = null;
    
    public PropertiesDialog(Frame αframe)
    {
        super(αframe);
        
        map = GUI.getWorkspace().getMap();
        
        // Map name label
        Label mnLabel = new Label("Map Name:");
        mnLabel.setBounds(10, 25, 80, 50);
        
        // Map name text field
        mnTextField = new TextField(map.getMapName(), 6);
        
        mnTextField.setBounds(100, 37, 50, 25);
        mnTextField.addActionListener(this);
        
        // Player angle label
        Label paLabel = new Label("Player Angle:");
        paLabel.setBounds(170, 25, 80, 50);
        
        // Player angle text field
        paTextField = new TextField(Byte.toString(map.getPlayerAngle()), 1);
        paTextField.setBounds(270, 37, 25, 25);
        paTextField.addActionListener(this);
        
        // Player X label
        Label pxLabel = new Label("Player X:");
        pxLabel.setBounds(10, 100, 80, 50);
        
        // Player X text field
        pxTextField = new TextField(Byte.toString(map.getPlayerX()), 1);
        pxTextField.setBounds(100, 112, 25, 25);
        pxTextField.addActionListener(this);
        
        // Player Y label
        Label pyLabel = new Label("Player Y:");
        pyLabel.setBounds(170, 100, 80, 50);
        
        // Player Y text field
        pyTextField = new TextField(Byte.toString(map.getPlayerY()), 1);
        pyTextField.setBounds(270, 112, 25, 25);
        pyTextField.addActionListener(this);
        
        // Wall styles load button
        Button wslButton = new Button("Load Wallstyle");
        wslButton.setBounds(10, 175, 100, 25);
        wslButton.addActionListener(new LoadWallStyle());
        
        Button wseButton = new Button("Export Wallstyle");
        wseButton.setBounds(170, 175, 100, 25);
        wseButton.addActionListener(new ExportWallStyle());
        
        add(mnLabel);
        add(paLabel);
        add(pxLabel);
        add(pyLabel);
        add(mnTextField);
        add(paTextField);
        add(pxTextField);
        add(pyTextField);
        add(wslButton);
        add(wseButton);
        
        addWindowListener(this);
        setSize(480, 320);
        setLayout(null);
        setVisible(true);
    }

    private static final long serialVersionUID = 1L;
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        map.setMapName(mnTextField.getText());
        map.setPlayerCoords
        (
            Byte.parseByte(pxTextField.getText()),
            Byte.parseByte(pyTextField.getText()),
            Byte.parseByte(paTextField.getText())
        );
        
        GUI.getWorkspace().repaint();
        
        mnTextField.setText(map.getMapName());
        paTextField.setText(Byte.toString(map.getPlayerAngle()));
        pxTextField.setText(Byte.toString(map.getPlayerX()));
        pyTextField.setText(Byte.toString(map.getPlayerY()));
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
