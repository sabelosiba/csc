import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;

public class MedianFilterSerial{
    private int  window = 0;   //size of window width
    private String inImage = ""; // input image name
    private String outImage ="";  // output image name
    BufferedImage image = null;   
    BufferedImage image1 = null;
    File f = null;
    
    int max=0;   // RGB array length
    int odd=0;    // index of pixel to set
    int[] redList = null;   // array to store red color values
    int[] greenList = null;  // array to store green color values
    int[] blueList = null;  // array to store blue color value
    int H = 0;   // image Height
    int W = 0;   // image width
    int index = 0;  // array iterator
    int sum=0;  // pixel median value 
    
    public MedianFilterSerial(String inIma, String outIma, String wind) throws IOException{
        inImage = inIma;
        outImage = outIma;
        window = Integer.parseInt(wind);
        read();
        max = window*window;
        odd = (window - 1) / 2;
        redList = new int[max];
        greenList = new int[max];
        blueList = new int[max];
        H = image1.getHeight();
        W = image1.getWidth();
    }
    
    public void read() throws IOException{
        try{
            f = new File("/home/sabelo/csc2/data/"+inImage+"\"");
            image = ImageIO.read(f);
            image1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            System.out.println("Image read successful");
        }catch(Exception e){
            System.out.println("Error reading Image: "+e);
        }
    }
    
    public void imageMedian(){
        int mid = max/2;  // index of median value
        int redMed =0;   //red color median value
        int greenMed =0;   //green color median value
        int blueMed =0;    //blue color median value
        final long start = System.currentTimeMillis() ; 
        for(int i=odd; i<H-odd; i++){
            for(int j=odd; j<W-odd; j++){
                index =0;
                for(int i1=i-odd; i1<=i+odd; i1++){
                    for(int j1=j-odd; j1<=j+odd; j1++){
                        int pixel = image1.getRGB(j1,i1);
                        redList[index] = (pixel >> 16) & 0xff;
                        greenList[index] = (pixel >> 8) & 0xff;
                        blueList[index] = (pixel) & 0xff;
                        index++; 
                    }
                }
                Arrays.sort(redList);
                Arrays.sort(greenList);
                Arrays.sort(blueList);
                if(max%2 == 0){
                    redMed = (redList[mid] + redList[mid-1]) / 2;
                    greenMed = (greenList[mid] + greenList[mid-1]) / 2 ;
                    blueMed = (blueList[mid] + blueList[mid-1]) / 2;
                    sum = (redMed << 16) | (greenMed << 8) | blueMed;
                }else{
                    redMed = redList[mid];
                    greenMed = greenList[mid];
                    blueMed = blueList[mid];
                    sum = (redMed << 16) | (greenMed << 8) | blueMed;
                }
                image1.setRGB(j,i, (int) sum );
            }
        }
        System.out.println("DONE filtering...");
        final long elapsed = System.currentTimeMillis() - start; 
      	System.out.println("Time elapsed = "+elapsed+" Milliseconds for "+inImage + " of width*Height: "+W+"*"+H +" window filter size:"+ window+"*"+window);
        writeImage();
    }
    
    public void writeImage(){
        try{
            f = new File("/home/sabelo/csc2/data/"+outImage+ "\"");
            ImageIO.write(image1, "jpeg", f );
            System.out.println("image write complete");
        }catch(Exception e){
            System.out.println("Error writing to image :" + e);
        }
    }
    public static void main(String args[]) throws FileNotFoundException, IOException{
        int space = Integer.parseInt(args[2]);
        try{
            if((space >= 3) && (args.length > 0)){
                MedianFilterSerial median = new MedianFilterSerial(args[0], args[1], args[2]);
                System.out.println(space);
                median.imageMedian();
            }else{
                System.out.println("Enter correct file names and/or odd number width atleast 3");
                System.exit(0);
            }
        }catch(Exception e){
            System.out.println("Error Execution : " + e);
        }
    }
}
