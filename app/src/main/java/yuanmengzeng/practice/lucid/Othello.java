package yuanmengzeng.practice.lucid;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/** Class Othello_online
 * represents a two person game of Othello_online (also known as Reversi or Black and White Chess).
 * The game starts on a 2D board with two black and two white pegs on diagonals in the
 * centre of the board. The players alternate turns starting with black.
 * The player puts a peg on one of the squares and where the new peg forms a line
 * that surrounds the opponent's pegs in any direction, the pegs in that line switch colours.
 * The game ends when the board is filled with pegs.
 */
public class Othello
{

    // Declare the constants
    /** A constant representing an empty square on the board. */
    private static final char EMPTY = ' ';
    /** A constant representing a black peg on the board. */
    private static final char BLACK = '*';
    /** A constant representing a white peg on the board. */
    private static final char WHITE = '+';
    /** A constant indicating the size of the game board. */
    private static final int BOARD_SIZE = 5;

    // Declare the instance variables
    /** This array keeps track of the logical state of the game. */
    private char[][] grid = new char[BOARD_SIZE][BOARD_SIZE];
    private char[][] lastGrid = new char[BOARD_SIZE][BOARD_SIZE];
    private char[][] tempGrid = new char[BOARD_SIZE][BOARD_SIZE];
    /** This board contains the physical state of the game. */
    private OthelloBoard gameBoard = new OthelloBoard(BOARD_SIZE, BOARD_SIZE);


    /**
     * Othello_online constructor.
     *
     * PRE: none
     * POST: Logically and physically initializes the Othello_online game with pegs.
     */
    public Othello()
    {
        // ADD CODE HERE
        initStatus();
    }

    private void initStatus(){
        for (int i=0; i<grid.length; i++) {
            Arrays.fill(grid[i], EMPTY);
        }
        grid[3][3]= WHITE;
        grid[3][4]= BLACK;
        grid[4][3]= BLACK;
        grid[4][4]= WHITE;
    }


