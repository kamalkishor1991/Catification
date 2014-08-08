package io.scrollback.rw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ReadWriteArray {
    /**
     *
     */
    public ReadWriteArray(){

    }





    /**
     * This method will read complete file to calculate (May take time).
     * @param f
     * @return no of lines.
     * @throws IOException
     */
    public int getNumberOfLines(File f) throws IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        int i=0;
        while(bis.readLine()!=null){
            i++;
        }
        return i;
    }
    /**
     *
     * @param f
     * @param r
     * @param c
     * @return
     * @throws IOException
     */
    public double[][] readDoubleMatrix(File f,int r,int c) throws IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));

        double rt[][]=new double[r][c];
        for(int i=0;i<r;i++){
            String str=bis.readLine()+",";
            for(int j=0;j<c;j++){
                rt[i][j]=new Double(str.substring(0,str.indexOf(','))).doubleValue();
                str=str.substring(str.indexOf(',')+1);
            }
        }
        return rt;
    }
    /**
     *
     * @param f
     * @param r
     * @return
     * @throws IOException
     */
    public double[] readDoubleVector(File f,int r) throws IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));

        double rt[]=new double[r];
        for(int i=0;i<r;i++){
            String str=bis.readLine();

            rt[i]=new Double(str).doubleValue();
            //str=str.substring(str.indexOf(',')+1);

        }
        return rt;
    }
    /**
     *
     * @param f
     * @param r
     * @param c
     * @return
     * @throws IOException
     */
    public int[][] readIntMatrix(File f,int r,int c) throws IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));

        int rt[][]=new int[r][c];
        for(int i=0;i<r;i++){
            String str=bis.readLine()+",";
            for(int j=0;j<c;j++){
                rt[i][j]=Integer.parseInt(str.substring(0,str.indexOf(',')));
                str=str.substring(str.indexOf(',')+1);
            }
        }
        return rt;
    }
    /**
     *
     * @param f
     * @param r
     * @return
     * @throws IOException
     */
    public int[] readIntVector(File f,int r) throws IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));

        int rt[]=new int[r];
        for(int i=0;i<r;i++){
            String str=bis.readLine();

            rt[i]=Integer.parseInt(str);
            //str=str.substring(str.indexOf(',')+1);

        }
        return rt;
    }
    /**
     *
     * @param in
     * @param f
     * @throws FileNotFoundException
     */
    public void writeDoubleMatrix(double in[][],File f) throws FileNotFoundException{
        PrintStream ps=new PrintStream(new FileOutputStream(f));

        for(int i=0;i<in.length;i++){
            for(int j=0;j<in[0].length;j++){
                ps.print(in[i][j]+",");
            }
            ps.println("");
        }

    }
    /**
     *
     * @param in
     * @param f
     * @throws FileNotFoundException
     */
    public void writeIntMatrix(int in[][],File f) throws FileNotFoundException{
        PrintStream ps=new PrintStream(new FileOutputStream(f));

        for(int i=0;i<in.length;i++){
            for(int j=0;j<in[0].length;j++){
                ps.print(in[i][j]+",");
            }
            ps.println("");
        }

    }
    /**
     * write value of vector of double in file.
     * @param in
     * @param f
     * @throws FileNotFoundException
     */
    public void writeDoubleVector(double in[],File f) throws FileNotFoundException{
        PrintStream ps=new PrintStream(new FileOutputStream(f));

        for(int i=0;i<in.length;i++){

            ps.println(in[i]+"");
        }

    }
    /**
     *
     * @param in
     * @param f
     * @throws FileNotFoundException
     */
    public void writeIntVector(int in[],File f) throws FileNotFoundException{
        PrintStream ps=new PrintStream(new FileOutputStream(f));

        for(int i=0;i<in.length;i++){

            ps.println(in[i]+"");
        }

    }
    /**
     * write 3 D array in file f
     * @param arr array
     * @param f File
     * @throws FileNotFoundException
     */
    public void writeMultArray(double arr[][][],File f) throws FileNotFoundException
    {
        PrintStream ps=new PrintStream(new FileOutputStream(f));
        ps.println(arr.length);
        for(int i=0;i<arr.length;i++)
        {	ps.println(arr[i].length);
            for(int j=0;j<arr[i].length;j++)
            {	ps.println(arr[i][j].length);
                for(int k=0;k<arr[i][j].length;k++)
                {
                    ps.println(arr[i][j][k]);
                }

            }
        }

    }

    /**
     * Read 3D array written by writeMultArray
     * @param f File
     * @return 3 D array
     * @throws NumberFormatException if Wrong file.
     * @throws IOException if file is not present.
     */
    public double[][][] readMultArray(File f) throws NumberFormatException, IOException{
        BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        double arr[][][]=new double[Integer.parseInt(bis.readLine())][][];
        for(int i=0;i<arr.length;i++)
        {	arr[i]=new double[Integer.parseInt(bis.readLine())][];
            for(int j=0;j<arr[i].length;j++)
            {	arr[i][j]=new double[Integer.parseInt(bis.readLine())];
                for(int k=0;k<arr[i][j].length;k++)
                {
                    arr[i][j][k]=new Double(bis.readLine()).doubleValue();
                }

            }
        }
        return arr;
    }

    /**
     * write 3 D array such that java can read this.
     * @param f File.
     * @param ar 3 D array
     * @param isHex write in Hex or not.
     * @throws FileNotFoundException
     */
    public void write3DArray(File f,double ar[][][],boolean isHex) throws FileNotFoundException{
        PrintStream ps=new PrintStream(new FileOutputStream(f));
        ps.print("{");
        if(isHex){
            for(int i=0;i<ar.length;i++){
                ps.print("{");
                for(int j=0;j<ar[i].length;j++){
                    ps.print("{");
                    for(int k=0;k<ar[i][j].length;k++){
                        String s=Double.toHexString(ar[i][j][k])+",";
                        //s=s+",";
                        ps.print(s);
                    }
                    ps.print("},");
                }
                ps.print("},");
            }
        }
        else{
            for(int i=0;i<ar.length;i++){
                ps.print("{");
                for(int j=0;j<ar[i].length;j++){
                    ps.print("{");
                    for(int k=0;k<ar[i][j].length;k++){
                        String s=ar[i][j][k]+",";
                        //s=s+",";
                        ps.print(s);
                    }
                    ps.print("},");
                }
                ps.print("},");
            }
        }
        ps.print("}");


    }

}