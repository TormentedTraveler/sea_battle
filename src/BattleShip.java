import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.SimpleTimeZone;

public class BattleShip {
    private static char[][] map = new char[8][8];
    private static int shipsDead = 0;
    private static char emptyPointSign = '*';
    private static char shipSign = '-';
    private static char shootedPointSign = '#';
    private static char deadShipsPartSign = '+';
    private static char deadShipSign = 'X';
    private static boolean mapIsVisible = true;
    private static String currentStatus =  "S B";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        startTheGame();
        while (true) {
            showTheMap();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (x == 101 || y == 101) {
                restartTheGame();
                continue;
            }
            shootOnPoint(x, y);
            if (shipsDead == 10) {
                showTheMap();
                break;
            }
        }
        System.out.println("You destroyed all the ships!!!");
    }

    private static void startTheGame() {
        fillTheMap();
        placeTheShips();
    }

    public static void restartTheGame () {
        startTheGame();
        System.out.println("The game has restarted");
        shipsDead = 0;
    }

    private static void fillTheMap() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = emptyPointSign;
            }
        }
    }

    private static void placeTheShips() {
        int placedShips = 0;
        while (placedShips < 6)
        {
            while (placedShips < 1)
            {
                placedShips = placeShip(3, placedShips, 3);
            }

            while (placedShips < 3)
            {
                placedShips = placeShip(2, placedShips, 2);
            }

            while (placedShips < 6) {
                placedShips = placeShip(1, placedShips, 1);
            }
        }
    }

    private static int placeShip(int count, int placedShips, int shipSize)
    {
        Random random = new Random();
        int innerPlacedShips = placedShips;
        int randomXPosition = random.nextInt(8);
        int randomYPosition = random.nextInt(8);
        int isHorizontal = random.nextInt(2);
        if (map[randomXPosition][randomYPosition] == emptyPointSign && checkPlaceAround(randomXPosition, randomYPosition, shipSize, isHorizontal)) {
            for (int i = 0; i < count; i++)
            {
                int innderRandomXPosition = randomXPosition;
                int innderRandomYPosition = randomYPosition;
                if (isHorizontal == 1)
                {
                    innderRandomYPosition += i;
                } else if (isHorizontal == 0)
                {
                    innderRandomXPosition += i;
                }
                map[innderRandomXPosition][innderRandomYPosition] = shipSign;
            }
            innerPlacedShips++;
        }
        return innerPlacedShips;
    }

    private static void showTheMap() {
        System.out.println("   1  2  3  4  5  6  7  8 ");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < 8; j++) {
                char point = map[i][j];
                if (!mapIsVisible) {
                    if (point == shipSign) {
                        point = emptyPointSign;
                    }
                }
                System.out.print(" " + point + " ");
            }
            System.out.println();
        }
        System.out.print("           " + currentStatus);
        System.out.println();
    }

    public static void shootOnPoint(int x, int y) {
        if (map[y - 1][x - 1] == shipSign) {
            map[y - 1][x - 1] = deadShipsPartSign;

            checkDestroyedShip(y-1, x-1);

            shipsDead++;
            currentStatus = "HIT";
        } else if (map[y - 1][x - 1] != emptyPointSign) {
            System.out.println("This point is already destroyed");
        } else {
            map[y - 1][x - 1] = shootedPointSign;
            currentStatus = "MISS";
        }
    }

    public static boolean checkPlaceAround (int x, int y, int shipLength, int isHorizontal)
    {
        for (int j = -1; j < 2; j++){
            for (int i = -1; i < shipLength+1; i++)
            {
                if (isHorizontal == 1)
                {
                    if (y + shipLength > 7) {
                        return false;
                    }

                    if (x + j < 0 || y + i < 0 || x + j > 7 || y + i > 7)
                    {
                        continue;
                    }

                    if (map[x+j][y+i] != emptyPointSign) {
                        return false;
                    }
                }
                else
                {
                    if (x + shipLength > 7) {
                        return false;
                    }

                    if (x + i < 0 || y + j < 0 || x + i > 7 || y + j > 7)
                    {
                        continue;
                    }

                    if (map[x+i][y+j] != emptyPointSign) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static ArrayList<int[]> nextPointsAreDead (int x, int y, boolean isHorizontal) {
        ArrayList<int[]> deadShipsPositions = new ArrayList<>();
        ArrayList<int[]> emptyArrayList = new ArrayList<>();
        int maximumShipLength = 3;

        for (int i = -2; i < maximumShipLength; i ++) {
            int innerCommon = x + i;
            int innerX = x + i;
            int innerY = y;
            int innerPreviousXPoint = innerX + 1;
            int innerPreviousYPoint = y;

            if (isHorizontal) {
                innerX = x;
                innerY = y + i;
                innerCommon = y + i;
                innerPreviousXPoint = x;
                innerPreviousYPoint = innerY + 1;
            }

            if ((innerCommon  < 0 && i == -2) || (innerCommon > 7 && i == 2) || (innerCommon < 0 && i == -1) || (innerCommon > 7 && i == 1))
            {
//                    System.out.println("continue on first" + i);
                continue;
            }

            if (pointIsShip(innerX, innerY) && !pointIsShip(innerPreviousXPoint, innerPreviousYPoint) && i == -2)
            {
//                    System.out.println("continue first elements");
                continue;
            }
            else if (pointIsShip(innerX, innerY) && !pointIsShip(innerPreviousXPoint,innerPreviousYPoint) && i == 2)
            {
//                    System.out.println("break on last elements");
                break;
            }

            if (!pointIsShip(innerX,innerY))
            {
//                    System.out.println("continue on non-ship");
                continue;
            }

            if (map[innerX][innerY] == deadShipsPartSign)
            {
//                    System.out.println("adding points");
                int[] positions = new int[2];
                positions[0] = innerX;
                positions[1] = innerY;
                deadShipsPositions.add(positions);
            }
            else if (map[innerX][innerY] == shipSign)
            {
//                    System.out.println("return on non-dead");
                return emptyArrayList;
            }
        }

        return deadShipsPositions;
    }

    private static void markDeadShipPositions (ArrayList<int[]> positions)
    {
        for (int[] position : positions) {
            int x = position[0];
            int y = position[1];

            map[x][y] = deadShipSign;
        }

    }

    private static void checkDestroyedShip (int x, int y)
    {
        ArrayList<int[]> deadShipPositions = new ArrayList<>();

        if (checkOnSingleShip(x, y))
        {
            int[] positions = new int[2];
            positions[0] = x;
            positions[1] = y;
            deadShipPositions.add(positions);

            markDeadShipPositions(deadShipPositions);
            return;
        }

        boolean isHorizontal = isShipHorizontal(x, y);

        deadShipPositions = nextPointsAreDead(x, y, isHorizontal);

        markDeadShipPositions(deadShipPositions);
    }

    private static boolean pointIsShip (int x, int y)
    {
        if (map[x][y] == shipSign || map[x][y] == deadShipsPartSign || map[x][y] == deadShipSign)
        {
            return true;
        }
        return false;
    }

    private static boolean checkOnSingleShip (int x, int y)
    {
        if (pointIsShip(x, y)) {
            boolean horizontal = true;
            boolean vertical = true;

            for (int i = -1; i < 2; i+=2) {
                if (y + i < 0 || y + i > 7) {
                    continue;
                }
                if (pointIsShip(x, y + i)) {
                    horizontal = false;
                }
            }

            for (int i = -1; i < 2; i+=2) {
                if (x + i < 0 || x + i > 7) {
                    continue;
                }
                if (pointIsShip(x + i, y)) {
                    vertical = false;
                }
            }

            return (horizontal && vertical);
        }
        return false;
    }



    private static boolean isShipHorizontal(int x, int y) {
        if ((x > 0 && pointIsShip(x - 1, y)) || (x < 7 && pointIsShip(x + 1, y))) {
            System.out.println("vertical");
            return false;
        }

        if ((y > 0 && pointIsShip(x, y - 1)) || (y < 7 && pointIsShip(x, y + 1))) {
            System.out.println("horizontal");
            return true;
        }

        System.out.println("horizontal on default");
        return true;
    }
}