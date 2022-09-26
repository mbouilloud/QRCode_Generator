package qrcode;

public class Main {

	public static final String INPUT = "ENTER YOUR TEXT HERE";
	/*
	 * Parameters
	 */
	public static final int VERSION = 4;
	public static final int MASK = 0;
	public static final int SCALING = 20;
	public static void main(String[] args) {

		/*
		 * Encoding
		 */
		//
		//
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);

		//boolean[] encodedData = {};

		
		/*
		 * image
		 */

		//int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData);




		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData,MASK);
		//MatrixConstruction.damage(qrCode, 100);


		/*
		 * Visualization
		 */
		Helpers.show(qrCode, SCALING);
		//Helpers.compare(qrCode,"NoDataV1M0.png");

	}





}
