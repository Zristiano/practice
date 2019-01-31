package yuanmengzeng.practice.lucid;

import	java.io.BufferedReader;
import	java.io.InputStreamReader;
import	java.util.Arrays;
import	java.util.Comparator;
import	java.util.HashSet;
public class Lucid	{
    /**
    Part	1:
    Sort	this array by the length of	the	string,	then alphabetical order (caseinsensitive). For example,	a correctly	sorted list	might be:
    a
    D
    z
    vb
    afd
    */
    static	String[]	data	=	{"Lorem",	"ipsum",	"dolor",	"sit",	"amet",	"consectetur",
            "adipisicing",	"elit",	"sed",	"do",	"eiusmod",	"tempor",	"incididunt",	"ut",	"labore",	"et"};
    static	java.util.Random	rand	=	new	java.util.Random();
    static	String[]	sortedData()	{
        Arrays.sort(data,	new	Comparator<String>()	{
            public	int	compare(String	s1,	String	s2)	{
                if(s1.length()	!=	s2.length()){
                    return	s1.length()	- s2.length();
                }
                s1	=	s1.toLowerCase();
                s2	=	s2.toLowerCase();
                return	s1.compareTo(s2);
            }
        });
        return	data;
    }
    /**
    Part 2:	Write a	small, reasonably efficient	class that takes strings as	input, and calls a callback	method whenever	a newline ("\n") is	reached,
     passing in	the	previous line's text. The class	should discard old data	when possible to save memory.
    Hint: This should not take more	than about 15-50 lines of code.
    */
    static class LineParser	{
        public interface LineReceiver {
            void lineComplete(String line);
        }

