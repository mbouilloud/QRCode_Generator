package qrcode;



public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 *
	 * W = White integer for ARGB
	 *
	 * B = Black integer for ARGB
	 *
	 * both needs to have their alpha component to 255
	 */
	public static  int  W = 0xFF_FF_FF_FF;
	public static int  B = 0xFF_00_00_00;


	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes
	public static int  red = 0xFF_FF_00_00;
	public static int  blue = 0xFF_00_00_FF;
	public static int  transparent = 0x00_00_00_00;

	/**
	 * Create the matrix of a QR code with the given data.
	 *
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 *
	 * ****************************** PART 2 *********************************
	 *
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 *
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		int matriceBase[][] = initializeMatrix(version);
		addFinderPatterns(matriceBase);
		addAlignmentPatterns(matriceBase, version);
		addTimingPatterns(matriceBase);
		addDarkModule(matriceBase);
		addFormatInformation(matriceBase,mask);
		return matriceBase;

	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 *
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {
		int taille = QRCodeInfos.getMatrixSize(version);
		int[][] matriceVide = new int[taille][taille];
		return matriceVide;
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 *
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		AjouterMotif(matrix, 0,0,1);  //// Finder Pattern HAUT GAUCHE
		RemplissageLigne(matrix, 0,7,7,W); /// Ligne Blanche
		RemplissageColonne(matrix, 0,7,7,W); /// Colonne Blanche

		AjouterMotif(matrix, (matrix.length) - 7,0,1);  //// Finder Pattern HAUT DROITE
		RemplissageLigne(matrix,(matrix.length) - 8, (matrix.length) - 1,7, W); /// Ligne Blanche
		RemplissageColonne(matrix,0, 7,(matrix.length) - 8, W); /// Colonne Blanche

		AjouterMotif(matrix, 0, (matrix.length) -7,1); //// Finder Pattern BAS GAUCHE
		RemplissageLigne(matrix,0,7,(matrix.length) - 8, W); /// Ligne Blanche
		RemplissageColonne(matrix,(matrix.length) - 8,(matrix.length) - 1,7, W); /// Colonne Blanche
	}

	
	/**
	 * Add a specific pattern to qrcode matrix.
	 *
	 * @param matrix
	 *          
	 * @param Start column
	 * 
	 * @param Start row
	 *      
	 * @param Number associated to a pattern ( 1 = finder) (2 = Alignment)   
	 */
	
	public static void AjouterMotif(int[][] matrix,int XDepart, int YDepart, int motif) {  //// Ajoute le pattern souhaite a la matrice

		int[][] finder = { {B,B,B,B,B,B,B},
				{B,W,W,W,W,W,B},
				{B,W,B,B,B,W,B},
				{B,W,B,B,B,W,B},
				{B,W,B,B,B,W,B},
				{B,W,W,W,W,W,B},
				{B,B,B,B,B,B,B}};

		int[][] alignment ={ {B,B,B,B,B},
				{B,W,W,W,B},
				{B,W,B,W,B},
				{B,W,W,W,B},
				{B,B,B,B,B} };

		//// motif 1 = Finder Patterns

		if (motif == 1){
			int indiceRow = 0;
			int indiceCol = 0;

			for(int row = YDepart; row < YDepart + 7; row++){
				for(int col = XDepart; col < XDepart + 7; col++){
					matrix[col][row] = finder[indiceCol][indiceRow];
					indiceRow+=1;
				}
				indiceRow = 0;
				indiceCol += 1;

			}


		}

		//// motif 2 = Alignment patterns

		if (motif == 2){
			int indiceRow = 0;
			int indiceCol = 0;

			for(int row = YDepart; row < YDepart + 5; row++){
				for(int col = XDepart; col < XDepart + 5; col++){
					matrix[col][row] = alignment[indiceCol][indiceRow];
					indiceRow+=1;
				}
				indiceRow = 0;
				indiceCol += 1;

			}

		}

	}
	
	/**
	 * Add a specific pattern to qrcode matrix.
	 *
	 * @param matrix
	 *            
	 * @param Start column
	 * 
	 * @param End column
	 *      
	 * @param Row we are filling
	 * 
	 * @param Color used
	 */

	public static void RemplissageLigne(int[][] matrix, int Depart, int Fin,int row,int couleur){ /// Remplis ligne delimitee d'une couleur
		for(int col = Depart; col <= Fin; col++){
			matrix[col][row] = couleur;
		}
	}
	
	/**
	 * Add a specific pattern to qrcode matrix.
	 *
	 * @param matrix
	 *            
	 * @param Start row
	 * 
	 * @param End row
	 *      
	 * @param Column we are filling
	 * 
	 * @param Color used
	 */

	public static void RemplissageColonne(int[][] matrix, int Depart, int Fin,int col,int couleur){ /// Remplis colonne delimitee d'une couleur
		for(int row = Depart; row <= Fin; row++){
			matrix[col][row] = couleur;
		}
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 *
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if (version >=2){  //// Si la version est 1 on ajoute pas ce pattern
			AjouterMotif(matrix,matrix.length-9, matrix.length-9,2);
		}
	}

	/**
	 * Add the timings patterns
	 *
	 * @param matrix
	 *            The 2D array to modify
	 */
	
	public static void addTimingPatterns(int[][] matrix) {
		RemplissageColonne(matrix, 8, matrix.length -8, 6,W); ///// Ligne Verticale FULL BLANC
		RemplissageLigne(matrix, 8, matrix.length -8, 6, W);  //// Ligne Hotizontale FULL BLANC

		for(int row = 8; row < matrix.length - 8; row += 2){           ////// ajout noir
			matrix[6][row] = B;
		}
		for(int col = 8; col < matrix.length - 8; col += 2){           ////// ajout noir
			matrix[col][6] = B;
		}
	}

	/**
	 * Add the dark module to the matrix
	 *
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		matrix[8][matrix.length - 8] = B;
	}
	

	/**
	 * Add the format information to the matrix
	 *
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
		boolean bitFormat[] = QRCodeInfos.getFormatSequence(mask);

		int indice = 0; /// Index de la chaine de booleans de Format
		for (int row = matrix.length-1; row >= matrix.length-7; row--) {     ///// En bas a gauche (0 - 6)
			if (bitFormat[indice]) {
				matrix[8][row] = B;
			}
			else{
				matrix[8][row] = W;
			}

			indice += 1;
		}

		///indice = 7;

		for(int col = matrix.length-8; col <= matrix.length-1; col++){      ///// En haut a droite (7 - 14)
			if (bitFormat[indice]) {
				matrix[col][8] = B;
			}
			else{
				matrix[col][8] = W;
			}

			indice += 1;
		}

		indice = 0; /// On recomment a 0

		//// En haut gauche

		for(int col = 0; col <= 8; col++) {  ///// 0 - 7
			if (col != 6) {  /// Sauter Alignment Pattern
				if (bitFormat[indice]) {
					matrix[col][8] = B;
				} else {
					matrix[col][8] = W;
				}
				indice += 1;
			}
		}

		for(int row = 7; row >=0; row--){    ///// 8 - 14
			if(row != 6) { /// Sauter Alignment Pattern
				if (bitFormat[indice]) {
					matrix[8][row] = B;
				}
				else {
					matrix[8][row] = W;
				}
				indice += 1;
			}
		}
	}


	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 *
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param /color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		int couleur = 0;
		if (dataBit){                     ////// Recupere Couleur du bit étudié
			couleur = B;
		}
		else{
			couleur = W;
		}

		switch(masking){
			case 0:
				if((col + row) % 2 == 0){          ///// Mask 0 /////// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 1:
				if(row % 2 == 0){          ///// Mask 1 /////// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 2:
				if(col % 3 == 0){          ///// Mask 2 /////// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 3:
				if((col + row) % 3 == 0){          ///// Mask 3 /////// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 4:
				if(((row/2) + (col/3)) % 2 == 0){          ///// Mask 4 ///////  division entier Java ---- > valeur entiere
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 5:
				if(((col*row) % 2 )+((col*row) % 3) == 0){          ///// Mask 5 /////// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;

			case 6:
				if((((col * row) % 2) + ((col * row) % 3)) % 2 == 0){          ///// Mask 6 ///////  
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;


			case 7:
				if((((col + row) % 2) + ((col * row) % 3)) % 2 == 0){          ///// Mask 7 //// 
					if (couleur == B){
						couleur = W;
					}
					else{
						couleur = B;
					}
				}
				break;



		}
		return couleur;
	}
	

	/**
	 * Add the data bits into the QR code matrix
	 *
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		int indicebit = 0;

		int nbDoubleColonne = ((matrix.length - 6)/2); //// Nombre de double-colonnes  jusqu'au timing pattern

		///// Double-colonnes = colonnes de 2 bits

		for(int doubleCol = 0; doubleCol < nbDoubleColonne; doubleCol++){            ///////// Double-colonnes jusqu'au timing pattern

			if(doubleCol % 2 == 0){                    /////// Double-colonnes paire MONTANTE

				//// Ces boucles montent les double-colonnes en ajoutant les bits cotes a cotes.

				/// (matrix.length-1) - 2*i recupere la coordonnee de colonne du bit grace a sa double colonne
				/// On ajoute 1 pour le bit situe juste a cote

				//// Un "if" est ajoute pour verifier si on a pas deja place tous les bits. Si oui, on complete avec des 0 (false).


				for(int row = matrix.length-1; row >=0; row--){ /// Monte double-colonne
					if ((matrix[(matrix.length-1) - 2*doubleCol][row]) >> 24 == 0) {    //// Verification alpha == 0
						if(indicebit >= data.length){
							matrix[(matrix.length-1) - 2*doubleCol][row] = maskColor((matrix.length-1) - 2*doubleCol, row, false , mask);
						}
						else {
							matrix[(matrix.length - 1) - 2 * doubleCol][row] = maskColor((matrix.length - 1) - 2 * doubleCol, row, data[indicebit], mask);
							indicebit += 1;
						}
					}
					if ((matrix[(matrix.length-1) - 2*doubleCol -1][row]) >> 24 == 0) {
						if(indicebit >= data.length){
							matrix[(matrix.length-1) - 2*doubleCol -1][row] = maskColor((matrix.length-1) - 2*doubleCol - 1, row, false, mask);
						}
						else{
							matrix[(matrix.length-1) - 2*doubleCol -1][row] = maskColor((matrix.length-1) - 2*doubleCol - 1, row, data[indicebit], mask);
							indicebit+=1;
						}
					}
				}

			}
			else{                           ///// Colonne impaire DESCENDANTE,

				/// meme fonctionnement que pour les colonnes paires mais en descendant

				for(int row = 0; row < matrix.length; row++){ /// Descend double-colonne
					if ((matrix[(matrix.length-1) - 2*doubleCol][row]) >> 24 == 0) {
						if(indicebit >= data.length){
							matrix[(matrix.length - 1) - 2 * doubleCol][row] = maskColor((matrix.length - 1) - 2 * doubleCol, row, false, mask);
						}
						else {
							matrix[(matrix.length - 1) - 2 * doubleCol][row] = maskColor((matrix.length - 1) - 2 * doubleCol, row, data[indicebit], mask);
							indicebit += 1;
						}
					}
					if ((matrix[(matrix.length-1) - 2*doubleCol -1][row]) >> 24 == 0) {
						if(indicebit >= data.length){
							matrix[(matrix.length - 1) - 2 * doubleCol - 1][row] = maskColor((matrix.length - 1) - 2 * doubleCol - 1, row, false, mask);
						}
						else {
							matrix[(matrix.length - 1) - 2 * doubleCol - 1][row] = maskColor((matrix.length - 1) - 2 * doubleCol - 1, row, data[indicebit], mask);
							indicebit += 1;
						}
					}

				}


			}

		}

		for(int doubleCol = 0; doubleCol <= 2; doubleCol++){         ///// 3 dernieres colonnes apres timing pattern

			if(doubleCol % 2 == 0){       ///// DESCENDANTE
				for(int row = 9; row <= matrix.length - 8; row++){

					if ((matrix[5 - 2*doubleCol][row]) >> 24 == 0) {
						if(indicebit >= data.length){
							matrix[5 - 2 * doubleCol][row] = maskColor(5 - 2 * doubleCol, row, false, mask);
						}
						else {
							matrix[5 - 2 * doubleCol][row] = maskColor(5 - 2 * doubleCol, row, data[indicebit], mask);
							indicebit += 1;
						}

					}
					if ((matrix[5 - 2*doubleCol - 1 ][row]) >> 24 == 0) {
						if(indicebit >= data.length){
							matrix[5 - 2 * doubleCol - 1][row] = maskColor(5 - 2 * doubleCol - 1, row, false, mask);
						}
						else {
							matrix[5 - 2 * doubleCol - 1][row] = maskColor(5 - 2 * doubleCol - 1, row,  data[indicebit], mask);
							indicebit += 1;
						}

					}
				}
			}
			else{   ///// MONTANTE
				for(int row = matrix.length - 8 ; row >= 9; row--){
					if((matrix[3][row]) >> 24 == 0){
						if(indicebit >= data.length){
							matrix[3][row] = maskColor(3, row, false, mask);
						}
						else {
							matrix[3][row] = maskColor(3, row, data[indicebit], mask);
							indicebit += 1;
						}

					}
					if((matrix[2][row]) >> 24 == 0){
						if(indicebit >= data.length){
							matrix[2][row] = maskColor(2, row, false, mask);
						}
						else {
							matrix[2][row] = maskColor(2, row, data[indicebit], mask);
							indicebit += 1;
						}

					}
				}
			}


		}


	}

	/*
	 * =======================================================================
	 *
	 * ****************************** BONUS **********************************
	 *
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 *
	 * The mask is computed automatically so that it provides the least penalty
	 *
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);



		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 *
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {

		int penaliteMin = evaluate(renderQRCodeMatrix(version, data, 0));
		int bestMask = 0;

		int penalite = 0;
		for(int i = 1; i<= 7; i++){ ///// calcule le nombre de penalites pour chaque mask
			penalite = evaluate(renderQRCodeMatrix(version, data, i));
			if(penalite < penaliteMin){
				penaliteMin = penalite;
				bestMask = i;
			}
			penalite = 0;
		}

		System.out.print(bestMask);
		return bestMask;


	}

	/**
	 * Compute the penalty score of a matrix
	 *
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		int penalite = 0;

		////// PENALITE 1 :  5 MODULES DE SUITE DE MEME COULEUR

						/// COLONNES

		for (int col = 0; col < matrix.length; col++) { ////// choisit colonnes
			for (int row = 0; row < matrix.length-4; row++) {    ///// parcoure lignes de la colonne
				if   ((matrix[col][row] == matrix[col][row+1])    /// verifie si 5 des suites meme couleur
						&& (matrix[col][row] == matrix[col][row+2])
						&& (matrix[col][row] == matrix[col][row+3])
						&& (matrix[col][row] == matrix[col][row+4])) {
					penalite += 3;
					row += 4;
					int indice = row; /// memorise indice 5eme element (pour comparer couleur)
					if(row+1 != matrix.length) {
						while ((row + 1 != matrix.length) && matrix[col][indice] == matrix[col][row + 1]) { ////// Verfier combien de plus sont de la meme couleur
							penalite += 1;
							row += 1;
							}
						}
					}
				}
			}



		////// PENALITE 2 :  5 MODULES DE SUITE DE MEME COULEUR

					   /// LIGNES

		for (int row = 0; row < matrix.length; row++) { ////// choisit ligne
			for (int col = 0; col < matrix.length-4; col++) {    ///// parcoure colonnes de la ligne
				if ((matrix[col][row] == matrix[col+1][row])   // verifie si 5 des suites meme couleur
						&& (matrix[col][row] == matrix[col+2][row])
						&& (matrix[col][row] == matrix[col+3][row])
						&& (matrix[col][row] == matrix[col+4][row])) {
					penalite += 3;
					col += 4;
					int indice = col; /// memorise indice 5eme element (pour comparer couleur)
					if(col+1 != matrix.length) {
						while ((col + 1 != matrix.length) && matrix[indice][row] == matrix[col+1][row]) { ////// Verfier combien de plus sont de la meme couleur
							penalite += 1;
							col += 1;
						}
					}
				}
			}
		}

		////// PENALITE 3 :  CARRE 2X2

		for(int row = 0; row < matrix.length - 1; row++){   ///// Parcoure lignes
			for(int col = 0; col < matrix.length - 1; col++){ ///// Parcoure colonnes de la ligne

				if((matrix[col][row] == matrix[col+1][row]) && (matrix[col][row] == matrix[col][row+1]) && (matrix[col][row]== matrix[col+1][row+1])){
					penalite +=3;
				}

			}
		}

		////// PENALITE 4 :  Sequences

		/// Ajouter bordure blanche

		int qrCodeBordure[][] = ajouterBordure(matrix);
		int[] sequence1 = {W,W,W,W,B,W,B,B,B,W,B};
		int[] sequence2 = {B,W,B,B,B,W,B,W,W,W,B};

		penalite += findsequencePenaliteLigne(qrCodeBordure, sequence1);

		penalite += findsequencePenaliteLigne(qrCodeBordure, sequence2);


		penalite += findsequencePenaliteColonne(qrCodeBordure, sequence1);


		penalite += findsequencePenaliteColonne(qrCodeBordure, sequence2);



		///// DERNIERS POINTS


		double moduleTotal = (matrix.length * matrix.length);
		double moduleNoir = 0;

		for(int col = 0; col < matrix.length; col++){        ///// Calcul nombre de modules noirs
			for(int row = 0; row < matrix.length; row++){
				if (matrix[col][row] == B){
					moduleNoir += 1 ;

				}
			}

		}


		int pourcentageModuleNoir  = (int) ((moduleNoir / moduleTotal) *100);


		int pourcentage5Pre = 0;
		int pourcentage5Pos = 0;

		if (pourcentageModuleNoir % 5 == 0){
			pourcentage5Pre = pourcentageModuleNoir;
			pourcentage5Pos = pourcentageModuleNoir + 1;
			while(pourcentage5Pos % 5 != 0){
				pourcentage5Pos+=1;
			}
		}
		else {
			pourcentage5Pre = pourcentageModuleNoir - 1;
			pourcentage5Pos = pourcentageModuleNoir + 1;


			while (pourcentage5Pos % 5 != 0) {

				pourcentage5Pos += 1;
			}
			while (pourcentage5Pre % 5 != 0) {
				pourcentage5Pre -= 1;
			}
		}

		pourcentage5Pos = Math.abs(pourcentage5Pos - 50);
		pourcentage5Pre = Math.abs(pourcentage5Pre - 50);

		if (pourcentage5Pos < pourcentage5Pre){
			penalite += 2 * pourcentage5Pos;
		}
		else{
			penalite += 2 * pourcentage5Pre;
		}


		System.out.println(penalite);
		return penalite;
		}
	
	
	
	
	/**
	 * Delete randomly pixel in the Qrcode to simulate data damage
	 *
	 * @param qrcode matrix 
	 * 
	 * @param pourcentage of pixel to delete
	 * 
	 * @return qrcode damaged
	 */

	public static void damage(int[][] qrcode, double errorPercentage){
		int  NbPixelsDelete =(int) (errorPercentage * (qrcode.length* qrcode.length)/100);
		NbPixelsDelete = NbPixelsDelete - 3*64;   //// Nb de Pixels  3 Finder Patterns
		boolean[][] checkDelete = new boolean[qrcode.length][qrcode.length]; ///// Matrice verifiant si pixel est effacé
		int colonne = 0;
		int ligne = 0;

		for(int i = 1; i <= NbPixelsDelete; i++){

			while(
					((colonne >= 0 && colonne <= 7) && (ligne >= 0 && ligne <= 7))
					|| ((colonne >= qrcode.length - 8 && colonne <= qrcode.length -1) && (ligne >= 0 && ligne <= 7))
					|| ((colonne >= 0 && colonne <= 7) && (ligne >= qrcode.length - 8 && ligne <= qrcode.length -1))
					//// Tant qu'on tombe sur un pixel situe sur un finder pattern on recherche une autre ligne et colonne
			) {
				colonne = (int) (Math.random() * qrcode.length);
				ligne = (int) (Math.random() * qrcode.length);
			}
			if (!checkDelete[colonne][ligne]){  //// Si pixel pas efface
				qrcode[colonne][ligne] = transparent;
				checkDelete[colonne][ligne] = true;
			}
			else{i -= 1;}  //// Si pixel deja efface reviens en arriere de 1 pour re essayer
			colonne = 0;
			ligne = 0;
		}


	}
	
	/**
	 * Add white border to qrCode
	 *
	 * @param qrcode matrix 
	 * 
	 * @return qrcode with white border
	 */

	public static int[][] ajouterBordure(int[][] matrix){
		int qrCodeBordure[][] = new int[matrix.length+2][matrix.length+2];

		for(int row = 0; row < qrCodeBordure.length; row += qrCodeBordure.length-1) {   ///// Ligne haut et basse blanche
			for (int col = 0; col < qrCodeBordure.length; col++) {
				qrCodeBordure[col][row] = W;
			}
		}

		for(int col = 0; col < qrCodeBordure.length; col += qrCodeBordure.length-1) {   ///// Colonne gauche et droite blanche
			for (int row = 0; row < qrCodeBordure.length; row++) {
				qrCodeBordure[col][row] = W;
			}
		}

		for(int col = 0; col < matrix.length; col++){
			for(int row = 0; row < matrix.length; row++){
				qrCodeBordure[col+1][row+1] = matrix[col][row]; //// On remplit la nouvelle matrice avec les infos du QR
			}
		}


		return qrCodeBordure;
	}
	
	
	/**
	 * Go through all row of qrCode searching for a specific sequence and add penalty point if it has been found
	 *
	 * @param qrcode matrix 
	 * 
	 * @param seuence to find
	 * 
	 * @return penalty points
	 */

	public static int findsequencePenaliteLigne(int[][] matrix, int[] sequence){
		boolean trouvee = true;
		int penalite = 0;

		///// Ligne

		for(int row = 0; row < matrix.length; row++) { //// Parcoure lignes
			for (int col = 0; col < matrix.length; col++) {  //// Parcoure colonnes de  la ligne
				if (col + sequence.length < matrix.length) {
					for (int sequenceIndex = 0; sequenceIndex < sequence.length; sequenceIndex++) {

						if (sequence[0] != matrix[col + sequenceIndex][row]) {
							trouvee = false;
						}
					}


					if (trouvee) {
						penalite += 40;
						trouvee = false;
					}
				}


			}
		}

		return penalite;
	}
	
	/**
	 * Go through all columns of qrCode searching for a specific sequence and add penalty point if it has been found
	 *
	 * @param qrcode matrix 
	 * 
	 * @param seuence to find
	 * 
	 * @return penalty points
	 */

	public static int findsequencePenaliteColonne(int[][] matrix, int[] sequence){
		boolean trouvee = true;
		int penalite = 0;

		///// Colonne

		for(int col = 0; col < matrix.length; col++) { //// Parcoure colonnes
			for (int row = 0; row < matrix.length; row++) {  //// Parcoure lignes de la colonne
				if (row + sequence.length < matrix.length) {
					for (int sequenceIndex = 0; sequenceIndex < sequence.length; sequenceIndex++) {

						if (sequence[0] != matrix[col][row+sequenceIndex]) {
							trouvee = false;
						}
					}


					if (trouvee) {
						penalite += 40;
						trouvee = false;
					}
				}


			}
		}

		return penalite;
	}

}
