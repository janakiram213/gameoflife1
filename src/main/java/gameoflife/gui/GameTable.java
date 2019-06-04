package gameoflife.gui;

import gameoflife.LifeUtils;
import gameoflife.lifereader.Pair;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JCheckBox;

public class GameTable extends JDialog{
    
    private boolean[][] status;
    private boolean[][] nextStatus;
    
    private ArrayList<Integer> rulesOfLiving; //it contains the number of alive neighbours required to continue living
    private ArrayList<Integer> rulesOfBirth;  //it contains the number of alive neighbours required to the birth of a cell
    
    private JPanel gamePanel;
    private JButton startButton;
    private JButton nextStepButton;
    private JButton backToMenuButton;
    private JLabel generationCounter;
    private JCheckBox gridCheck;
    
    private boolean play;
    private boolean displayGrid;
    private boolean toMenu;
    
    private int generation;
    
    public GameTable(Pair<ArrayList<Integer>,ArrayList<Integer>> rules,boolean[][] cells){
    
        this.status = cells;
        this.rulesOfLiving = rules.first;
        this.rulesOfBirth = rules.second;
        this.play = false;
        this.displayGrid = true;
        this.toMenu = false;
        this.generation = 0;
        
        gamePanel = new JPanel(){
        
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                
                //paint the background
                g.setColor(this.getBackground());
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                
                //paint the status
                for(int i=0;i<status.length;i++){
                    for(int j=0;j<status[0].length;j++){
                        if(status[i][j]) {
                            g.setColor(new Color(255, 187, 91));
                            int x = j * this.getWidth()/status[0].length;
                            int y = i * this.getHeight()/status.length;
                            g.fillRect(x, y, this.getWidth()/status[0].length, this.getHeight()/status.length);
                        }
                    }
                }
                
                //paint the grid
                if(displayGrid){
                    g.setColor(Color.BLACK);
                    for (int i = 1; i < status.length; i++) {
                        int y = i * this.getHeight() / status.length;
                        g.drawLine(0, y, this.getWidth(), y);
                    }
                    for (int j = 1; j < status[0].length; j++) {
                        int x = j * this.getWidth() / status[0].length;
                        g.drawLine(x, 0, x, this.getHeight());
                    }
                }
            }
        };
        gamePanel.setBackground(Color.WHITE);
        
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                play = !play;
                if(play) { startButton.setText("Pause"); }
                else     { startButton.setText("Play"); }
            }
        });
        
        nextStepButton = new JButton("Next Generation");
        nextStepButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {  update(); }
        });
        
        backToMenuButton = new JButton("To Menu");
        backToMenuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toMenu = true;
                dispose();
            }
        });
        
        gridCheck = new JCheckBox("Grid");
        gridCheck.setSelected(true);
        gridCheck.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) { displayGrid = !displayGrid; }
        });
        
        generationCounter = new JLabel("Generation:");
        
        
        GroupLayout panelLayout = new GroupLayout(gamePanel);
        gamePanel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
        );
        
        //setting up layout
        GroupLayout mainLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                    .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(mainLayout.createSequentialGroup()
                            .addGap(125, 125, 125)
                                
                            .addComponent(backToMenuButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(7, 7, 7)
                                
                            .addComponent(gridCheck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGap(7, 7, 7)
                            .addComponent(startButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nextStepButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(101, 101, 101)
                            .addComponent(generationCounter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(101, 101, 101)    
                        )
                        .addGroup(mainLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                    )
                .addContainerGap()
            )
        );
        
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gamePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(startButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextStepButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(generationCounter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(gridCheck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(backToMenuButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        //layout is ready
        
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Game of Life");
    }
    
    public boolean run(){
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            
            @Override
            public void run(){ if(play) { update(); } }
        };
        
        timer.scheduleAtFixedRate(task, 100, 100);
        
        setModal(true);
        setVisible(true);
        
        timer.cancel();
        return toMenu;
    }
    
    private void update(){
        calculateStatus();
        generationCounter.setText("Generation: " + generation++);
        if(System.getProperty("os.name").equals("Linux")){ Toolkit.getDefaultToolkit().sync();  }
        gamePanel.repaint();
        gamePanel.revalidate();
    }
    
    private void calculateStatus(){
    
        nextStatus = new boolean[status.length][status[0].length];
        
        for(int i=0;i<status.length;i++){
            for(int j=0;j<status[0].length;j++){
                int numberOfAliveNeighbours = LifeUtils.numberOfAliveNeighbours(status, i, j);
                if     (status[i][j] && rulesOfLiving.contains(numberOfAliveNeighbours)) {nextStatus[i][j]=true;}
                else if(!status[i][j] && rulesOfBirth.contains(numberOfAliveNeighbours)) {nextStatus[i][j]=true;}
                else                                                                     {nextStatus[i][j]=false;}
            }
        }
        status = nextStatus;
    }
    
    
}
