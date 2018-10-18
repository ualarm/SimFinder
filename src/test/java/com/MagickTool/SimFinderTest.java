package com.MagickTool;

import org.junit.Assert;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class SimFinderTest {
	
	@BeforeClass
	public static void initSim() {
	}

	@Before
	public void beforeEachTest() {
		
	}
	
	@After
	public void afterEachTest() {
		
	}
	
	// test process same file where similarity should be 0
	@SuppressWarnings("deprecation")
	@Test
	public void TestSame() {
		String imageFile = "test\\image_name_same.csv";
		String imageDir = "test\\images";

		try {
			SimFinder.processFiles(imageFile, imageDir);
			String output_csv = imageFile.replace(".csv", "_output.csv");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(output_csv));
			int count = 0;
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				count++;
				if( count == 2 )
				{
					String[] imgs = line.split(",");
					Assert.assertEquals(imgs.length, 4);
					double d = Double.parseDouble(imgs[2]);
					Assert.assertEquals(d, 0.0, 0.0001);
				}
			}
			Assert.assertEquals(count, 2);
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
	
	// test process same file where similarity should be 0.02
	@SuppressWarnings("deprecation")
	@Test
	public void TestDiff() {
		String imageFile = "test\\image_name_diff.csv";
		String imageDir = "test\\images";

		try {
			SimFinder.processFiles(imageFile, imageDir);
			String output_csv = imageFile.replace(".csv", "_output.csv");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(output_csv));
			int count = 0;
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				count++;
				if( count == 2 )
				{
					String[] imgs = line.split(",");
					Assert.assertEquals(imgs.length, 4);
					double d = Double.parseDouble(imgs[2]);
					Assert.assertEquals(d, 0.02, 0.001);
				}
			}
			Assert.assertEquals(count, 2);
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
	
	// test image file in csv not found
	@SuppressWarnings("deprecation")
	@Test
	public void TestImageFileNotFound() {
		String imageFile = "test\\image_name_notfound.csv";
		String imageDir = "test\\images";

		try {
			SimFinder.processFiles(imageFile, imageDir);
			String output_csv = imageFile.replace(".csv", "_output.csv");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(output_csv));
			int count = 0;
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				count++;
			}
			Assert.assertEquals(count, 1);
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
	
	// test csv file not found
	@SuppressWarnings("deprecation")
	@Test
	public void TestFileNotFound() {
		String imageFile = "test\\image_name_nofound.csv";
		String imageDir = "test\\images";

		try {
			String output_csv = imageFile.replace(".csv", "_output.csv");
			File f = new File(output_csv);
			if( f.exists() )
				f.delete();
			SimFinder.processFiles(imageFile, imageDir);
			if( f.exists() )
				Assert.fail("No result file should be generated if csv file not found");
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
}
