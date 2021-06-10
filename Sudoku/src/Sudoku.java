import java.util.Random;
import java.lang.*;

public class Sudoku {
	
	private final static int width = 9;
	private final static int height = 9;
	
	private int[][] sudokuAnswer = new int[width][height];
	private int[][] sudokuPuzzle = new int[width][height];
	private int[][][] sudokuStance = new int[width][height][10];
	
	Random rand = new Random();

	/* You can add any fields and methods */

	Sudoku() {
		initSudoku();		// Initialize sudokuAnswer & sudokuPuzzle to 0
		generateAnswer();	// Generate sudokuAnswer
		generatePuzzle();	// Generate sudokuPuzzle
	}
	
	void initSudoku() {
		for(int i = 0; i< height; i++) {
			for (int j = 0; j < width; j++) {
				sudokuAnswer[i][j] = 0;
				sudokuPuzzle[i][j] = 0;
			}
		}
	}

	void initStance() {
		for(int i = 0; i< height; i++)
			for (int j = 0; j < width; j++) 
				for(int k = 0; k < 10; k++) 
					sudokuStance[i][j][k] = 0;
	}
	
	boolean check_duplicity(int[] sudoku_part) {
		for(int i = 0; i < (height - 1); i++)
			for (int j = i + 1; j < width; j++)
				if( (sudoku_part[i] != 0) && (sudoku_part[j] != 0) && (sudoku_part[i] == sudoku_part[j]))
					return true;
		return false;
	}
	
	boolean check_subgrid(int x, int y, int[][] sudoku) {
		int a = 0, b = 0;
		
		switch(x) {
		case 0:
		case 1:
		case 2:
			a = 1;
			break;
			
		case 3:
		case 4:
		case 5:
			a = 4;
			break;
			
		case 6:
		case 7:
		case 8:
			a = 7;
			break;
			
			default:
				System.out.println("Wrong!");
				break;
		}
		
		switch(y) {
		case 0:
		case 1:
		case 2:
			b = 1;
			break;
			
		case 3:
		case 4:
		case 5:
			b = 4;
			break;
			
		case 6:
		case 7:
		case 8:
			b = 7;
			break;
			
		default:
			System.out.println("Wrong!");
			break;
		}
		
		int[] grid_array = new int[9];
		int i, j, k = 0;
		for(i = a - 1; i <= a + 1; i++)
			for(j = b - 1; j <= b + 1; j++)
				grid_array[k++] = sudoku[i][j];
		
		return !check_duplicity(grid_array);
	}
	
	boolean check_row(int y, int[][] sudoku) {
		int[] rows = new int[9];
		
		for(int j = 0; j < width; j++)
			rows[j] = sudoku[j][y];
		
		return !check_duplicity(rows);
	}
	
	boolean check_column(int x, int[][] sudoku) {
		return !check_duplicity(sudoku[x]);
	}
	
	boolean check_validity(int x, int y, int[][] sudoku) {
		return (check_subgrid(x, y, sudoku) && check_row(y, sudoku) && check_column(x, sudoku));
	}
	 
	void generateAnswer() {
		int x = 0, y = 0;
		int i;
		int input;
		boolean found = false;
		rand.setSeed(System.currentTimeMillis());
		initStance();
		
		while(sudokuAnswer[8][8] == 0) {
			i = 1;
			input = rand.nextInt(9) + 1;
			found = false;
			while(i <= 9 && !found) {
				
				if(sudokuStance[x][y][i-1] == 0) {
					sudokuAnswer[x][y] = input;
					sudokuStance[x][y][input-1] = 1;
					if(check_validity(x, y, sudokuAnswer)) {
						found = true;
						break;
					}
				}
				i++;
				input++;
				if(input >= 10) input = 1;
			}
			//i: the value to be tested.
			if(i <= 9) {
				y++;
				if(y >= 9) {
					y = 0;
					x++;
				}
			}
			
			else {
				sudokuAnswer[x][y] = 0;
				for(i = 0; i < 9;i++)
					sudokuStance[x][y][i] = 0;
					if(y == 0) {
						x -= 1;
						y = 8;
						sudokuStance[x][y][sudokuAnswer[x][y]] = 1;
					}
				
					else {
						y--;
						sudokuStance[x][y][sudokuAnswer[x][y]] = 1;
					}
					sudokuAnswer[x][y] = 0;
			}
		}
		/*implement your code*/
	} 
	
	
	void generatePuzzle() {
		int i = 0, j = 0;
		int assigned = 0;
		rand.setSeed(System.currentTimeMillis());
		
		while(!(30 <= assigned && assigned <= 40)) {
			assigned = 0;
			for(i = 0; i < 9; i++)
				for(j = 0; j < 9; j++) 
					sudokuPuzzle[i][j] = 0;
			
			boolean middle = rand.nextBoolean();
			if(middle) {
				sudokuPuzzle[4][4] = sudokuAnswer[4][4];
				assigned++;
			}
			
			for(i = 0; i < 4; i++)
				for(j = 0; j < 9; j++)
					if(rand.nextInt(40) <= 16) {
						sudokuPuzzle[i][j] = sudokuAnswer[i][j];
						sudokuPuzzle[8-i][8-j] = sudokuAnswer[8-i][8-j];
						assigned+=2;
					}
				
			for(i = 0; i < 4; i++)
				if(rand.nextInt(40) <= 16) {
					sudokuPuzzle[4][i] = sudokuAnswer[4][i];
					sudokuPuzzle[4][8-i] = sudokuAnswer[4][8-i];
					assigned+=2;
				}
		}
	}
		
	void printSudoku(int[][] sudoku) {
		for(int i = 0; i<height;i++) {
			if( i % 3 == 0)
				System.out.println("+-----------------------+");
			for(int j = 0; j<width;j++) {
				if( j % 3 == 0)
					System.out.print("| ");
				System.out.print(sudoku[i][j] + " ");
			}
			System.out.print("|\n");
		}
		System.out.print("+-----------------------+");
	}
	
	int[][] getAnswer(){
		return sudokuAnswer;
	}
	
	int[][] getPuzzle(){
		return sudokuPuzzle;
	}
	
	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		
		System.out.println(" # Sudoku Puzzle #");
		sudoku.printSudoku(sudoku.getPuzzle());
		System.out.println("\n # Sudoku Answer #");
		sudoku.printSudoku(sudoku.getAnswer());
	}

}