/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author tchung
 */
public class Engine {
    
    private Properties prop = new Properties();
    private InputStream input=null;
    private ArrayList<Data> emailData;
    private final boolean debug = true;
   
    public Engine(){
        //read property file
        try{
            input = new FileInputStream("src/config.properties");
            prop.load(input);
        }catch(IOException ex){
            ex.printStackTrace();
            
        }finally{
            if(input !=null){
              try{
                input.close();
              }catch(IOException e){
                e.printStackTrace();
              }
            }
        }      
    }
    
    public void run()throws IOException{
        //make a CSV Parser object
        DelimitedFileParser parser = new DelimitedFileParser(prop); 
        parser.parseLine();
        this.emailData= parser.getEmailData();
        if(debug == true){
          
            System.out.println("Set of Emails & Document ID created are : ");
            System.out.println("********************");
            for(Data dat:emailData){
                System.out.println(dat.getDocumentId());
                System.out.println(dat.getEmailAddress());
                System.out.println("*********************");
            }
        }
        System.out.println("Creating new test file with the new email & document id....");
        ReplaceText rep = new ReplaceText(prop,emailData);
        rep.replace();   
        System.out.println("Done.Please see to output folder.");
    }
    
    public static void main(String[] args){
        
        Engine engine = new Engine();
        
        try{
            engine.run();
            
        }catch(IOException e){
            System.out.println("Something's wrong :");
            e.printStackTrace();
            System.exit(0);
        }
               
        
    }
}
