package qrcode;

import java.nio.charset.StandardCharsets;

import reedsolomon.ErrorCorrectionEncoding;


public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
			int maxlength = QRCodeInfos.getMaxInputLength(version);
			int[] sequence1 = encodeString(input, maxlength);

			if (sequence1.length != 0) { /// Si chaine encodee pas vide


				int[] sequence2 = addInformations(sequence1);

				int finalLength = QRCodeInfos.getCodeWordsLength(version);

				int[] sequence3 = fillSequence(sequence2, finalLength);


				int eccLength = QRCodeInfos.getECCLength(version);
				int[] sequence4 = addErrorCorrection(sequence3, eccLength);

				boolean[] sequenceFinale = bytesToBinaryArray(sequence4);
				return sequenceFinale;


			}

			else{ /// Si chaine encodee vide
				int[] sequence2 = {0};
				int[] sequence3 = addInformations(sequence2);

				int finalLength = QRCodeInfos.getCodeWordsLength(version);

				int[] sequence4 = fillSequence(sequence3, finalLength);


				int eccLength = QRCodeInfos.getECCLength(version);
				int[] sequence5 = addErrorCorrection(sequence4, eccLength);

				boolean[] sequenceFinale = bytesToBinaryArray(sequence5);
				return sequenceFinale;
			}
		}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code)
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	public static int[] encodeString(String input, int maxLength) {
		byte[] chaineByte = input.getBytes(StandardCharsets.ISO_8859_1);
		int[] chaineEntier;


		if (chaineByte.length >= maxLength) {
			chaineEntier = new int[maxLength];
			for (int i = 0; i < maxLength; i++) {
				chaineEntier[i] = (chaineByte[i] & 0xFF);
			}
		}
		else{
			chaineEntier = new int[chaineByte.length];
			for (int i = 0; i < chaineByte.length; i++) {
				chaineEntier[i] = (chaineByte[i] & 0xFF);
			}

		}
		return chaineEntier;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 *
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {

		int[] tableauInfo = new int[inputBytes.length + 2];

		int tailleBitFort = ((inputBytes.length & 0xFF) >> 4);   ////// Octet 0
		tableauInfo[0] = ((0b0100 << 4) | (tailleBitFort));


		int tailleBitFaible = (inputBytes.length & 0xF);         ////// Octet 1

		int bitFortByte0 = ((inputBytes[0] & 0xFF) >> 4) ;

		tableauInfo[1] = ((tailleBitFaible << 4) | (bitFortByte0));


		for (int i = 2; i < tableauInfo.length - 1; i++){        ////// Octet 2 --- > Octet taille(nouveau tableau)-2

			int BitFaibleElementAvant = (inputBytes[i-2] & 0xF);

			int BitFortElementApres = ((inputBytes[i-1] & 0xFF) >> 4);

			tableauInfo[i] = ((BitFaibleElementAvant << 4) | (BitFortElementApres));

		}

		int BitFaibleDernier = ((inputBytes[inputBytes.length - 1] & 0xF));     ////// Dernier Octet
		tableauInfo[tableauInfo.length - 1] = (BitFaibleDernier << 4);



		return tableauInfo;
	}

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 *
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		if (finalLength > encodedData.length) {
			int[] tableauComplete = new int[finalLength];
			for (int i = 0; i < encodedData.length; i++) { /// Ajoute au nouveau tableau bytes encodés
				tableauComplete[i] = encodedData[i];
			}
			int compte = encodedData.length;
			while (compte < finalLength) { //// Complete tableau avec 236 et 17
				tableauComplete[compte] = 236;
				compte += 1;

				if (compte < finalLength) {
					tableauComplete[compte] = 17;
					compte += 1;
				}
			}
			return tableauComplete;
		}
		else{
			return encodedData;
		}

	}

	/**
	 * Add the error correction to the encodedData
	 *
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		int[] tableauCorrige = new int[encodedData.length + eccLength];

		for(int i = 0; i < encodedData.length; i++){ // Ajoute au nouveau tableau bytes encodés
			tableauCorrige[i] = encodedData[i];
		}

		int[] ErrorCorrection = ErrorCorrectionEncoding.encode(encodedData, eccLength);

		for(int i = 0; i < ErrorCorrection.length; i++){ //// Complete avec bytes de Error correction
			tableauCorrige[encodedData.length + i] = ErrorCorrection[i];
		}

		return tableauCorrige;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 *
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) {
		boolean[] tableauBit = new boolean[(data.length) * 8];
		char element = '0';

		for (int i = 0; i < data.length; i++){
			String nul = "";
			String chaine =  (Integer.toBinaryString(data[i]));


			for(int k = 0; k < 8 - chaine.length(); k++){  ///// Ajoute 0 non significatif si necessaire
				nul += element;
			}

			chaine = nul +chaine;

			for (int j = 0 ;j < chaine.length(); j++){
				if (chaine.charAt(j) == '1'){
					tableauBit[(i*8) +j]= true;           /////// 8*i ---> A quel octet je suis
					                                     //////// j -----> Index bit de cet octet
				}
				else{
					tableauBit[(i*8) +j]= false;
				}


			}

		}


		return tableauBit;
	}

}
