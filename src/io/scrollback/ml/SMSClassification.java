package io.scrollback.ml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import io.scrollback.rw.ReadWriteArray;

public class SMSClassification {
    private static NeuralNet neuralNet;
    private static int sizeLayer[];
    private static double theta[][][];
    private static ArrayList<String> al;
    private static SMSClassification instance;
    private SMSClassification() throws IOException {
        String f = "data/freqWords.txt";
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        al = new ArrayList<String>();
        String line = "";
        while ((line = br2.readLine()) != null) {
            String s[] = line.split(" ");
            if(Integer.parseInt(s[0]) > 2) al.add(s[1]);
        }
        sizeLayer = new int[]{al.size(), 10, 1};
        theta = new ReadWriteArray().readMultArray(new File("data/theta.out"));
        neuralNet = new NeuralNet(sizeLayer);
        neuralNet.setTheta(theta);
    }
    public static SMSClassification getInstance() {
        try {
            return instance == null ? (instance = new SMSClassification()) : instance;
        } catch (IOException e) {
            System.out.println("Error:" +  e);
           //ignored
        }
        return instance;
    }

    public static boolean isSpam(String msg) {
        int out[] = new int[al.size()];
        for (int i = 0;i < al.size();i++) {
            if(msg.contains(al.get(i))) out[i] = 1;
        }
        double v[] = neuralNet.calculateOutput(out);
        return v[0] >= .5;
    }

    public static void main(String[] args) throws IOException {
        train();

    }

    private static void train() throws IOException {
        //NeuralNet nn = new NeuralNet()
        String file = "data/output.txt";
        String f = "data/freqWords.txt";
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        ArrayList<String> input = new ArrayList<String>();
        ArrayList<Integer> out = new ArrayList<Integer>();
        String line = "";
        while ((line = br1.readLine()) != null) {
            input.add(line.substring(2));
            out.add(Integer.parseInt(line.substring(0, 1)));
        }
        double in[][] = new double[input.size()][];
        ArrayList<String> al = new ArrayList<String>();
        while ((line = br2.readLine()) != null) {
            String s[] = line.split(" ");
            if(Integer.parseInt(s[0]) > 2) al.add(s[1]);
        }
        int ot[][] = new int[input.size()][1];
        for (int i = 0;i < input.size();i++) {
            in[i] = new double[al.size()];
            for (int j = 0;j < al.size();j++ ) {
                if (input.get(i).indexOf(al.get(j)) != -1) in[i][j] = 1;
            }
            ot[i][0] = out.get(i);
        }
        int sizeLayer[] = {in[0].length, 15, 1};
        NeuralNet nn = new NeuralNet(sizeLayer, in, ot);
        nn.initializeTheta();
        int nr  = 10;
        nn.setParameter(1, 0);
        nn.learnNeuralNetwork(nr);
        ReadWriteArray rw = new ReadWriteArray();
        rw.writeMultArray(nn.getTheta(), new File("data/theta.out"));
        System.out.printf("cost" + nn.cost());
        int err = 0;
        for (int i = 0;i < in.length;i++) {
            double o[] = nn.calculateOutput(in[i]);
            if((o[0] > .5 && ot[i][0] == 1) ||
                o[0] <=.5 && ot[i][0] == 0) {
                System.out.println("Correct:" +  ot[i][0] + "," + input.get(i));
                continue;
            } else {
                System.out.println("inCorrect: " + ot[i][0] +  "," + input.get(i) );
                err++;
            }
        }
        System.out.printf("Error" + ((err * 100.0)/in.length) + ", " + err + " ," + in.length);
    }


    private static void genFreq() throws IOException {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("output.txt")));
        String line = "";
        HashMap<String, Integer> freq = new HashMap<String, Integer>();

        while ((line = br1.readLine()) != null) {
            line = line.substring(2);
            String w[] = line.split("[ \t,]");
            System.out.println(Arrays.toString(w));
            for (String s : w) {
                s = s.toLowerCase().trim();
                if(s.equals("")) continue;
                if(freq.containsKey(s)) {
                    Integer c = freq.get(s);
                    freq.put(s, c + 1);
                } else {
                    freq.put(s, 1);
                }
            }
        }
        ArrayList<Word> al = new ArrayList<Word>();
        for (String s : freq.keySet()) {
            al.add(new Word(s, freq.get(s)));
        }
        Collections.sort(al);
        PrintStream f = new PrintStream(new FileOutputStream("freqWords.txt"));
        for (Word w : al) {
            f.println(w.count + " " + w.w);
        }
        f.close();
    }

    private static void genOutputFile() throws IOException {
        String f1 = "data/spam.xml";
        String f2 = "data/smsSpam.txt";
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));


        PrintStream ps = new PrintStream(new FileOutputStream("output.txt"));
        String line = "";
        while ((line = br1.readLine()) != null) {
            System.out.println(line);
            line = line.trim();
            line = line.toLowerCase();
            line = line.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            if(line.indexOf("<text>") == 0) {
                ps.println("1 " + line.substring(6, line.length() - 7));
            }
        }


        while ((line = br2.readLine()) != null) {
            line = line.toLowerCase();
            if(line.startsWith("spam")) {
                ps.println("1 " + line.substring(5));
            } else {
                ps.println("0 " + line.substring(5));
            }
        }

        ps.close();
    }



}

class Word implements Comparable<Word> {
    String w;
    int count = 0;
    Word(String w, int count) {
        this.w = w;
        this.count = count;
    }

    @Override
    public int compareTo(Word o) {
        return (o.count - this.count);
    }
}