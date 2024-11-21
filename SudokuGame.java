import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuGame {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }

    public SudokuGame() {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                boardPanel.add(cells[row][col]);
            }
        }

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());

        JButton checkButton = new JButton("Check Moves");
        checkButton.addActionListener(e -> checkMoves());

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solvePuzzle());

        JButton generateButton = new JButton("Generate Puzzle");
        generateButton.addActionListener(e -> generatePuzzle());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(solveButton);

        frame.setLayout(new BorderLayout());
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Generate initial puzzle
        generatePuzzle();
    }

    private void generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];

        if (fillBoard(board)) {
            removeNumbers(board, 40); // Remove numbers to create a puzzle
            displayBoard(board);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to generate a puzzle.");
        }
    }

    private void displayBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    cells[row][col].setEditable(false); // Make given numbers non-editable
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true); // Allow user input in empty cells
                }
            }
        }
    }

    private void resetBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
                cells[row][col].setEditable(true); // Reset all cells to be editable
                cells[row][col].setBackground(Color.WHITE); // Reset background color
            }
        }
    }

    private void checkMoves() {
        int[][] board = new int[SIZE][SIZE];

        // Reset all cell backgrounds to white before checking
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setBackground(Color.WHITE); // Reset background color
            }
        }

        // Fill the board with user inputs
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText();
                if (!text.isEmpty()) {
                    board[row][col] = Integer.parseInt(text);
                }
            }
        }

        // Validate the board
        boolean valid = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!isSafe(board, row, col, board[row][col])) {
                    valid = false;
                    cells[row][col].setBackground(Color.RED); // Highlight incorrect entries
                }
            }
        }

        if (valid) {
            JOptionPane.showMessageDialog(null, "All moves are correct!");
        } else {
            JOptionPane.showMessageDialog(null, "The moves have been checked!");
        }
    }

    private void solvePuzzle() {
         int[][] board = new int[SIZE][SIZE];

         // Fill the board with current values
         for (int row = 0; row < SIZE; row++) {
             for (int col = 0; col < SIZE; col++) {
                 String text = cells[row][col].getText();
                 if (!text.isEmpty()) {
                     board[row][col] = Integer.parseInt(text);
                 }
             }
         }

         if (solveSudoku(board)) { 
             displayBoard(board); 
             JOptionPane.showMessageDialog(null, "Puzzle solved!");
         } else { 
             JOptionPane.showMessageDialog(null, "No solution exists."); 
         }
     }

     private boolean solveSudoku(int[][] board) { 
         for (int row = 0; row < SIZE; row++) { 
             for (int col = 0; col < SIZE; col++) { 
                 if (board[row][col] == 0) { 
                     for (int num = 1; num <= SIZE; num++) { 
                         if (isSafe(board, row, col, num)) { 
                             board[row][col] = num;
                             if (solveSudoku(board)) return true;
                             board[row][col] = 0;
                         } 
                     } 
                     return false;
                 } 
             } 
         } 
         return true;
     }

     private boolean isSafe(int[][] board, int row, int col, int num) { 
         // Check row and column 
         for (int x = 0; x < SIZE; x++) { 
             if (board[row][x] == num || board[x][col] == num) return false;
         }

         // Check 3x3 subgrid 
         int startRow = row - row % SUBGRID_SIZE;
         int startCol = col - col % SUBGRID_SIZE;

         for (int i = 0; i < SUBGRID_SIZE; i++) { 
             for (int j = 0; j < SUBGRID_SIZE; j++) { 
                 if (board[i + startRow][j + startCol] == num) return false;
             } 
         } 

         return true;
     }

     private void removeNumbers(int[][] board, int count) { 
         Random rand = new Random();

         while (count != 0) { 
             int i = rand.nextInt(SIZE); 
             int j = rand.nextInt(SIZE);

             if (board[i][j] != 0) { 
                 board[i][j] = 0;
                 count--;
             } 
         } 
     }

     private boolean fillBoard(int[][] board) { 
         for (int row = 0; row < SIZE; row++) { 
             for (int col = 0; col < SIZE; col++) { 
                 if (board[row][col] == 0) { 
                     for (int num = 1; num <= SIZE; num++) { 
                         if (isSafe(board, row, col, num)) { 
                             board[row][col] = num;
                             if (fillBoard(board)) return true;
                             board[row][col] = 0;
                         } 
                     } 
                     return false;
                 } 
             } 
         } 
         return true;
     }
}
