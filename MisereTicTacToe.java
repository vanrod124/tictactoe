import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MisereTicTacToe {
    private static final int SIZE = 3;
    private static final char EMPTY = '-';
    private static final char PLAYER = 'X';
    private static final char COMPUTER = 'O';

    private char[][] board;
    private Scanner scanner;

    public MisereTicTacToe() {
        board = new char[SIZE][SIZE];
        scanner = new Scanner(System.in);
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void play() {
        System.out.println("Welcome to Misère Tic Tac Toe!");
        System.out.println("Choose your difficulty level: ");
        System.out.println("1 for Easy");
        System.out.println("2 for Medium");
        System.out.println("3 for Hard");
        System.out.print("Enter your choice: ");
        String difficulty = scanner.next();

        boolean playerTurn = true;
        boolean gameOver = false;

        while (!gameOver) {
            displayBoard();

            if (playerTurn) {
                playerMove();
            } else {
                // Change the difficulty level here
                if (difficulty.equals("1")) {
                    easyComputerMove();
                } else if (difficulty.equals("2")) {
                    mediumComputerMove();
                } else if (difficulty.equals("3")) {
                    hardComputerMove();
                }
            }

            char loser = checkLoser();
            if (loser != EMPTY || isBoardFull()) {
                gameOver = true;
                displayBoard();
                if (loser == PLAYER) {
                    System.out.println("Computer wins! Better luck next time.");
                } else if (loser == COMPUTER) {
                    System.out.println("Congratulations! You win!");
                } else {
                    System.out.println("It's a draw!");
                }
            } else {
                playerTurn = !playerTurn;
            }
        }
    }

    private void displayBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void playerMove() {
        System.out.print("Enter your move (row[1-3] and column[1-3]): ");
        int row = scanner.nextInt() - 1;
        int col = scanner.nextInt() - 1;
        if (isValidMove(row, col)) {
            board[row][col] = PLAYER;
        } else {
            System.out.println("Invalid move. Try again.");
            playerMove();
        }
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        return board[row][col] == EMPTY;
    }

    private void easyComputerMove() {
        List<int[]> emptyCells = getEmptyCells();
        if(emptyCells.isEmpty()){
            System.out.println("The board is full. It's a draw.");
            return;
        }
        int randomIndex = (int) (Math.random() * emptyCells.size());
        int[] randomCell = emptyCells.get(randomIndex);
        int row = randomCell[0];
        int col = randomCell[1];
        board[row][col] = COMPUTER;
        System.out.println("Computer's move: " + (row + 1) + " " + (col + 1));
    }

    private void mediumComputerMove() {
        // Check if there is a non-winning move.
        // If all moves result in a win, choose one at random.
        if (!makeLosingMove(COMPUTER)) {
            randomComputerMove();
        }
    }

    private void hardComputerMove() {
        // Implementing a perfect strategy for misère Tic Tac Toe is more complicated than for regular Tic Tac Toe,
        // but you could try to adapt the minimax algorithm to work for misère Tic Tac Toe as a heuristic.
        if (!makeLosingMove(COMPUTER)) {
            randomComputerMove();
        }
    }

    private void randomComputerMove() {
        List<int[]> emptyCells = getEmptyCells();
        if(emptyCells.isEmpty()){
            System.out.println("The board is full. It's a draw.");
            return;
        }
        int randomIndex = (int) (Math.random() * emptyCells.size());
        int[] randomCell = emptyCells.get(randomIndex);
        int row = randomCell[0];
        int col = randomCell[1];
        board[row][col] = COMPUTER;
        System.out.println("Computer's move: " + (row + 1) + " " + (col + 1));
    }

    private List<int[]> getEmptyCells() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }

    private boolean makeLosingMove(char player) {
        for (int[] cell : getEmptyCells()) {
            board[cell[0]][cell[1]] = player;
            if (checkLoser() == player) {
                board[cell[0]][cell[1]] = ' ';
                return true;
            }
            board[cell[0]][cell[1]] = ' ';
        }
        return false;
    }

    private int evaluate() {
        char loser = checkLoser();
        if (loser == COMPUTER) {
            return +10;
        } else if (loser == PLAYER) {
            return -10;
        } else {
            return 0;
        }
    }

    private int[] minimax(int depth, char player) {
        int bestScore;
        int currentScore;
        int[] bestMove = {-1, -1};

        char loser = checkLoser();
        if (loser == COMPUTER) {
            return new int[]{10, -1, -1};
        } else if (loser == PLAYER) {
            return new int[]{-10, -1, -1};
        } else if (isBoardFull()) {
            return new int[]{0, -1, -1};
        }

        if (player == COMPUTER) {
            bestScore = Integer.MIN_VALUE;
            for (int[] cell : getEmptyCells()) {
                board[cell[0]][cell[1]] = player;
                currentScore = minimax(depth + 1, PLAYER)[0];
                board[cell[0]][cell[1]] = ' ';
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestMove = cell;
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int[] cell : getEmptyCells()) {
                board[cell[0]][cell[1]] = player;
                currentScore = minimax(depth + 1, COMPUTER)[0];
                board[cell[0]][cell[1]] = ' ';
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestMove = cell;
                }
            }
        }
        return new int[]{bestScore, bestMove[0], bestMove[1]};
    }

    private char checkLoser() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != ' ') {
                return board[i][0];
            }

            if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] != ' ') {
                return board[0][i];
            }
        }

        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != ' ') {
            return board[0][0];
        }

        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != ' ') {
            return board[0][2];
        }

        return ' ';
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        MisereTicTacToe game = new MisereTicTacToe();
        game.play();
    }
}

can you make this misere tic tac toe game playable, and for the hard engine, i am not going to use the minimax method
