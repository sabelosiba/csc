/* Serial filter program for image processing 
 * by smoothing RGBcolor of each pixel setting it to 
 * the average of the surrounding pixels
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.*;


public class MeanFilterSerial{
    private String inImage;
    private String outImage;
    private int window;
    BufferedImage image =null;
    BufferedImage image1 = null;
    File f = null;
    File f1 =null;
    int max =0;
    int h=0;
    int w=0;
    int odd=0;
    int index=0;
    
    /** Main method for the application of the Serial filter
     * @param args store string array, if not specified it is empty
     */   
    public static void main(String args[]) throws FileNotFoundException, IOException{
        MeanFilterSerial mean;
        int wii = Integer.parseInt(args[2]);
        try{
            if(wii%2 != 0 && wii >= 3 ){
                mean = new MeanFilterSerial(args[0], args[1], args[2]);
                final long start = System.currentTimeMillis() ; 
                mean.imageMean();
                final long elapsed = System.currentTimeMillis() - start; 
                System.out.println("Time elapsed = "+elapsed+" Milliseconds for "+mean.inImage + " of width*Height: "+mean.w+"*"+mean.h +" window filter size:"+ mean.window+"*"+mean.window);
                mean.printImage();
            }
            else{
                System.out.println("Enter correct file names with extension and/or window width that is an odd number and atleast 3");
                System.exit(0);
            }
        }catch(Exception e){
            System.out.println("Error somewhere");
            System.exit(0);
        }
    }
    
    /* constructor method which passed arguments if instance of the class created and 
     * calls method to read an image to set image parameters
     * @param inIma is name of input image String
     * @param outIma is name of output image String
     * @param wind is window width that defines neighbouring pixels
     */
     
    public MeanFilterSerial(String inIma, String outIma, String wind) throws IOException{
        inImage = inIma;
        outImage = outIma ;
        window = Integer.parseInt(wind);
        read();
        max = window*window;
        odd = (window -1)/2;
        h = image1.getHeight();
        w= image1.getWidth();
    }
    
    /*void Helper method to read input image
     */
    public void read() throws IOException{
        try{
            f = new File("C:\\\\Users\\\\wwwsa\\\\OneDrive - University of Cape Town\\\\Desktop\\\\csc2\\\\data"+ "\\\\" +inImage);
            image = ImageIO.read(f);
            image1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            image1 = ImageIO.read(f);
            System.out.println("Read image successful");
        }catch(IOException e){
            System.out.println("Error reading image: " + e);
            System.exit(0);
        }
    }
    /*void Helper method which use sliding square window algorithm  
     * to calculate mean and set each pixel accordingly
     */
    public void imageMean() throws IOException{
        for(int i=odd; i< h-odd; i++){
            for(int j=odd; j< w-odd; j++){
                int sumRed =0;
                int sumGreen=0;
                int sumBlue=0;
                for(int i1= i-odd; i1<= i+odd; i1++){
                    for(int j1= j-odd; j1<= j+odd; j1++){
                        int pixel = image1.getRGB(j1,i1);
                        sumRed += (pixel >> 16) & 0xff;
                        sumGreen += (pixel >> 8) & 0xff;
                        sumBlue += (pixel) & 0xff;
                    }
                }
                sumRed =sumRed/ max;
                sumGreen = sumGreen/max;
                sumBlue = sumBlue/max;
                int mean = (sumRed<<16) | (sumGreen<<8) | sumBlue;
                image1.setRGB(j,i, (int)(mean));
            }
        }  
    }
    /* void helper method to write the filtered image to output image
     */
    public void printImage() throws IOException{
        try{
            f1 = new File("C:\\\\Users\\\\wwwsa\\\\OneDrive - University of Cape Town\\\\Desktop\\\\csc2\\\\data"+ "\\\\" +outImage);
            ImageIO.write(image1, "jpeg", f1);
            System.out.println("write image complete");
        }catch(IOException e){
            System.out.println("Error writing image: :" + e);
            System.exit(0);
        }
    } 
}
