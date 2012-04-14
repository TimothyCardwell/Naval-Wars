package tec.battleship;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;

public class PlayerController {
    List<Point> hitCoord;    
    List<Point> missCoord;   
    public List<Ship> ships;        
    List<Rect> hitList;
    List<Rect> missList;
    Grid grid;
    
    public PlayerController(Grid grid_){
        hitCoord = new ArrayList<Point>(17);    
        missCoord = new ArrayList<Point>(47);   
        ships = new ArrayList<Ship>(5);        
        hitList = new ArrayList<Rect>(17);
        missList = new ArrayList<Rect>(47);
        grid = grid_;
    }
}
