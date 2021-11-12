import java.io.IOException;
import java.util.*;

public class Game {


    byte size = 4;
//    int[][] array = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
//    static int[][] completedArray = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
    byte[] array = {};
    static byte[] completedArray = {};

    Draw draw;
    public void init() throws IOException {
        if (size == 3) {
            array = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            completedArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
        else if (size == 4) {
//            array = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
            array = new byte[]{7,10,11,1,16,9,3,4,5,8,13,2,14,6,12,15};
//            array = new byte[]{3,5,14,4,16,10,12,7,15,9,6,11,2,1,13,8};
            completedArray = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        }

        draw = new Draw(size);
        draw.init();
        redraw();
        loop();
    }
    public void loop() {
        System.out.println("Input move:  w a s d");
        Scanner userInput = new Scanner(System.in);
        while (true) {
            String input = userInput.next();

            if (!input.isEmpty()) {
                switch (input) {
                    case "w":
                        moveUp(true, array);
                        break;
                    case "a":
                        moveLeft(true, array);
                        break;
                    case "s":
                        moveDown(true, array);
                        break;
                    case "d":
                        moveRight(true, array);
                        break;
                    case "r":
                        shuffle(array);
                        System.out.println(Arrays.toString(array));
                        break;
                    case "i":
                        System.out.println(getInversions(array));
                        break;
                    case "c":
                        System.out.println(checkArray(array));
                        break;
                    case "m":
                        System.out.println(getManhattan(array));
                        System.out.println(getEuclidean(array));
                        break;
                    case "testM":
                        solve(array, completedArray);
                        break;
                    case "testE":
                        solve_Euclidean(array, completedArray);
                        break;
                }
            }
        }
    }
    public void shuffle(byte[] array) {
        boolean correct = false;
        Random random = new Random();
        while (!correct) {
            for (int j = 0; j < size*size; j++) {
                if (array[j] == size*size) {
                    continue;
                }
                int n = random.nextInt(j + 1);

                byte temp = array[j];
                array[j] = array[n];
                array[n] = temp;
            }

            correct = checkArray(array);
        }
        redraw();
    }
    public boolean checkArray(byte[] array) {
        if (size%2 == 0) {
            if (getBlankFromBottom(array)%2 == 1) {
                return (getInversions(array)%2 == 0);
            }
            else {
                return (getInversions(array)%2 == 1);
            }
        }
        else {
            return (getInversions(array)%2 == 0);
        }
    }

    public int getBlankFromBottom(byte[] array) {
        for (int i = 0; i < (size); i++) {
            for (int j = 0; j < (size); j++) {
                if(array[i*size +j] == size * size) {
                    return (size - i);
                }
            }
        }
        System.out.println("Error i counting blank position");
        return 0;
    }
    public int getInversions(byte[] array) {
        int invCount = 0;
        int position = 0;
        byte[] tempArray = new byte[size * size];
        for (int i = 0; i < (size); i++) {
            for (int j = 0; j < (size); j++) {
                tempArray[position] = array[i*size + j];
                position++;
            }
        }
//        System.out.println(Arrays.toString(tempArray));
        for (int i = 0; i < tempArray.length - 1; i++) {
            for (int j = i + 1; j < tempArray.length; j++) {
                if (tempArray[i] == size * size || tempArray[j] == size * size) {
                    continue;
                }
                if (tempArray[i] > tempArray[j]) {
                    invCount++;
                }
            }
        }
        return invCount;
    }

    public void solve_Euclidean(byte initArray[], byte completedArray[]) {
        Queue<Board> openSet = new PriorityQueue<>();
        List<Board> allBoards = new ArrayList<Board>();

        Board startBoard = new Board(Arrays.copyOf(initArray, initArray.length), null, getEuclidean(initArray), 0);
        startBoard.parent = startBoard;
        openSet.add(startBoard);

        int iterations = 0;
        int states = 0;
        while (true) {
            iterations++;
            if (iterations > 100000 && iterations%100000 == 0) {
                System.out.println(iterations);
            }
            Board parent = openSet.poll();
//            System.out.println(parent.heuristic);
//            System.out.println(Arrays.toString(parent.array));

            if (compareWin(parent.array)) {
                String solve = "";
                String solveDir = "";
                System.out.println("Found");
                System.out.println(iterations);
                System.out.println(parent.moves);
                Board tempBoard = parent;
                byte[] tempArray;
                while (tempBoard.moves != 0) {
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);

                    if (moveDown(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " s ";
                            solveDir += "d";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveUp(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " w ";
                            solveDir += "u";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveLeft(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " a ";
                            solveDir += "l";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveRight(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " d ";
                            solveDir += "r";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                }
                StringBuilder sbdir = new StringBuilder();
                for(int i = solveDir.length() - 1; i >= 0; i--)                 {
                    sbdir.append(solveDir.charAt(i));
                }
                StringBuilder sb = new StringBuilder();
                for(int i = solve.length() - 1; i >= 0; i--)                 {
                    sb.append(solve.charAt(i));
                }
                System.out.println(sbdir.toString());
                System.out.println(sb.toString());
                break;
            }

            byte[] leftArray = Arrays.copyOf(parent.array, parent.array.length);
            moveLeft(false, leftArray);
            byte[] rightArray = Arrays.copyOf(parent.array, parent.array.length);
            moveRight(false, rightArray);
            byte[] upArray = Arrays.copyOf(parent.array, parent.array.length);
            moveUp(false, upArray);
            byte[] downArray = Arrays.copyOf(parent.array, parent.array.length);
            moveDown(false, downArray);

            if (!Arrays.equals(leftArray, parent.array)) {
                if (!Arrays.equals(leftArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(leftArray, parent, getEuclidean(leftArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(rightArray, parent.array)) {
                if (!Arrays.equals(rightArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(rightArray, parent, getEuclidean(rightArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(upArray, parent.array)) {
                if (!Arrays.equals(upArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(upArray, parent, getEuclidean(upArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(downArray, parent.array)) {
                if (!Arrays.equals(downArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(downArray, parent, getEuclidean(downArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
        }
    }

    public void solve(byte initArray[], byte completedArray[]) {
        Queue<Board> openSet = new PriorityQueue<>();
        List<Board> allBoards = new ArrayList<Board>();

        Board startBoard = new Board(Arrays.copyOf(initArray, initArray.length), null, getManhattan(initArray), 0);
        startBoard.parent = startBoard;
        openSet.add(startBoard);

        int iterations = 0;
        int states = 0;
        while (true) {
            iterations++;
            if (iterations > 100000 && iterations%100000 == 0) {
                System.out.println(iterations);
            }
            Board parent = openSet.poll();
//            System.out.println(parent.heuristic);
//            System.out.println(Arrays.toString(parent.array));

            if (compareWin(parent.array)) {
                String solve = "";
                String solveDir = "";
                System.out.println("Found");
                System.out.println(iterations);
                System.out.println(parent.moves);
                Board tempBoard = parent;
                byte[] tempArray;
                while (tempBoard.moves != 0) {
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);

                    if (moveDown(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " s ";
                            solveDir += "d";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveUp(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " w ";
                            solveDir += "u";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveLeft(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " a ";
                            solveDir += "l";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                    tempArray = Arrays.copyOf(tempBoard.parent.array, tempBoard.parent.array.length);
                    if (moveRight(false, tempArray) == 0) {
                        if (Arrays.equals(tempBoard.array, tempArray)) {
                            solve += " d ";
                            solveDir += "r";
                            tempBoard = tempBoard.parent;
                            continue;
                        }
                    }
                }
                StringBuilder sbdir = new StringBuilder();
                for(int i = solveDir.length() - 1; i >= 0; i--)                 {
                    sbdir.append(solveDir.charAt(i));
                }
                StringBuilder sb = new StringBuilder();
                for(int i = solve.length() - 1; i >= 0; i--)                 {
                    sb.append(solve.charAt(i));
                }
                System.out.println(sbdir.toString());
                System.out.println(sb.toString());
                break;
            }

            byte[] leftArray = Arrays.copyOf(parent.array, parent.array.length);
            moveLeft(false, leftArray);
            byte[] rightArray = Arrays.copyOf(parent.array, parent.array.length);
            moveRight(false, rightArray);
            byte[] upArray = Arrays.copyOf(parent.array, parent.array.length);
            moveUp(false, upArray);
            byte[] downArray = Arrays.copyOf(parent.array, parent.array.length);
            moveDown(false, downArray);

            if (!Arrays.equals(leftArray, parent.array)) {
                if (!Arrays.equals(leftArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(leftArray, parent, getManhattan(leftArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(rightArray, parent.array)) {
                if (!Arrays.equals(rightArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(rightArray, parent, getManhattan(rightArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(upArray, parent.array)) {
                if (!Arrays.equals(upArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(upArray, parent, getManhattan(upArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
            if (!Arrays.equals(downArray, parent.array)) {
                if (!Arrays.equals(downArray, parent.parent.array)) {
                    states++;
                    Board child = new Board(downArray, parent, getManhattan(downArray), parent.moves+1);
                    allBoards.add(child);
                    openSet.add(child);
                }
            }
        }
    }

    public int getManhattan(byte[] array) {
        int count = 0;
        int i,j,k,l;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (array[i * size + j] == size * size) {
                    continue;
                }
                inner:
                for (k = 0; k < size; k++) {
                    for (l = 0; l < size; l++) {
                        if (array[i*size + j] == completedArray[k*size + l]) {
                            count += ((Math.abs(i-k) + Math.abs(j-l)));
                            break inner;
                        }
                    }
                }
            }
        }
        return count;
    }


    public int getEuclidean(byte[] array) {
        double count = 0;
        int i,j,k,l;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (array[i * size + j] == size * size) {
                    continue;
                }
                inner:
                for (k = 0; k < size; k++) {
                    for (l = 0; l < size; l++) {
                        if (array[i*size + j] == completedArray[k*size + l]) {
                            count += (Math.sqrt((Math.abs(i-k)*Math.abs(i-k) + Math.abs(j-l)*Math.abs(j-l))));
                            break inner;
                        }
                    }
                }
            }
        }
        return (int)Math.floor(count);
    }

    public int moveUp(boolean redraw, byte[] array) {
//        System.out.println("moveUp");
        iloop:
        for (byte i = 0; i < size; i++) {
            for (byte j = 0; j < size; j++) {
                if (array[i*size + j] == size * size) {
                    if (i == 0) {
//                        System.out.println("Wrong move");
                        return 1;
                    }
                    else {
                        byte temp = array[(i-1)*size + j];
                        array[(i-1) * size + j] = (byte) (size * size);
                        array[size * i + j] = temp;
                        break iloop;
                    }
                }
            }
        }
        if (redraw) {
            redraw();
            compareWin(array);
        }
        return 0;
    }
    public int moveLeft(boolean redraw, byte[] array) {
//        System.out.println("moveLeft");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (array[i*size + j] == size * size) {
                    if (j == 0) {
//                        System.out.println("Wrong move");
                        return 1;
                    }
                    else {
                        byte temp = array[i*size + j-1];
                        array[i*size + j-1] = (byte) (size * size);
                        array[i*size + j] = temp;
                        break;
                    }
                }
            }
        }
        if (redraw) {
            redraw();
            compareWin(array);
        }
        return 0;
    }
    public int moveDown(boolean redraw, byte[] array) {
//        System.out.println("moveDown");
        iloop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (array[i*size + j] == size * size) {
                    if (i == size - 1) {
//                        System.out.println("Wrong move");
                        return 1;
                    }
                    else {
                        byte temp = array[(i+1)*size + j];
                        array[(i+1)*size + j] = (byte) (size * size);
                        array[i*size + j] = temp;
                        break iloop;
                    }
                }
            }
        }
        if (redraw) {
            redraw();
            compareWin(array);
        }
        return 0;
    }
    public int moveRight(boolean redraw, byte[] array) {
//        System.out.println("moveRight");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (array[i*size + j] == size * size) {
                    if (j == size-1) {
//                        System.out.println("Wrong move");
                        return 1;
                    }
                    else {
                        byte temp = array[i*size + j+1];
                        array[i*size + j+1] = (byte) (size * size);
                        array[i*size + j] = temp;
                        break;
                    }
                }
            }
        }
        if (redraw) {
            redraw();
            compareWin(array);
        }
        return 0;
    }

    private void redraw() {
        try {
            draw.redraw(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean compareWin(byte[] array) {
        boolean win = true;
        outerloop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (array[i * size + j] != completedArray[i *size + j]) {
                   win = false;
                   break outerloop;
                }
            }
        }
        if (win) {
            System.out.println("VICTORY!");
        }
        return win;
    }
}
