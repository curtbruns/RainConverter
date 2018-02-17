import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.Scanner;
public class RainDriver {

	// 8-bit to 4-bit conversion with gamma color correction for the
	// Adafruit 1484 LED matrix
	static int gamma[] = {
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
			  0x00,0x00,0x01,0x01,0x01,0x01,0x01,0x01,
			  0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,
			  0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,
			  0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,
			  0x01,0x01,0x01,0x01,0x01,0x01,0x02,0x02,
			  0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,
			  0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,
			  0x02,0x02,0x02,0x02,0x02,0x03,0x03,0x03,
			  0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x03,
			  0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x04,
			  0x04,0x04,0x04,0x04,0x04,0x04,0x04,0x04,
			  0x04,0x04,0x04,0x04,0x04,0x04,0x05,0x05,
			  0x05,0x05,0x05,0x05,0x05,0x05,0x05,0x05,
			  0x05,0x05,0x05,0x06,0x06,0x06,0x06,0x06,
			  0x06,0x06,0x06,0x06,0x06,0x06,0x06,0x07,
			  0x07,0x07,0x07,0x07,0x07,0x07,0x07,0x07,
			  0x07,0x07,0x08,0x08,0x08,0x08,0x08,0x08,
			  0x08,0x08,0x08,0x08,0x09,0x09,0x09,0x09,
			  0x09,0x09,0x09,0x09,0x09,0x0a,0x0a,0x0a,
			  0x0a,0x0a,0x0a,0x0a,0x0a,0x0a,0x0b,0x0b,
			  0x0b,0x0b,0x0b,0x0b,0x0b,0x0b,0x0c,0x0c,
			  0x0c,0x0c,0x0c,0x0c,0x0c,0x0c,0x0d,0x0d,
			  0x0d,0x0d,0x0d,0x0d,0x0d,0x0e,0x0e,0x0e,
			  0x0e,0x0e,0x0e,0x0e,0x0f,0x0f,0x0f,0x0f
			};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner myKeyboard = new Scanner(System.in);
		
		boolean run = true;
		
		// Ask the user for the directory containing the images
		// and whether they wish to specify timing information
//		System.out.print("Enter the directory containing the images: ");
//		String fdir = myKeyboard.nextLine();
                String fdir = "/Users/cebruns/Documents/workspace/RainConverter/assets_raw";
		int timing = -1;
//		System.out.print("Enter a timing for the files? (y/n) "); 
//		if(myKeyboard.nextLine().equalsIgnoreCase("y")) {
//			System.out.print("Enter a timing value (0-15): ");
//			timing = myKeyboard.nextInt();
//			
//		}
		
		File folder = new File(fdir + "/");
		
		// Iterate through each image file
                
		for(final File fileEntry : folder.listFiles()) {
			BufferedImage img = null;
			try {
			    img = ImageIO.read(fileEntry);
			} catch (IOException e) {
				String workingDir = System.getProperty("user.dir");
			    System.out.println("The file " + fileEntry.getName() + " could not be loaded");
			    continue;
			}
			
			String output = "";
                        System.out.println("Working on file " + fileEntry.getName());
			if (!fileEntry.getName().endsWith(".png")) {
                            continue;
                        }
			// Go through the first 32 by 32 pixels and create a 3 character string
			// for each pixel that represents its RGB values in hex
			for(int i = 0; i < 32; i++) {
				for(int j = 0; j < 32; j++) {
					Color colo = new Color(img.getRGB(i, j), true);
					output += Integer.toHexString(gamma[colo.getRed()]) + Integer.toHexString(gamma[colo.getGreen()]) + Integer.toHexString(gamma[colo.getBlue()]);  
					System.out.print(Integer.toHexString(gamma[colo.getRed()]));
					System.out.print(Integer.toHexString(gamma[colo.getGreen()]));
					System.out.print(Integer.toHexString(gamma[colo.getBlue()]));
				}
			}
			
			// Write the timing information at the end of the file if any
			if(timing >= 0) {
				output += Integer.toHexString(timing);
			}
			
			String fOut = fileEntry.getName() + ".txt";
						
			PrintWriter myFile = null;
			
			try {
				 myFile = new PrintWriter(fOut); 
			} catch(FileNotFoundException e) {
				
				System.err.println("Could not save file " + fOut);
				System.exit(0);
			}
			
			// Write the converted image information to the new file
			myFile.print(output);
			myFile.close();
			
			System.out.println("File " + fOut + " was saved successfully");
			
		}
		
		System.out.println("Goodbye!");
		myKeyboard.close();
	}

}
