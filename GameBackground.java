import java.awt.*;
import java.util.Random;

public class GameBackground {
    private Random random = new Random();
    private int[] starX = new int[50];
    private int[] starY = new int[50];
    private boolean[] starOn = new boolean[50];
    
    public GameBackground() {
        for (int i = 0; i < 50; i++) {
            starX[i] = random.nextInt(8000);
            starY[i] = random.nextInt(250);
            starOn[i] = random.nextBoolean();
        }
    }
    
    public void draw(Graphics2D g, int cameraX, int level) {
        Color[] skyColors = {
            new Color(20, 20, 40),    
            new Color(30, 20, 50),    
            new Color(20, 40, 30),    
            new Color(40, 20, 30),    
            new Color(20, 30, 50)    
        };
        
        g.setColor(skyColors[level % skyColors.length]);
        g.fillRect(0, 0, 1024, 400);
        
        drawPixelStars(g, cameraX);
        
        drawCelestial(g, cameraX, level);
        
        drawPixelMountains(g, cameraX, level);
        
        drawPixelGround(g, level);
        
        drawPixelFeatures(g, cameraX, level);
    }
    
    private void drawPixelStars(Graphics2D g, int cameraX) {
        g.setColor(Color.WHITE);
        
        for (int i = 0; i < 50; i++) {
            int drawX = (starX[i] - cameraX/4) % 8000;
            if (drawX < -10) drawX += 8000;
            
            if (drawX >= 0 && drawX < 1024) {
                if (random.nextInt(100) == 0) {
                    starOn[i] = !starOn[i];
                }
                
                if (starOn[i]) {
                    g.fillRect(drawX, starY[i], 2, 2);
                }
            }
        }
    }
    
    private void drawCelestial(Graphics2D g, int cameraX, int level) {
        int celestialX = 800 - cameraX/20;
        
        if (level % 2 == 0) {
            g.setColor(new Color(220, 220, 200));
            g.fillRect(celestialX - 15, 60, 30, 30);
            
            g.setColor(new Color(200, 200, 180));
            g.fillRect(celestialX - 8, 65, 6, 6);
            g.fillRect(celestialX + 3, 70, 4, 4);
            g.fillRect(celestialX - 3, 80, 5, 5);
        } else {
            g.setColor(new Color(255, 200, 50));
            g.fillRect(celestialX - 20, 50, 40, 40);
            
            g.setColor(new Color(255, 150, 0));
            for (int i = 0; i < 8; i++) {
                int rayX = celestialX + (int)(25 * Math.cos(i * Math.PI / 4));
                int rayY = 70 + (int)(25 * Math.sin(i * Math.PI / 4));
                g.fillRect(rayX - 2, rayY - 2, 4, 4);
            }
        }
    }
    
    private void drawPixelMountains(Graphics2D g, int cameraX, int level) {
        Color[] mountainColors = {
            new Color(40, 45, 60),
            new Color(50, 40, 70),
            new Color(40, 60, 50),
            new Color(60, 40, 40),
            new Color(40, 40, 70)
        };
        
        Color mountainColor = mountainColors[level % mountainColors.length];
        
        for (int i = 0; i < 5; i++) {
            int baseX = (i * 250 - cameraX/10) % 1250;
            if (baseX < -250) baseX += 1250;
            
            if (baseX > -250 && baseX < 1024) {
                int[] xPoints = {baseX, baseX + 125, baseX + 250};
                int[] yPoints = {400, 320, 400};
                
                g.setColor(mountainColor);
                g.fillPolygon(xPoints, yPoints, 3);
                
                if (level > 2) {
                    g.setColor(Color.WHITE);
                    g.fillRect(baseX + 50, 340, 125, 20);
                }
            }
        }
    }
    
    private void drawPixelGround(Graphics2D g, int level) {
        Color[] groundColors = {
            new Color(60, 50, 40),
            new Color(70, 50, 50),
            new Color(50, 60, 45),
            new Color(70, 45, 45),
            new Color(50, 45, 70)
        };
        
        g.setColor(groundColors[level % groundColors.length]);
        g.fillRect(0, 400, 1024, 368);
        
        g.setColor(new Color(70, 90, 60));
        for (int i = 0; i < 256; i++) {
            if (i % 8 == 0) {
                g.fillRect(i * 4, 400, 4, 8);
            }
        }
        
        g.setColor(new Color(90, 70, 60));
        g.fillRect(0, 420, 1024, 40);
        
        g.setColor(new Color(110, 90, 80));
        for (int i = 0; i < 128; i++) {
            if (i % 16 == 0) {
                g.fillRect(i * 8, 425, 8, 8);
            }
        }
    }
    
    private void drawPixelFeatures(Graphics2D g, int cameraX, int level) {
        for (int i = 0; i < 20; i++) {
            int treeX = (i * 100 - cameraX/8) % 2000;
            if (treeX < -50) treeX += 2000;
            
            if (treeX >= 0 && treeX < 1024) {
                g.setColor(new Color(80, 60, 50));
                g.fillRect(treeX + 10, 380, 5, 20);
                
                g.setColor(new Color(40, 80, 40));
                g.fillRect(treeX, 370, 25, 15);
            }
        }
        
        if (level > 1) {
            int castleX = 2000 - cameraX/3;
            if (castleX > -100 && castleX < 1124) {
                g.setColor(new Color(100, 90, 80));
                g.fillRect(castleX, 350, 20, 50);
                g.fillRect(castleX + 40, 350, 20, 50);
                
                g.fillRect(castleX + 10, 370, 40, 30);
                
                g.setColor(Color.RED);
                g.fillRect(castleX + 8, 340, 4, 10);
                g.fillRect(castleX + 48, 340, 4, 10);
                
                g.setColor(new Color(255, 200, 0));
                g.fillRect(castleX + 15, 380, 5, 5);
                g.fillRect(castleX + 40, 380, 5, 5);
            }
        }
    }
}