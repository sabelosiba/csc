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
    String loc = "/home/sabelo/csc2/data/";
    BufferedImage image =null;
    File f = null;
    
    int sumRed = 0;
    int sumGreen =0;
    int sumBlue =0;
    
       
    public static void main(String args[]) throws FileNotFoundException{
        MeanFilterSerial mean;
        int wii = Integer.parseInt(args[2]);
        try{
            if(wii >= 3 ){
                mean = new MeanFilterSerial(args[0], args[1], wii);
                mean.imageMean();
            }
            else{
                System.out.println("Enter window width that is an odd number and atleast 3");
            }
        }catch(Exception e){
            System.out.println("Error somewhere");
        }
    }
    
    public MeanFilterSerial(String inImage, String outImage, int window) throws IOException{
        inImage = this.inImage;
        outImage = this.outImage;
        window = this.window;
        read();
    }
    
    public void read() throws IOException{
        try{
            f = new File("/home/sabelo/csc2/data/index.jpeg");
            image = ImageIO.read(f);
            System.out.println("Read image successful");
        }catch(IOException e){
            System.out.println("Error reading image: " + e);
        }
    }
    
    public void imageMean() throws IOException{
        System.out.println("WhAT");
        int max = window*window;
        int odd = (window - (int)(window/2)) -1;
        int h = image.getHeight();
        int w = image.getWidth();
        //int[] pixelArr = new int[max];
        for(int i=odd; i< h-odd; i++){
            for(int j=odd; j< w-odd; j++){
                int index =0;
                int[] pixelArr = new int[max];
                for(int i1= i-odd; i1<= i+odd; i1++){
                    for(int j1= j-odd; j1<= j+odd; j1++){
                        pixelArr[index++] = image.getRGB(j1,i1);
                    }
                }
                for(int x=0; x<max; x++){
                    sumRed += (pixelArr[x] >> 16) & 0xff;
                    sumGreen += (pixelArr[x] >> 8) & 0xff;
                    sumBlue += (pixelArr[x]) & 0xff; 
                }
                sumRed =sumRed/ max;
                sumGreen = sumGreen/max;
                sumBlue = sumBlue/max;
                int mean = (sumRed<<16) | (sumGreen<<8) | sumBlue;
                image.setRGB(j,i, (int)(mean));
            }
        }    
        System.out.println("DONE");
        printImage();
    }
    
    public void printImage() throws IOException{
        try{
            f = new File("/home/sabelo/csc2/data/inde.jpeg");
            //image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(image, "jpg", f);
            System.out.println("write image complete");
        }catch(IOException e){
            System.out.println("Error writing image: :" + e);
        }
    } 
}