    private Point getClick(){
        System.out.println("Please enter coordinate of the grid you want to place chess piece (in format: 2,3 ): ");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        Point point = null;
        try {
            String line = reader.readLine();
            String[] axis = line.split(",");
            int x = Integer.valueOf(axis[0]);
            int y = Integer.valueOf(axis[1]);
            if (x<=0 || x>BOARD_SIZE || y<=0 || y>BOARD_SIZE){
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

    /**
     * This method will initiate the play of the Othello_online game.
     * A player takes a turn placing a peg on the board which
     * will cause one or more pegs of the opposite colour to be switched.
     * Once the game is over, the message "Game over. The winner is x.",
     * where x is the colour of the player that has the most pegs up
     * at the end of the game. If the player's have the same number of
     * pegs up then the message "Game over. It is a tie game." should appear.
     *
     * PRE: this is properly constructed
     * POST: a game of Othello_online is completed
     */
    public void play()
    {
        // DO NOT MODIFY THIS CODE
        initStatus();
        // First move is made by black
        char move = BLACK;
        while (move== BLACK || move == WHITE){
            updateView();
            String m = move==BLACK? "BLACK" : "WHITE";
            System.out.println("next move is " + m+"   [BLACK - '*' , WHITE - '+']");
            preserveCurBoard();

            Point point;
            do {
                point = getClick();
            }while (point==null || !placePeg(move, point.x,point.y));

            preserveToLastBoard();
            move = gameOver(move);
        }
        updateView();
        this.endGame();
    }

    /**
     * @param x  axis-x
     * @param y  axis-y
     * @return ture-the grid has been changed;  false-[x,y] is invalid
     */
    private boolean placePeg(char move, int x, int y){
        if (grid[x][y]!= EMPTY) {
            System.out.println(" Invalid Place ");
            return false;
        }
        boolean changed = false;
        changed |= changePegs(move,x,y,-1,0);
        changed |= changePegs(move,x,y,1,0);
        changed |= changePegs(move,x,y,0,1);
        changed |= changePegs(move,x,y,0,-1);
        changed |= changePegs(move,x,y,-1,-1);
        changed |= changePegs(move,x,y,-1,1);
        changed |= changePegs(move,x,y,1,-1);
        changed |= changePegs(move,x,y,1,1);

        if (!changed){
            System.out.println(" Invalid Place ");
        }
        return changed;
    }

    private boolean changePegs(char move, int x, int y, int dirX, int dirY){
        int curX = x+dirX;
        int curY = y+dirY;
        char opposite = WHITE;
        if (move==WHITE) opposite = BLACK;
        if (curX<0 || curX>=BOARD_SIZE || curY <0 || curY>=BOARD_SIZE) return false;
        if (grid[curX][curY] !=opposite) return false;
        curX += dirX;
        curY += dirY;
        while (curX>=0 && curX<BOARD_SIZE && curY >=0 && curY<BOARD_SIZE){
            // we meet the same color on the other side of the line
            if (grid[curX][curY] == move){
                int backX = curX - dirX;
                int backY = curY - dirY;
                while (backX!=x || backY!=y){
                    grid[backX][backY] = move;
                    backX -= dirX;
                    backY -= dirY;
                }
                grid[x][y] = move;
                return true;
            }
            if (grid[curX][curY] == EMPTY) return false;
            curX += dirX;
            curY += dirY;
        }
        return false;
    }

    private void preserveCurBoard(){
        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                lastGrid[i][j] = grid[i][j];
            }
        }
    }

    private void preserveToLastBoard(){
        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                lastGrid[i][j] = tempGrid[i][j];
                tempGrid[i][j] = grid[i][j];
            }
        }
    }


    /**
     * This method will display the current status of the game.
     * The message will appear as
     * **black** has x pieces up --- white has y pieces up OR
     * black has x pieces up --- **white** has y pieces up
     * will appear at the bottom of the board, where x and y are numbers
     * indicating how many pegs of that colour are showing. The ** **
     * surrounding one of the colours indicates whose turn it is.
     * PRE: turn == BLACK || WHITE
     * POST: a message is displayed on the board that shows the number
     *       of pieces each player has. There are **  ** surrounding
     *       the colour of the player whose turn is next.
     */
    private void displayStatus(char turn)
    {
        // ADD CODE HERE
        int countb=0;
        int countw=0;
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                if (grid[i][j]== BLACK)
                {
                    countb++;
                }else if(grid[i][j]== WHITE)
                {
                    countw++;
                }
            }
        }
        if (turn== BLACK)
        {
            this.gameBoard.displayMessage("**black** has " +countb+ " pieces up --- white has " +countw+ " pieces up");
        }else if(turn== WHITE)
        {
            this.gameBoard.displayMessage("black has " +countb+ " pieces up --- **white** has " +countw+ " pieces up");
        }
    }

    private void undo(boolean isUserAction){
        char[][] temp;
        if (isUserAction){
            temp = grid;
            grid = lastGrid;
            lastGrid = temp;
        }else {
            temp = grid;
            grid = tempGrid;
            tempGrid = temp;
        }
    }

    /**
     * This method will determine when the game is over.
     * The game is over when the board is filled with pegs and no player can place a new peg according to the rule.
     *
     * @return which color of peg should mvoe
     */
    private char gameOver(char currentMove)
    {
        // ADD CODE HERE
        char opposite = WHITE;
        if ( currentMove == WHITE ) opposite = BLACK;
        if (checkNextMove(opposite)) return opposite;
        if (checkNextMove(currentMove)) return currentMove;
        return ' ';
    }

    private boolean checkNextMove(char move){
        for (int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                if (grid[i][j]!=EMPTY) continue;
                if (changePegs(move,i,j,0,1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,1,0)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,0,-1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,-1,0)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,1,1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,1,-1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,-1,1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
                if (changePegs(move,i,j,-1,-1)){
                    System.out.printf("[%d,%d] is valid%n",i+1,j+1);
                    undo(false);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method will display the message "Game over. The winner is x.",
     * where x is the colour of the player that has the most pegs up
     * at the end of the game. If the player's have the same number of
     * pegs up then the message "Game over. It is a tie game." should appear.
     *
     * PRE: the game is over
     * POST: a message indicating the winner of the game will appear
     */
    private void endGame()
    {
        int countw=0;
        int countb=0;
        for (int i=0; i<grid.length; i++)
        {
            for (int j=0; j<grid[i].length; j++)
            {
                if (grid[i][j]== BLACK)
                {
                    countb++;
                }else if (grid[i][j]== WHITE)
                {
                    countw++;
                }
            }
        } if (countb>countw)
    {
        this.gameBoard.displayMessage("Game over. The winner is black.");
    }else if (countw>countb)
    {
        this.gameBoard.displayMessage("Game over. The winner is white.");
    }else
    {
        this.gameBoard.displayMessage("Game over. It is a tie game.");
    }

    }

    /**
     * This method will reflect the current state of the pegs on the board.
     * It should be called at the end of the constructor and the end of the
     * takeTurn method and any other time the game has logically changed.
     *
     * NOTE: This is the only method that requires calls to putPeg and removePeg.
     *       All other methods should manipulate the logical state of the game in the
     *       grid array and then call this method to refresh the gameBoard.
     *
     * PRE: none
     * POST: Where the array holds a value of BLACK, a black peg is put in that spot.
     *       Where the array holds a value of WHITE, a white peg is put in that spot.
     *       Where the array holds a value of EMPTY, a peg should be removed from that spot.
     */
    private void updateView()
    {
        String divider = "-----------------------------------------------";
        System.out.print("    ");
        for (int i=1;i<=grid.length;i++){
            System.out.printf(" %d  ",i);
        }
        System.out.println();
        System.out.println("   "+divider);

        for (int i=0; i<grid.length; i++)
        {
            System.out.printf(" %d ",i+1);
            System.out.print("|");
            for (int j=0; j<grid[i].length; j++)
            {
                if (grid[i][j]== EMPTY)
                {
                    System.out.print("   |");
                }else {
                    System.out.printf(" %c |",grid[i][j]);
                }
            }
            System.out.println();
            System.out.println("   "+divider);
        }

    }


    /*
     * This main method is included in the Othello_online class so
     * that is not necessary to switch between files to run the program.
     */
    public static void main(String[] args)
    {
        Othello game = new Othello();
        game.play();
    }


}
