import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SideScrollingGame extends JPanel implements ActionListener {
    private Knight knight;
    private ArrayList<Dragon> dragons;
    private ArrayList<Platform> platforms;
    private ArrayList<Question> questions;
    private GameBackground background;
    private Random random = new Random();
    
    private int cameraX = 0;
    private int currentLevel = 1;
    private int score = 0;
    private int lives = 3;
    private boolean showingQuestion = false;
    private Question currentQuestion;
    private int questionTimer = 300; 
    private Timer gameTimer;
    private boolean gameOver = false;
    private boolean levelComplete = false;
    private int levelWidth = 3000;
    private int dragonsDefeated = 0;
    
    private int[] levelDragonCount = {3, 4, 5, 6, 7};
    
    private Dragon currentDragon = null;
    
    public SideScrollingGame() {
        setPreferredSize(new Dimension(1024, 768));
        setBackground(Color.BLACK);
        
        initializeGame();
        
        gameTimer = new Timer(16, this);
        gameTimer.start();
        
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                
                if (!gameOver && !levelComplete && !showingQuestion) {
                    knight.keyPressed(keyCode);
                }
                
                if (keyCode == KeyEvent.VK_R && (gameOver || levelComplete)) {
                    initializeGame();
                }
                
                if (showingQuestion) {
                    if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) {
                        checkAnswer(keyCode - KeyEvent.VK_1);
                    } 
                    else if (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD4) {
                        checkAnswer(keyCode - KeyEvent.VK_NUMPAD1);
                    }
                }
            }
            
            public void keyReleased(KeyEvent e) {
                if (!gameOver && !levelComplete && !showingQuestion) {
                    knight.keyReleased(e.getKeyCode());
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
    }
    
    private void initializeGame() {
        knight = new Knight(100, 500);
        dragons = new ArrayList<>();
        platforms = new ArrayList<>();
        questions = new ArrayList<>();
        background = new GameBackground();
        
        cameraX = 0;
        currentLevel = 1;
        score = 0;
        lives = 3;
        showingQuestion = false;
        gameOver = false;
        levelComplete = false;
        dragonsDefeated = 0;
        questionTimer = 300;
        currentDragon = null; 
        
        createPlatforms();
        loadQuestions();
        spawnDragons();
    }
    
    private void createPlatforms() {
        platforms.clear();
        
        platforms.add(new Platform(-100, 600, levelWidth + 200, 100, new Color(80, 60, 50)));
        
        platforms.add(new Platform(0, 550, levelWidth, 50, new Color(90, 70, 60)));
        
        for (int i = 0; i < levelDragonCount[currentLevel - 1]; i++) {
            int dragonX = 300 + (i * 200);
            int platformY = 450;  
            
            platforms.add(new Platform(dragonX - 40, platformY, 120, 20, new Color(100, 80, 70)));
            
            platforms.add(new Platform(dragonX - 60, platformY + 20, 40, 15, new Color(110, 90, 80)));
            platforms.add(new Platform(dragonX - 40, platformY + 35, 40, 15, new Color(120, 100, 90)));
            platforms.add(new Platform(dragonX - 20, platformY + 50, 40, 15, new Color(130, 110, 100)));
            
            dragons.add(new Dragon(dragonX, platformY - 30, currentLevel));
        }
        
        for (int i = 0; i < levelDragonCount[currentLevel - 1] - 1; i++) {
            int startX = 300 + (i * 200) + 60;
            int endX = 300 + ((i + 1) * 200) - 60;
            int connectY = 500 + random.nextInt(50);
            
            platforms.add(new Platform(startX, connectY, endX - startX, 20, 
                new Color(80, 70, 60)));
        }
    }
    
    private void loadQuestions() {
        questions.clear();
        
        questions.add(new Question(
            "Which keyword prevents inheritance?",
            new String[]{"final", "private", "static", "abstract"},
            0
        ));
        questions.add(new Question(
            "What does 'public static void main' mean?",
            new String[]{"Program entry point", "Method declaration", "Class starter", "Import statement"},
            0
        ));
        questions.add(new Question(
            "Which is NOT a Java primitive type?",
            new String[]{"String", "int", "boolean", "double"},
            0
        ));
        questions.add(new Question(
            "How to create an ArrayList of Strings?",
            new String[]{"ArrayList<String>", "String[]", "List(String)", "Array[String]"},
            0
        ));
        questions.add(new Question(
            "What is JVM?",
            new String[]{"Java Virtual Machine", "Java Visual Mod", "Java Variable Mod", "Java Vector Machine"},
            0
        ));
        questions.add(new Question(
            "Which loop is guaranteed once?",
            new String[]{"do-while", "for", "while", "foreach"},
            0
        ));
        questions.add(new Question(
            "What is polymorphism?",
            new String[]{"Many forms", "One form", "No form", "Same form"},
            0
        ));
        questions.add(new Question(
            "Which is a Java interface?",
            new String[]{"Runnable", "Abstract", "Final", "Static"},
            0
        ));
    }
    
    private void spawnDragons() {
        dragons.clear();
        int dragonCount = levelDragonCount[currentLevel - 1];
        
        for (int i = 0; i < dragonCount; i++) {
            int x = 300 + (i * 200);
            int y = 420;  
            dragons.add(new Dragon(x, y, currentLevel));
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (gameOver || levelComplete) return;
        
        if (!showingQuestion) {
            knight.update(platforms);
            
            cameraX = (int)knight.x - 512;
            if (cameraX < 0) cameraX = 0;
            if (cameraX > levelWidth - 1024) cameraX = levelWidth - 1024;
            
            Iterator<Dragon> it = dragons.iterator();
            while (it.hasNext()) {
                Dragon dragon = it.next();
                dragon.update();
                
                if (knight.getBounds().intersects(dragon.getBounds()) && dragon.isAlive()) {
                    if (!showingQuestion) {
                        dragon.triggerQuestion();
                        showingQuestion = true;
                        currentQuestion = questions.get(random.nextInt(questions.size()));
                        questionTimer = 300;
                        currentDragon = dragon; 
                        break;
                    }
                }
            }
            
            if (knight.y > 700) {
                knight.x = cameraX + 100;
                knight.y = 500;
                knight.takeDamage(20);
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                }
            }
            
            if (dragons.isEmpty()) {
                levelComplete = true;
                score += 500 * currentLevel;
            }
            
            if (knight.x > levelWidth - 100) {
                levelComplete = true;
            }
            
        } else {
            questionTimer--;
            if (questionTimer <= 0) {
                showingQuestion = false;
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                }
                currentDragon = null;
            }
        }
        
        repaint();
    }
    
    private void checkAnswer(int answerIndex) {
        showingQuestion = false;
        
        if (answerIndex == currentQuestion.correctIndex) {
            score += 200 * currentLevel;
            
            if (currentDragon != null && currentDragon.isAlive()) {
                currentDragon.takeDamage(100); 
                if (!currentDragon.isAlive()) {
                    dragons.remove(currentDragon);
                    dragonsDefeated++;
                    score += 100 * currentLevel;
                }
            }
            
        } else {
            knight.takeDamage(30); 
            lives--;
            
            knight.x -= 50;
            
            if (lives <= 0) {
                gameOver = true;
            }
        }
        
        currentDragon = null; 
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        background.draw(g2d, cameraX, currentLevel);
        
        for (Platform p : platforms) {
            p.draw(g2d, cameraX);
        }
        
        for (Dragon dragon : dragons) {
            dragon.draw(g2d, cameraX);
        }
        
        knight.draw(g2d, cameraX);
        
        drawPixelUI(g2d);
        
        if (showingQuestion) {
            drawPixelQuestion(g2d);
        }
        
        if (gameOver) {
            drawPixelGameOver(g2d);
        }
        if (levelComplete) {
            drawPixelLevelComplete(g2d);
        }
    }
    
    private void drawPixelUI(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(10, 10, 250, 90);
        g.setColor(Color.WHITE);
        g.drawRect(10, 10, 250, 90);
        
        g.setColor(new Color(100, 200, 255));
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString("CODE KNIGHT", 20, 30);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("Level: " + currentLevel, 20, 50);
        g.drawString("Score: " + score, 20, 70);
        g.drawString("Lives: " + "♥".repeat(lives) + "♡".repeat(3 - Math.max(0, lives)), 20, 90);
        
        g.drawString("Dragons: " + dragons.size() + "/" + levelDragonCount[currentLevel - 1], 150, 50);
        
        g.setColor(new Color(200, 200, 100));
        g.drawString("←→/AD Move", 650, 30);
        g.drawString("↑/Space Jump", 650, 50);
        g.drawString("A Attack", 650, 70);
        g.drawString("1-4/Numpad Answer", 650, 90);
        g.drawString("R Restart", 650, 110);
    }
    
    private void drawPixelQuestion(Graphics2D g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(new Color(40, 30, 50));
        g.fillRect(centerX - 250, centerY - 180, 500, 360);
        
      
        g.setColor(new Color(100, 200, 255));
        for (int i = 0; i < 4; i++) {
            g.drawRect(centerX - 250 - i, centerY - 180 - i, 500 + i * 2, 360 + i * 2);
        }
        
        g.setColor(new Color(255, 255, 100));
        g.setFont(new Font("Monospaced", Font.BOLD, 24));
        g.drawString("⚔️ JAVA QUIZ ⚔️", centerX - 100, centerY - 130);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        
        String[] words = currentQuestion.text.split(" ");
        String line = "";
        int textY = centerY - 80;
        
        for (String word : words) {
            if (g.getFontMetrics().stringWidth(line + word) < 460) {
                line += word + " ";
            } else {
                g.drawString(line, centerX - 230, textY);
                textY += 25;
                line = word + " ";
            }
        }
        g.drawString(line, centerX - 230, textY);
        
        String[] numbers = {"[1]", "[2]", "[3]", "[4]"};
        int buttonY = centerY - 20;
        
        for (int i = 0; i < 4; i++) {
            Color btnColor = new Color(60 + i * 20, 80 + i * 15, 120 - i * 10);
            g.setColor(btnColor);
            g.fillRect(centerX - 240, buttonY + i * 60, 480, 40);
            
            g.setColor(Color.WHITE);
            g.drawRect(centerX - 240, buttonY + i * 60, 480, 40);
            
            g.setFont(new Font("Monospaced", Font.BOLD, 16));
            g.drawString(numbers[i] + " " + currentQuestion.options[i], 
                        centerX - 220, buttonY + i * 60 + 25);
        }
        
        g.setColor(new Color(255, 200, 100));
        g.setFont(new Font("Monospaced", Font.ITALIC, 14));
        int seconds = questionTimer / 60;
        int tenths = (questionTimer % 60) / 6;
        g.drawString("Time: " + seconds + "." + tenths + "s", centerX - 50, centerY + 160);
    }
    
    private void drawPixelGameOver(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.BOLD, 48));
        g.drawString("GAME OVER", 350, 300);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 24));
        g.drawString("Score: " + score, 400, 360);
        g.drawString("Level: " + currentLevel, 400, 400);
        g.drawString("Press R to Restart", 400, 460);
    }
    
    private void drawPixelLevelComplete(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (currentLevel < 5) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            g.drawString("LEVEL " + currentLevel + " CLEAR!", 280, 300);
            
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.PLAIN, 24));
            g.drawString("Score: +" + (500 * currentLevel), 400, 360);
            g.drawString("Dragons Defeated: " + dragonsDefeated, 400, 400);
            g.drawString("Press R for Level " + (currentLevel + 1), 400, 460);
        } else {
            g.setColor(new Color(255, 215, 0));
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            g.drawString("YOU WIN!", 400, 300);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 24));
            g.drawString("Final Score: " + score, 400, 360);
            g.drawString("All Dragons Defeated!", 400, 400);
            g.drawString("Press R to Play Again", 400, 460);
        }
    }
}