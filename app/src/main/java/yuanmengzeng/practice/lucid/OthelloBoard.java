package yuanmengzeng.practice.lucid;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OthelloBoard {

    int width;
    int height;

    OthelloBoard(int width, int height){
        this.width = width;
        this.height = height;
    }

    public Point getClick(){
        System.out.println("Please enter coordinate of the grid you want to place chess piece (in format: 2,3 ): ");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        Point point = null;
        try {
            String line = reader.readLine();
            String[] axis = line.split(",");
            int x = Integer.valueOf(axis[0]);
            int y = Integer.valueOf(axis[1]);
            if (x<=0 || x>height || y<=0 || y>width){
                System.out.println("The coordinate must be limited in the scope of the board");
            }else {
                point = new Point(x-1,y-1);
            }
        }catch (IOException e){
            System.out.println("internal error, please try again!");
            return null;
        }catch (NumberFormatException e){
            System.out.println("Please enter in valid format");
            return null;
        }
        return point;

    }


    public void displayMessage(String msg){
        System.out.println(msg);
    }

}
