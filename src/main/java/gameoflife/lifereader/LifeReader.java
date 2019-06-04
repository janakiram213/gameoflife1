package gameoflife.lifereader;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//For extracting data from Life 1.05 files
public final class LifeReader {
    
    private LifeReader(){}  //to prevent the creation of instance
    
    public final static Pair<String,Point[]> readLif(File file) throws NotSupportedLifeVersionException, FileNotFoundException{   
    
        String rule = "23/3";  //the default rules for a game (stored and returned in String)
        ArrayList<Point> livingCells = new ArrayList<>(); //only stores the living cells in java.awt.Point (x,y) format
        Point startPoint = null; 
        Integer x = null;
        Integer y = null;
        
        try(Scanner sc = new Scanner(file)){
            
            //checking if the file has .LIF extension
            String fn = file.getName();
            if(!fn.substring(fn.lastIndexOf(".") + 1, fn.length()).equals("LIF")) {
                throw new NotSupportedLifeVersionException("Only .LIF files are supported.");
            }
            //checking version
            if(sc.hasNext()){   
                String version = sc.nextLine().replaceAll("\\s+","");
                if      (version.matches("#Life1.05")){ }
                else if (version.matches("#Life1.06")){ throw new NotSupportedLifeVersionException("Life 1.06 is not supported currently. Try Life 1.05."); }
                else                                  { throw new NotSupportedLifeVersionException("Not supported file-type. Try Life 1.05."); }
            }
            //extract the data
            while(sc.hasNext()){
                String line = sc.nextLine();
                if(line.matches("#D.*")){ continue;}  //comments
                if(line.matches("#N.*")){ continue;} // Normal rule (equals #R 23/3)
                if(line.matches("#R.*")){ rule = line.replaceAll("\\s+", "").substring(2); continue; } //custom rule TODO what if corrupted?
                if(line.matches("#P.*")){                                                              //TODO what if corrupted?
                
                    String[] temp = line.split(" ");
                    x = Integer.valueOf(temp[1]);
                    y = Integer.valueOf(temp[2]);
                    startPoint = new Point(x, y);
                    continue;
                }
                if(line.matches("[\\*\\.]+")){
                    
                    for(char c:line.toCharArray()){
                        //increase x for every cell (dead or alive)
                        if(c == '.')     { x++; } 
                        else if(c == '*'){ livingCells.add(new Point(x++, y)); } 
                    }
                    
                    x = startPoint.x; //after every line the x will reset
                    y++;             //y will be increased after every line
                }
            }
        } 
            
        return (new Pair<>(rule, livingCells.toArray(new Point[livingCells.size()])) );
    }
}
