package yuanmengzeng.practice.lucid;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
        *	The	player is provided a 4x4 grid of numbers, each being a power of	two	(2, 4, 8, 16, 32, 64, 128...)
        *	The player may make a move LEFT, RIGHT, UP, or DOWN, which causes all numbered tiles to slide in that direction.
        *	If a tile slides into a	tile having	the	same number, they combine into one tile containing	the	sum	of	the	two	tiles.
        *	A newly combined tile does not combine with other tiles.
        *	Every time the player makes a move, a new "2" tile is spawned at a random location on the board.
        *
        *	Example: (zeroes denote empty spaces and are excluded below)
        *   0  1  2  3
        *   -------------                -------------               -------------
        * 0	|  |  |  |  |               |  |  |  |*2| <-new tile    |  |  |  |  |
        *   -------------                -------------               -------------
        * 1	| 4| 2| 2| 8| (2s combine)  | 4| 4| 8|  |               |  |*2|  |  | <- new tile
        *   ------------- -- LEFT -->    ------------- -- DOWN -->   -------------
        * 2 |16| 4| 2| 4|               |16| 4| 2| 4|               | 4|  | 8| 2|
        *   ------------- ------------- (4s	combine)                 -------------
        * 3 |  |  |  |  |               |  |  |  |  |               |16| 8| 2| 4|
        *   -------------                -------------               -------------
        *
        *  The game is over when no move the user can make will change the state of the board.
        *  If making a move does not result in any changes, the move isn't valid, and no new piece is spawned
        *
        *  If you have time remaining, try taking on some additional functionality (pick one or more of the following):
        *  -- Keep track of score (every time two tiles merge, add the combined value of the tiles to the score)
        *  -- Implement an "undo" action
        *  -- Implement an AI to recommend moves and/or play the game by itself
        */
public class Game2048 {

    enum Direction {
        UP, DOWN, LEFT, RIGHT, INVALID, UNDO
    }

    private int	height = 3;
    private int	width = 3;
    private int[][]	board = new int[height][width];
    private Random random;
    private List<Point> emptySlots;
    private int score = 0;
    private int lastScore = 0;
    private int[][] lastStepBoard = new int[height][width];
    private Point newTile;

    /**
     * if the tile in the slot is a newly combined tile
     */
    private boolean[][] combined = new boolean[height][width];

    public Game2048() {
        for(int	y = 0; y < height; y++) {
            for(int	x = 0; x < width; x++)	{
                board[y][x]	=	0;
            }
        }
        random = new Random();
        emptySlots = new ArrayList<>();
    }
    public void render() {
        System.out.println("    Score:"+score);
        String divider = "_____________________";
        System.out.println(divider);
        for(int	y =	0; y < height;	y++) {
            System.out.printf("|");
            for(int	x = 0; x < width; x++) {
                if (newTile!=null && newTile.x==y && newTile.y==x){
                    System.out.printf("  *%d|",board[y][x]);
                    newTile = null;
                }else {
                    System.out.printf("%4d|",board[y][x]);
                }
            }
            System.out.printf("%n");
            System.out.println(divider);
        }
    }

