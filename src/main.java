import java.util.ArrayList;

public class main {
    public static int SUBSECTION_SIZE=3;
    public static int[][] playarea = {
            {7,3,4,0,0,0,0,2,1},
            {6,8,5,0,0,2,0,9,0},
            {2,0,0,0,0,4,0,0,0},
            {5,6,0,0,2,0,0,3,9},
            {3,0,0,0,1,0,0,7,6},
            {0,0,0,0,0,0,5,0,2},
            {0,2,6,0,7,0,0,5,3},
            {8,5,1,3,4,0,2,0,0},
            {4,0,3,0,5,0,0,1,0}
    };
    public static void main(String[] args){

        // DISPLAY SUDOKU GROUND
        for(int i = 0; i < playarea.length; ++i){
            for (int j = 0; j < playarea[0].length; ++j)
                System.out.print(playarea[i][j] + ", ");
            System.out.println();
        }
        /**
         * //Allow the program to run itself. using ******backtracking algorithm*******.
         * Backtracking algorithm for sudoku
         *
         * At a cell with value zero, the algorithm will first set it to 1 and check if it satisfies the rules of the game
         * if yes
         *      move forward
         * else
         *      increment one number and continue.
         * Now if you reach num 9 by incrementing and if that also doesnt work, go back to the previous block where you set an input and
         * increment it by 1 and so on.
         */
        solveSudoku();
        //CHECK IF THE RULES HOLD TRUE OR NOT.
        for(int i = 0; i < playarea.length; ++i) {
            for (int j = 0; j < playarea[0].length; ++j)
                System.out.print(playarea[i][j] + ", ");
            System.out.println();
        }
    }

    public static void solveSudoku(){
    ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();
    int size = 0;
        for (int i=0; i<playarea.length; ++i){
            for (int j=0; j<playarea[i].length;++j){        // Loop every number,
                if(playarea[i][j] == 0){                    //check if a number is 0 or not.
                    indexes.add(new ArrayList<>());
                    indexes.get(size).add(i);
                    indexes.get(size).add(j);

                    int k = 1;                         //if yes, start inserting numbers and checking the validity
                    boolean isValid;
                    do{
                        playarea[i][j] = k;
                        isValid = check();
                        if(isValid)
                            indexes.get(size).add(k);
                        ++k;
                    }while(k < 10 && !isValid);

                    if (k == 10 && !isValid){
                        playarea[i][j] = 0;
                        i = indexes.get(size-2).get(0);
                        j = indexes.get(size-2).get(1);
                        indexes.remove(size-1);
                        --size;
                    }
                    ++size;
                }
            }
        }
    }
    private static boolean check(){
        /**
         * RULES OF SUDOKU
         * 1. Each row, column, and nonet can contain each number (typically 1 to 9) exactly once.
         * 2. The sum of all numbers in any nonet, row, or column must match the small number printed in its corner.
         *      For traditional Sudoku puzzles featuring the numbers 1 to 9, this sum is equal to 45.
         */

        boolean checkRow=false;
        boolean checkBox=false;
        boolean checkColumn=false;
        int curnum;
        for(int i = 0; i < playarea.length; ++i){
            for (int j = 0; j<playarea[0].length; ++j){
               curnum = playarea[i][j];                             //assign current number
               checkRow = rowCheck(playarea[i], curnum);            //check row
               checkColumn = columnCheck( curnum, j);      //check column
               checkBox = subSectionCheck( curnum, i, j);  //check box validation
                System.out.println("box: "+checkBox + " "+ checkRow+" "+ checkColumn +", currnum: "+curnum+" ("+i+", "+j+")");
                if (!checkRow)
                   return false;
               else if(!checkBox)
                   return false;
               else if(!checkColumn)
                   return false;
            }
        }

        return true;
    }

    //METHOD TO CHECK THE ROW
    private static boolean rowCheck(int[] arr, int curnum){
        if(curnum == 0)
            return true;

        int count = 0;
        for(int i=0; i < arr.length; ++i){
            if(arr[i] == curnum)                                //checking if the number matches, if it does, add 1 to count
                count++;
            if(count > 1)                                       // if count more than one then you cannot add the number. Therefore, return false
                return false;
        }
        return true;                                            // if array goes through the whole loop without returning anything then it checks out.
    }

    //METHOD TO CHECK COLUMN
    private static boolean columnCheck( int curnum, int colnum){
        if(curnum == 0)
            return true;

        int count = 0;
        for (int i = 0; i < playarea.length; ++i ){
            if(playarea[i][colnum] == curnum)
                count++;
            if(count > 1)
                return false;
        }

        return true;
    }

    //METHOD FOR BOX VALIDATION
     /**
     *   1  1  1 - 2  2  2 - 3  3  3
     *   1  1  1 - 2  2  2 - 3  3  3
     *   1  1  1 - 2  2  2 - 3  3  3
     *   -  -  - - -  -  - - -  -  -
     *   4  4  4 - 5  5  5 - 6  6  6
     *   4  4  4 - 5  5  5 - 6  6  6
     *   4  4  4 - 5  5  5 - 6  6  6
     *   -  -  - - -  -  - - -  -  -
     *   7  7  7 - 8  8  8 - 9  9  9
     *   7  7  7 - 8  8  8 - 9  9  9
     *   7  7  7 - 8  8  8 - 9  9  9
     *
     */
    public static boolean subSectionCheck( int curnum, int x, int y){

        if(curnum == 0)
            return true;

        int[][] subSection = retrieveSubSection( x, y);
        int count = 0;
        for (int i = 0; i<subSection.length; ++i){
            for(int j = 0; j<subSection[i].length; ++j){
                if(subSection[i][j] == curnum)
                    count++;
                if(count > 1)
                    return false;
            }
        }
        return true;
    }
    public static int[][] retrieveSubSection( int x, int y){
        int rowStart = (x /SUBSECTION_SIZE)*SUBSECTION_SIZE;
        int rowEnd = rowStart+(SUBSECTION_SIZE);

        int columnStart = (y /SUBSECTION_SIZE)*SUBSECTION_SIZE;
        int columnEnd = columnStart+(SUBSECTION_SIZE);

        int[][] subSection = new int[3][3]; //Since this sudoku solver is only solving a 9x9 grid sudoku game, we can hard code it for easiness
        int row= rowStart;
        int column;

        for (int i = 0; i < SUBSECTION_SIZE; ++i,++row){
            column = columnStart;
            for (int j = 0; j < SUBSECTION_SIZE; ++j,++column) {
                subSection[i][j] = playarea[row][column];
            }
        }

        return subSection;
    }
}