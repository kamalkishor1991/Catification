package io.scrollback.main;

import io.scrollback.ml.SMSClassification;
import io.scrollback.server.ProcessHttpReq;
import io.scrollback.server.WebServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer(8080, new ProcessHttpReq() {
            @Override
            public void doGet(ArrayList<String> header, OutputStream out) {
                SMSClassification smsClassification = SMSClassification.getInstance();
                String s= header.get(0);
                System.out.println(s);
                try {
                    s = URLDecoder.decode(s, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return;
                }
                String q = s.substring(s.indexOf('?') + 1);
                PrintStream ps = new PrintStream(out);
                ps.println("HTTP/1.1 200 OK");
                //write
                String r = "callback({results: " + smsClassification.isSpam(q) + "})";
                ps.println("content-type: application/x-javascript");
                ps.println("Content-length: " + r.length());

                ps.println();
                ps.println(r);
                System.out.println(r);
                ps.close();
            }

            @Override
            public void doPost(ArrayList<String> header, OutputStream out) {

            }
        });
        webServer.start();
    }
}
