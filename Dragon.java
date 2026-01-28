import java.awt.*;
import java.util.Random;

public class Dragon {
    public float x, y;
    private float speed = 1.0f;
    private Random random = new Random();
    private int frame = 0;
    private int direction = 1;
    private float startX;
    private int level;
    private int health = 100;
    private boolean alive = true;
    private int attackCooldown = 0;
    private int questionTimer = 0;
    private boolean askingQuestion = false;
    private Dragon currentDragon;
    
    public Dragon(float x, float y, int level) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.level = level;
        this.speed += level * 0.2f;
        this.health += level * 25;
    }
    
    public void update() {
        if (!alive) return;
        
        frame++;
        
        x += direction * speed;
        
        if (Math.abs(x - startX) > 100) {
            direction *= -1;
        }
        
        if (attackCooldown > 0) attackCooldown--;
        
        if (!askingQuestion && random.nextInt(200) == 0) {
            askingQuestion = true;
            questionTimer = 120; 
        }
        
        if (askingQuestion) {
            questionTimer--;
            if (questionTimer <= 0) {
                askingQuestion = false;
            }
        }
    }
    
    public void draw(Graphics2D g, int cameraX) {
        if (!alive) return;
        
        int drawX = (int)(x - cameraX);
        int drawY = (int)y;
        
        Color body = getDragonColor(level);
        Color belly = new Color(240, 230, 200);
        Color dark = body.darker();
        Color eye = new Color(255, 255, 100);
        
        g.setColor(new Color(0, 0, 0, 60));
        g.fillRect(drawX + 4, drawY + 32, 24, 6);
        
        g.setColor(dark);
        g.fillRect(drawX, drawY, 32, 24);
        
        g.setColor(body);
        g.fillRect(drawX + 2, drawY + 2, 28, 20);
        
      
        g.setColor(belly);
        g.fillRect(drawX + 6, drawY + 12, 20, 10);
        
      
        g.setColor(dark);
        g.fillRect(drawX + 28, drawY - 8, 16, 16);
        
        g.setColor(body);
        g.fillRect(drawX + 30, drawY - 6, 12, 12);
        
   
        g.setColor(eye);
        g.fillRect(drawX + 32, drawY - 2, 3, 3);
        g.fillRect(drawX + 38, drawY - 2, 3, 3);
        
      
        g.setColor(new Color(150, 80, 80));
        g.fillRect(drawX + 34, drawY + 2, 6, 2);
     
        if (askingQuestion) {
            g.setColor(new Color(255, 255, 255));
            g.fillRect(drawX + 36, drawY - 24, 16, 14);
            g.setColor(Color.BLACK);
            g.drawRect(drawX + 36, drawY - 24, 16, 14);
            g.setFont(new Font("Monospaced", Font.BOLD, 12));
            g.drawString("?", drawX + 41, drawY - 14);
        }
        
   
        drawHealthBar(g, drawX, drawY);
  
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 10));
        g.drawString("L" + level, drawX + 12, drawY - 6);
    }
    
    private Color getDragonColor(int level) {
        switch (level % 5) {
            case 0: return new Color(200, 100, 100);   
            case 1: return new Color(100, 180, 100);    
            case 2: return new Color(100, 140, 200);    
            case 3: return new Color(180, 140, 100);    
            case 4: return new Color(180, 100, 180);    
            default: return new Color(200, 100, 100);
        }
    }
    
    private void drawHealthBar(Graphics2D g, int x, int y) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(x - 4, y - 16, 40, 6);
        
        g.setColor(new Color(255, 50, 50));
        g.fillRect(x - 2, y - 14, 36, 2);
        
        g.setColor(new Color(50, 255, 50));
        g.fillRect(x - 2, y - 14, (int)(36 * health / 100.0), 2);
        
        g.setColor(Color.WHITE);
        g.drawRect(x - 4, y - 16, 40, 6);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public boolean triggerQuestion() {
        if (!askingQuestion) {
            askingQuestion = true;
            questionTimer = 300; 
            return true;
        }
        return false;
    }
    public boolean hasActiveQuestion() {
    return askingQuestion && questionTimer > 0;
}
    public boolean isAskingQuestion() {
        return askingQuestion;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x - 16, (int)y - 8, 48, 32);
    }
}