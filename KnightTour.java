import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class KnightTour {

    private static final int moves[][] = { // 2d array representig the valid moves from a knight; moves[x][0]-> horizontal move, moves[x][1] -> vertical move
        {2, -1}, // 0 to 3: right moves
        {2, 1},
        {1, -2},
        {1, 2},
        {-2, -1}, // 4 to 7: left moves
        {-2, 1},
        {-1, -2},
        {-1, 2}
    };

    private static final int[][] accessibility = { // accessibility[i][j] = number of squares that this square is accessible from
        {2, 3, 4, 4, 4, 4, 3, 2},
        {3, 4, 6, 6, 6, 6, 4, 3},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {4, 6, 8, 8, 8, 8, 6, 4},
        {3, 4, 6, 6, 6, 6, 4, 3},
        {2, 3, 4, 4, 4, 4, 3, 2}
    };

    private static final int[][] board = { // game board
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };

    public static void main(String[] args) {
        if (args.length != 2) {
           System.out.println("error: invalid number of arguments");
           return; 
        }

        int count = 1;
        int currentRow = Integer.parseInt(args[0]);
        int currentCol = Integer.parseInt(args[1]);
        board[currentRow][currentCol] = count;

        while (count < 64) {
            ArrayList<int[]> validMoves = new ArrayList<>(8); // valid moves to be made from the current coordinates
            int minAccess = 10; // minimum accessibility from the valid moves

            for (int[] move : moves) { // check and add all the valid moves to the validMoves ArrayList
                if (isValidMove(currentRow, currentCol, move)) {
                    validMoves.add(move);
                    if (accessibility[currentRow+move[0]][currentCol+move[1]] < minAccess) // store the minimum accessibility value to this point
                        minAccess = accessibility[currentRow+move[0]][currentCol+move[1]];
                }
            }
            for (int i = 0; i < validMoves.size(); i++) { // delete all the valid moves that have a different accessibility from minAccess
                if (accessibility[currentRow+validMoves.get(i)[0]][currentCol+validMoves.get(i)[1]] != minAccess)
                    validMoves.remove(i);
            }
            if (validMoves.size()==0) { // there is no valid moves left, then the knight is stuck in a dead-end 
                break; 
            }
            else if (validMoves.size()==1) { // if there is exactly one valid move with the minimum accessibility
                currentRow += validMoves.getFirst()[0];
                currentCol += validMoves.getFirst()[1];
                board[currentRow][currentCol] = ++count;
            }
            else { // if there is more than one valid move with the minimum accessibility (tie)
                Collections.shuffle(validMoves); // shuffle the validMoves array so it can create a differente possibility each play
                
                int minTiedAccess = accessibility[currentRow+validMoves.get(0)[0]][currentCol+validMoves.get(0)[1]]; // assume that the lowest accessibility is from the first element of validMoves
                int minTiedAccessIndex = 0;

                for (int i = 1; i < validMoves.size(); i++) {
                    int access = lookAhead(currentRow+validMoves.get(i)[0], currentCol+validMoves.get(i)[1]); // lookAhead returns the lowest accessibility reachable from the argument square
                    if (access < minTiedAccess) {
                        minTiedAccess = access;
                        minTiedAccessIndex = i;
                    }
                }
                currentRow += validMoves.get(minTiedAccessIndex)[0];
                currentCol += validMoves.get(minTiedAccessIndex)[1];
                board[currentRow][currentCol] = ++count;
            }
            subAccess(currentRow, currentCol);
        }

        System.out.printf("%nNumber of squares filled: %d%n%n", count);
        printBoard();
    }

    public static void subAccess(int row, int col) { // dinamically subtracts accessibility[i][j] based on the current state of the tour
        for (int i = 0; i < accessibility.length; i++) {
            for (int j = 0; j < accessibility[0].length; j++) {
                if (board[i][j]==0) {
                    int[] src = {row, col};
                    int[] dst = {i, j};
                    if (isReachable(src, dst)) {
                        accessibility[i][j]--;
                    }
                }
            }
        }
    }

    public static boolean isReachable(int[] src, int[] dst) { // test if the dst coordinates are reachable from src coordinates
        for (int[] move : moves) {
            int[] result = {src[0]+move[0], src[1]+move[1]};
            if (Arrays.compare(dst, result)==0) {
                return true;
            } 
        }
        return false;
    }

    public static int lookAhead(int nextRow, int nextCol) { // returns the lowest accessibility from all reachable squares from the argument square
        int minAccess = 10;
        for (int[] move : moves) {
            if (isValidMove(nextRow, nextCol, move)) {
                if (accessibility[nextRow+move[0]][nextCol+move[1]] < minAccess) {
                    minAccess = accessibility[nextRow+move[0]][nextCol+move[1]];
                }
            }
        }
        return minAccess;
    }

    public static boolean isValidMove(int currentRow, int currentCol, int[] nextMove) {
        return (currentRow+nextMove[0]>=0 && currentRow+nextMove[0]<=7) && 
               (currentCol+nextMove[1]>=0 && currentCol+nextMove[1]<=7) &&
               (board[currentRow+nextMove[0]][currentCol+nextMove[1]]==0);
    }

    public static void printBoard() {
        for (int row[] : board) {
            for (int val : row) {
                System.out.printf("%3d ", val);
            }
            System.out.println('\n');
        }
    }
}
