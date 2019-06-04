package gameoflife;

import gameoflife.lifereader.Pair;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public final class LifeUtils {
    
    private LifeUtils(){}  //to prevent the creation of an instance
    
    public static File unzip(File source) throws ZipException, IOException {

        ZipFile zipFile = new ZipFile(source);

        Path p = Paths.get(System.getProperty("java.io.tmpdir")).resolve("lifs");
        if (p.toFile().exists()) { return p.toFile(); }  //if we already unZipped the files, then return that folder
        
        Path path = Files.createDirectory(p);              // if not, then we create a temporal directory for storage
        zipFile.extractAll(path.toAbsolutePath().toString());
        return path.toFile();
    }
    
    public static boolean[][] translate(Point[] points){     
        
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        
        //find the min/max coordinates (so we can know the minimum grid size)
        for(Point point:points){
            minX = Integer.min(minX, point.x);
            minY = Integer.min(minY, point.y);
            maxX = Integer.max(maxX, point.x);
            maxY = Integer.max(maxY, point.y);
        }
        //translate the coordinates to positive, but keeping the shapes
        if(minX < 0) { maxX-=minX; for(Point point:points) { point.translate(-minX, 0); }}
        if(minY < 0) { maxY-=minY; for(Point point:points) { point.translate(0, -minY); }}
        
        //the boolean matrix represent the grid
        boolean result[][] = new boolean[maxY+1][maxX+1];           // this will initialize to false every value, so it is enough to change the living cells
        for(Point point:points){ result[point.y][point.x] = true;}
        
        return result;
    }
    
    public static Pair<ArrayList<Integer>,ArrayList<Integer>> calculateRules(String rules){
        
        ArrayList<Integer> lives = new ArrayList<>();
        ArrayList<Integer> birth = new ArrayList<>();
        String[] s = rules.split("/");
        for(char c:s[0].toCharArray()) { lives.add(Character.getNumericValue(c)); }
        for(char c:s[1].toCharArray()) { birth.add(Character.getNumericValue(c)); }
        return new Pair<>(lives,birth);
    }
    
    public static int numberOfAliveNeighbours(boolean[][] cells, int i, int j){
    
        int neighbours = 0;
        int row_limit = cells.length-1;
        if (row_limit > 0) {
            int column_limit = cells[0].length-1;
            for (int x = Integer.max(0, i - 1); x <= Integer.min(i + 1, row_limit); x++) {
                for (int y = Integer.max(0, j - 1); y <= Integer.min(j + 1, column_limit); y++) {
                    if ((x != i || y != j) && cells[x][y]) {
                        neighbours++;
                    }
                }
            }
        }
        return neighbours;
    }
}    