    private Direction getMoveInput() {
        System.out.println("Which direction would you like to move? (WASD: W=up, A=left, S=down, D=right).  If you want to undo an action, Please type in 'z' or 'Z' ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Direction dir ;
        try {
            String text = in.readLine();
            switch	(text)	{
                case "W":
                case "w":
                    dir =  Direction.UP;
                    break;
                case "A":
                case "a":
                    dir = Direction.LEFT;
                    break;
                case "S":
                case "s":
                    dir = Direction.DOWN;
                    break;
                case "D":
                case "d":
                    dir = Direction.RIGHT;
                    break;
                case "Z":
                case "z":
                    dir = Direction.UNDO;
                    break;
                default:
                    System.out.println("Invalid	input!");
                    dir = Direction.INVALID;
            }
        } catch (Exception e) {
            System.out.println("Internal error, please input again ");
            dir = Direction.INVALID;
        }
        return dir;
    }

    private	void clearBoard()	{
        for(int	y = 0; y < this.height; y++)	{
            for(int x = 0; x < this.width; x++)	{
                board[y][x]	= 0;
            }
        }
        spawnNewTile();
    }


    // Things to remember:
    // - Sliding
    // - Merging
    // - Spawning a new 2 (only after valid moves)
    // - Detecting the end of the game
    public void play() {
        boolean gameOver = false;
        this.clearBoard();
        while(!gameOver)	{
            render();
            Direction dir ;
            // user input the moving direction
            do {
                dir = getMoveInput();
            }while (Direction.INVALID.equals(dir));

            if (Direction.UNDO.equals(dir)){
                System.out.println("undo the last action");
                undo();
            }else{
                // slide all the tiles towards the direction user specified
                int stepScore = slideToMerge(dir);
                if (stepScore>=0){
                    System.out.println("current step score->"+stepScore);
                    // one or more tiles get combined, there must be empty slot for new tile.
                    spawnNewTile();
                    this.score+=stepScore;
                }
                gameOver = isGameOver();
            }
        }
        render();
        System.out.println("Game Over! No more move can be made");
    }


    /**
     * place a new tile on the board
     */
    private void spawnNewTile(){
        emptySlots.clear();
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                if (board[i][j]==0) emptySlots.add(new Point(i,j));
            }
        }
        int r = random.nextInt(emptySlots.size());
        Point point = emptySlots.get(r);
        board[point.x][point.y] = 2;
        newTile = point;
    }

    /**
     * slide all the tiles towards the direction to merge for scores
     * @param dir sliding direction
     * @return int
     * return -1: no tile has been moved
     * return 0: some tiles has been moved but no merge happening
     * return >0: the scores user gets upon this sliding merge.
     */
    private int slideToMerge(Direction dir){

        int stepScore = -1;
        // reserve a copy of current board in case of action of undo
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                lastStepBoard[i][j] = board[i][j];
            }
        }
        lastScore = score;

        switch (dir){
            case UP:
                System.out.println("Slide upward");
                stepScore =  slideUp();
                break;
            case DOWN:
                System.out.println("Slide downward");
                stepScore =  slideDown();
                break;
            case LEFT:
                System.out.println("Slide leftward");
                stepScore = slideLeft();
                break;
            case RIGHT:
                System.out.println("Slide rightward");
                stepScore = slideRight();
                break;
        }

        // reset the flag of every tiles to false, which means it is not newly combined
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                combined[i][j] = false;
            }
        }
        return stepScore;
    }

    private int slideUp(){
        int score = -1;
        for (int j=0; j<width; j++){
            for (int i=1; i<height; i++){
                // we don't need to slide 0
                if (board[i][j]==0) continue;
                // slide the tile upwards consecutively until we meet a non-empty slot
                int row = i-1;
                // find the slot
                while (row>=0 && board[row][j]==0 ) row--;
                // all the slots above the tile are empty
                if (row<0){
                    board[0][j] = board[i][j];
                    board[i][j] = 0;
                    if (score==-1) score =0;
                }
                // the tile in the found slot is not a newly combined tile and has the same value as tile in board[i][j]
                else if (board[i][j]==board[row][j] && !combined[row][j]) {
                    board[i][j] = 0;
                    board[row][j] <<= 1;
                    if (score == -1) score=0;
                    score+=board[row][j];
                    combined[row][j] = true;
                }else {
                    int temp = board[i][j];
                    board[i][j] = 0;
                    board[row+1][j] = temp;
                    if (score==-1 && i!=row+1) score =0;
                }
            }
        }
        return score;
    }

    private int slideDown(){
        int score = -1;
        for (int j=0; j<width; j++){
            for (int i= height-2; i>=0; i--){
                if (board[i][j]==0) continue;
                int row = i+1;
                while (row<height && board[row][j]==0) row++;
                if (row>=height){
                    board[height-1][j]= board[i][j];
                    board[i][j] = 0;
                    if (score == -1) score=0;
                }else if (board[i][j]==board[row][j] && !combined[row][j]){
                    board[i][j] = 0;
                    board[row][j] <<= 1;
                    combined[row][j] = true;
                    if (score == -1) score=0;
                    score+=board[row][j];
                }else {
                    int temp = board[i][j];
                    board[i][j] = 0;
                    board[row-1][j] = temp;
                    if (score==-1 && i!=row-1) score =0;
                }
            }
        }
        return score;
    }

    private int slideLeft(){
        int score = -1;
        for (int i=0;i<height;i++){
            for (int j=1; j<width; j++){
                if (board[i][j]==0) continue;
                int col = j-1;
                // find the slot
                while (col>=0 && board[i][col]==0 ) col--;
                // all the slots above the tile are empty
                if (col<0){
                    board[i][0] = board[i][j];
                    board[i][j] = 0;
                    if (score==-1) score =0;
                }
                // the tile in the found slot is not a newly combined tile and has the same value as tile in board[i][j]
                else if (board[i][j]==board[i][col] && !combined[i][col]) {
                    board[i][j] = 0;
                    board[i][col] <<=1;
                    combined[i][col] = true;
                    if (score == -1) score=0;
                    score+=board[i][col];
                }else {
                    int temp = board[i][j];
                    board[i][j] = 0;
                    board[i][col+1] = temp;
                    if (score==-1 && j!=col+1) score =0;
                }
            }
        }
        return score;
    }

    private int slideRight(){
        int score = -1;
        for (int i=0; i<height; i++){
            for (int j= width-2; j>=0; j--){
                if (board[i][j]==0) continue;
                int col = j+1;
                while (col<width && board[i][col]==0) col++;
                if (col>=width){
                    board[i][height-1]= board[i][j];
                    board[i][j] = 0;
                    if (score == -1) score=0;
                }else if (board[i][j]==board[i][col] && !combined[i][col]){
                    board[i][j] = 0;
                    board[i][col] <<= 1;
                    combined[i][col] = true;
                    if (score == -1) score=0;
                    score+=board[i][col];
                }else {
                    int temp = board[i][j];
                    board[i][j] = 0;
                    board[i][col-1] = temp;
                    if (score==-1 && j!=col-1) score =0;
                }
            }
        }
        return score;
    }

    /**
     * recover the board of last step
     */
    private void undo(){
        int[][] tempBoard = board;
        board = lastStepBoard;
        lastStepBoard = tempBoard;

        int tempScore = score;
        score = lastScore;
        lastScore = tempScore;
    }

    /**
     * Detecting the end of the game
     * @return if the game is over
     */
    private boolean isGameOver(){

        // check if there is still empty slot for new tile
        for (int i=0; i<height; i++){
            for (int j=0; j< width; j++){
                if (board[i][j]==0) return false;
            }
        }

        // we mock a slide for the board, if the structure of the board changes (score!=-1), we can resume the game, otherwise we come to an end
        int score = slideToMerge(Direction.DOWN);
        // if the game can go on, we need to undo the mocking slide.
        if (score!=-1){
            undo();
            return false;
        }

        score = slideToMerge(Direction.LEFT);
        // if the game can go on, we need to undo the mocking slide.
        if (score!=-1){
            undo();
            return false;
        }

        score = slideToMerge(Direction.RIGHT);
        // if the game can go on, we need to undo the mocking slide.
        if (score!=-1){
            undo();
            return false;
        }

        score = slideToMerge(Direction.UP);
        // if the game can go on, we need to undo the mocking slide.
        if (score!=-1){
            undo();
            return false;
        }

        return true;
    }

}
