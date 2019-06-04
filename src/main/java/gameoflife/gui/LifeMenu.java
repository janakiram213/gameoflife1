package gameoflife.gui;

import gameoflife.LifeUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle;
import net.lingala.zip4j.exception.ZipException;

public class LifeMenu extends JDialog{
    
    private final static String lifDirectoryPath = "lifep.zip";
    private boolean continueRunning = false;
    private File choosenFile = null;          //the file choosen for the game
    
    private final JButton startButton;
    private final JButton browseButton;
    private final JButton exitButton;
    
    private JComboBox lifeList;
    
    public LifeMenu() {
    
        try{
            
            File f = new File(getClass().getClassLoader().getResource(lifDirectoryPath).getFile()) ;
            lifeList = new JComboBox(LifeUtils.unzip(f).listFiles());
        }
        catch(NullPointerException|ZipException|IOException ex) { //if there is and error while unpacking the zip, 
            System.out.println(ex);                                                      //then the combobox will be empty, and frozen
            lifeList = new JComboBox(); 
            lifeList.setEnabled(false); 
        } 
         
        lifeList.setRenderer(new ComboBoxRenderer());   // Setting up custom renderer for the combobox, 
                                                         //so it can display just the name of the lif, not the whole path. 
        lifeList.addActionListener(new ActionListener() {                
            @Override
            public void actionPerformed(ActionEvent e) { 
                choosenFile = (File) lifeList.getSelectedItem();
                startButton.setEnabled(true);
            }
        });
        
        browseButton = new JButton("Browse files");
        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(LifeMenu.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    choosenFile = fc.getSelectedFile();
                    startButton.setEnabled(true);
                }
            }
        });
        
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                continueRunning = true;
                dispose();
            }
        });
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                continueRunning = false;
                dispose();
            }
        });
        
        //setting up the layouts
        GroupLayout mainLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(  
            mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()          
                    .addContainerGap()
                    .addComponent(exitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(316,316,316)
                    .addComponent(startButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()
            )
            .addGroup( GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                    .addGap(210,210,210)
                    .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(browseButton, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(lifeList, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    )
                    .addGap(194, 194, 194)
            )
        );
        
        mainLayout.setVerticalGroup( 
                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(lifeList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(browseButton)
                        .addGap(26,26,26)
                        .addGroup(
                            mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(startButton)
                            .addComponent(exitButton)
                        )
                        .addContainerGap()
                )
        );
        //layouts are ready 
        
        pack();
        setTitle("Game Of Life - Menu");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
    }
    
    public boolean run(){
    
        this.setModal(true);
        this.setVisible(true);
        return continueRunning;
    }
    
    public boolean getNextStep()    { return continueRunning; }
    public File getChoosenFile()    { return choosenFile; }
    
}
