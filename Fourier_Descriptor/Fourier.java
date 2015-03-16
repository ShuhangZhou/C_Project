
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
public class Fourier {
public static void main (String[] args) throws IOException, InterruptedException{
   
	
 int debug = 0;	
 BufferedImage image1 = null;
 String im_path = "C:/Users/foosa/Desktop/bat.gif";			// enter the image path here
 String output  = "C:/Users/foosa/Desktop/output.bmp";		// output(boundary image) image path
 String test	= "C:/Users/foosa/Desktop/test.bmp";	
 File imagefile1 = new File(im_path);                  
           
 image1 = ImageIO.read(imagefile1);

 int width  = image1.getWidth(); 					    	// Dimensions of the image
 int height = image1.getHeight();
 
 BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);

 WritableRaster raster = im.getRaster();
 	       
 // initialize all the values used
 int rgba1	    = 0; 
 int rgba2	    = 0;
 int rgba3 	    = 0;
 int r1	   	    = 0;
 int r2   	    = 0;
 int r3   	    = 0;
 int matrix[][] = new int[height*width][2];
 int count 		= 0;
 int sum_x	    = 0;
 int sum_y	    = 0;
 int set 		= 0;
 
 for(int i =0;i<=(height-1);i++){
	 for (int j=0;j<=(width-2);j++){


		 rgba1 = image1.getRGB(j,i);
		 rgba2 = image1.getRGB(j+1,i);
		 if(i==0){
		 }else{
			 rgba3 = image1.getRGB(j,i-1);
		 }
		 Color col1 = new Color(rgba1);
		 Color col2 = new Color(rgba2);
		 Color col3 = new Color(rgba3);
		 r1 = col1.getRed();
		 r2 = col2.getRed();
		 r3 = col3.getRed();
		 if(r1 != r2){														// check for color difference with horizontal neighbor.
			 raster.setSample(j,i,0,0);										// 0,0 for black and 0,1 for white
			 raster.setSample(j+1, i, 0, 1);
			 sum_x += j;
			 sum_y += i;                                               
			
			 count++;
			 set = 1;
			 
		 }else{
			 raster.setSample(j+1, i, 0, 1);
			 raster.setSample(j+1,i,0,1);
		 }

		 if(r1 != r3 ){														// check for color difference with vertical neighbor.

			 raster.setSample(j,i,0,0);
			
			if(set == 0){
				sum_x += j;
				sum_y += i; 
				count++;
			}
		 }

	 }
	// check this condition   set = 0;
	 
  }
 // for loop ends

 int centroid_x = sum_x/count;
 int centroid_y = sum_y/count;

 raster.setSample(centroid_x,centroid_y,0,0);
 File boundry = new File(output);
 ImageIO.write(im, "bmp",boundry);

 
 
 // point sequence computation
 
 System.out.println("count : " + count);    
 Thread.sleep(1000);
 BufferedImage image3 = null;
 image3 = ImageIO.read(boundry);

 int rgba = 0;
 int r 	  = 0; 
 int sx	  = 0;						// sx = startpoint x coordinate
 int sy   = 0;						// sy = startpoint y coordinate

 
 
	 for (int i = centroid_x ;i<width ; i++){									// find start point (zero degrees to the right of centroid)
		 rgba  = image3.getRGB(i,centroid_y);
		 Color col = new Color(rgba);
		 r = col.getRed();

		 if(r == 0){
			 sx = i;
			 sy = centroid_y;
			 
		 }

	 }
	 //System.out.println(centroid_x +" "+ centroid_y+ " "+sx + " "+sy);
	 int start[] = new int[2];
	 start[0] = sx;
	 start[1] = sy;
	// System.out.println(start[0] + " " + start[1] );
	// System.out.println(centroid_x + " " + centroid_y);
	 
	 int[] current   = new int[2];
	 int[] prevpoint = new int[2];
	 current   = start.clone();
	 prevpoint = start.clone();
	 	
   	 int sequence[][] = new int[2][5000];	
   	 
