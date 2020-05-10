import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sound.midi.Sequence;

public class Workspace extends Canvas
implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener,
FocusListener
{
    private static final long serialVersionUID = 1L;
    
    private PopupMenu rClickMenu = null;
    private Save save = null;
    private Load load = null;
    private Properties properties = null;
    private Map map = null;
    private WallStyle wallStyle = null;
    private TiColors tiColors = null;
    private String notification = null;
    private String telemetry = null;
    private boolean shift = false;
    private boolean ctrl = false;
    private int button = 0;
    private int scale = 25;
    private int relativeX = 0;
    private int relativeY = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    private byte selectedTexture = 0;
    private byte selectedColor = 0;
    private File savefile = null;
    private String savefileName = null;
    private String savefileDir = null;
    private Scene selectedScene = null;
    
    public Map getMap()
    {
        return map;
    }
    
    public Save getSave()
    {
        return save;
    }
    
    public File getSavefile()
    {
        return savefile;
    }
    
    public String getSavefileName()
    {
        return savefileName;
    }
    
    public String getSavefileDir()
    {
        return savefileDir;
    }
    
    public void setSavefile(File αfile)
    {
        savefile = αfile;
    }
    
    public void setSavefileName(String αname)
    {
        savefileName = αname;
    }
    
    public void setSavefileDir(String αdir)
    {
        savefileDir = αdir;
    }
    
    public void setWallStyle(WallStyle αwallStyle)
    {
        wallStyle = αwallStyle;
        repaint();
    }
    
    public void setMusic(Sequence αsequence)
    {
        map.setMusic(αsequence);
    }
    
    public WallStyle getWallStyle()
    {
        return wallStyle;
    }
    
    public void setNotification(String αnotification)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(calendar.getTime());
        notification = αnotification + " (" + time + ")";
        repaint();
    }
    
    public void updateTelemetry()
    {
        int wallX = (mouseX - relativeX) / scale;
        int wallY = (mouseY - relativeY) / scale;
        telemetry = new String();
        telemetry += "x:" + wallX + " ";
        telemetry += "y:" + wallY + " ";
        telemetry += "saved:" + map.isSaved() + " ";
        
        telemetry += "tex#:" + (selectedTexture + 1) + " ";
        
        byte wallColor = wallStyle.getColor(selectedColor);
        byte fColor = (byte)((wallColor & 0xF0) >> 4);
        byte bColor = (byte)(wallColor & 0xF);
        
        telemetry += "color:" + tiColors.getName(fColor);
        telemetry += "/" + tiColors.getName(bColor) + " ";
    }
    
    public Workspace()
    {
        map = new Map();
        wallStyle = new WallStyle();
        tiColors = new TiColors();
        rClickMenu = new PopupMenu();
        save = new Save();
        load = new Load();
        properties = new Properties();
        notification = new String();
        
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Add all the menu items
        rClickMenu.add(save);
        rClickMenu.add(load);
        rClickMenu.add(properties);
        // Add all the listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
        addMouseWheelListener(this);
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
                    // Draw wall texture
                    byte wall = map.getWall(point.x, point.y);
                    point.x *= scale; point.y *= scale;    
                    point.x += relativeX; point.y += relativeY;
                    int background = wallStyle.getColor(wall >> 3) & 0xF;
                    background = tiColors.TItoRGB((byte)background);
                    
                    graphics.setColor(new Color(background));
                    graphics.fillRect(point.x, point.y, scale, scale);
                    
                    int foreground = (wallStyle.getColor(wall >> 3) & 0xF0) >> 4;
                    foreground = tiColors.TItoRGB((byte)foreground);
                    graphics.setColor(new Color(foreground));
                    
                    for
                    (
                        Point texPoint :
                        wallStyle.getPointsFromTexture(wall & 7)
                    )
                    {
                        texPoint.x = Math.round
                        (
                            (float)texPoint.x *
                            (float)scale /
                            (float)WallStyle.TEXWIDTH
                        );
                        texPoint.y = Math.round
                        (
                            (float)texPoint.y *
                            (float)scale /
                            (float)WallStyle.TEXHEIGHT
                        );
                        texPoint.x += point.x; texPoint.y += point.y;
                        graphics.fillRect
                        (
                            texPoint.x,
                            texPoint.y,
                            Math.round
                                ((float)scale / (float)WallStyle.TEXWIDTH),
                            Math.round
                                ((float)scale / (float)WallStyle.TEXHEIGHT)
                        );
                    }
                    
                    graphics.setColor(Color.WHITE);
                    graphics.drawRect(point.x, point.y, scale, scale);
                }
                
                // Draw scenes
                graphics.setColor(Color.GREEN);
                for(Scene scene : map.getScenes())
                {
                    int sceneX = scene.getSceneX() * scale + relativeX;
                    int sceneY = scene.getSceneY() * scale + relativeY;
                    graphics.drawRect(sceneX, sceneY, scale, scale);
                    sceneX += scale / 2;
                    sceneY += scale / 2;
                    
                    switch(scene.getAngle())
                    {
                        case(0):
                            graphics.drawLine
                            (
                                sceneX,
                                sceneY,
                                sceneX,
                                sceneY + scale / 2
                            );
                            break;
                        case(1):
                            graphics.drawLine
                            (
                                sceneX,
                                sceneY,
                                sceneX + scale / 2,
                                sceneY
                            );
                            break;
                        case(2):
                            graphics.drawLine
                            (
                                sceneX,
                                sceneY,
                                sceneX,
                                sceneY - scale / 2
                            );
                            break;
                        case(3):
                            graphics.drawLine
                            (
                                sceneX,
                                sceneY,
                                sceneX - scale / 2,
                                sceneY
                            );
                            break;
                    }
                }
                
                // Draw the player
                graphics.setColor(Color.BLUE);
                int playerX = map.getPlayerX(); int playerY = map.getPlayerY();
                playerX *= scale; playerY *= scale;
                playerX += relativeX + scale / 2;
                playerY += relativeY + scale / 2;
                
                switch(map.getPlayerAngle())
                {
                    case(0):
                        graphics.drawLine
                        (
                            playerX,
                            playerY,
                            playerX,
                            playerY + scale / 2
                        );
                        break;
                    case(1):
                        graphics.drawLine
                        (
                            playerX,
                            playerY,
                            playerX + scale / 2,
                            playerY
                        );
                        break;
                    case(2):
                        graphics.drawLine
                        (
                            playerX,
                            playerY,
                            playerX,
                            playerY - scale / 2
                        );
                        break;
                    case(3):
                        graphics.drawLine
                        (
                            playerX,
                            playerY,
                            playerX - scale / 2,
                            playerY
                        );
                        break;
                }
                
                // And finally the notification bar/telemetry bar
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
                
                updateTelemetry();
                if(telemetry.length() > 0)
                    graphics.drawChars
                    (
                        telemetry.toCharArray(),
                        0,
                        telemetry.length(),
                        10,
                        30
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
                else map.placeWall(wallX, wallY, (byte)(selectedTexture | selectedColor << 3));
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
                else map.placeWall(wallX, wallY, (byte)(selectedTexture | selectedColor << 3));
                break;
                
            case(MouseEvent.BUTTON3): // Right button
                mouseX = αevent.getX();
                mouseY = αevent.getY();
                break;
            
            case(MouseEvent.BUTTON2): // Center button
                int mouseNewX = αevent.getX();
                int mouseNewY = αevent.getY();
                repaint();
                relativeX += mouseNewX - mouseX;
                relativeY += mouseNewY - mouseY;
            
                mouseX = mouseNewX;
                mouseY = mouseNewY;
                break;
        }
        updateTelemetry();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent αevent)
    {
        mouseX = αevent.getX();
        mouseY = αevent.getY();
        updateTelemetry();
        repaint();
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
        updateTelemetry();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent αevent)
    {
        int sceneX = (mouseX - relativeX) / scale;
        int sceneY = (mouseY - relativeY) / scale;
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
                    ctrl = false;
                }
                else
                {
                    if(map.getScene(sceneX, sceneY) == null)
                        map.addScene(sceneX, sceneY);
                }
                break;
            // Copy (only works for scenes)
            case(KeyEvent.VK_C):
                if(ctrl && (selectedScene = map.getScene(sceneX, sceneY)) != null);
                break;
            // Paste (only works for scenes)
            case(KeyEvent.VK_V):
                if(ctrl && selectedScene != null)
                    map.addScene(selectedScene, sceneX, sceneY);
                break;
            case(KeyEvent.VK_E):
                Scene scene;
                if((scene = map.getScene(sceneX, sceneY)) != null)
                    new SceneDialog(GUI.getFrame(), scene);
                break;
            case(KeyEvent.VK_D):
                sceneX = (mouseX - relativeX) / scale;
                sceneY = (mouseY - relativeY) / scale;
                if(shift) map.removeScene(sceneX, sceneY);
                break;
            case(KeyEvent.VK_1):
                if(shift) selectedColor = 0;
                else selectedTexture = 0;
                break;
            case(KeyEvent.VK_2):
                if(shift) selectedColor = 2;
                else selectedTexture = 1;
                break;
            case(KeyEvent.VK_3):
                if(shift) selectedColor = 4;
                else selectedTexture = 2;
                break;
            case(KeyEvent.VK_4):
                if(shift) selectedColor = 6;
                else selectedTexture = 3;
                break;
            case(KeyEvent.VK_5):
                if(shift) selectedColor = 8;
                else selectedTexture = 4;
                break;
            case(KeyEvent.VK_6):
                if(shift) selectedColor = 10;
                else selectedTexture = 5;
                break;
            case(KeyEvent.VK_7):
                if(shift) selectedColor = 12;
                else selectedTexture = 6;
                break;
            case(KeyEvent.VK_8):
                if(shift) selectedColor = 14;
                else selectedTexture = 7;
                break;
        }
        updateTelemetry();
        repaint();
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
                ctrl = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent αevent)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void focusGained(FocusEvent arg0)
    {
        addKeyListener(this);
    }

    @Override
    public void focusLost(FocusEvent arg0)
    {
        removeKeyListener(this);
    }
}
