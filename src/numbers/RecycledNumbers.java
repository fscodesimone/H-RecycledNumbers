package numbers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
/**
 * 
 */

/**
 * @author Francesco De Simone
 *
 */
public class RecycledNumbers {
	
	
	static int caseNumber;
	static int totalCases;
	static String NEW_LINE = System.getProperty("line.separator");

	/*
	 * Handles all of the file IO for the project, and iterates through every test case
	 * so that all of the other methods only need to be aware of, at most, a single test
	 * case and nothing else. It throws IOException because if there were any in the context
	 * of running the test input, I would be happy with a program crash and stacktrace 
	 * available immediately.
	 */
	public static void main(String[] args) throws IOException {

		caseNumber = 1;
		String path = new File(".").getAbsolutePath();
		File fileIn = new File(path+"/large.in");
		FileInputStream fileInputStream = null;
		BufferedInputStream inputStream = null;
		BufferedReader reader = null;

		String fileOutPath = "largeOutput.txt";
		File fileOut = new File(fileOutPath);
		if (!fileOut.createNewFile()) {
			fileOut.delete();
			fileOut.createNewFile();
		}

		Writer out = new OutputStreamWriter(new FileOutputStream(fileOutPath), "US-ASCII");

		StringBuilder builder = new StringBuilder();

		try {
			fileInputStream = new FileInputStream(fileIn);
			inputStream = new BufferedInputStream(fileInputStream);
			reader = new BufferedReader(new InputStreamReader(inputStream));

			totalCases = Integer.parseInt(reader.readLine());
			while (caseNumber <= totalCases) {
				builder.append(handleTestCase(reader));
				builder.append(NEW_LINE);
				caseNumber++;
			}

			// dispose all the resources after using them.
			fileInputStream.close();
			inputStream.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(builder.toString());
		out.write(builder.toString());
		out.close();
	}

	/*
	 * The first number of each line is the lower end of numbers for which we're supposed to
	 * find the Recycled Numbers. Simply iterates through all of the numbers, using helper
	 * methods to get the results for each one, and formats the result for the whole test case
	 */
	public static String handleTestCase(BufferedReader reader) throws Exception {
		String[] numsAsStrings = reader.readLine().split(" ");
		int start = Integer.parseInt(numsAsStrings[0]);
		int end = Integer.parseInt(numsAsStrings[1]);
		int pairs = 0;
		int current = start;
		while (current <= end){
			pairs += getNumRecycledPairsInRange(current, start, end);
			current++;
		}
		return "Case #"+caseNumber+": "+pairs;
	}
	
	public static int getNumRecycledPairsInRange(int orig, int start, int end){
		int pairs = 0;
		ArrayList<Integer> options = getAllRotationsInRange(orig, start, end);
		for(int i = 0; i < options.size(); i++){
			if (orig < options.get(i)){
				pairs++;
			}
		}
		return pairs;
	}
	
	/*
	 * returns an ArrayList of Integers that are "rotations" of the int parameter "orig".
	 * These rotations are all between the ints start and end (inclusive). This is used
	 * to find the number of possible options for each number that are determined to be 
	 * valid or invalid in another method.
	 */
	public static ArrayList<Integer> getAllRotationsInRange(int orig, int start, int end){
		if(orig < 10)
			return new ArrayList<Integer>(0);
		char[] origAsChars = String.valueOf(orig).toCharArray();
		ArrayList<Integer> allRotations = new ArrayList<Integer>(origAsChars.length-1);
		char[] curRotation = origAsChars;
		
		/*
		 * Rotates the char array that represents the original integer numDigits - 1 times
		 * to create all rotations. Every time it's made a rotation, it creates an Integer
		 * from the array of chars, and adds it to the list allRotations if it is within
		 * the given range defined in the method parameters.
		 * i < length - 1 because I don't want to repeat the original by rotating it 
		 * as many times as the length of the array (which is the number of digits in the number)
		 */
		for(int i = 0; i < origAsChars.length - 1; i++){ 
			char temp = curRotation[0];
			for(int j = 1; j < origAsChars.length; j++){
				curRotation[j-1] = curRotation[j];
			}
			curRotation[curRotation.length-1] = temp;
			Integer curInt = new Integer(String.valueOf(curRotation));
			
			//Adds it to the rotations list only if it is within the range, and not a copy of the original
			//Which could be created by the number 2222, for example.
			if (curInt >= start && curInt <= end && curInt != orig)
				allRotations.add(curInt);
		}
		
		//I would have used a data structure that assures uniqueness, or dealt with it 
		//earlier, had I thought ahead of time of the possibility of duplicates such as "1212"
		//having "2121" be a valid rotation and recycled number pair twice.
		makeListUnique(allRotations); 
		return allRotations;
	}
	
	/*
	 * Makes the list unique in the most obvious, niave solution. This O(n^2) solution was
	 * used due to time constraints, and the fact that it's dependent only on the number of
	 * digits in each number, which is trivially small for an O(n^2) solution.
	 */
	public static void makeListUnique(ArrayList<Integer> list){
		for(int i = 0; i < list.size(); i++){
			for(int j = i+1; j < list.size(); j++){
				if(list.get(i).equals(list.get(j))){
					list.remove(j);
				}
			}
		}
	}

}