   	 int pcount = 0;
   	 int current1[]   = new int[2];
   	 int prevpoint1[] = new int[2];
   	 int prevpoint2[] = new int[2];
	 int prevpoint3[] = new int[2];
	 int prevpoint4[] = new int[2];
	 int prevpoint5[] = new int[2];
	 int prevpoint6[] = new int[2];
   	 int prevpoint7[] = new int[2];
	 int prevpoint8[] = new int[2];
	 int prevpoint9[] = new int[2];
	 int prevpoint10[] = new int[2];
   	 boolean stop = false;
   	 BufferedImage im1 = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);

   	 WritableRaster raster1 = im1.getRaster();
   	 
   	 System.out.println("start");
   	 do{
		 
		sequence[0][pcount] = current[0];						// our sequence of points 0 - n-1
		sequence[1][pcount] = current[1];
		
		current1[0] = current[0];
		current1[1] = current[1];
		
		//System.out.println("current: "+Arrays.toString(current1));
		//System.out.println("previous: "+Arrays.toString(prevpoint));
		
		if(pcount<12){
			Arrays.fill(prevpoint1,0);
			Arrays.fill(prevpoint2,0);
			Arrays.fill(prevpoint3,0);
			Arrays.fill(prevpoint4,0);
			Arrays.fill(prevpoint5,0);
			Arrays.fill(prevpoint6,0);
			Arrays.fill(prevpoint7,0);
			Arrays.fill(prevpoint8,0);
			Arrays.fill(prevpoint9,0);
			Arrays.fill(prevpoint10,0);
			
		}else{
			prevpoint1[0] = sequence[0][pcount-2];
			prevpoint1[1] = sequence[1][pcount-2];
			prevpoint2[0] = sequence[0][pcount-3];
			prevpoint2[1] = sequence[1][pcount-3];
			prevpoint3[0] = sequence[0][pcount-4];
			prevpoint3[1] = sequence[1][pcount-4];
			prevpoint4[0] = sequence[0][pcount-5];
			prevpoint4[1] = sequence[1][pcount-5];
			prevpoint5[0] = sequence[0][pcount-6];
			prevpoint5[1] = sequence[1][pcount-6];
			prevpoint6[0] = sequence[0][pcount-7];
			prevpoint6[1] = sequence[1][pcount-7];
			prevpoint7[0] = sequence[0][pcount-8];
			prevpoint7[1] = sequence[1][pcount-8];
			prevpoint8[0] = sequence[0][pcount-9];
			prevpoint8[1] = sequence[1][pcount-9];
			prevpoint9[0] = sequence[0][pcount-10];
			prevpoint9[1] = sequence[1][pcount-10];
			prevpoint10[0] = sequence[0][pcount-11];
			prevpoint10[1] = sequence[1][pcount-11];
			
			
		}
		current =  nextpoint(current1,prevpoint,prevpoint1,prevpoint2,prevpoint3,prevpoint4,prevpoint5,
							 prevpoint6,prevpoint7,prevpoint8,prevpoint9,prevpoint10,image3).clone();
		//if(current)
		
		raster1.setSample(current[0],current[1],0,1);
		
		prevpoint[0] = current1[0];
		prevpoint[1] = current1[1];
		
		System.out.println("current: "+Arrays.toString(current));
		System.out.println();
		//System.out.println("previous: "+ Arrays.toString(prevpoint));
		pcount++;
		int closex = Math.abs(current[0]-sx);
		int closey = Math.abs(current[1]-sy);
		int total = closex+closey;
		if(pcount>10 ){
			if((total<=2)){
			stop = true;
			}
		}
	 }while (!stop);
   // System.out.println("done");
    File test1 = new File(test);
    ImageIO.write(im1, "bmp",test1);
   	
    
    int seqsum_x = 0;
    int seqsum_y = 0;
    
    /*if(debug == 1){
    	System.out.println("sequence   : " + Arrays.toString(sequence));
    }
    //remove repeated points
    for(int i = 0; i<pcount-11;i++ ){
    	
    	for(int j=1 ;j<=100;j++){
    		if ((sequence[0][i] == sequence[0][i+j]) && (sequence[1][i] == sequence[1][i+j])){
    			sequence[0][i+j] = 0;
    			sequence[1][i+j] = 0;
    			//System.out.println(i + " " + j);
    		}
    	}
    	
    	 if(debug == 1){
    	    	System.out.println("sequence after zero set   : " + Arrays.toString(sequence));
    	    }
    	
    }
    int zeros = 0;
    for(int i=0;i<sequence.length;i++){
    	if(sequence[0][i] == 0 && sequence[1][i] == 0){
    		zeros++;
    	}
    }
    if(debug == 1){
    	System.out.println("zeros : " +  zeros);
    }
    int sequence_new[][] = new int[2][sequence.length-zeros];
    int j=0;
    for(int i=0;i<sequence.length;i++){
    	if(sequence[0][i] == 0 && sequence[1][i] == 0){
    		
    	}else{
    	sequence_new[0][j] = sequence[0][i];
    	sequence_new[1][j] = sequence[1][i];
    	j++;
    	
    	}
    }
    
    if(debug == 1){
    	System.out.println("sequence_new   : " + Arrays.toString(sequence_new));
    }
    */
    if(debug == 1){
    	System.out.println("sequence  : " + Arrays.toString(sequence));
    }
    int points[][] = new int[2][pcount]  ;
    for(int i=0;i<pcount;i++){
    	points[0][i] = sequence[0][i];
    	seqsum_x += points[0][i];
    	points[1][i] = sequence[1][i];
    	seqsum_y += points[1][i];
    }
    
    if(debug == 1){
    	System.out.println("points   : " + Arrays.toString(points));
    }
    //centroid distance computation
    
    int center_x = seqsum_x/pcount;
    int center_y = seqsum_y/pcount;
    
    double cen_dist[] = new double[pcount];
    
    for(int i=0;i<points.length;i++){
    	
    	double squares = Math.pow(points[0][i] - center_x, 2) + Math.pow(points[1][i] - center_y, 2);
    	cen_dist[i] = Math.sqrt(squares);
    }
    System.out.println("number of points"  + cen_dist.length);
    System.out.println("done");
    int padding = 0;
   loop:
    for(int i = 0;i<15;i++){
    	
    	if(cen_dist.length < Math.pow(2, i)){
    		padding = (int) (Math.pow(2, i)-cen_dist.length);
    		break loop;
    	}
    }
    
    System.out.println("padding : " + padding);
    System.out.println("total input :" +(cen_dist.length + padding));
    int pad[] = new int[padding];
    Arrays.fill(pad,0);
    double dist_array[] = new double[cen_dist.length + padding];
   
   
    for(int i =0;i<cen_dist.length;i++){
    	dist_array[i] = cen_dist[i];
    	    }
    for(int i= cen_dist.length;i<pad.length;i++){
    	
    	dist_array[i] = pad[i];
    }
    
    /*
	 for(int i = 0 ;i<=pcount-1;i++){
		 System.out.println(sequence[0][i] + " " + sequence[1][i]);
		 
		 
	 }*/
	//System.out.println(Arrays.deepToString(sequence));
 
