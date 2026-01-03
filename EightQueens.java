import java.util.random.RandomGenerator;
import java.util.ArrayList;

public class EightQueens {

    private static final RandomGenerator random = RandomGenerator.getDefault();

    private static final int[][] block = { // block[i][j] = number of blocked squares if a queen is placed there with an empty board
        {22, 22, 22, 22, 22, 22, 22, 22},
        {22, 24, 24, 24, 24, 24, 24, 22},
        {22, 24, 26, 26, 26, 26, 24, 22},
        {22, 24, 26, 28, 28, 26, 24, 22},
        {22, 24, 26, 28, 28, 26, 24, 22},
        {22, 24, 26, 26, 26, 26, 24, 22},
        {22, 24, 24, 24, 24, 24, 24, 22},
        {22, 22, 22, 22, 22, 22, 22, 22}
    };

    private static final int[][] board = { // game board; 2->queen placed, 0->no queen placed, -1 -> blocked by another queen
      // 0  1  2  3  4  5  6  7 
        {0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0},// 4
        {0, 0, 0, 0, 0, 0, 0, 0},// 5
        {0, 0, 0, 0, 0, 0, 0, 0},// 6
        {0, 0, 0, 0, 0, 0, 0, 0} // 7
    };

    public static void main(String[] args) {
        if (args.length!=2) {
            System.out.println("error: invalid number of arguments");
            return;
        }

        int currentRow = Integer.parseInt(args[0]);
        int currentCol = Integer.parseInt(args[1]);
        int count = 1; // number of queens placed
        board[currentRow][currentCol] = count;
        blockSquare(currentRow, currentCol);

        while (count < 8) {
            int minBlocks = 28; // minimum number of squares that a possible queen can block
            ArrayList<int[]> valid = new ArrayList<>();

            for (int i = 0; i < 8; i++) { // store all possible valid moves in an array
                for (int j = 0; j < 8; j++) {
                    if (isFree(i, j)) {
                        int[] possible = {i, j};
                        valid.add(possible);
                        if (block[i][j] < minBlocks) { // store the minimum blocks possible
                            minBlocks = block[i][j];
                        }
                    }
                }
            }
            for (int i = 0; i < valid.size(); i++) { // remove from the array all moves that block more than minBlocks
                if (block[valid.get(i)[0]][valid.get(i)[1]] != minBlocks) {
                    valid.remove(i);
                }
            }
            if (valid.size()==0) {
                break;
            }
            else if (valid.size()==1) {
                currentRow = valid.getFirst()[0];
                currentCol = valid.getFirst()[1];
                board[currentRow][currentCol] = ++count;
            }
            else {
                int rand = random.nextInt(0, valid.size());
                currentRow = valid.get(rand)[0];
                currentCol = valid.get(rand)[1];
                board[currentRow][currentCol] = ++count;
            }
            blockSquare(currentRow, currentCol);
        } 

        System.out.printf("Number of queens placed: %d%n%n", count);

        printBoard();
        
    }

    public static void blockSquare(int currRow, int currCol) { // arguments: row and col of the recently placed queen
        int col = currCol+1;
        while (col < 8) { // block all the squares to the right
            board[currRow][col++] = -1;
        }

        col = currCol-1;
        while (col >= 0) { // block all the squares to the left
            board[currRow][col--] = -1;
        }

        int row = currRow-1; 
        while (row >= 0) { // block all the squares upwards
            board[row--][currCol] = -1;
        }

        row = currRow+1;
        while (row < 8) { // block all the squares downwards
            board[row++][currCol] = -1;
        }

        row = currRow-1;
        col = currCol+1;
        while (row >= 0 && col < 8) { // block all the squares in the up-right diagonal
            board[row--][col++] = -1;
        }

        row = currRow+1;
        col = currCol+1;
        while (row < 8 && col < 8) { // block all the squares in the down-right diagonal
            board[row++][col++] = -1;
        }

        row = currRow-1;
        col = currCol-1;
        while (row >= 0 && col >= 0) { // block all the squares in the up-left diagonal
            board[row--][col--] = -1;
        }

        row = currRow+1;
        col = currCol-1;
        while (row < 8 && col >= 0) { // block all the squares in the down-left diagonal
            board[row++][col--] = -1;
        }
    }

    public static boolean isFree(int row, int col) {
        return board[row][col]==0;
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

// movimentos da rainha: quantas casas quiser pra frente, trás e diagonal
// nao pode ter rainha na mesma linha, coluna ou diagonal  