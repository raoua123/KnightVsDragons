import java.awt.*;

public class Platform {
    public float x, y;
    public int width, height;
    private Color color;
    private Color highlight;
    
    public Platform(float x, float y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.highlight = color.brighter();
    }
    
    public void draw(Graphics2D g, int cameraX) {
        int drawX = (int)(x - cameraX);
        
      
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(drawX + 3, (int)y + 3, width, height);
        
        g.setColor(color);
        g.fillRect(drawX, (int)y, width, height);
        
        g.setColor(highlight);
        g.fillRect(drawX, (int)y, width, 3);
        g.fillRect(drawX, (int)y, 3, height);
        
        g.setColor(color.darker());
        for (int i = 0; i < width / 8; i++) {
            for (int j = 0; j < height / 8; j++) {
                if ((i + j) % 3 == 0) {
                    g.fillRect(drawX + i * 8 + 2, (int)y + j * 8 + 2, 4, 4);
                }
            }
        }
        
        if (Math.random() > 0.7) {
            g.setColor(new Color(60, 100, 60));
            for (int i = 0; i < 3; i++) {
                int mossX = drawX + (int)(Math.random() * width);
                int mossY = (int)y - 3 - i * 4;
                g.fillRect(mossX, mossY, 2, 3 + i * 2);
            }
        }
    }
}