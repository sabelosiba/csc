import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;

public class MedianFilterSerial{
    private int  window = 0;
    private String inImage = "";
    private String outImage ="";
    BufferedImage image = null;
    File f = null;
    
    int max=0;
    int odd=0;
    int[] redList = null;
    int[] greenList = null;
    int[] blueList = null;
    int H = 0;
    int W = 0;
    int index = 0;
    int sum=0;
    
    public MedianFilterSerial(String inImage, String outImage, String wind) throws IOException{
        inImage = this.inImage;
        outImage = this.outImage;
        window = Integer.parseInt(wind);
        read();
        max = window*window;
        odd = (window - 1) / 2;
        redList = new int[max];
        greenList = new int[max];
        blueList = new int[max];
        H = image.getHeight();
        W = image.getWidth();
        System.out.println(window +"\n"+ max +"\n" + H +"\n" + W +"\n" + odd);
    }
    
    public void read() throws IOException{
        try{
            f = new File("/home/sabelo/csc2/data/index.jpeg");
            image = ImageIO.read(f);
            System.out.println("Image read successful");
        }catch(Exception e){
            System.out.println("Error reading Image: "+e);
        }
    }
    
    public void imageMedian(){
        int mid = max/2;
        int redMed =0;
        int greenMed =0;
        int blueMed =0;
        for(int i=odd; i<H-odd; i++){
            for(int j=odd; j<W-odd; j++){
                index =0;
                for(int i1=i-odd; i1<=i+odd; i1++){
                    for(int j1=j-odd; j1<=j+odd; j1++){
                        int pixel = image.getRGB(j1,i1);
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
                image.setRGB(j,i, (int) sum );
            }
        }
        writeImage();
    }
    
    public void writeImage(){
        try{
            f = new File("/home/sabelo/csc2/data/"+outImage+ "\"");
            ImageIO.write(image, "jpeg", f );
            System.out.println("image write complete");
        }catch(Exception e){
            System.out.println("Error writing to image :" + e);
        }
    }
    public static void main(String args[]) throws FileNotFoundException, IOException{
        int space = Integer.parseInt(args[2]);
        System.out.println(space);
        try{
            if((space >= 3) && (args.length > 0)){
                MedianFilterSerial median = new MedianFilterSerial(args[0], args[1], args[2]);
                System.out.println(space);
                median.imageMedian();
            }else{
                System.out.println("Enter correct file names and/or odd number width atleast 3");
            }
        }catch(Exception e){
            System.out.println("Error Execution : " + e);
        }
    }
}