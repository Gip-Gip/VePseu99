import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Workspace extends Canvas
implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
    private static final long serialVersionUID = 1L;
    
    private PopupMenu rClickMenu = null;
    private Save save = null;
    private Load load = null;
    private Map map = null;
    private String notification = null;
    private boolean shift = false;
    private boolean ctrl = false;
    private int button = 0;
    private int scale = 25;
    private int relativeX = 0;
    private int relativeY = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    
    public Map getMap()
    {
        return map;
    }
    
    public Save getSave()
    {
        return save;
    }
    
    public void setNotification(String αnotification)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(calendar.getTime());
        notification = αnotification + " (" + time + ")";
        repaint();
    }
    
    public Workspace()
    {
        map = new Map();
        rClickMenu = new PopupMenu();
        save = new Save();
        load = new Load();
        notification = new String();
        
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add all the menu items
        rClickMenu.add(save);
        rClickMenu.add(load);
        // Add all the listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
        add(rClickMenu);
    }
    
    public void center()
    {
        // Center the whole shebang
        relativeX = getWidth() / 2 - scale * Map.MAPWIDTH / 2 - scale / 2;
        relativeY = getHeight() / 2 - scale * Map.MAPHEIGHT / 2 - scale / 2;
    }
    
    @Override
    public void paint(Graphics graphics)
    {
        // Double buffered to remove flicker
        BufferStrategy bs = getBufferStrategy();
        if(bs != null) do
        {
            try
            {
                graphics = bs.getDrawGraphics();
                
                // Clear the screen
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw the gridlines
                graphics.setColor(Color.DARK_GRAY);
                for(int ι = relativeX % scale; ι < getWidth(); ι += scale)
                    graphics.drawLine(ι, 0, ι, getHeight());
                for(int ι = relativeY % scale; ι < getHeight(); ι += scale)
                    graphics.drawLine(0, ι, getWidth(), ι);
                
                // Draw the box outlining the map borders
                graphics.setColor(Color.RED);
                graphics.drawRect
                (
                    relativeX,
                    relativeY,
                    scale * Map.MAPWIDTH,
                    scale * Map.MAPHEIGHT
                );
                
                // Draw all the walls
                for(Point point : map.getPoints())
                {
                    // My inner programmer tells me to put this all on one line,
                    // but for the sake of readability I've put this on 3
                    point.x *= scale; point.y *= scale;    
                    point.x += relativeX; point.y += relativeY;
                    graphics.setColor(Color.GRAY);
                    graphics.fillRect(point.x, point.y, scale, scale);
                    graphics.setColor(Color.WHITE);
                    graphics.drawRect(point.x, point.y, scale, scale);
                }
                
                // And finally the notification bar
                graphics.setColor(Color.WHITE);
                if(notification.length() > 0)
                    graphics.drawChars
                    (
                        notification.toCharArray(),
                        0,
                        notification.length(),
                        10,
                        10
                    );
            }
            finally
            {
                graphics.dispose();
            }
            
            bs.show();
        } while(bs.contentsLost());    
    }
    
    @Override
    public void repaint()
    {
        paint(null);
    }
    
    @Override
    public void mouseClicked(MouseEvent αevent)
    {
    }

    @Override
    public void mouseEntered(MouseEvent αevent)
    {
    }

    @Override
    public void mouseExited(MouseEvent αevent)
    {
    }

    @Override
    public void mousePressed(MouseEvent αevent)
    {
        mouseX = αevent.getX();
        mouseY = αevent.getY();
        switch(button = αevent.getButton())
        {
            case(MouseEvent.BUTTON1): // Left button
                int wallX = (mouseX - relativeX) / scale;
                int wallY = (mouseY - relativeY) / scale;
                if(shift) map.removeWall(wallX, wallY);
                else map.placeWall(wallX, wallY, (byte)0x08);
                repaint();
                break;
                
            case(MouseEvent.BUTTON3): // Right button
                rClickMenu.show(this, mouseX, mouseY);
                break;
            
            case(MouseEvent.BUTTON2): // Center button
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent αevent)
    {
        switch(button = αevent.getButton())
        {
            case(MouseEvent.BUTTON1): // Left button
                break;
            
            case(MouseEvent.BUTTON3): // Right button
                break;
            
            case(MouseEvent.BUTTON2): // Center button
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent αevent)
    {
        switch(button)
        {
            case(MouseEvent.BUTTON1): // Left button
                mouseX = αevent.getX();
                mouseY = αevent.getY();
                int wallX = (mouseX - relativeX) / scale;
                int wallY = (mouseY - relativeY) / scale;
                
                if(shift) map.removeWall(wallX, wallY);
                else map.placeWall(wallX, wallY, (byte)0x08);
                
                repaint();
                break;
                
            case(MouseEvent.BUTTON3): // Right button
                break;
            
            case(MouseEvent.BUTTON2): // Center button
                int mouseNewX = αevent.getX();
                int mouseNewY = αevent.getY();
            
                relativeX += mouseNewX - mouseX;
                relativeY += mouseNewY - mouseY;
            
                mouseX = mouseNewX;
                mouseY = mouseNewY;
                repaint();
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent αevent)
    {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent αevent)
    {
        // Invert the mouse wheel to make it CAD-standard...
        int wheelRotation = -αevent.getWheelRotation();
        scale += wheelRotation * 2;
        // Make it so the farthest you can zoom out is to where each wall is
        // 10x10 pixels
        scale = Math.max(10, scale);
        if(scale != 10)
        {
            relativeX -= wheelRotation;
            relativeY -= wheelRotation;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent αevent)
    {
        switch(αevent.getKeyCode())
        {
            case(KeyEvent.VK_SHIFT):
                shift = true;
                break;
            
            case(KeyEvent.VK_CONTROL):
                ctrl = true;
                break;
            // Ctrl+s shortcut
            case(KeyEvent.VK_S):
                if(ctrl)
                {
                    save.run();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent αevent)
    {
        switch(αevent.getKeyCode())
        {
            case(KeyEvent.VK_SHIFT):
                shift = false;
                break;
            
            case(KeyEvent.VK_CONTROL):
                ctrl = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent αevent)
    {
        // TODO Auto-generated method stub
        
    }
}
