import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class MatrixPanel extends JPanel {
	private JTextField height;
	private JTextField width;
	private JTextArea matrixField;
	private JComboBox actions;

	private HashMap<String,Matrix> matrixMap;
	private int currentGeneraton; //this will track how many generated matrices are there.
	public MatrixPanel() {

		matrixMap = new HashMap<String,Matrix>();

		matrixField = new JTextArea();

		JButton accept = new JButton("complete action");
		accept.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				switch(actions.getSelectedIndex()) {
				case -1: JOptionPane.showMessageDialog(new JFrame(), "Please choose an action!","warning!", JOptionPane.WARNING_MESSAGE); break;
				case 0: try {
					saveMatrix(JOptionPane.showInputDialog("Save as..."));
				} catch (WrongInputException e1) {
					e1.printStackTrace();
				} break;
				case 1: loadMatrix(JOptionPane.showInputDialog("What matrix would you want to load?")); break;
				case 2: try {
					Matrix m = calculate(parseExpression(JOptionPane.showInputDialog("What function would you want to run?")));
					height.setText(String.valueOf(m.getHeight()));
					width.setText(String.valueOf(m.getWidth()));
					matrixField.setText(m.toString());
				} catch (WrongInputException e) {
					if(e.isUnknownBug())
						e.printStackTrace();
				} break;
				case 3: generateMatrix(JOptionPane.showInputDialog("Enter the seed: ")); break;
				case 4: try {
					JOptionPane.showMessageDialog(null, "the determinant is: " + new Matrix(getHeightOfMatrix(), getWidthOfMatrix(),getMatrix()).Determinant());
				} catch (WrongInputException e) {
					e.printStackTrace();
				}break;
				case 5: try {
					solveEquation(JOptionPane.showInputDialog("please enter all your variables. divide them only using ','.").split(","));
				} catch (WrongInputException e) {
					e.printStackTrace();
				}
				}
			}
		});
		height = new JTextField();
		height.setHorizontalAlignment(SwingConstants.CENTER);
		height.setText("M");
		height.setColumns(10);

		JLabel lblX = new JLabel("x");
		lblX.setHorizontalAlignment(SwingConstants.CENTER);

		width = new JTextField();
		width.setText("N");
		width.setHorizontalAlignment(SwingConstants.CENTER);
		width.setColumns(10);

		String[] allActions = {"save matrix","load matrix","Math funtions","generate matrix by seed","calculate determinant","solve the equation"};
		actions = new JComboBox(allActions);
		actions.setRenderer(new PromptComboBoxRenderer("select action..."));
		actions.setSelectedIndex(-1);
		actions.setToolTipText("choose action");
		
		JLabel lblNewLabel = new JLabel("Chairs inc.");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
							.addComponent(accept))
						.addComponent(matrixField, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(height, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblX, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(width, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
							.addComponent(actions, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(actions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(width, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblX)
						.addComponent(height, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(matrixField, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(accept)
						.addComponent(lblNewLabel))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	public int getHeightOfMatrix() {
		try {
			return Integer.parseInt(height.getText());
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid number!", "EROR!", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}
	public int getWidthOfMatrix() {
		try {
			return Integer.parseInt(width.getText());
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid number!", "EROR!", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}
	private double[] getMatrix() {
		String matrix = this.matrixField.getText().replaceAll("\n"," ");
		String[] cutMatrix = matrix.split(" ");
		double[] returnMatrix = new double[this.getHeightOfMatrix()*this.getWidthOfMatrix()];
		int count = 0;
		for(String m : cutMatrix) {
			if(!m.equals("")) {
				returnMatrix[count] = Double.parseDouble(m);
				count++;
			}
		}
		return returnMatrix;
	}
	private void setMatrix(double[] matrix) {
		Matrix m = new Matrix(this.getHeightOfMatrix(), this.getWidthOfMatrix(), matrix);
		String matrixString = "";
		for(int i = 1; i<=m.getHeight(); i++) {
			for(int j = 1; j<=m.getWidth(); j++) {
				if(m.getMember(i, j)%1 == 0)
					matrixString += ((int) m.getMember(i, j)) +" ";
				else {
					matrixString += m.getMember(i, j) +" ";
				}
			}
			matrixString += "\n";
		}
		this.matrixField.setText(matrixString);
	}
	private void saveMatrix(String name) throws WrongInputException {
		char first = name.charAt(0);
		if(first<65 || first>123 || (first>90 && first<97))
			throw new WrongInputException(WrongInputException.UNSUPPORTED_SYMBOL_AS_FIRST_CHAR);
		Set<String> nameSet = matrixMap.keySet();
		String[] saves = nameSet.toArray(new String[nameSet.size()]);
		if(Arrays.asList(saves).contains(name)) {
			int answer = JOptionPane.showConfirmDialog(new JFrame(), "would you want to override an old save that goes by the same name?", "override?", JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION) {
				matrixMap.remove(name);
				matrixMap.put(name, new Matrix(this.getHeightOfMatrix(), this.getWidthOfMatrix(), this.getMatrix()));
			}
		}
		else
			matrixMap.put(name, new Matrix(this.getHeightOfMatrix(), this.getWidthOfMatrix(), this.getMatrix()));
	}
	private void loadMatrix(String name) {
		Matrix m = matrixMap.get(name);
		this.height.setText(Integer.toString(m.getHeight()));
		this.width.setText(Integer.toString(m.getWidth()));
		this.setMatrix(m.getAllMembers());
	}
	public Matrix generateMatrix(String seed) {
		int height;
		int width;
		if(MatrixPanel.countMatches(seed, ',') == 3) {
			String[] cut = seed.split(",");
			height = Integer.parseInt(cut[0]);
			width = Integer.parseInt(cut[1]);
			seed = cut[2];
		}
		else {
			height = this.getHeightOfMatrix();
			width = this.getWidthOfMatrix();
		}
		double[] matrix = new double[height*width];
		if(seed.equalsIgnoreCase("random_double")) {
			Random rng = new Random();
			for(int i = 0; i<height*width; i++) {
				matrix[i] = rng.nextDouble();
			}
		}
		else if(seed.contains("random_int")) {
			Random rng = new Random();
			if(countMatches(seed, '<') == 2) {
				int small = Integer.parseInt(seed.substring(0,seed.indexOf("<"))) + 1;
				int big = Integer.parseInt(seed.substring(seed.lastIndexOf("<")+1));
				for(int i = 0; i<height*width; i++)
					matrix[i] += (rng.nextInt(big-small)+small);
			}
			else {
				for(int i = 0; i<height*width; i++)
					matrix[i] = rng.nextInt();
			}
		}
		else if(seed.equals("DEBUG")) {
			this.matrixMap.put("A", new Matrix(3,3,new double[]{1,2,3,4,5,6,7,8,9}));
			double[] matRay = new double[9];
			Random rng = new Random();
			for(int i = 0; i<matRay.length; i++)
				matRay[i] = rng.nextInt(8)+2;
			this.matrixMap.put("B",new Matrix(3,3,matRay));
			JOptionPane.showMessageDialog(null, "you have succcsesfully entered debug mode");
		}
		return new Matrix(height,width,matrix);
	}
	private String currentGeneration() {
		this.currentGeneraton++;
		return "~gen"+currentGeneraton;
	}
	public String parseExpression(String expression) throws WrongInputException { //takes and expression and calculates all commands and things inside of brackets;
		try {
			System.out.println(expression);
			if(!expression.contains("(")) //if there are no brackets, it means that there is nothing to change;
				return expression;
			ArrayList<String> returnList = new ArrayList<String>();
			String command = "";
			for(int i = 0; i<expression.length(); i++) {
				System.out.println(i);
				System.out.println(expression.charAt(i));
				String c = String.valueOf(expression.charAt(i)); //c stands for char;
				if(isNumber(c))
					returnList.add(c);
				else {
					if(c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/")) {
						returnList.add(c);
					}
					else if(c.equals("(")) {
						String inside = ""; //here will be stored everything that is inside the brackets;
						int startBrackets = 1;
						i++;
						int endBrackets = 0;

						do {
							System.out.println(i);
							System.out.println(expression.charAt(i));
							if(i>=expression.length())
								throw new WrongInputException(WrongInputException.BRACKETS_NOT_CLOSED);
							if(expression.charAt(i) == '(')
								startBrackets++;
							else if(expression.charAt(i) == ')')
								endBrackets++;
							if(startBrackets!=endBrackets)
								inside+=expression.charAt(i);
							i++;
						}while(startBrackets != endBrackets);
						i--;//it loops an extra one time so I need to shift it back
						if(startBrackets>1)
							inside = this.parseExpression(inside);
						if(command=="") {
							String s = this.currentGeneration();
							this.matrixMap.put(s,this.calculate(inside));
							returnList.add(s); break;
						}
						else {
							switch(command) {
							case "det": returnList.add(String.valueOf(onlyMatrices(new String[]{inside})[0].Determinant())); break; //calculates determinant. I use the "onlyMatrices" method so that "this" will work;
							case "gen": //generates a new matrix and stores it in the map as ~gen+(number);
								String s = this.currentGeneration();
								this.matrixMap.put(s,this.generateMatrix(inside));
								returnList.add(s); break;
							case "sqrt": returnList.add(String.valueOf(Math.sqrt(Double.parseDouble(inside)))); break;//square root
							default: throw new WrongInputException(WrongInputException.OTHER);
							}
							command="";
						}
					}
					else { //this will continue adding letters into the "command" String and one it will reach a '(' it will stop;
						command+=c;
					}
				}
			}
			String[] returnArray = returnList.toArray(new String[returnList.size()]);
			System.out.println(String.join("", returnArray));
			return String.join("", returnArray);
		}catch(WrongInputException e) {
			return expression;
		}
	}
	public Matrix calculate(String expression) throws WrongInputException {
		if(expression.contains("/"))
			throw new WrongInputException(WrongInputException.MATRIX_DIVIDED);
		int size = -1;
		int height = -1;
		int oldHeight = -1;
		double[] endResult = new double[0];

		String ex = expression.replaceAll("-", "+-1*");
		String[] expressions = ex.split("\\+");

		for(int i = 0; i<expressions.length; i++) {
			double[] multResult;
			String[] parts = expressions[i].split("\\*");
			Matrix[] calcMatrices = this.onlyMatrices(parts);
			double[] calcNums = this.onlyNumbers(parts);
			double mult = 1;
			Matrix returnMatrix;
			if(calcMatrices.length == 0)
				returnMatrix = new Matrix(1,1,new double[]{1});
			else
				returnMatrix = calcMatrices[0];
			size = returnMatrix.getSize();
			height = returnMatrix.getHeight();
			for(double num : calcNums)
				mult *= num;
			for(int j = 1; j<calcMatrices.length; j++) {
				Matrix m = calcMatrices[j];
				if(returnMatrix.getWidth()!=m.getHeight())
					throw new WrongInputException(WrongInputException.MATRICES_HAVE_DIFFERENT_DIMENSIONS);
				size = returnMatrix.getHeight()*m.getWidth();
				height = returnMatrix.getHeight();
				returnMatrix = new Matrix(height,size/height,returnMatrix.multiplyWithAnotherMatrix(m));
			}
			multResult = new double[returnMatrix.getSize()];
			returnMatrix = new Matrix(height, size/height, returnMatrix.multiplyByScalar(mult));
			multResult = returnMatrix.getAllMembers();
			if(i==0) {
				endResult = new double[size];
				oldHeight = height;
			}
			if(endResult.length!=multResult.length || oldHeight!=height)
				throw new WrongInputException(WrongInputException.MATRICES_HAVE_DIFFERENT_DIMENSIONS);
			for(int count = 0; count<endResult.length; count++) {
				endResult[count] += multResult[count];
			}
		}
		return new Matrix(height,size/height,endResult);
	}
	public void solveEquation(String[] vars) throws WrongInputException {
		String equationGain = matrixField.getText(); //get the equations as a single text block;
		equationGain = equationGain.replaceAll(" ", "");
		equationGain = equationGain.replaceAll("\\+", " +"); //here and next line I add a space before them so that I am able to cut them later;
		equationGain = equationGain.replaceAll("\\-", " -");
		equationGain = equationGain.replaceAll("\\=", "\n");
		String[] allEquationsText = filter(equationGain.split("\n"),""); //I cut them into separate lines and also divide each of them into two parts;
		if(allEquationsText.length%2 != 0 || allEquationsText.length/2 != vars.length)
			throw new WrongInputException(WrongInputException.NOT_AN_EQUATION);
		double[][] prefixArray = new double[allEquationsText.length/2][allEquationsText.length/2];
		double[] answerArray = new double[allEquationsText.length/2];
		for(int i = 0; i<allEquationsText.length; i+=2) {
			double[] leftNumbers = onlyNumbers(allEquationsText[i].split(" "));
			double[] rightNumbers = onlyNumbers(allEquationsText[i+1].split(" "));
			HashMap<String, Double> leftVariables = getPrefix(allEquationsText[i].split(" "));
			HashMap<String, Double> rightVariables = getPrefix(allEquationsText[i+1].split(" "));
			double rightSide = 0; //takes the first side,
			for(int j = 0; j<Math.max(leftNumbers.length, rightNumbers.length); j++) {
				rightSide +=((rightNumbers.length>j ? rightNumbers[j]:0)-(leftNumbers.length>j ? leftNumbers[j]:0)); // we subtract the left side from the right side. if the length of the array is smaller them j, it replaces it with 0;
			}
			for(String key : rightVariables.keySet()) { //Subtracts the right side from the left side;
				if(leftVariables.containsKey(key))
					leftVariables.replace(key,(leftVariables.get(key)-rightVariables.get(key)));
				else
					leftVariables.put(key,-1*rightVariables.get(key));
			}
			for(String var : vars) {
				if(!leftVariables.containsKey(var)) {
					leftVariables.put(var, 0.0);
				}
			}
			for(int j = 0; j<vars.length; j++)
				prefixArray[i/2][j] = leftVariables.get(vars[j]);
			answerArray[i/2] = rightSide;
		}
		double bigDeter = new Matrix(vars.length,vars.length,prefixArray).Determinant(); //this is the determinant of the main matrix;
		System.out.println(new Matrix(vars.length,vars.length,prefixArray).toString());
		String answerSheet = ""; //here all the answers will be stored;
		for(int i = 0; i<answerArray.length; i++) {
			System.out.println("==================");
			double[][] newMatrix = new double[prefixArray.length][prefixArray.length];
			for(int j = 0; j<prefixArray.length; j++) { //setting up the array...
				for(int k = 0; k<prefixArray.length; k++)
					newMatrix[j][k] = prefixArray[j][k];
			}
			for(int j = 0; j<newMatrix.length; j++)
				newMatrix[j][i] = answerArray[j]; // here we swap the row for the answer array,
			answerSheet += vars[i] + " = " + (new Matrix(vars.length,vars.length,newMatrix).Determinant()/bigDeter) + "\n"; //and calculate its determinant;
			System.out.println(new Matrix(vars.length,vars.length,newMatrix).toString());
		}
		this.matrixField.setText(answerSheet);
	}
	private static boolean isNumber(String num) {
		try {
			double check = Double.parseDouble(num.trim());
		} catch(NumberFormatException e ) {
			return false;
		}
		return true;
	}
	private Matrix[] onlyMatrices(String[] expression) {
		ArrayList<Matrix> returnList = new ArrayList<Matrix>();
		for(String part : expression) {
			if(!MatrixPanel.isNumber(part)) {
				if(part.equals("this"))
					returnList.add(new Matrix(this.getHeightOfMatrix(),this.getWidthOfMatrix(),this.getMatrix()));
				else
					returnList.add(this.matrixMap.get(part));
			}
		}
		Matrix[] returnArray = new Matrix[returnList.size()];
		for(int i = 0 ; i<returnList.size(); i++) {
			returnArray[i] = returnList.get(i);
		}
		return returnArray;
	}
	private static double[] onlyNumbers(String[] expression) {
		ArrayList<Double> returnList = new ArrayList<Double>();
		for(String part : expression) {
			if(MatrixPanel.isNumber(part)) {
				returnList.add(Double.parseDouble(part));
			}
		}
		double[] returnArray = new double[returnList.size()];
		for(int i = 0; i<returnList.size(); i++)
			returnArray[i] = returnList.get(i);
		return returnArray;
	}
	private static HashMap<String,Double> getPrefix(String[] expression) {
		ArrayList<Double> returnList = new ArrayList<Double>();
		ArrayList<String> variables = new ArrayList<String>(); //here we will store all of the variables;
		for(String part : expression) {
			if(part.equals("") || part.length() == 0)
				continue;
			if(!isNumber(String.valueOf(part.charAt(part.length()-1)))) {
				String prefix = "";
				char[] allChars = part.toCharArray();
				for(int i = 0; i<allChars.length-1; i++)
					prefix+=allChars[i];
				int index = variables.indexOf(String.valueOf(part.charAt(part.length()-1))); //index of the variable
				if(prefix.equals("-"))
					prefix = "-1";
				else if(prefix.equals("+"))
					prefix = "+1";
				else if(prefix.equals("") && expression[0].equals(part)) //in case first variable is equal to 1 (i.e. x+y=1);
					prefix = "1";
				if(index==-1) {
					returnList.add((Double.parseDouble(prefix)));
					variables.add(String.valueOf(part.charAt(part.length()-1)));
				}
				else
					returnList.set(index, (returnList.get(index)+Double.parseDouble(prefix)));
			}

		}
		HashMap<String,Double> returnMap = new HashMap<String,Double>();
		for(int i = 0; i<variables.size(); i++)
			returnMap.put(variables.get(i),returnList.get(i));
		return returnMap;
	}
	private static int countMatches(String s, char c) {
		char[] ca = s.toCharArray();
		int count = 0;
		for(char ch : ca) {
			if(ch == c)
				count++;
		}
		return count;
	}
	private static String[] filter(String[] array, String removable) {
		ArrayList<String> returnList = new ArrayList<String>();
		for(String s : array) {
			if(!(s.equals(removable)))
				returnList.add(s);
		}
		return returnList.toArray(new String[returnList.size()]);
	}
	public static void main(String args[] ) {
		MatrixPanel m = new MatrixPanel();
		JFrame frame = new JFrame("Matrix Calculator");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 350);
		frame.getContentPane().add(m);
	}
}