package gameoflife;

import gameoflife.gui.ErrorDialog;
import gameoflife.gui.GameTable;
import gameoflife.gui.LifeMenu;
import gameoflife.lifereader.LifeReader;
import gameoflife.lifereader.NotSupportedLifeVersionException;
import gameoflife.lifereader.Pair;
import java.awt.Point;
import java.io.FileNotFoundException;

public class GameOfLife {

    public GameOfLife(){ }
    
    public void run(){
        
        boolean countinueToRun = true;
        
        while(countinueToRun){
            
            try{
                LifeMenu menu = new LifeMenu();

                if (menu.run()) {

                    Pair<String, Point[]> data;
                    data = LifeReader.readLif(menu.getChoosenFile());
                    
                    GameTable gameTable = new GameTable(
                            LifeUtils.calculateRules(data.first), 
                            LifeUtils.translate(data.second));
                    
                    countinueToRun = gameTable.run();
                } 
                else {  countinueToRun = false; }
            }
            catch(NotSupportedLifeVersionException|FileNotFoundException e) { new ErrorDialog(e.toString()); }
        }
    }
}
