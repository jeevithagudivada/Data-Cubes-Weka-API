
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;


public class DM03JeevithaG
{
	
    private static Scanner sc;

	public static void main(String[] args) throws IOException, FileNotFoundException, Exception 
    {
        String filename = "C:\\Users\\jeevitha\\Desktop\\5th sem\\6th sem\\data mining\\DM3\\bank-data.csv";
        DataSource source;
        double cutPoints[];
        
        CSVSaver s=new CSVSaver();
        ArffLoader l = new ArffLoader();
   	    BufferedWriter w = new BufferedWriter(new FileWriter("output.arff"));
    try {
        // Create new data source
        source = new DataSource(filename);
           // Read instances from the CSV file
        Instances instances = source.getDataSet();
        // Delete the ID attribute
        instances.deleteAttributeAt(0);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
		instances.deleteAttributeAt(4);
        
       
        
        //Using Unsupervised Discretize to partition according to age
        Discretize filter=new Discretize();
        filter.setAttributeIndices("1");
        filter.setBins(3);
        filter.setInputFormat(instances);
        
        Instances output = Filter.useFilter(instances, filter);
        //Get the cut-points
        cutPoints = filter.getCutPoints(0);
        //Rename the attributes
        double[] age=instances.attributeToDoubleArray(0);
        for(int i=0;i<age.length;i++)
        {
            if(i<= cutPoints[0])
                output.renameAttributeValue(0, 0, "YOUNG");
            else if(i>cutPoints[0] && i<=cutPoints[1])
                output.renameAttributeValue(0, 1, "MIDDLE");
            else
                output.renameAttributeValue(0, 2, "OLD");
        }
        w.write(output.toString());
		w.newLine();
		w.close();
		
		l.setSource (new File("output.arff"));
		Instances data= l.getDataSet();
		
		///Save the modified data into a file.
		s.setInstances(data);
		s.setFile(new File("bank.csv"));
		s.writeBatch();
		
        //Computing data cuboids
        cube c= new cube();
        
        sc = new Scanner(System.in);
        System.out.print("Enter dimension of cuboid: ");
        int n=sc.nextInt();
        if(n==0)
        {
            c.read();
            c.cuboid0();
        }
        else if(n==1)
        {
            c.read();
            c.cuboid1();
        }
        else if(n==2)
        {
            c.read();
            c.cuboid2();
        }
        else if(n==3)
        {
            c.read();
            c.cuboid3();
        }
        else
            System.out.println("Invalid dimension");
        } 
    catch (Exception e) 
        { 
            e.printStackTrace();
        }
    }
}

 class cube
{
   BufferedReader br=null;
   String filename1="C:\\Users\\jeevitha\\Desktop\\5th sem\\6th sem\\data mining\\DM3\\bank.csv";
   
   ArrayList<String> young=new ArrayList<String>();;
   ArrayList<String> middle=new ArrayList<String>();;
   ArrayList<String> old=new ArrayList<String>();;
   ArrayList<String> inner=new ArrayList<String>();;
   ArrayList<String> town=new ArrayList<String>();;
   ArrayList<String> rural=new ArrayList<String>();;
   ArrayList<String> suburban=new ArrayList<String>();;
   ArrayList<String> male=new ArrayList<String>();;
   ArrayList<String> female=new ArrayList<String>();;
   
