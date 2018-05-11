public class Matrix {
	private double[][] matrix;
	private int height;
	private int width;
	private int size;
	public Matrix(int height, int width, double[] matrix) {
		this.height = height;
		this.width = width;
		this.size = height*width;
		this.matrix = new double[height][width];
		int i = 0;
		int j = 0;
		if(size!=matrix.length)
			System.out.println("wrong");
		else {
			for(double num : matrix) {
				this.matrix[i][j] = num;
				if(j+1==width) {
					i++;
					j = 0;
				}
				else
					j++;
			}
		}
	}
	public Matrix(int height, int width, double[][] matrix) { //for use only in special cases;
		this.height = height;
		this.width = width;
		this.size = height*width;
		this.matrix = matrix;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int getSize() {
		return size;
	}
	public double[] getAllMembers() {
		double[] returnArray = new double[size];
		int count = 0;
		for(int i = 0; i<height; i++) {
			for(int j = 0; j<width; j++) {
				returnArray[count] = matrix[i][j];
				count++;
			}
		}
		return returnArray;
	}
	public double getMember(int i, int j) {
		return matrix[i-1][j-1];
	}
	public double[] multiplyByScalar(double scalar) {
		double[] returnArray = new double[size];
		int count = 0;
		for(int i = 0; i<height; i++) {
			for(int j = 0; j<width; j++) {
				returnArray[count] = matrix[i][j] * scalar;
				count++;
			}
		}
		return returnArray;
	}
	public double[] multiplyWithAnotherMatrix(Matrix M) throws WrongInputException {
		if(this.width != M.height)
			throw new WrongInputException(WrongInputException.MATRICES_HAVE_DIFFERENT_DIMENSIONS);
		double[] returnArray = new double[this.height*M.width];
		int count = 0;
		// MXN * N*P
		for(int p = 1; p<=this.height; p++) {
			for(int m = 1; m<=M.width; m++) {
				double multResult = 0;
				for(int n = 1; n<=this.width; n++) {
					//System.out.println(this.getMember(n, m)*M.getMember(p, n));
					multResult+=(this.getMember(p, n)*M.getMember(n, m)); 
				}
				//System.out.println(multResult);
				returnArray[count] = multResult;
				count++;
			}
		}
		return returnArray;
	}
	public double Determinant() throws WrongInputException {
		if(this.height!=this.width)
			throw new WrongInputException(WrongInputException.MATRICES_HAVE_DIFFERENT_DIMENSIONS);
		if(this.size == 1)
			return this.getMember(1, 1);
		if(this.size == 4)
			return (this.getMember(1, 1)*this.getMember(2, 2)) - (this.getMember(1, 2)*this.getMember(2, 1));
		double returnNum = 0;
		for(int pivot = 1; pivot<=this.width; pivot++) {
			double[] newMatrix = new double[(int) Math.pow((this.height-1),2)];
			int count = 0;
			for(int i = 1; i<=this.height; i++) {
				for(int j = 1; j<=this.width; j++) {
					if(i!=1 && j!=pivot) {
						newMatrix[count] = this.getMember(i, j);
						System.out.println("count " + count + ": " + newMatrix[count]);
						count++;
					}
				}
			}
			Matrix m = new Matrix(this.height-1, this.width-1,newMatrix);
			System.out.println(m.Determinant());
			returnNum += m.Determinant()*(pivot%2 == 0 ? -1:1)*this.getMember(1, pivot);
			System.out.println(returnNum);
		}
		return returnNum;
	}
	public boolean equals(Matrix m) {
		if(this.height != m.height)
			return false;
		if(this.width != m.width)
			return false;
		double[] thisMembers = this.getAllMembers();
		double[] otherMembers = m.getAllMembers();
		for(int i = 0; i<thisMembers.length; i++) {
			if(thisMembers[i] != otherMembers[i])
				return false;
		}
		return true;
	}
	public String toString() {
		String matrixString = "";
		for(int i = 1; i<=this.getHeight(); i++) {
			for(int j = 1; j<=this.getWidth(); j++) {
				if(this.getMember(i, j)%1 == 0)
					matrixString += ((int) this.getMember(i, j)) +" ";
				else {
					matrixString += this.getMember(i, j) +" ";
				}
			}
			matrixString += "\n";
		}
		return matrixString;
	}
}
