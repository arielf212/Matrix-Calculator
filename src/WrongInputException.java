import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WrongInputException extends Exception {
	public static final int MATRIX_BY_SCALAR = 1;
	public static final int UNSUPPORTED_SYMBOL_AS_FIRST_CHAR = 2;
	public static final int MATRIX_DIVIDED = 3;
	public static final int MATRICES_HAVE_DIFFERENT_DIMENSIONS = 4;
	public final static int NOT_AN_EQUATION = 5;
	public final static int BRACKETS_NOT_CLOSED = 6;
	public static final int OTHER = 100;
	private boolean isRandomBug = false;
	public WrongInputException(int whatsWrong) {
		switch(whatsWrong) {
		case 1: JOptionPane.showMessageDialog(null, "if you want to multiply a scalar with a matrix please do it this way: \n (number*matrix.)"); break;
		case 2: JOptionPane.showMessageDialog(null, "the first syllable of the name of the matrix was an unsuported symbol. please call your matrix by a diferent name."); break;
		case 3: JOptionPane.showMessageDialog(null, "please dont try to divide Matrices. you can't."); break; 
		case 4: JOptionPane.showMessageDialog(null, "you have entered two or more matrices who have wrong dimensions that cant be computed; \n hint: (4,3) is not (3,4)"); break;
		case 5: JOptionPane.showMessageDialog(null,"this mode solves equations. if you want to calculate something, please use the \"calculate\" option in the menu"); break;
		case 6: JOptionPane.showMessageDialog(null, "please check and see that you have closed all your brackets");
		case 100: JOptionPane.showMessageDialog(null, "something went wrong, but im not sure what. please contact the developer about this."); isRandomBug = true; break;
		}
	}
	public boolean isUnknownBug() {
		return isRandomBug;
	}
}
