/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pachet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 *
 * @author Alex
 */
public class Logs {
    String mesaj;
    Calendar time = Calendar.getInstance();
    int day = time.get(Calendar.DAY_OF_MONTH);
    
    public void logwriter(String mesaj){
        try(FileWriter fw = new FileWriter("logs.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
           
{
    out.println(mesaj+""+day);
    //more code
}   catch (IOException e) {
    //exception handling left as an exercise for the reader
}
    }
    
}
