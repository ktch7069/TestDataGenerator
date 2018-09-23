/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.Properties;

/**
 *
 * @author Trump-PC
 */
public class DelimitedFileParser {
   
    private final String inputPathKey="emailInputPath";
    private final String outputPathKey="outPutPath";
    private final String fieldDelimiterKey="fieldDelimiter";
    private final String fieldQuoteKey="fieldQuote";
    private final String minDocNumberKey="mixDocNumber";
    private final String maxDocNumberKey="maxDocNumber";
    private final boolean debug =false;
    
    //arraylist to store the email address 
    private ArrayList<DocumentData> documentDataCollection;
    private String inputPath;
    private String outputPath;
    private char fieldDelimiter;
    private char fieldQuote;
    private int minDocIdNbr;
    private int maxDocIdNbr;
    
    public DelimitedFileParser(Properties prop){      
        inputPath= prop.getProperty(inputPathKey);
        outputPath =prop.getProperty(outputPathKey);
        fieldDelimiter=(prop.getProperty(fieldDelimiterKey)).charAt(0);
        fieldQuote=(prop.getProperty(fieldQuoteKey)).charAt(0);
        minDocIdNbr=Integer.parseInt(prop.getProperty(minDocNumberKey));
        maxDocIdNbr=Integer.parseInt(prop.getProperty(maxDocNumberKey));
        documentDataCollection = new ArrayList<DocumentData>();
    }
    
    public void parseLine() throws IOException{
        
        Scanner scanner = new Scanner(new File(inputPath));
      
        while (scanner.hasNext()) {
            
            //Create a data object and put it to the arraylist
            String emailString = scanner.nextLine();
            
            if(debug == true){
                System.out.print("Scanner input : ");
                System.out.println(emailString);
            }
             //if empty, return!
            if (emailString == null && emailString.isEmpty()) {
                break;
            }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;
         
        char[] chars = emailString.toCharArray();
        
        if(debug == true){
                System.out.print("char array : ");
                System.out.println(chars);
            }
        
        for (char ch : chars) {
            if(debug == true){
                System.out.print("The char is : ");
                System.out.println(ch);
            }
            if (inQuotes) {
                
                startCollectChar = true;
                
                //hitting end quote
                if (ch == fieldQuote) {
                    inQuotes = false;
                    //doubleQuotesInColumn = false;
                } else {
                    //Fixed : allow "" in custom quote enclosed
                    /*if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }*/
                    curVal.append(ch);
                }
            } else {
                    if (ch ==fieldQuote ) {

                        inQuotes = true;

                        //Fixed : allow "" in empty quote enclosed
                        //Not quire sure what this does
                        /*if (chars[0] != '"' && fieldQuote == '\"') {
                            curVal.append('"');
                        }*/
                        //double quotes in column will hit this!
                        //example "blah""Dada","dadada"
                        /*if (startCollectChar) {
                            curVal.append('"');
                        }*/
                    } 
                    //Reaching end of field
                    else if (ch == fieldDelimiter) {
                        
                        DocumentData aDat = new DocumentData(maxDocIdNbr,minDocIdNbr); 
                        
                        if(debug == true){
                            System.out.print("Putting String : ");
                            System.out.print(curVal.toString());
                            System.out.println(" to the arraylist!");
                          }
                        
                        aDat.setEmailAddress(curVal.toString());
                        documentDataCollection.add(aDat);

                        curVal = new StringBuffer();
                        startCollectChar = false;

                    } else if (ch == '\r') {
                        //ignore LF characters
                        continue;    
                    } 
                    //end of the char array
                    else if (ch == '\n') {
                         if(debug == true){
                            System.out.print("Reaching the end stopping... ");
                         }
                        //the end, break!
                        break;
                    } else {
                        curVal.append(ch);
                    }
            }

        }
        
       //putting in the data just before EOF .... 
       DocumentData aDat = new DocumentData(maxDocIdNbr,minDocIdNbr);
       aDat.setEmailAddress(curVal.toString());
       documentDataCollection.add(aDat);
       curVal = new StringBuffer();
     
    }
 
  }
  
  public ArrayList<DocumentData> getEmailData(){
    return documentDataCollection;
   }
    
}