        private LineReceiver callback;
        private StringBuilder sb;
        public	LineParser(LineReceiver	lineReceiver) {
            callback = lineReceiver;
            sb = new StringBuilder();

        }
        public void read(String	data) {
            //TODO:	Fill in	this function
            for (char c : data.toCharArray()){
                if (c=='\n' && sb.length()!=0){
                    // we meet a '\n' and StringBuilder is not empty, then we callback a new line
                    callback.lineComplete(sb.toString());
                    sb.setLength(0);
                }else {
                    sb.append(c);
                }
            }

//            while(data.length() != 0 &&	data.contains("\n")){
//                int	i	=	data.indexOf("\n");
//                sb.append(data.substring(0,i));
//                callback.lineComplete(sb.toString());
//                sb.setLength(0);
//                if(i+1<=data.length())
//                    data	=	data.substring(i+1);
//            }
//            sb.append(data);
        }
    }
    /**
    *	Part 3:	Finish this	implementation of a 2048 game.
    *
    *	https://en.wikipedia.org/wiki/2048_(video_game)
    *
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
    static	class	Game2048	{
        enum Direction {
            UP, DOWN, LEFT, RIGHT
        }

        public	int	height = 4;
        public	int	width = 4;
        public	int[][]	board = new int[height][width];


        public Game2048() {
            for(int	y = 0; y < height; y++) {
                for(int	x = 0; x < width; x++)	{
                    board[y][x]	=	0;
                }
            }
        }
        public void render() {
            String	divider	=	"_____________________";
            System.out.println(divider);
            for(int	y	=	0;	y	<	height;	y++)	{
                System.out.printf("|");
                for(int	x	=	0;	x	<	width;	x++)	{
                    System.out.printf("%4d|",board[y][x]);
                }
                System.out.printf("%n");
                System.out.println(divider);
            }
        }

        private Direction getMoveInput() {
            Direction toret = null;
            System.out.print("Which	direction	would	you	like	to	move?	(WASD: W=up,A=left,S=down,D=right)	");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            try {
                String	text	=	in.readLine();
                switch	(text)	{
                    case "W":
                    case "w": return Direction.UP;
                    case "A":
                    case "a": return Direction.LEFT;
                    case "S":
                    case "s": return Direction.DOWN;
                    case "D":
                    case "d": return Direction.RIGHT;
                    default:
                        System.out.println("Invalid	input!");
                        return getMoveInput();
                }
            } catch (Exception e) {
                System.out.println("Exception	thrown	in	get	move	input,	try	again (check	input):	");
                return	getMoveInput();
            }
        }

        private	void clearBoard()	{
            for(int	y	=	0;	y	<	this.height;	y++)	{
                for(int	x	=	0;	x	<	this.width;	x++)	{
                    board[y][x]	=	0;
                }
            }
            //	Starting	board	has	two	2s	- one	at	[1,1]	and	the	other	at	[2,3]
            this.board[1][1]	=	2;
            this.board[2][3]	=	2;
        }
        // Things to remember:
        // - Sliding
        // - Merging
        // - Spawning	a	new	2	(only	after	valid	moves)
        // - Detecting	the	end	of	the	game
        public void play() {
            this.clearBoard();
            while(true)	{
                render();
                Direction c = getMoveInput();
                if(!this.checkBoard(board)){
                    continue;
                }
                this.merge(board, c);
            }
        }


        public	void	randomPut(int[][]	board){
            int	m	=	board.length,	n	=	board[0].length;
            int	x	=	rand.nextInt(m);
            int	y	=	rand.nextInt(n);
            while(board[x][y]!=	0){
                x	=	rand.nextInt(m);
                y	=	rand.nextInt(n);
            }
            board[x][y]	=	2;
        }


        public	boolean	merge(int[][]	board,	Direction	c){
// HashSet<Integer>	set	=	new	HashSet<>();
            int	m	=	board.length,	n	=	board[0].length;
            int	score	=	0;
            if(c	==	Direction.UP){
                for(int	i	=	0;	i	<	n;	i++){
                    for(int	j	=	m-1;	j	>	0;	j--){
                        if(board[j][i]	==	0)
                            continue;
                        if(board[j][i]	==	board[j-1][i]){
                            board[j-1][i]	=	board[j-1][i]*2;
                            board[j][i]	=	0;
                            score++;
                        }
                    }
                }
            }else	if(c	==	Direction.DOWN){
                for(int	i	=	0;	i	<	n;	i++){
                    for(int	j	=	0;	j	<	m-1;	j++){
                        if(board[j][i]	==	0)
                            continue;
                        if(board[j][i]	==	board[j+1][i]){
                            board[j+1][i]	=	board[j+1][i]*2;board[j][i]	=	0;
                            score++;
                        }
                    }
                }
            }else	if(c	==	Direction.LEFT){
                for(int	i	=	0;	i	<	m;	i++){
                    for(int	j	=	n-1;	j	>	0;	j--){
                        if(board[i][j]	==	0)
                            continue;
                        if(board[i][j]	==	board[i][j-1]){
                            board[i][j-1]	=	board[i][j-1]*2;
                            board[i][j]	=	0;
                            score++;
                        }
                    }
                }
            }else	if(c	==	Direction.RIGHT){
                for(int	i	=	0;	i	<	m;	i++){
                    for(int	j	=	0;	j	<	n-1;	j++){
                        if(board[i][j]	==	0)
                            continue;
                        if(board[i][j]	==	board[i][j+1]){
                            board[i][j+1]	=	board[i][j+1]*2;
                            board[i][j]	=	0;
                            score++;
                        }
                    }
                }
            }
            int	valid_move	=	slide(board,c);
            if(score	!=	0	||	valid_move	!=	0){
                this.randomPut(board);
                return	true;
            }
            return	false;
        }

        public	int	slide(int[][]	board,	Direction	c){
            int	m	=	board.length,	n	=	board[0].length;
            int	score	=	0;
            if(c	==	Direction.UP){
                for(int	i	=	n-1;	i	>=	0;	i--){
                    int	j	=	0;
                    int	k	=	0;while(k	<	m){
                        if(board[k][i]	==	0){
                            k++;
                        }else{
                            board[j++][i]	=	board[k++][i];
                            if(k!=j){
                                score++;
                            }
                        }
                    }
                    while(j	<	m	){
                        board[j++][i]	=	0;
                    }
                }
            }else	if(c	==	Direction.DOWN){
                for(int	i	=	n-1;	i	>=	0;	i--){
                    int	j	=	m-1;
                    int	k	=	m-1;
                    while(k	>=	0){
                        if(board[k][i]	==	0){
                            k--;
                        }else{
                            board[j--][i]	=	board[k--][i];
                            if(k!=j){
                                score++;
                            }
                        }
                    }
                    while(j	>=0	){
                        board[j--][i]	=	0;
                    }
                }
            }else	if(c	==	Direction.LEFT){
                for(int	i	=	m-1;	i	>=	0;	i--){
                    int	j	=	0;
                    int	k	=	0;
                    while(k	<	n){
                        if(board[i][k]	==	0){
                            k++;
                        }else{
                            board[i][j++]	=	board[i][k++];
                            if(k!=j){
                                score++;
                            }}
                    }
                    while(j	<	n	){
                        board[i][j++]	=	0;
                    }
                }
            }else	if(c	==	Direction.RIGHT){
                for(int	i	=	m-1;	i	>=	0;	i--){
                    int	j	=	n-1;
                    int	k	=	n-1;
                    while(k	>=0){
                        if(board[i][k]	==	0){
                            k--;
                        }else{
                            board[i][j--]	=	board[i][k--];
                            if(k!=j){
                                score++;
                            }
                        }
                    }
                    while(j	>=0	){
                        board[i][j--]	=	0;
                    }
                }
            }
            return	score;
        }
        public	boolean	checkBoard(int[][]	board){
            //copy	board
            int	length	=	board.length;
            // int[][]	target	=	new	int[length][board[0].length];
            // for	(int	i	=	0;	i	<	length;	i++)	{
            //  System.arraycopy(board[i], 0, target[i], 0, board[i].length);
            // }
            if(this.checkValidCell(board)) return true;
            if(this.checkMerge(board)) return true;
            return false;
        }


        public	boolean	checkValidCell(int[][] board){
            for(int	i = 0; i < board.length; i++) {
                for(int j = 0; j < board[0].length; j++){
                    if(board[i][j] == 0) return	true;
                }
            }
            return	false;
        }

        public	boolean	checkMerge(int[][]	board){
            int	m	=	board.length,	n	=	board[0].length;
            for(int	i	=	0;	i	<	m;	i++){
                for(int	j	=	0;	j	<	n;	j++){
                    if(board[i][j]	==	0)
                        continue;
                    if(i	>	0	&&	board[i][j]	==	board[i-1][j]){
                        return	true;
                    }
                    if(j	>	0	&&	board[i][j]	==	board[i][j-1]){
                        return	true;
                    }
                }
            }
            return	false;
        }
    }
    /**
     *	@param	args
     */
    public	static	void	main(String[]	args)	{
        System.out.print("Part 1:\n");
        for(String	s	:	sortedData())	{
            System.out.println(s);
        }
        System.out.print("\n\nPart 2:\n");
        LineParser	parser	=	new	LineParser(new	LineParser.LineReceiver()	{
            @Override
            public	void	lineComplete(String	line)	{
                System.out.println(line);
            }
        });
        parser.read("This is t");
        parser.read("he first l");
        parser.read("ine.\nAnd	this is	the	second.\n");
        parser.read("And this is the third.\nAnd the");
        parser.read(" fourth.\n");
        System.out.print("\n\nPart	3:\n");
        Game2048 b = new Game2048();
        b.play();
    }
}