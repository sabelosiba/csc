/* Median Parallel filter program for image processing 
 * by smoothing RGBcolor of each pixel setting it to 
 * the median of surrounding pixels
 * Java Fork/Join framework used to achived parallel threading 
 */
import java.io.File; 
import java.io.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.util.*;
import java.lang.InterruptedException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class MedianFilterParallel extends RecursiveAction{

    private int W;
    private int H;
    private int Window;
    private BufferedImage image1;
    private int odd;
    private int max;
    protected static int SEQUANTIAL_CUTOFF =321632;
    
    int[] redList = null; 
    int[] greenList = null;
    int[] blueList = null;
    int mid = 0;
    int sum=0;
    int odd1 =0;
    int odi =0;
    int start =0;
    int end =0;
    
    /* constructor method which passed arguments if instance of the class created and 
     * calls method to read an image to set image parameters
     * @param od is the index of Width in column of an image  INT
     * @param  width is vertical size of image  INT
     * @param ods is the index of Height in a row of an image INT
     * @param height is horizontal size of image INT
     * @param window is window width that defines neighbouring pixels INT
     * @param image
     */
    public MedianFilterParallel(int o,int width,int ods ,int height, int window, BufferedImage image){
        image1 = image;
        W = width;
        H = height;
        Window = window;
        max = window*window;
        odd =o;
        odi= ods;
        redList = new int[max];
        greenList = new int[max];
        blueList = new int[max];
        mid = max /2;
        odd1 = (window-1)/2;
        start = H - odd1; 
        end = W - odd1;
        
    }
    
    /* Override Method of Fork/Join 
    * Which is invoked by pool of threads 
    */
    protected void compute(){
      if((H-odi)*(W-odd) < SEQUANTIAL_CUTOFF){
         doMedian();
      }else{
         int gh = (int) Math.floor((double)H/2) ;
         int gg = (int) Math.floor((double)W/2);
         int g = gh + odd1;
         int h = gg + odd1;
         MedianFilterParallel left1 = new MedianFilterParallel( odd , h , odi , g, Window, image1); // top left
         MedianFilterParallel right2 = new MedianFilterParallel( gg , image1.getWidth() , gh,  image1.getHeight() ,Window, image1);    // bottom right
         MedianFilterParallel left2 = new MedianFilterParallel( odd ,h , gh ,  image1.getHeight(), Window, image1); //bottom left
         MedianFilterParallel right1 = new MedianFilterParallel( gg, image1.getWidth() , odi, g, Window, image1);  // top rigth        
         left1.fork();
         right1.compute();
         left1.join();  
         left2.fork();
         right2.compute();
         left2.join();     
      }
   }
   
   /*void Helper method which use sliding square window algorithm  
     * to calculate Median and set each pixel accordingly to the median
     */ 
   protected void doMedian(){
        for(int i=odi; i<start; i++){
         for(int j=odd; j<end; j++){
            int r=0;
            for(int i1 = i-odd1 ; i1<= i+odd1 ; i1++){
               for(int j1 = j-odd1; j1<= j+odd1 ; j1++){
                  int pixel = image1.getRGB(j1,i1);
                  redList[r] = (pixel >> 16) & 0xff;
                  greenList[r] = (pixel >> 8 ) & 0xff;
                  blueList[r] = (pixel ) & 0xff;
                  r++;
               }
            }
            Arrays.sort(redList);
            Arrays.sort(greenList);
            Arrays.sort(blueList);
            if(max % 2 == 0){
               int red =(redList[(mid)] + redList[(mid)-1] ) /2;
               int green =(greenList[mid] + greenList[(mid)-1] ) /2;
               int blue =(blueList[mid] + blueList[(mid)-1] ) /2;
               sum = (red << 16) | (green << 8) | blue ;
            }else{
               int red =redList[mid];
               int green =greenList[mid];
               int blue =blueList[mid];
               sum = (red << 16) | (green << 8) | blue ;
            }
            image1.setRGB(j,i, (int)sum);
         }
      } 
    }
    
    /** Main method for the application of the Median Parallel filter
     * @param args store string array, if not specified it is empty
     */ 
    public static void main(String args[]) throws FileNotFoundException , IOException{
      BufferedImage image = null;
      int Wid = 0;
      int Hei= 0;
      int wii = Integer.parseInt(args[2]);
      int oddd = (wii-1)/2;
      String inImage = "";
      String outImage = "";
      
      try{
         if(wii%2 != 0 && wii >= 3){
      		inImage = args[0];
      		outImage = args[1];
        	File f = new File("C:\\\\Users\\\\wwwsa\\\\OneDrive - University of Cape Town\\\\Desktop\\\\csc2\\\\data"+ "\\\\"+inImage);
         	BufferedImage info = ImageIO.read(f);
         	Wid = info.getWidth();
         	Hei= info.getHeight();
         	image = new BufferedImage(Wid, Hei, BufferedImage.TYPE_INT_RGB);
         	image = ImageIO.read(f);
         	System.out.println("Reading file successful"); 
         }else{
      		System.out.println("Enter correct file names with extension and/or window width that is an odd number and atleast 3");
                System.exit(0);
          }       
      }catch(IOException e){
         System.out.println("Error reading file : " + e);
         System.exit(0);
      }
      
      
      MedianFilterParallel Med = new MedianFilterParallel(oddd, Wid, oddd,  Hei, wii, image);   
      ForkJoinPool pool = new ForkJoinPool();
      final long start = System.currentTimeMillis() ; 
      pool.invoke(Med);
      final long elapsed = System.currentTimeMillis() - start; 
      System.out.println("Time elapsed = "+elapsed+" Milliseconds for "+inImage + " of width*Height: "+Wid+"*"+Hei +" window filter size:"+ wii+"*"+wii);
      
      
      
      try{
         File f1 = new File("C:\\\\Users\\\\wwwsa\\\\OneDrive - University of Cape Town\\\\Desktop\\\\csc2\\\\data"+ "\\\\"+outImage);
         ImageIO.write(image, "jpg", f1);
         System.out.println("write image complete");
      }catch(IOException e){
         System.out.println("Error writing image :" + e);
         System.exit(0);
      }
      
   }
}
