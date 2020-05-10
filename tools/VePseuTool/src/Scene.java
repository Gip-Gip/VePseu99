import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Scene extends Canvas
implements MouseMotionListener, KeyListener, FocusListener
{
    private static final long serialVersionUID = 1L;
    private int sceneX = 0;
    private int sceneY = 0;
    private int sceneAngle = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    private Map map = null;
    private ArrayList<Sprite> sprites = null;
    private Sprite selectedSprite = null;
    private TiColors tiColors = null;
    private Action[] actions = null;
    private boolean[] drawnl = null;
    private boolean loadingSprite = false;
    private boolean shift = false;
    public final int SCALE = 16;
    public final int WIDTH = 32;
    public final int HEIGHT = 24;
    public final int SPRITEWIDTH = 8;
    public final int SPRITEHEIGHT = 8;
    public final int SPRITESCALE = 2;
    public final int SPRITEPMULT = 2;
    public final int ACTIONCNT = 8;
    public final Frame frame;
    
    public int getSceneX()
    {
        return sceneX;
    }
    
    public int getSceneY()
    {
        return sceneY;
    }
    
    public void setSceneX(int x)
    {
        sceneX = x;
    }
    
    public void setSceneY(int Y)
    {
        sceneY = Y;
    }
    
    public int getAngle()
    {
        return sceneAngle;
    }
    
    public void notLoadingSprite()
    {
        loadingSprite = false;
    }
    
    public class Sprite
    {
        int x = 0;
        int y = 0;
        Image sprite = null;
        
        public Sprite(int αx, int αy, Image αsprite)
        {
            x = αx;
            y = αy;
            sprite = αsprite;
        }
        
        public int getX()
        {
            return x;
        }
        
        public int getY()
        {
            return y;
        }
        
        public Image getSprite()
        {
            return sprite;
        }
        
        public void incX(int Δ)
        {
            x += Δ;
        }
        
        public void decX(int Δ)
        {
            x -= Δ;
        }
        
        public void incY(int Δ)
        {
            y += Δ;
        }
        
        public void decY(int Δ)
        {
            y -= Δ;
        }
        
        public int getColor()
        {
            int color = 0;
            BufferedImage bufferedSprite = (BufferedImage)sprite;
            for(int y = 0; y < SPRITEHEIGHT && color == 0; y ++)
            {
                for(int x = 0; x < SPRITEWIDTH && color == 0; x ++)
                {
                    color = bufferedSprite.getRGB(x, y) & 0x00FFFFFF;
                }
            }
            
            return color;
        }
    }
    
    static public class Action
    {
        private String name = null;
        private String label = null;
        
        public Action(String αname, String αlabel)
        {
            name = αname;
            label = αlabel;
        }
        
        public String getName()
        {
            return name;
        }
        
        public String getLabel()
        {
            return label;
        }
    }
    
    public class Wall
    {
        private int[] hpos;
        private int[] height;
        public int xd = 0;
        public int yd = 0;
        
        public Wall(int αxd, int αyd, int[] αhpos, int[] αheight)
        {
            xd = αxd;
            yd = αyd;
            hpos = αhpos;
            height = αheight;
        }
        
        public void draw(Graphics αgraphics)
        {
            for(int ι = 0; ι < hpos.length; ι++)
            {
                drawVertLine(height[ι], hpos[ι], αgraphics);
            }
        }
    }
    
    Wall wall1 = new Wall
    (
        -1,
        0,
        new int[]{29, 30, 31},
        new int[]{22, 22, 22}
    );
    
    Wall wall2 = new Wall
    (
        1,
        0,
        new int[]{0, 1, 2},
        new int[]{22, 22, 22}
    );
    
    Wall wall3 = new Wall
    (
        0,
        1,
        new int[]
        {
            3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
            22, 23, 24, 25, 26, 27, 28
        },
        new int[]
        {
            22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
            22, 22, 22, 22, 22, 22, 22, 22, 22
        }
    );
    
    Wall wall4 = new Wall
    (
        -1,
        1,
        new int[]{22, 23, 24, 25, 26, 27, 28, 29, 30, 31},
        new int[]{14, 16, 17, 18, 19, 20, 22, 22, 22, 22}
    );
    
    Wall wall5 = new Wall
    (
        1,
        1,
        new int[] {0,1,2,3,4,5,6,7,8,9},
        new int[] {22,22,22,22,20,19,18,17,16,14}
    );
    
    Wall wall6 = new Wall
    (
        0,
        2,
        new int[]{10,11,12,13,14,15,16,17,18,19,20,21},
        new int[]{12,12,12,12,12,12,12,12,12,12,12,12}
    );
    
    Wall wall7 = new Wall
    (
        -1,
        2,
        new int[]{19,20,21,22,23,24,25,26,27,28,29,30,31},
        new int[]{8,10,12,12,12,12,12,12,12,12,12,12,12}
    );
    
    Wall wall8 = new Wall
    (
        1,
        2,
        new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12},
        new int[]{12,12,12,12,12,12,12,12,12,12,12,10,8}
    );
    
    Wall wall9 = new Wall
    (
        -2,
        2,
        new int[]{26,27,28,29,30,31},
        new int[]{7,8,9,10,11,12}
    );
    
    Wall wallA = new Wall
    (
        2,
        2,
        new int[]{0,1,2,3,4,5},
        new int[]{12,11,10,9,8,7}
    );
    
    Wall wallB = new Wall
    (
        0,
        3,
        new int[]{13,14,15,16,17,18},
        new int[]{6,6,6,6,6,6}
    );
    
    Wall wallC = new Wall
    (
        -1,
        3,
        new int[]{17,18,19,20,21,22,23,24,25},
        new int[]{4,6,6,6,6,6,6,6,6}
    );
    
    Wall wallD = new Wall
    (
        1,
        3,
        new int[]{6,7,8,9,10,11,12,13,14},
        new int[]{6,6,6,6,6,6,6,6,4}
    );
    
    Wall wallE = new Wall
    (
        -2,
        3,
        new int[]{24,25,26,27,28,29,30,31},
        new int[]{4,6,6,6,6,6,6,6}
    );
    
    Wall wallF = new Wall
    (
        2,
        3,
        new int[]{0,1,2,3,4,5,6,7},
        new int[]{6,6,6,6,6,6,6,4}
    );
    
    Wall[] walls =
    {
        wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, wall9, wallA,
        wallB, wallC, wallD, wallE, wallF
    };
    
    public Scene(int αx, int αy)
    {
        frame = GUI.getFrame();
        tiColors = new TiColors();
        sprites = new ArrayList<Sprite>();
        actions = new Action[ACTIONCNT];
        sceneX = αx;
        sceneY = αy;
        map = GUI.getWorkspace().getMap();
        addFocusListener(this);
        addMouseMotionListener(this);
        
        setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    public Scene(AsmData asmData)
    {
        frame = GUI.getFrame();
        tiColors = new TiColors();
        sprites = new ArrayList<Sprite>();
        actions = new Action[ACTIONCNT];
        sceneAngle = asmData.getByte();
        int sceneXY = asmData.getByte();
        sceneX = sceneXY & 0x0F;
        sceneY = (sceneXY >> 4) & 0x0F;
        map = GUI.getWorkspace().getMap();
        addFocusListener(this);
        addMouseMotionListener(this);
        // We don't need the reference to the scene
        asmData.getRef();
        byte counts = asmData.getByte();
        int spriteCnt = (counts >> 4) & 0x0F;
        int actionCnt = counts & 0x0F;
        ArrayList<Byte> spritesX = new ArrayList<Byte>();
        ArrayList<Byte> spritesY = new ArrayList<Byte>();
        ArrayList<Byte> spritesColor = new ArrayList<Byte>();
        sprites = new ArrayList<Sprite>();
   
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        for(int ι = spriteCnt; ι > 0; ι --)
        {
            spritesY.add(asmData.getByte());
            spritesX.add(asmData.getByte());
            spritesColor.add(asmData.getByte());
        }
        
        for(int ι = 0; ι < spriteCnt; ι ++)
        {
            BufferedImage sprite = new BufferedImage
                (SPRITEWIDTH, SPRITEHEIGHT, BufferedImage.TYPE_INT_ARGB);
            
            int rgbColor = tiColors.TItoRGB(spritesColor.get(ι));
            
            for(int y = 0; y < SPRITEHEIGHT; y ++)
            {
                byte row = asmData.getByte();
                for(int x = 0; x < SPRITEWIDTH; x ++, row <<= 1)
                {
                    if((row & 0x80) != 0) sprite.setRGB(x, y, rgbColor | 0xFF000000);
                    else sprite.setRGB(x, y, 0x00000000);
                }
            }
            
            sprites.add(new Sprite(spritesX.get(ι) & 0xFF, spritesY.get(ι) & 0xFF, sprite));
        }
        
        for(int ι = 0; ι < actionCnt; ι ++)
        {
            actions[ι] = new Action(asmData.getText(), asmData.getRef());
        }
    }
    
    public void addSprite(Image αimage)
    {
        sprites.add(new Sprite(mouseX / SPRITESCALE, mouseY / SPRITESCALE, αimage));
    }
    
    public Sprite getSprite(int αx, int αy)
    {
        
        for(Sprite sprite : sprites)
        {
            if
            (
                αx >= sprite.getX() &&
                αy >= sprite.getY() &&
                αx <= (sprite.getX() + SPRITEPMULT * SPRITEWIDTH) &&
                αy <= (sprite.getY() + SPRITEPMULT * SPRITEWIDTH)
            )
                return sprite;
        }
        
        return null;
    }
    
    public AsmData getData(String αsceneName, String αnextSceneName)
    {
        AsmData data = new AsmData();
        data.setName(αsceneName);
        data.addByte((byte)sceneAngle);
        data.addByte((byte)(sceneX | (sceneY << 4)));
        data.addRef(αnextSceneName);
        ArrayList<Action> outActions = new ArrayList<Action>();
        for(Action action : actions)
        {
            if(action != null) outActions.add(action);
        }
        
        data.addByte((byte)((sprites.size() << 4) + (outActions.size() & 0xF)));
        
        for(Sprite sprite : sprites)
        {
            data.addByte((byte)sprite.getY());
            data.addByte((byte)sprite.getX());
            data.addByte(tiColors.RGBtoTI(sprite.getColor()));
        }
        for(Sprite sprite : sprites)
        {
            BufferedImage bufferedSprite = (BufferedImage)sprite.getSprite();
            for(int y = 0; y < SPRITEHEIGHT; y++)
            {
                String row = new String();
                for(int x = 0; x < SPRITEWIDTH; x++)
                {
                    row +=
                    (
                        (bufferedSprite.getRGB(x, y) & 0x00FFFFFF) > 0 ?
                        '1' :
                        '0'
                    );
                }
                data.addByte((byte)Integer.parseInt(row, 2));
            }
        }
        for(Action action : outActions)
        {
            data.addText(action.getName());
            data.addRef(action.getLabel());
        }
        return data;
    }
    
    public void drawVertLine(int αheight, int αhpos, Graphics αgraphics)
    {
        int vpos = (HEIGHT / 2 - αheight / 2) * SCALE;
        
        if(!drawnl[αhpos])
        {
            drawnl[αhpos] = true;
            αheight *= SCALE;
            αhpos *= SCALE;
            αgraphics.drawRect
            (
                αhpos,
                vpos,
                SCALE,
                αheight
            );
        }
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
                drawnl = new boolean[WIDTH];
                graphics = bs.getDrawGraphics();
                
                // Draw walls
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, getWidth(), getHeight());
                graphics.setColor(Color.RED);
                for(Wall wall : walls)
                {
                    int rx = wall.xd;
                    int ry = wall.yd;
                    
                    if((sceneAngle & 1) > 0)
                    {
                        int β = rx;
                        rx = ry;
                        ry = -β;
                    }
                    
                    if((sceneAngle & 2) > 0)
                    {
                        rx = -rx;
                        ry = -ry;
                    }
                    
                    rx = Math.max(Math.min(sceneX + rx, 15), 0);
                    ry = Math.max(Math.min(sceneY + ry, 15), 0);
                    
                    if(map.isWall(rx, ry)) wall.draw(graphics);
                }
                
                // Draw sprites
                graphics.setColor(Color.GREEN);
                for(Sprite sprite : sprites)
                {
                    graphics.drawImage
                    (
                        sprite.getSprite(),
                        sprite.getX() * SPRITESCALE,
                        sprite.getY() * SPRITESCALE,
                        SPRITEPMULT * SPRITESCALE * SPRITEWIDTH,
                        SPRITEPMULT * SPRITESCALE * SPRITEHEIGHT,
                        null,
                        null
                    );
                    graphics.drawRect
                    (
                        sprite.getX() * SPRITESCALE,
                        sprite.getY() * SPRITESCALE,
                        SPRITEPMULT * SPRITESCALE * SPRITEWIDTH,
                        SPRITEPMULT * SPRITESCALE * SPRITEHEIGHT
                    );
                }
                
                // And last, draw the mouse position notification
                graphics.setColor(Color.WHITE);
                String notification =
                    (mouseX/SPRITESCALE) + ", " + (mouseY/SPRITESCALE);
                
                if(selectedSprite != null)
                {
                    notification += "(" + selectedSprite.getX() + ", ";
                    notification += selectedSprite.getY() + ")";
                }
                
                for(Action action : actions)
                {
                    if(action != null) notification +=
                        " " + action.getName() + "(" + action.getLabel() + ")";
                }
                
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
        GUI.getWorkspace().repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent αevent)
    {
        switch(αevent.getKeyCode())
        {
            case(KeyEvent.VK_SHIFT):
                shift = true;
                break;
            case(KeyEvent.VK_A):
                sceneAngle ++;
                sceneAngle &= 3;
                break;
            case(KeyEvent.VK_D):
                if(shift)
                {
                    if(selectedSprite != null)
                    {
                        sprites.remove(selectedSprite);
                        selectedSprite = null;
                    }
                }
                else
                {
                    sceneAngle --;
                    sceneAngle &= 3;
                }
                break;
            case(KeyEvent.VK_S):
                loadingSprite = true;
                selectedSprite = null;
                new LoadSprite(this);
                break;
            case(KeyEvent.VK_1):
                new ActionDialog(frame, actions, 0);
                break;
            case(KeyEvent.VK_2):
                new ActionDialog(frame, actions, 1);
                break;
            case(KeyEvent.VK_3):
                new ActionDialog(frame, actions, 2);
                break;
            case(KeyEvent.VK_4):
                new ActionDialog(frame, actions, 3);
                break;
            case(KeyEvent.VK_5):
                new ActionDialog(frame, actions, 4);
                break;
            case(KeyEvent.VK_6):
                new ActionDialog(frame, actions, 5);
                break;
            case(KeyEvent.VK_7):
                new ActionDialog(frame, actions, 6);
                break;
            case(KeyEvent.VK_8):
                new ActionDialog(frame, actions, 7);
                break;
            case(KeyEvent.VK_LEFT):
                if(selectedSprite != null) selectedSprite.decX(1);
                break;
            case(KeyEvent.VK_RIGHT):
                if(selectedSprite != null) selectedSprite.incX(1);
                break;
            case(KeyEvent.VK_UP):
                if(selectedSprite != null) selectedSprite.decY(1);
                break;
            case(KeyEvent.VK_DOWN):
                if(selectedSprite != null) selectedSprite.incY(1);
                break;
        }
        
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
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent ε)
    {
        mouseX = ε.getX();
        mouseY = ε.getY();
        
        if(selectedSprite != null && !loadingSprite)
        {
            Image spriteImage = selectedSprite.getSprite();
            sprites.remove(selectedSprite);
            sprites.add
                (selectedSprite = new Sprite(mouseX / SPRITESCALE, mouseY / SPRITESCALE, spriteImage));
        }
        
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent ε)
    {
        mouseX = ε.getX();
        mouseY = ε.getY();
        repaint();
        selectedSprite = getSprite(mouseX / SPRITESCALE, mouseY / SPRITESCALE);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        addKeyListener(this);
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        removeKeyListener(this);
    }
}