File input = new File("‪input1.txt");
if (!input.exists())
{
    input.createNewFile();
}
FileWriter m = new FileWriter(input);
BufferedWriter out = new BufferedWriter(m);

for(int i= 0;i<dist_array.length;i++){
	
	out.write(Double.toString(Math.round(dist_array[i]*1000.0)/1000.0) + "	 " + i);
	out.newLine();
}
out.close();
	 
	 
}

public static int[] nextpoint(int[] point,int[] pp,int[] pp1,int[] pp2,int[] pp3,int[] pp4,int[] pp5,
		int[] pp6,int[] pp7,int[] pp8,int[] pp9,int[] pp10,BufferedImage image) {
	 
	
	int rgba =0;
	int r    =0;
	int nextpoint[] = new int[2];
	//int found = 0;
	loop:
		for(int i = point[0]-1 ;i<= point[0]+1;i++){
			for(int j = point[1]-1 ;j<= point[1]+1;j++){

				
				rgba       = image.getRGB(i,j);
				Color col  = new Color(rgba);
				r 	       = col.getBlue();
				//System.out.println(r);
				if(r==0){

					if (((i == point[0]) && (j == point[1])) || ((i == pp[0]) && (j == pp[1])) || 
							((i == pp1[0]) && (j == pp1[1])) || ((i == pp2[0]) && (j == pp2[1])) ||
							((i == pp3[0]) && (j == pp3[1])) || ((i == pp4[0]) && (j == pp4[1])) ||
							((i == pp5[0]) && (j == pp5[1])) || ((i == pp6[0]) && (j == pp6[1])) ||
							((i == pp7[0]) && (j == pp7[1])) || ((i == pp8[0]) && (j == pp8[1])) ||
							((i == pp9[0]) && (j == pp9[1])) || ((i == pp10[0]) && (j == pp10[1]))){

						// ignore the point and move on 
					}else{
						nextpoint[0] = i;
						nextpoint[1] = j;
						//found = 1;
						break loop;
					}
				}
			}
		} 

// for loop ends
	/*if(found == 0){
		System.out.println("point not found");
		}*/
	System.out.println("nextpoint");
	if(nextpoint[0] == 0 && nextpoint[1] == 0){
		return pp;
	}else{
	return nextpoint;
	}
}


}
 
 
 
 