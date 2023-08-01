import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int ROW_COUNT = 3;
    private static int COL_COUNT = 3;
    private static String CELL_STATE_EMPTY = " ";
    private static String CELL_STATE_X = "X";
    private static String CELL_STATE_0 = "0";
    private static String GAME_STATE_X_WON = "X выиграли";
    private static String GAME_STATE_0_WON = "0 выиграли";
    private static String GAME_STATE_DRAW = "Ничья";
    private static String GAME_STATE_NOT_FINISHED = "Игра не закончена";
    private static Scanner scanner =new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            startGameRound();

        }

    }
    //начинает раунд
    public static void startGameRound() {
        System.out.println("Начат новый раунд");
        String[][] board = createBoard();
        startGameLoop(board);
    }

    //создает доску
    public static String[][] createBoard() {

        String[][] board = new String[ROW_COUNT][COL_COUNT]; //куда записываются значения доски

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                board[row][col] = CELL_STATE_EMPTY; // задаем пустую доску, с циклом будет запол
            }
        }
        return board;
    }

    //цикличность игры
    public static void startGameLoop(String[][] board) {
        boolean playerTurn = true;
        do {
            if (playerTurn) {
                makePlayerTurn(board);
                printBoard(board);
            } else {
                makeBotTurn(board);
                printBoard(board);
            }
            playerTurn = !playerTurn;

            System.out.println();
            //bot turn
            String gameState = checkGameState(board);
            if (!Objects.equals(gameState, GAME_STATE_NOT_FINISHED)) {
                System.out.println(gameState);
                return;
            }
        } while (true);

        //check game state (X_WON, 0_WON, DRAW, GAME_NOT_OVER)
    }

    //ход игрока
    public static void makePlayerTurn(String[][] board) {
        //get input
        int[] coordinates = inputCellCoordinates(board);
        //validate input
        //place X on board
        board[coordinates[0]][coordinates[1]] = CELL_STATE_X;
    }

    //ввод значений пользователем
    public static int[] inputCellCoordinates(String[][] board) {
        System.out.println("Введите координаты (ряд, колонка), два числа через пробел (0-2)");

        do {
            String[] input = scanner.nextLine().split(" "); //массив для двух чисел через пробел
            int row = Integer.parseInt(input[0]);
            int col = Integer.parseInt(input[1]);

            if ((row < 0) || (row >= ROW_COUNT) || (col < 0) || (col >= COL_COUNT)) {
                System.out.println("некорректное значение! Введите два числа от 0-2, через пробел!");
            } else if (!Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                System.out.println("Данная ячейка уже занята");
            } else {
                return new int[]{row, col};
            }
        } while(true);
    }

    //Ход бота
    public static void makeBotTurn(String[][] board) {
        int[] coordinates = getRandomEmptyCellCoordinates(board);
        board[coordinates[0]][coordinates[1]] = CELL_STATE_0;
    }

    //рандомный выбор пустой клетки ботом
    public static int[] getRandomEmptyCellCoordinates(String[][] board) {
        //get random empty cell
        //place 0 on board

        do {
            int row = random.nextInt(ROW_COUNT);
            int col = random.nextInt(COL_COUNT);
            // empty
            if (!Objects.equals(board[row][col], CELL_STATE_EMPTY)){

            } else {
                return new int[]{row, col};
            }
            //if not -> try again
            //if yes -> return
        } while (true);

    }

    //проверка стадии игры
    public static String checkGameState(String[][] board) {
        ArrayList<Integer> sums= new ArrayList<>();

        //iterate rows
        for (int row = 0; row < ROW_COUNT; row++) {
            int rowSum = 0;
            for (int col = 0; col < COL_COUNT; col++) {
                rowSum += calculateNumValue(board[row][col]);
            }
            sums.add(rowSum);
        }
        
        //iterate columns 
        for (int col = 0; col < COL_COUNT; col++) {
            int colSum = 0;
            for (int row = 0; row < ROW_COUNT; row++) {
                colSum += calculateNumValue(board[row][col]);
            }
            sums.add(colSum);
        }

        //diagonal for top-left to bottom-right
        int leftDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            leftDiagonalSum += calculateNumValue(board[i][i]);
        }
        sums.add(leftDiagonalSum);


        //diagonal for top-right to bottom-left
        int rightDiagonalSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            rightDiagonalSum += calculateNumValue(board[i][(ROW_COUNT - 1) - i]);
        }
        sums.add(rightDiagonalSum);

        if (sums.contains(3)) {
            return GAME_STATE_X_WON;
        } else if (sums.contains(-3)) {
            return GAME_STATE_0_WON;
        } else if (areAllCellsTaken(board)) {
            return GAME_STATE_DRAW;
        } else {
            return GAME_STATE_NOT_FINISHED;
        }

        // X = 1, 0 = -1, empty = 0
        //count sum for rows, columns, diagonals

        //if sum.contains(3) -> X won
        //if sum.contains(-3) -> 0 won
        //if allCellsTaken() -> draw
        //else -> game not over
    }
    // Присваивание значений Х, 0 и пустой клетке для подсчета результата
    private static int calculateNumValue(String cellState) {
        if (Objects.equals(cellState, CELL_STATE_X)) {
            return 1;
        } else if(Objects.equals(cellState, CELL_STATE_0)) {
            return -1;
        } else {
            return 0;
        }
    }

    // Проверка занятости ячеек
    public static boolean areAllCellsTaken(String[][] board) {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                if (Objects.equals(board[row][col], CELL_STATE_EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }

    //выводит доску
    public static void printBoard(String[][] board) {
        System.out.println("_________");
        for (int row = 0; row < ROW_COUNT; row++) {
            String line = "| ";
            for (int col = 0; col < COL_COUNT; col++) {
                line += board[row][col] + " ";
            }
            line += "|";
            System.out.println(line);
        }
        System.out.println("_________");
    }
}