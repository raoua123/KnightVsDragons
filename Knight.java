import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Knight {
    public float x, y;
    public float velocityX = 0, velocityY = 0;
    private boolean onGround = false;
    private boolean facingRight = true;
    private int frame = 0;
    private int health = 100;
    private int attackCooldown = 0;
    private int hitCooldown = 0;
    
    public Knight(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
public void keyPressed(int keyCode) {
    if (hitCooldown > 0) return;
    
    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
        velocityX = -4;
    }
    if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
        velocityX = 4;
    }
    if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) && onGround) {
        velocityY = -16;
        onGround = false;
    }
    if (keyCode == KeyEvent.VK_A && attackCooldown == 0) {
        attackCooldown = 20;
    }
}

public void keyReleased(int keyCode) {
    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
        velocityX = 0;
    }
    if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
        velocityX = 0;
    }
}
    public void update(ArrayList<Platform> platforms) {
        if (attackCooldown > 0) attackCooldown--;
        if (hitCooldown > 0) hitCooldown--;
        
        x += velocityX;
        y += velocityY;
        velocityY += 0.6f;
        
        if (x < 0) x = 0;
        if (x > 5000) x = 5000;
        
        if (velocityX != 0 && onGround) {
            frame = (frame + 1) % 8;
        }
        
        onGround = false;
        Rectangle knightRect = new Rectangle((int)x, (int)y, 32, 48);
        
        for (Platform p : platforms) {
            Rectangle platformRect = new Rectangle((int)p.x, (int)p.y, p.width, p.height);
            
            if (velocityY > 0 && knightRect.intersects(platformRect)) {
                if (y + 48 <= p.y + 10) {
                    y = p.y - 48;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }
        
        if (y > 550) {
            y = 550;
            velocityY = 0;
            onGround = true;
        }
    }
    
    public void draw(Graphics2D g, int cameraX) {
        int drawX = (int)(x - cameraX);
        int drawY = (int)y;
        
        Color armor = new Color(100, 140, 200);   
        Color armorDark = new Color(70, 100, 160); 
        Color cape = new Color(180, 60, 60);       
        Color skin = new Color(255, 200, 150);     
        Color sword = new Color(220, 220, 220);    
        
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(drawX + 4, drawY + 48, 24, 6);
        
        g.setColor(cape);
        g.fillRect(drawX + 4, drawY + 16, 24, 28);
        
        g.setColor(armorDark);
        g.fillRect(drawX, drawY, 32, 48);
        
        g.setColor(armor);
        g.fillRect(drawX + 2, drawY + 2, 28, 44);
        
        g.setColor(skin);
        g.fillRect(drawX + 8, drawY - 12, 16, 14);
        
        g.setColor(armorDark);
        g.fillRect(drawX + 6, drawY - 14, 20, 6);
        g.fillRect(drawX + 4, drawY - 8, 24, 4);
        
        g.setColor(Color.BLACK);
        g.fillRect(drawX + 10, drawY - 6, 3, 3);
        g.fillRect(drawX + 19, drawY - 6, 3, 3);
        
        g.fillRect(drawX + 12, drawY - 2, 8, 2);
        
        int legOffset = (frame % 4 < 2) ? 0 : 2;
        g.setColor(armorDark);
        
        g.fillRect(drawX + 6, drawY + 46, 8, 14 + legOffset);
        g.fillRect(drawX + 18, drawY + 46, 8, 14 - legOffset);
        
        g.setColor(new Color(60, 50, 40));
        g.fillRect(drawX + 4, drawY + 56 + legOffset, 10, 6);
        g.fillRect(drawX + 18, drawY + 56 - legOffset, 10, 6);
        
        if (attackCooldown > 0) {
            g.setColor(sword);
            g.fillRect(drawX + 32, drawY + 12, 20, 4); 
            g.setColor(new Color(150, 100, 50));
            g.fillRect(drawX + 30, drawY + 10, 4, 8);  
        } else {
            g.setColor(sword);
            g.fillRect(drawX + 28, drawY + 6, 16, 3);
        }
        
        drawHealthBar(g, drawX, drawY);
        
        if (hitCooldown > 0 && hitCooldown % 4 < 2) {
            g.setColor(new Color(255, 50, 50, 100));
            g.fillRect(drawX, drawY, 32, 48);
        }
    }
    
    private void drawHealthBar(Graphics2D g, int x, int y) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(x - 2, y - 20, 36, 8);
        
        g.setColor(new Color(255, 50, 50));
        g.fillRect(x, y - 18, 32, 4);
        
        g.setColor(new Color(50, 255, 50));
        g.fillRect(x, y - 18, (int)(32 * health / 100.0), 4);
        
        g.setColor(Color.WHITE);
        g.drawRect(x - 2, y - 20, 36, 8);
    }
    
    public void takeDamage(int damage) {
        if (hitCooldown == 0) {
            health -= damage;
            hitCooldown = 30;
            if (health < 0) health = 0;
        }
    }
    
    public boolean isDead() {
        return health <= 0;
    }
    
    public void heal(int amount) {
        health += amount;
        if (health > 100) health = 100;
    }
    
    public boolean isAttacking() {
        return attackCooldown > 15;
    }
    
    public Rectangle getAttackBounds() {
        if (isAttacking()) {
            return new Rectangle((int)x + 32, (int)y + 10, 20, 20);
        }
        return new Rectangle(0, 0, 0, 0);
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 32, 48);
    }
}