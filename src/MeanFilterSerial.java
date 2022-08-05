import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;

public class MeanFilterSerial{
    private String inImage;
    private String outImage;
    private int window;
    String loc = "/home/sabelo/csc2/data/";
    BufferedImage image =null;
    File f = null;
    
       
    public static void main(String args[]) throws FileNotFoundException{
        MeanFilterSerial mean;
        try{
            if(args.length > 0)){
                mean = new MeanFilterSerial(args[0], args[1], args[2]); 
                mean.imageMean();
            }
        }catch(Excepton e){
            System.out.println("Error somewhere");
        }
    }
    
    public MeanFilterSerial(String inImage, String outImage, int window){
        inImage = this.inpImage;
        outImage = this.outImage;
        window = this.window
        read();
    }
    
    public void read() throws IOException{
        try{
            f = new File(loc + inImage + "\"");
            image = ImageIO.read(f);
            System.out.println("Read image successful");
        }catch(IOException e){
            System.out.println("Error reading image: " + e);
        }
    }
    
    public void imageMean(){
        
        
    }
    
    public void printImage(){
        try{
            f = new File(loc + outImage + "\"");
            image = new BufferedImage(image.Width, image.Heigth, BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(image, "jpg", f);
            System.out.println("write image complete");
        }catch(IOException e){
            System.out.println("Error writing image: :" + e);
        }
    } 
}