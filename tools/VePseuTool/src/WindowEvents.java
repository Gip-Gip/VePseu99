/*
 * WindowEvents.java of Chromascript,
 * the high-density paper-based data storage program
 *
 * by Charles Thompson, do not distribute!
 */

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class WindowEvents extends JFrame implements WindowListener
{
  /* Variable that makes eclipse shut up */
  private static final long serialVersionUID = 1L;

  @Override
  public void windowActivated(WindowEvent αevent)
  {
  }

  @Override
  public void windowClosed(WindowEvent αevent)
  {
  }
  
  /* When the exit button's clicked, this is called */
  @Override
  public void windowClosing(WindowEvent αevent)
  {
    try
    {
      if(!GUI.getWorkspace().getMap().isSaved())
          new NotSavedDialog(GUI.getFrame());
      else System.exit(0);
    }
    catch(Exception ε)
    {
      GUI.errorMessage(ε);
    }
  }

  @Override
  public void windowDeactivated(WindowEvent αevent)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent αevent)
  {
  }

  @Override
  public void windowIconified(WindowEvent αevent)
  {
  }

  @Override
  public void windowOpened(WindowEvent αevent)
  {
  }
  
}
