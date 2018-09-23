import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.Properties;

public class ReplaceText {
  
    //define all the key variables for config.properties file
    private final String emailRegxKey = "emailFieldRegx";
    private final String docIdRegxKey = "docIdRegx";
    private final String templateLocationKey = "templateInputPath";  
    private final String dataOutPutPathKey = "outPutPath";
    private final String cdEmailRegexKey = "cdEmailRegex";
    private final String dataTypeKey= "dataType";
    
    
    private final String emailXmlTag1="<Customer_Email>";
    private final String emailXmlTag2="</Customer_Email>";
    private final String docIdXmlTag1="<document_id>";
    private final String docIdXmlTag2="</document_id>";
    private final String cdEmailXmlTag1="<CDEMAIL>";
    private final String cdEmailXmlTag2="</CDEMAIL>";
    
    
    private final boolean debug = false; 
    private String emailFieldRegx;
    private String outPutLocation;
    private String cdEmailRegex;
    private String docIdFieldRegx;
    private String templatePath; 
    private String dataType;
    private ArrayList<DocumentData> documentDataCollection;
   
    public ReplaceText(Properties prop, ArrayList<DocumentData> dat){
        
       this.emailFieldRegx= prop.getProperty(emailRegxKey);
       this.docIdFieldRegx =prop.getProperty(docIdRegxKey);
       this.templatePath = prop.getProperty(templateLocationKey);
       this.cdEmailRegex =  prop.getProperty(cdEmailRegexKey);
       this.outPutLocation=prop.getProperty(dataOutPutPathKey);
      
       //This is the arraislist which contains the DocumentData object
       //which encapsulate email address and document id 
       this.documentDataCollection = dat;
    }
    
    public void replace(){       
        
        try {          
            //Read the sampe test data file into a Buffer
            BufferedReader file = new BufferedReader(new FileReader(templatePath));
            String line;
            StringBuffer inputBuffer = new StringBuffer();
         
            int count=0;
            
            //Now read file from the buffer line by line in to a string variable "line"
            //looop over while there are lines in the buffer
            while ((line = file.readLine()) != null) {
                
                //dat arraylist contains data object which encapsulates customer_email and document_id
                //If we have already at the end of the documentDataCollection arraylist 
                //then we start from the begining
                if(count>documentDataCollection.size()-1){
                    count=0;
                }
                
                if(line.matches(emailFieldRegx)){
                    
                    String newEmail= emailXmlTag1 + documentDataCollection.get(count).getEmailAddress() +emailXmlTag2;
                    inputBuffer.append(newEmail);
                    inputBuffer.append('\n'); 
                }                
                else if(line.matches(docIdFieldRegx)){
                    
                    String newDocId=  docIdXmlTag1+ documentDataCollection.get(count).getDocumentId() +docIdXmlTag2;
                    inputBuffer.append(newDocId);
                    inputBuffer.append('\n'); 
                    
                } else if(line.matches(cdEmailRegex)){
                    
                    String newCdEmail=  cdEmailXmlTag1 + documentDataCollection.get(count).getEmailAddress() + cdEmailXmlTag2;
                    inputBuffer.append(newCdEmail);
                    inputBuffer.append('\n');
                }
                else{
                    inputBuffer.append(line);
                    inputBuffer.append('\n');             
                }
                count++;
            }
            
            String inputStr = inputBuffer.toString();
            file.close();

            if(debug == true){
                System.out.println(inputStr); // check that it's inputted right    
            }
            
            FileOutputStream fileOut = new FileOutputStream(outPutLocation);
            fileOut.write(inputStr.getBytes());
            fileOut.close();

        } catch (Exception e) {
            
            e.printStackTrace();
            System.out.println("Problem reading file.");
            System.exit(1);
        } 
    }   
}
