import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SceneDialog extends Dialog
implements ActionListener, WindowListener
{
    Scene scene = null;
    
    public SceneDialog(Frame αframe, Scene αscene)
    {
        super(αframe);
        
        scene = αscene;
        scene.setBackground(Color.BLACK);
        
        add(scene);
        
        addWindowListener(this);
        setLayout(null);
        setVisible(true);
        setBackground(Color.BLACK);
        scene.setSize(scene.WIDTH*scene.SCALE, scene.HEIGHT * scene.SCALE);
        scene.setBounds(64, 48, scene.WIDTH*scene.SCALE, scene.HEIGHT*scene.SCALE);
        setSize(scene.WIDTH*scene.SCALE + 128, scene.HEIGHT*scene.SCALE + 96);
        
        scene.createBufferStrategy(2);
    }

    private static final long serialVersionUID = 1L;
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
    }

    @Override
    public void windowActivated(WindowEvent arg0)
    {
        scene.repaint();
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