    public cube()
    {
        try {
            br=new BufferedReader(new FileReader(filename1));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Method.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //part the data according to age,gender and region
    public void read()
    {
        try {
            String in=br.readLine();
            in=br.readLine();
            while(in!=null)
            {
                partage(in);
                partRegion(in);
                partGender(in);
                in=br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Method.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Part according to age
    public void partage(String in)
     {
         String t[]=in.split(",");
         if((t[0]).equals("YOUNG"))
                 young.add(in);
         else if((t[0]).equals("MIDDLE"))
             middle.add(in);
         else if((t[0]).equals("OLD"))
             old.add(in);
     }   
    
    //Part according to Region
    public void partRegion(String in)
    {
        String t[]=in.split(",");
         if((t[2]).equals("INNER_CITY"))
                 inner.add(in);
         else if((t[2]).equals("TOWN"))
             town.add(in);
         else if((t[2]).equals("RURAL"))
             rural.add(in);
         else if(t[2].equals("SUBURBAN"))
             suburban.add(in);
    }
    
    //Partitions according to Gender
    public void partGender(String in)
    {
        String t[]=in.split(",");
         if((t[1]).equals("MALE"))
                 male.add(in);
         else if((t[1]).equals("FEMALE"))
             female.add(in);
    }
    
    //Computes the 0-D cuboid
    public void cuboid0()
    {
        System.out.println("0D");
        System.out.println("Count=");
        int count=young.size()+middle.size()+old.size();
        System.out.println(count);
        float sum=(mean(young)*young.size())+(mean(middle)*middle.size())+(mean(old)*old.size());
        System.out.println("AVG_INCOME=");
        System.out.println(sum/count);
    }
    
    //Computes the 1-D cuboid
    public void cuboid1() throws Exception
    {
        System.out.println("\n1D");
        System.out.println("COUNT:");
        System.out.println("AGE:");
        System.out.println("YOUNG\tMIDDLE\tOLD");
        System.out.println(young.size()+"\t"+middle.size()+"\t"+old.size());
        
        System.out.println("REGION:");
        System.out.println("INNER_CITY\tTOWN\tRURAL\tSUBURBAN");
        System.out.println(inner.size()+"\t\t"+town.size()+"\t"+rural.size()+"\t"+suburban.size());
        
        System.out.println("SEX:");
        System.out.println("FEMALE\tMALE");
        System.out.println(female.size()+"\t"+male.size());
        //display the 1d cuboid for fact average income
        System.out.println("\nAVG INCOME:");
        System.out.println("AGE:");
        System.out.println("YOUNG\t\tMIDDLE\t\tOLD");
        System.out.println(mean(young)+"\t"+mean(middle)+"\t"+mean(old));
        
        System.out.println("REGION:");
        System.out.println("INNER_CITY\tTOWN\t\tRURAL\t\tSUBURBAN");
        System.out.println(mean(inner)+"\t"+mean(town)+"\t"+mean(rural)+"\t"+mean(suburban));

        System.out.println("SEX:");
        System.out.println("FEMALE\t\tMALE");
        System.out.println(mean(female)+"\t"+mean(male));
    }
    
    //Computes the average of income of elemnets in arrayList o
    public float mean(ArrayList<String> o)
    {
        float avg=0;
        int n=o.size();
        for(Object m:o)
        {
            String in=m.toString();
            String t[]=in.split(",");
            avg+=Float.valueOf(t[3]);
        }
        float m=avg/n;
        return m;
    } 
    //To find the cuboid of dimension=2
   public void cuboid2()
   {
       System.out.println("2D");
       fill2();
   }
   
   //Computes all the value to be filled in all the three 2D cuboids.
   public void fill2()
   {
       //count of young,middle,old(male|female)
       int cym=0,cyf=0,cmm=0,cmf=0,com=0,cof=0;
       //count of young,middle,old(inner|town|rural|suburban)
       int cyi=0,cyt=0,cyr=0,cys=0;
       int cmi=0,cmt=0,cmr=0,cms=0;
       int coi=0,cot=0,cor=0,cos=0;
       //count of male,female(inner|town|rural|suburban)      
       int cfi2=0,cft2=0,cfr2=0,cfs2=0;
       int cmi2=0,cmt2=0,cmr2=0,cms2=0;
       
       //Income of young(male|female),middle(male|female),old(male|female)
       float inym1=0,inyf1=0,inmm1=0,inmf1=0,inom1=0,inof1=0;
       //Income of young,middle,old(inner|town|rural|suburban)
       float inyi=0,inyt=0,inyr=0,inys=0;
       float inmi=0,inmt=0,inmr=0,inms=0;
       float inoi=0,inot=0,inor=0,inos=0;
       float inym=0,inyf=0,inmm=0,inmf=0,inom=0,inof=0;
       //Income of male,female(inner|town|rural|suburban)
       float infi2=0,inft2=0,infr2=0,infs2=0;
       float inmi2=0,inmt2=0,inmr2=0,inms2=0;
       
       for(Object o:young)
       {
           String temp[]=(o.toString()).split((","));
           if(temp[1].equals("MALE"))
           {
               cym+=1;
               inym1+=Float.valueOf(temp[3]);
           }
           else
           {
               cyf+=1;
               inyf1+=Float.valueOf(temp[3]);
           }
           
           if(temp[2].equals("INNER_CITY"))
           {
               cyi+=1;
               inyi+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmi2+=1;
                   inmi2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfi2+=1;
                   infi2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("TOWN"))
           {
               cyt+=1;
               inyt+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmt2+=1;
                   inmt2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cft2+=1;
                   inft2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("RURAL"))
           {
               cyr+=1;
               inyr+=Float.valueOf(temp[3]);
               if(temp[1].equals("MALE"))
               {
                   cmr2+=1;
                   inmr2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfr2+=1;
                   infr2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("SUBURBAN"))
           {
               cys+=1;
               inys+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cms2+=1;
                   inms2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfs2+=1;
                   infs2+=Float.valueOf(temp[3]);
               }
           }
       }
       
       
       for(Object o:middle)
       {
           String temp[]=(o.toString()).split((","));
           if(temp[1].equals("MALE"))
           {
               cmm+=1;
               inmm1+=Float.valueOf(temp[3]);
           }
           else
           {
               cmf+=1;
               inmf1+=Float.valueOf(temp[3]);
           }
           
           if(temp[2].equals("INNER_CITY"))
           {
               cmi+=1;
               inmi+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmi2+=1;
                   inmi2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfi2+=1;
                   infi2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("TOWN"))
           {
               cmt+=1;
               inmt+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmt2+=1;
                   inmt2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cft2+=1;
                   inft2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("RURAL"))
           {
               cmr+=1;
               inmr+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmr2+=1;
                   inmr2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfr2+=1;
                   infr2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("SUBURBAN"))
           {
               cms+=1;
               inms+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cms2+=1;
                   inms2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfs2+=1;
                   infs2+=Float.valueOf(temp[3]);
               }
           }
       }
       
       for(Object o:old)
       {
           String temp[]=(o.toString()).split((","));
           if(temp[1].equals("MALE"))
           {
               com+=1;
               inom1+=Float.valueOf(temp[3]);
           }
           else
           {
               cof+=1;
               inof1+=Float.valueOf(temp[3]);
           }
           
           if(temp[2].equals("INNER_CITY"))
           {
               coi+=1;
               inoi+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmi2+=1;
                   inmi2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfi2+=1;
                   infi2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("TOWN"))
           {
               cot+=1;
               inot+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmt2+=1;
                   inmt2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cft2+=1;
                   inft2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("RURAL"))
           {
               cor+=1;
               inor+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cmr2+=1;
                   inmr2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfr2+=1;
                   infr2+=Float.valueOf(temp[3]);
               }
           }
           else if(temp[2].equals("SUBURBAN"))
           {
               cos+=1;
               inos+=Float.valueOf(temp[3]);
               
               if(temp[1].equals("MALE"))
               {
                   cms2+=1;
                   inms2+=Float.valueOf(temp[3]);
               }
               else
               {
                   cfs2+=1;
                   infs2+=Float.valueOf(temp[3]);
               }
           }
       }
       //Displaying the tables
       System.out.println("COUNT-");       
       System.out.println("AGE-SEX:");
       System.out.println("\tMale\tFemale");
       System.out.println("Young\t"+cym+"\t"+cyf);
       System.out.println("Middle\t"+cmm+"\t"+cmf);
       System.out.println("Old\t"+com+"\t"+cof);
       
       System.out.println("AGE-REGION:");
       System.out.println("\tInner_city\tTown\tRural\tSuburban");
       System.out.println("Young\t"+cyi+"\t\t"+cyt+"\t"+cyr+"\t"+cys);
       System.out.println("Middle\t"+cmi+"\t\t"+cmt+"\t"+cmr+"\t"+cms);
       System.out.println("Old\t"+coi+"\t\t"+cot+"\t"+cor+"\t"+cos);

       System.out.println("SEX-REGION");
       System.out.println("\tInner_city\tTown\tRural\tSuburban");
       System.out.println("Female\t"+cfi2+"\t\t"+cft2+"\t"+cfr2+"\t"+cfs2);
       System.out.println("Male\t"+cmi2+"\t\t"+cmt2+"\t"+cmr2+"\t"+cms2);
       
       System.out.println("\n\nAVERAGE-INCOME");
       System.out.println("AGE-SEX");
       System.out.println("\tMale\t\tFemale");
       System.out.println("Young\t"+inym1/cym+"\t"+inyf1/cyf);
       System.out.println("Middle\t"+inmm1/cmm+"\t"+inmf1/cmf);
       System.out.println("Old\t"+inom1/com+"\t"+inof1/cof);
       
       System.out.println("AGE-REGION");
       System.out.println("\tInner_city\tTown\t\tRural\t\tSuburban");
       System.out.println("Young\t"+inyi+"\t"+inyt+"\t"+inyr+"\t"+inys);
       System.out.println("Middle\t"+inmi+"\t"+inmt+"\t"+inmr+"\t"+inms);
       System.out.println("Old\t"+inoi+"\t"+inot+"\t"+inor+"\t"+inos);
       
       System.out.println("SEX-REGION");
       System.out.println("\tInner_city\tTown\t\tRural\t\tSuburban");
       System.out.println("Female\t"+infi2+"\t"+inft2+"\t"+infr2+"\t"+infs2);
       System.out.println("Male\t"+inmi2+"\t"+inmt2+"\t"+inmr2+"\t"+inms2);
   }
   
   public void cuboid3()
   {
       System.out.println("3D");
       System.out.println("Slicing by age");
       System.out.println("YOUNG");
       fill3(young);
       
       System.out.println("\nMIDDLE");
       fill3(middle);
       
       System.out.println("\nOLD");
       fill3(old);
   }
   
   public void fill3(ArrayList<String> o)
   {
       //count of female,male in different regions.
       int cfi=0,cft=0,cfr=0,cfs=0;
       int cmi=0,cmt=0,cmr=0,cms=0;
      //Average income of male,female in different regions. 
       float infi=0,inft=0,infr=0,infs=0;
       float inmi=0,inmt=0,inmr=0,inms=0;
       
       for(Object m:o)
       {
           String temp[]= (m.toString()).split(",");
           if(temp[1].equals("MALE"))
           {
               if(temp[2].equals("INNER_CITY"))
               {
                   cmi+=1;
                   inmi+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("TOWN"))
               {
                   cmt+=1;
                   inmt+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("RURAL"))
               {
                   cmr+=1;
                   inmr+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("SUBURBAN"))
               {
                   cms+=1;
                   inms+=Float.valueOf(temp[3]);
               }
           }
           
           else
           {
               if(temp[2].equals("INNER_CITY"))
               {
                   cfi+=1;
                   infi+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("TOWN"))
               {
                   cft+=1;
                   inft+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("RURAL"))
               {
                   cfr+=1;
                   infr+=Float.valueOf(temp[3]);
               }
               else if(temp[2].equals("SUBURBAN"))
               {
                   cfs+=1;
                   infs+=Float.valueOf(temp[3]);
               }
           }
       }
       //displaying the 3-d cuboid
       System.out.println("COUNT-");
       System.out.println("\tInner_city\tTown\tRural\tSuburban");
       System.out.println("Female\t"+cfi+"\t\t"+cft+"\t"+cfr+"\t"+cfs);
       System.out.println("Male\t"+cmi+"\t\t"+cmt+"\t"+cmr+"\t"+cms);
       
       System.out.println("\nAVG_INCOME-");
       System.out.println("\tInner_city\t\tTown\t\tRural\t\tSuburban");
       System.out.println("Female\t"+infi/cfi+"\t\t"+inft/cft+"\t"+infr/cfr+"\t"+infs/cfs);
       System.out.println("Male\t"+inmi/cmi+"\t\t"+inmt/cmt+"\t"+inmr/cmr+"\t"+inms/cms);
   }
}