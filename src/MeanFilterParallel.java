/* Mean Parallel filter program for image processing 
 * by smoothing RGBcolor of each pixel setting it to 
 * the average of the surrounding pixels
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


public class MeanFilterParallel extends RecursiveAction{
  
   BufferedImage image1 = null;
   int odd1 =0;  
   int window =0;
   int max = 0;
   int odd =0;
   int[] myArr = null;
   int H = 0;
   int W = 0;
   final int SEQUANTIAL_CUTOFF = 321632;
   int start =0;
   int end =0;
   int odi = 0;
	
   /* constructor method which passed arguments if instance of the class created and 
     * calls method to read an image to set image parameters
     * @param odds is the index of Width in column of an image  INT
     * @param  Width is vertical size of image  INT
     * @param of is the index of Height in a row of an image INT
     * @param Height is horizontal size of image INT
     * @param window1 is window width that defines neighbouring pixels INT
     * @param image
     */
   public MeanFilterParallel(int odds ,int Width, int of ,int Height, int window1, BufferedImage image){
      image1 = image;
      W = Width;
      H = Height;
      odd = odds;
      odi = of;
      window = window1;
      odd1 = (window - 1) /2 ;
      max = window * window;  
      start = H - odd1; 
      end = W - odd1;  
   }
  
  
   /* Override Method of Fork/Join 
    * Which is invoked by pool of threads 
    */
   protected void compute(){
      
      if((H-odi)*(W-odd) <SEQUANTIAL_CUTOFF){
         doMean();
      }else{
         int gh = (int) Math.floor((double)H/2) ;   // half of height
         int gg = (int) Math.floor((double)W/2) ;   // half of width
         int g = gh + odd1;  // adding border value to conterpart the height as height/2
         int h = gg + odd1;
         
         MeanFilterParallel left1 = new MeanFilterParallel( odd , h, odi , g, window, image1); // top left
         MeanFilterParallel right2 = new MeanFilterParallel( gg, image1.getWidth(), gh,  image1.getHeight() ,window, image1);    // bottom right       
         MeanFilterParallel left2 = new MeanFilterParallel( odd ,h , gh ,  image1.getHeight(), window, image1); //bottom left
         MeanFilterParallel right1 = new MeanFilterParallel( gg ,  image1.getWidth() , odi, g, window, image1);  // top right 
         left1.fork();
         right1.compute();
         left1.join(); // wait until top left thread is done
     
         left2.fork();
         right2.compute(); 
         left2.join();    // wait until bottom left thread is done 
      }
   }    
   
   
   /*void Helper method which use sliding square window algorithm  
     * to calculate mean and set each pixel accordingly
     */
   public void doMean(){  
      for(int i=odi; i<start; i++){
         for(int j=odd; j<end; j++){
            int sumRed =0;
            int sumGreen =0;
            int sumBlue =0;
            for(int i1 = i-odd1 ; i1<= i+odd1 ; i1++){
               for(int j1 = j-odd1; j1<= j+odd1 ; j1++){
                  int pixel = image1.getRGB(j1,i1);
                  sumRed += (pixel >> 16) & 0xff;
                  sumGreen += (pixel >> 8) & 0xff;
                  sumBlue += (pixel) & 0xff;
               }
            }
            sumRed = sumRed / (max);
            sumGreen = sumGreen /(max);
            sumBlue = sumBlue / (max);
            int ave = ((sumRed << 16) | (sumGreen << 8) | (sumBlue));
            image1.setRGB(j,i, (int)(ave));
         }
      }  
   }    
   
   /** Main method for the application of the Parallel filter
     * @param args store string array, if not specified it is empty
     */  
   public static void main(String args[]) throws FileNotFoundException , IOException{
      BufferedImage image = null;
      int Wid= 0;
      int Hei = 0;
      int wii = Integer.parseInt(args[2]);
      int oddd = (wii-1)/2;
      
      //
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
         	image = new BufferedImage(Wid,Hei, BufferedImage.TYPE_INT_RGB);
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
      
      MeanFilterParallel paraFilter = new MeanFilterParallel(oddd, Wid, oddd,  Hei , wii, image); 
      final long start = System.currentTimeMillis() ; 
      ForkJoinPool pool = new ForkJoinPool();
      pool.invoke(paraFilter);
      final long elapsed = System.currentTimeMillis() - start; 
      System.out.println("Time elapsed = "+elapsed+" Milliseconds for "+inImage + " of width*Height: "+Wid+"*"+Hei +" window filter size:"+ wii+"*"+wii);
      try{
         File f1 = new File("C:\\\\Users\\\\wwwsa\\\\OneDrive - University of Cape Town\\\\Desktop\\\\csc2\\\\data"+ "\\\\" +outImage);
         ImageIO.write(image, "jpg", f1);
         System.out.println("write image complete");
      }catch(IOException e){
         System.out.println("Error writing image :" + e);
         System.exit(0);
      }
   }
}
