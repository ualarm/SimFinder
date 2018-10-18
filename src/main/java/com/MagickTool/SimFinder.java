package com.MagickTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;


// class for output data: similarity and elapsed
class MagickOutput {
	private double similarity;
	private double elapsed;
	
	public double GetSimilarity() {
		return similarity;
	}
	public double GetElapsed() {
		return elapsed;
	}
	
	public void SetSimilarity(double s) {
		similarity = s;
	}
	public void SetElapsed(double e) {
		elapsed = e;
	}
}

// Main class
public class SimFinder {

	// open image names csv file, looping to compare image pairs line by line, write output to another csv file 
	public static void processFiles(String csv, String base)
	{
		File csvFile = new File(csv);
		if( !csvFile.exists() ) 
		{
			System.err.println("csv file not found: " + csvFile);
			return;
		}
		File imgDir = new File(base);
		if( !imgDir.exists() ) 
		{
			System.err.println("image directory not found: " + imgDir);
			return;
		}
		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
			String output_csv = csv.replace(".csv", "_output.csv");
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(output_csv))));
			String line, output_line;
			int count = 0;
			bufferedWriter.write("image1,image2,similar,elapsed");
			bufferedWriter.newLine();
			while ((line = bufferedReader.readLine()) != null)
			{
				count++;
				if( count > 1 )
				{
					try
					{
						String[] imgs = line.split(",");
						String f1Path = base+File.separator+imgs[0];
						String f2Path = base+File.separator+imgs[1];
						File f1 = new File(f1Path);
						File f2 = new File(f2Path);
						if( !f1.exists() )
						{
							System.err.println("At line " + count + " in csv file, File1 not found: " + f1Path);
							continue;
						}
						if( !f2.exists() )
						{
							System.err.println("At line " + count + " in csv file, File2 not found: " + f2Path);
							continue;
						}
						MagickOutput m = compareImage(f1Path, f2Path);
						DecimalFormat formatter = new DecimalFormat("#0.00");
						bufferedWriter.write(imgs[0]+","+imgs[1]+","+formatter.format(m.GetSimilarity())+","+m.GetElapsed());
						bufferedWriter.newLine();
						
					}
					catch(Exception e)
					{
						System.err.println("exception comparing line " + count + " in csv file");
					}
				}
			}
			bufferedReader.close();
			bufferedWriter.close();
			System.out.println("Output written in " + output_csv);
		}
		catch(Exception e)
		{
			System.err.println("Exception in processFiles: " + e.getMessage());
		}
	}
	
	// main method: process program argument and call processFiles function
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if( args.length != 2)
		{
			System.err.println("Usage: simfinder path_of_csv path_of_image_folder");
			return;
		}
		else 
		{
			processFiles(args[0], args[1]);
		}
//		String img1 = "C:\\Eclipse\\SimFinder\\test\\images\\wizard.jpg";
//		String img2 = "C:\\Eclipse\\SimFinder\\test\\images\\wizard.png";
//		try {
//			File f1 = new File(img1);
//			File f2 = new File(img2);
//			if( !f1.exists() )
//				throw new java.io.FileNotFoundException("File 1 not found: " + img1);
//			if( !f2.exists() )
//				throw new java.io.FileNotFoundException("File 2 not found: " + img2);
//			compareImage(img1, img2);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	// compare a image pair by calling ImageMagick command tool 'compare'
	// parse command line output and return MagickOutput object
	public static MagickOutput compareImage(String img1, String img2) throws Exception
	{
		String execOS = System.getProperty("os.name");
		String baseDir = System.getProperty("user.dir");
		String compFile;
		String[] cmd;
		if ( execOS.contains("Windows") )
		{
			compFile = baseDir + File.separator + "compare_win.bat";
			cmd = new String[] {"cmd.exe", "/c", compFile, img1, img2};			
		}
		else if (execOS.contains("MacOS") || execOS.contains("Solaris") || execOS.contains("Linux") )
		{
			compFile = baseDir + File.separator + "compare_mac.sh";
			cmd = new String[] {"./"+compFile, img1, img2};
		}
		else
		{
			System.err.println("Operating System not supported: " + execOS);
			return null;
		}
		
		long startTime = System.currentTimeMillis();
		Process compProces = Runtime.getRuntime().exec(cmd, new String[0]);
		InputStream std = compProces.getInputStream();
		InputStream err = compProces.getErrorStream();

		StringBuffer outsb = new StringBuffer(40);
		StringBuffer errsb = new StringBuffer(40);

		do 
		{
			int ch = std.read();
			if (ch == -1) break;
			outsb.append( (char) ch);
		} while (true);

		do 
		{
			int ch = err.read();
			if (ch == -1) break;
			errsb.append( (char) ch);
		} while (true);
		
		int exitValue = compProces.waitFor();
		//System.out.println("Process exit with " + exitValue);
		long endTime = System.currentTimeMillis();
		double elapsed = (endTime - startTime)/1000.0;
		compProces.getOutputStream().close();
		std.close();
		err.close();
		
		String ret = outsb.toString() + errsb.toString();
		//System.out.println(ret);
		// parse the first number in output
		int n1 = ret.indexOf("(");
		int n2 = ret.indexOf(")");
		MagickOutput mo = new MagickOutput();
		mo.SetSimilarity(Double.parseDouble(ret.substring(n1+1, n2)));
		mo.SetElapsed(elapsed);
		return mo;
	}
}
