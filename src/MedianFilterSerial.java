import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;

public class MedianFilterSerial{
    private int window = 0;
    private String inImage = "";
    private String outImage ="";
    BufferedImage image = null;
    File f = null;
    
    int max=0;
    int odd=0;
    int[] pixelArr = null;
    int H = 0;
    int W = 0;
    int index = 0;
    int sum=0;
    
    public MedianFilterSerial(String inImage, String outImage, int window){
        inImage = this.inImage;
        outImage = this.outImage;
        window = this.window;
        read();
        max = window*window;
        odd = (window - 1) / 2;
        pixelArr = new int[max];
        H = image.getHeight();
        W = image.getWidth();
    }
    
    public void read(){
        try{
            f = new File("/home/csc2/data/"+inImage+"\"");
            image = ImageIO.read(f);
            System.out.println("Image read successful");
        }catch(Exception e){
            System.out.println("Error reading Image: "+e);
        }
    }
    
    public void imageMedian(){
        for(int i=odd; i<H-odd; i++){
            for(int j=odd; j<W-odd; j++){
                for(int i1=i-odd; i1<=i+odd; i1++){
                    for(int j1=j-odd; j1<=j+odd; j1++){
                        pixelArr[index++] = image.getRGB(j1,i1);
                    }
                }
                Arrays.sort(pixelArr);
                if(max%2 == 0){
                    sum = (pixelArr[max/2] + pixelArr[(max/2)-1]) / 2;
                }else{
                    sum = pixelArr[max/2];
                }
                image.setRGB(j,i, (int) sum );
            }
        }
        writeImage();
    }
    
    public void writeImage(){
        try{
            f = new File("/home/csc2/data/"+outImage+ "\"");
            ImageIO.write(image, "jpeg", f );
            System.out.println("image write complete");
        }catch(Exception e){
            System.out.println("Error writing to image :" + e);
        }
    }
    public static void main(String args[]) throws FileNotFoundException, IOException{
        int space = Integer.parseInt(args[2]);
        try{
            if((space >= 3) && (args.length > 0)){
                MedianFilterSerial median = new MedianFilterSerial(args[0], args[1], space);
                median.imageMedian();
            }else{
                System.out.println("Enter correct file names and/or odd number width atleast 3");
            }
        }catch(Exception e){
            System.out.println("Error Execution : " + e);
        }
    }
}