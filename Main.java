import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static List<List<String>> board = Arrays.asList(
            Arrays.asList("_", "_", "_"),
            Arrays.asList("_", "_", "_"),
            Arrays.asList("_", "_", "_")
        );
    
    public static String currentPlayer = "X";

    public static void main(String[] args) {
        getFieldId(false, null, false);
    };
    
    public static void printBoard() {
        board.forEach((line) -> System.out.println(line));
    };

    public static void getFieldId(boolean isIdAlreadyTaken, Integer insertId, boolean isInvalidInput) {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        printBoard();

        System.out.println();
        System.out.println("Player " + currentPlayer + " Please select a field (1-9)");
        System.out.println("To exit the game, please press ctrl + c");

        if (isIdAlreadyTaken) System.out.println("Id " + Integer.toString(insertId) + " is already taken");
        if (isInvalidInput) System.out.println("Input is invalid. Please enter a number 1 - 9");

        String key = scanner.nextLine();

        try {
            int keyAsNum = Integer.parseInt(key);
            
            if (keyAsNum < 1 || keyAsNum > 9) {
                getFieldId(false, null, true);
                return;
            }

            drawInChar(keyAsNum);
        } catch (Exception e) {
            getFieldId(false, 0, true);
        }
    };

    public static void drawInChar(Integer insertId) {

        int id = insertId - 1;

        int verticalIndex = (int) Math.round(Math.floor(id / 3));
        int horizontalIndex = id % 3;

        if (!board.get(verticalIndex).get(horizontalIndex).equals("_")) {
            getFieldId(true, insertId, false);
            return;
        }

        board.get(verticalIndex).set(horizontalIndex, currentPlayer);

        currentPlayer = currentPlayer.equals("X") ? "O" : "X";

        checkIfSomeoneHasWon();

        getFieldId(false, null, false);
    };

    static void checkIfSomeoneHasWon() {

        int[][] winScenarios = {
            { 1, 3 }, // horizontals
            { 4, 6 },
            { 7, 9 },

            { 1, 7 }, // verticals
            { 2, 8 },
            { 3, 9 },

            { 1, 9 }, // diagonals
            { 3, 7 },
        };
        
        for (int[] array : winScenarios) {
            checkLine(array[0], array[1]);
        }
        
        List<Boolean> areFieldsOccupied = new LinkedList<>();
    
        for (List<String> line: board) {
            for (String field : line) {
                    areFieldsOccupied.add(!field.equals("_"));
            }
        }
    
        boolean isDraw = areFieldsOccupied.stream().allMatch(occupied -> occupied == true);
    
        if (isDraw) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printBoard();
            System.out.println("Both Player have won!");
            System.exit(0);
        }
    };

    static void checkLine(int firstId, int lastId) {
        String firstField = getFieldValueById(firstId);
        String lastField = getFieldValueById(lastId);
        int middleId = ((lastId - firstId) / 2) + firstId;
        String middleField = getFieldValueById(middleId);

        if (
            !firstField.equals("_") &&
            firstField.equals(middleField) &&
            firstField.equals(lastField)
        ) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printBoard();
            System.out.println("\nCongratulations! Player " + firstField + " has won with field id from " + firstId + " to " + lastId + ".");
            System.exit(0);
        }

    }

    static String getFieldValueById(int id) {
        return board.get((id - 1) / 3).get((id - 1) % 3);
    }
}