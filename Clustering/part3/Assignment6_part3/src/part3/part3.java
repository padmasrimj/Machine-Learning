package part3;
import java.awt.Color;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
public class part3
{
  public static void main( String [] args ) 
  {
    try 
    {
      BufferedImage input_image = ImageIO.read( new File(args[0]) );
      BufferedImage output_image = Kmeans_ImageProcessing( input_image, Integer.parseInt(args[1]) );
      ImageIO.write( output_image, "png", new File("ClusteredImages/"+args[2]) );
    }
     catch ( IOException e ) 
     {
       System.out.println( e.getMessage() );
     }

  }
private static boolean convergence( int[] mean, int[] mean1 ) {
    for ( int i = 0; i < mean1.length; i++ )
      if ( mean[i] != mean1[i] )
        return false;
    return true;

  } 
private static BufferedImage Kmeans_ImageProcessing( BufferedImage input_image, int k ) 
{
  int width = input_image.getWidth();
  int height = input_image.getHeight();
  BufferedImage kmeansImage = new BufferedImage( width, height, input_image.getType() );
  Graphics2D g = kmeansImage.createGraphics();
  g.drawImage( input_image, 0, 0, width, height, null );
  int[] redGreenBlue = new int[(width*height)];
  int number1 = 0;
  for ( int i = 0; i < width; i++ ) {
    for( int j = 0; j < height; j++ ) {
         redGreenBlue[number1++] = kmeansImage.getRGB(i,j);
    }
  }
 KMeans_Algorithm( redGreenBlue,k );
 number1 = 0;
  for( int i = 0; i < width; i++ ) {
    for( int j = 0; j < height; j++ ) {
        kmeansImage.setRGB( i, j, redGreenBlue[number1++] );
    }
  }
  return kmeansImage;
  }
private static void KMeans_Algorithm( int[] redGreenBlue, int k ) 
  {
	  if ( redGreenBlue.length < k ) {
      System.out.println( "the length of the pixel<k!!!" );
      return;
    }
	double thres_distance = 0.0;   
    double distance = 0;                   
    int center = 0;  
    int[] nRed = new int[k];   
    int[] nGreen = new int[k]; 
    int[] nBlue = new int[k]; 
	int[] mean = new int[k];   
    int[] mean1 = new int[k];   
    int[] total_summation = new int[k];
    int[] image_clustering = new int[redGreenBlue.length];        
    for ( int i = 0; i < k; i++ ) 
	{
      Random random = new Random();
      mean1[i] = redGreenBlue[random.nextInt( redGreenBlue.length )];
    }
    do {
      for ( int i = 0; i < mean1.length; i++ ) 
	  {
        mean[i] = mean1[i];
        total_summation[i] = nRed[i] = nGreen[i] = nBlue[i] = 0;
      }
      for ( int i = 0; i < redGreenBlue.length; i++ ) 
	  {
        thres_distance = Double.MAX_VALUE;
        for ( int j = 0; j < mean1.length; j++ ) 
		{
			Color d = new Color(redGreenBlue[i]);
			Color e = new Color(mean1[j]);
			int dRed = d.getRed()-e.getRed();
			int dGreen = d.getGreen()-e.getGreen();
			int dBlue = d.getBlue()-e.getBlue();
			distance = Math.sqrt( dRed*dRed + dGreen*dGreen + dBlue*dBlue );
          if ( distance < thres_distance ) 
		  {
            thres_distance = distance;
            center = j;
          }
        }
        image_clustering[i] = center;
        total_summation[center]++;
		Color c = new Color(redGreenBlue[i]);
        nRed[center] += c.getRed();
        nGreen[center] += c.getGreen();
        nBlue[center] += c.getBlue();
      }	
      for (int i = 0; i < mean1.length; i++ ) 
	  {
        int average_red = Average_calculation(nRed[i],total_summation[i]);
        int average_green = Average_calculation(nGreen[i],total_summation[i]);
        int average_blue= Average_calculation(nBlue[i],total_summation[i]);
        mean1[i] =((average_red & 0x000000FF) << 16) |((average_green & 0x000000FF) << 8) |((average_blue& 0x000000FF));
      }

    } while( !convergence(mean, mean1) );
    for ( int i = 0; i < redGreenBlue.length; i++ ) {
      redGreenBlue[i] = mean1[image_clustering[i]];
    }
  }
private static int Average_calculation(double s,double k)
	{
		int avg=(int)(s/k);
		return avg;
	
	}
}