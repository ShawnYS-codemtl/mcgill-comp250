package starterCode;

import java.io.*;

public class Maze {
    private final int dimension = 15;
    public int counter = 0;

    // Checks if we can move to (x,y)
    boolean canMove(char maze[][], boolean found, int x, int y) {
        if(found) {
            return (x >= 0 && x < dimension && y >= 0 && y < dimension && (maze[x][y] == '.' || maze[x][y] == '0'));
        } else {
            return (x >= 0 && x < dimension && y >= 0 && y < dimension && (maze[x][y] == '.' || maze[x][y] == 'k'));
        }
    }


    boolean solveMaze(char maze[][]) {

        if (!solveMazeUtil(maze, false, 0, 1, 0, 1, '.')) {
            System.out.print("Solution doesn't exist\n");
            return false;
        }

        return true;
    }

    // A recursive function to solve Maze problem
    boolean solveMazeUtil(char[][] maze, boolean found, int x, int y, int prevX, int prevY, char symbol) {
        // please do not delete/modify the next line!
        counter++;

        // if we reach the key
        if (maze[x][y] == 'k'){
            found = true;
        }

        // print 0 if maze is not solved yet and key not found
        if (maze[14][13] != '0' && found == false && maze[x][y] != 'k') {
            symbol = maze[x][y];
            maze[x][y] = '0';
        }

        // print 1 if maze is not solved and key is found
        if (maze[14][13] != '1' && found == true && maze[x][y] != 'k' && !(x == 0 && y == 1)){
            symbol = maze[x][y];
            maze[x][y] = '1';
        }
        // prevX and prevY store previous position
        // move only if canMove, next position is not the previous one, maze is not solved yet
        // left

        if (canMove(maze, found, x, y - 1) && !(x == prevX && y - 1 == prevY) && (maze[14][13] != '1')) {
            prevX = x;
            prevY = y;
            boolean left = solveMazeUtil(maze, found, x, y - 1, prevX, prevY, symbol);
        }
        // right
        if (canMove(maze, found, x, y + 1) && !(x == prevX && y + 1 == prevY) && (maze[14][13] != '1')) {
            prevX = x;
            prevY = y;
            boolean right = solveMazeUtil(maze, found, x, y + 1, prevX, prevY, symbol);
        }
        // down
        if (canMove(maze, found, x + 1, y) && !(x + 1 == prevX && y == prevY) && (maze[14][13] != '1')) {
            prevX = x;
            prevY = y;
            boolean down = solveMazeUtil(maze, found, x + 1, y, prevX, prevY, symbol);
        }
        // up
        if (canMove(maze, found, x - 1, y) && !(x - 1 == prevX && y == prevY) && (maze[14][13] != '1')) {
            prevX = x;
            prevY = y;
            boolean up = solveMazeUtil(maze, found, x - 1, y, prevX, prevY, symbol);
        }

        // if k is a dead end
        if (maze[x][y] == 'k' ){
            if (canMove(maze, found, x, y - 1) && (maze[14][13] != '1')) {
                prevX = x;
                prevY = y;
                boolean left = solveMazeUtil(maze, found, x, y - 1, prevX, prevY, symbol);
            }
            // right
            if (canMove(maze, found, x, y + 1) && (maze[14][13] != '1')) {
                prevX = x;
                prevY = y;
                boolean right = solveMazeUtil(maze, found, x, y + 1, prevX, prevY, symbol);
            }
            // down
            if (canMove(maze, found, x + 1, y) && (maze[14][13] != '1')) {
                prevX = x;
                prevY = y;
                boolean down = solveMazeUtil(maze, found, x + 1, y, prevX, prevY, symbol);
            }
            // up
            if (canMove(maze, found, x - 1, y) && (maze[14][13] != '1')) {
                prevX = x;
                prevY = y;
                boolean up = solveMazeUtil(maze, found, x - 1, y, prevX, prevY, symbol);
            }

        }
        // base cases

        // if we reach the end of the maze
        if (x == 14 && y == 13 && found == true) {
            return true;
        }
        // if we return to initial point
        else if (x == 0 && y == 1) {
            return (maze[14][13] == '1');
        }
        // if we reach a dead end and maze is not yet solved and key not yet found
        else if (!(maze[14][13] == '1') && found == false) {
            maze[x][y] = '.';
            return false;
        }

        // if we reach a dead end and maze is not yet solved and key is found
        else if (!(maze[14][13] == '1') && found == true){
            if (symbol == '.'){
                maze[x][y] = '.';
            }
            else if (symbol == '0'){
                maze[x][y] = '0';
            }
            return false;
        }

        // if we reach a dead end
        // if maze was solved, and we are returning through points
        else {
            return false;
        }
    }

    //Loads maze from text file
    char[][] loadMaze(String directory) throws IOException{
        char[][] maze = new char[dimension][dimension];

        try (BufferedReader br = new BufferedReader(new FileReader(directory))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length(); col++){
                    maze[row][col] = line.charAt(col);
                }
                row++;
            }
        }
        return maze;

    }

    //Prints maze
    private static void printMaze(char[][] maze) {
        for (int i = 0; i < maze[0].length; i++) {
            for (int j = 0; j < maze[0].length; j++)
                System.out.print(" " + maze[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String args[]) {
        Maze m = new Maze();
        for (int i = 0; i < 4; i++) {
            try {
                char[][] myMaze = m.loadMaze("mazes/m" +i+".txt");
                System.out.println("\nMaze "+i);
                Maze.printMaze(myMaze);
                if(m.solveMaze(myMaze)){
                    Maze.printMaze(myMaze);
                }
            } catch (Exception e){
                System.out.print("File was not found.");
            }

        }
    }
}