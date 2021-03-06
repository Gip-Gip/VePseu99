
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GUI
{
    private static Frame frame = null;
    private static Workspace workspace = null;
    private static final String frameName = "VePseuTool " + Main.version;
    private static Dimension screenSize = null;
    private static final int FRAMEWIDTH = 640;
    private static final int FRAMEHEIGHT = 480;
    
    public static Frame getFrame()
    {
      return frame;
    }
    
    public static Workspace getWorkspace()
    {
        return workspace;
    }
    
    public static void errorMessage(Exception αε)
    {
      αε.printStackTrace();
      System.err.println("Error! " + αε.toString());
      new ErrorDialog(frame, αε);
    }
  
    public static void start()
    {
      screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      frame = new Frame(frameName);
      workspace = new Workspace();

      workspace.setBackground(Color.BLACK);
      
      // Set it to a size most if not all monitors can handle
      frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
      frame.setLocation
      (
          screenSize.width / 2 - FRAMEWIDTH / 2,
          screenSize.height / 2 - FRAMEHEIGHT / 2
      );
      
      
      
      frame.addWindowListener(new WindowEvents());
      frame.add(workspace);
      
      
      // Resize the canvas with the window...
      frame.addComponentListener(new ComponentAdapter()
      {
          public void componentResized(ComponentEvent componentEvent)
          {
              workspace.setSize(frame.getSize());
          }
      });
      
      frame.setVisible(true);
      workspace.createBufferStrategy(2);
      workspace.setSize(frame.getSize());
      workspace.center();
    }
}
