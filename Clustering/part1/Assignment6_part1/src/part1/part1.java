package part1;
import java.io.*;
import java.util.*;
public class part1
{
  public static int stage=0;
  public static double[][] pointX_11=new double[50][1];
  public static double[][] pointY_11=new double[50][1];
  public static double[][] pointX_00=new double[50][1];
  public static double[][] pointY_00=new double[50][1];
  public static int[][] pointX_12=new int[50][1];
  public static double[] point1_X=new double[50];
  public static double[] pointY_1=new double[50];
  public static double[] point_X2=new double[50];
  public static double[] point_Y2=new double[50];
  public static Random random = new Random();
  public static int count=0;
  public static void SSError(cluster[] c,int l1)
  {
  double point_distance1=0,point_distance2=0;
  LinkedList<Number> pointCluster4=new LinkedList<Number>();
	for(int i=0;i<l1;i++)
	{
	 LinkedList<Number> pointCluster3=new LinkedList<Number>();
	  for(int k=0;k<100;k++){
	  	  	if(c[k].get_clusno()==i){
	  	  	 double[] Instance4=new double[2];
	  	  	if(stage==1){
	  	  		point_distance1=Eucleidian_distance(pointX_00[i][0],pointY_00[i][0],Instance4[0],Instance4[1]);
	  	  		}
	  	    if(stage!=1){
	  	    	point_distance1=Eucleidian_distance(pointX_11[i][0],pointY_11[i][0],Instance4[0],Instance4[1]);
	  	    	}
	  	     point_distance2=Math.pow(point_distance1,2);
	  	     pointCluster3.add(point_distance2);
	  	    }
	  	  }
	  	double total_summation=0;
	  	for(int j=0;j<pointCluster3.size();j++){
	  		total_summation=total_summation+(double)pointCluster3.get(j);
	  		}
	  	pointCluster4.add(total_summation);
     }
    double initial_total=0;
   for(int j=0;j<pointCluster4.size();j++)
   {
	   initial_total=initial_total+(double)pointCluster4.get(j);
	   } 
   System.out.println("the Sum Of SQUARES ERROR is:"+initial_total);
  }
  public static  double Centroid(double points_minimummargin, double points_maxmargin) {
   double varied_range = points_maxmargin - points_minimummargin;
   double scaled_data = random.nextDouble() * varied_range;
   double new_shifted_data = scaled_data + points_minimummargin;
   return new_shifted_data; 
  }
  public static  void update_centroid_kmeans(cluster[] c,int l1){
   for(int i=0;i<l1;i++)
   	{
   	 double x_total_centroid=0,y_total_centroid=0;
    LinkedList<Number> pointCluster1=new LinkedList<Number>();
    LinkedList<Number> pointCluster2=new LinkedList<Number>();
   	   for(int j=0;j<100;j++)
   	   	 {
   	   	   if(c[j].get_clusno()==i){
   	   	     pointCluster1.add(c[j].instance_coordinates[0]);
   	   	     pointCluster2.add(c[j].instance_coordinates[1]);
   	   	   }
   	   	 }
   	   pointX_12[i][0]=pointCluster1.size();
   	  for(int k=0;k<pointCluster1.size();k++ )
      {
   		  x_total_centroid=x_total_centroid+(double)pointCluster1.get(k);
   		  y_total_centroid=y_total_centroid+(double)pointCluster2.get(k); 
   		  }
      if(pointCluster1.size()!=0)
      {
    	  point1_X[i]=x_total_centroid/(double)pointCluster1.size();
          pointY_1[i]=y_total_centroid/(double)pointCluster2.size();
          }
      else if(pointCluster1.size()==0)
      {
      	 point1_X[i]=0;
      	 pointY_1[i]=0;
      }
      pointX_11[i][0]=point1_X[i];
      pointX_11[i][0]=pointY_1[i];
   	}
}
  public static  double Eucleidian_distance(double x1,double y1,double x2,double y2)
  {
     double XVaraiable=0,Yvariable=0,X_sum=0,Y_sum=0,distance_points=0;
     XVaraiable=(x2-x1);
     Yvariable=(y2-y1);
     X_sum=Math.pow(XVaraiable,2);
     Y_sum=Math.pow(Yvariable,2);
     distance_points=(Math.sqrt(X_sum+Y_sum));
     return distance_points;
  }
  public static  int minimum_distance(double[] pointx,double[] pointy,double x,double y,int l2)
  {
  	 double[] distance_points=new double[pointx.length];
  	 double p=0;
  	 for(int j=0;j<pointx.length;j++)
     {
     	distance_points[j]= Eucleidian_distance(pointx[j],pointy[j],x,y);
     }
       p=distance_points[0];
     for(int m=0;m<l2;m++)
     	 {
     	   if(distance_points[m]<p){p=distance_points[m]; }
     	 }
      for(int k=0;k<distance_points.length;k++)
      	  {
             if(distance_points[k]==p){
            	 return k; 
            	 }      	  
      	  }
      	  return 10;
  }
 public static void main(String[] args) throws FileNotFoundException   
	{
	 int[] ss1=new int[100];
     int u=0,b=0;
     double[] ss4=new double[200];
     double[] ss11=new double[100];
     double[] ss22=new double[100];
	 Scanner input_scanner2 = null;
	 File f= new File(args[2]);
     FileOutputStream input_file1= new FileOutputStream(f);
     PrintStream print_input= new PrintStream(input_file1);
     System.setOut(print_input); 	
     cluster[] c=new cluster[100];
     for(int p=0;p<100;p++)
     	 {
     	   c[p]=new cluster();
      	 }   
    try {
        input_scanner2 = new Scanner(new File(args[1]));
    } 
    catch (FileNotFoundException e) {
        e.printStackTrace();  
    }
   while (input_scanner2.hasNextLine()) {
            Scanner input_2 = new Scanner(input_scanner2.nextLine());
        while (input_2.hasNext()) {
        	if(input_2.hasNextInt())
        	{
        		ss1[u] = input_2.nextInt();
        	    u++;
        	}
        	if(input_2.hasNextDouble())
            { 
                ss4[b] = input_2.nextDouble();
        		b++;
             }
        }
       }
        int r=0;
        for(int q=0;q<100;q++)
        {
             	ss11[q]=ss4[r++];
             	  ss22[q]=ss4[r++];
         }
    double[] instance=new double[4];
    for(int x=0;x<100;x++)
    {
    c[x].set_instance(ss1[x],ss11[x],ss22[x]);
    instance=c[x].get_instance();
    }
    int l1=Integer.parseInt(args[0]);
     stage=1;
    if(stage==1)
    {
     double[] pointx=new double[40];
     double[] pointy=new double[40];
     for(int i=0;i<l1;i++){
      pointx[i]=Centroid(-0.100,1.100);
      pointy[i]=Centroid(-0.100,1.100);
      pointX_00[i][0]=pointx[i];
      pointY_00[i][0]=pointy[i];
    }
   double[] instance1=new double[2];
   for(int k=0;k<100;k++)
    	{ 
    		instance1=c[k].get_instance();
    	    int md= minimum_distance(pointx,pointy,instance1[0],instance1[1],l1);
    	    c[k].set_clusno(md);
    	}
    for(int h=0;h<pointx.length;h++)
		{ point1_X[h]=pointx[h];
		  pointY_1[h]=pointy[h];
		}
    }
 l:   for(int h1=0;h1<25;h1++){
	  double[] Instance4=new double[2];
	 
	int ff=0;
	for(int h=0;h<point1_X.length;h++)
		{ point_X2[h]=point1_X[h];
		  point_Y2[h]=pointY_1[h];
		}
	update_centroid_kmeans(c,l1);
	for(int h=0;h<point1_X.length;h++)
		{ if(point_X2[h]==point1_X[h] && point_Y2[h]==pointY_1[h])
			{
			ff++; 
			}
		}
	if(ff==point1_X.length){
		break l;
		}
	
	for(int k=0;k<100;k++)
    	{ 
    		Instance4=c[k].get_instance();
    	    int md= minimum_distance(point1_X,pointY_1,Instance4[0],Instance4[1],l1);
    	    c[k].set_clusno(md); 
    	}
    	
    System.out.println("iteration_number: "+(h1+1));
    for(int p=0;p<l1;p++){
    	int boolean_flag=1;
  	System.out.print("      "+(p+1)+"<--------------------->");
  	for(int x=0;x<100;x++){
  	 if(c[x].get_clusno()==p){boolean_flag=0;
  	 	 System.out.print(c[x].instance_number+",");}}
  if(boolean_flag==1){System.out.print("<This Cluster is Empty>");}
  System.out.println();}
	}

  SSError(c,l1);
  	}
 }
 class cluster{
 
	public int cluster_number,instance_number;
	public double[] instance_coordinates=new double[2];
	public void set_clusno(int n)
	{
		cluster_number=n;
		}
	public int get_clusno(){
		return cluster_number;
		}
	public void set_instance(int num,double x1,double x2)
	{
	  instance_number=num;	
	  instance_coordinates[0]=x1;
	  instance_coordinates[1]=x2;
    }
	public double[] get_instance()
    {
      return (instance_coordinates);
    }
    public int get_instance_number()
    {
       return instance_number;
    }
}