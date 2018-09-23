/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Random;

/**
 *
 * @author tchung
 */
public class Data {
    
    private int document_id;
    private String customer_email;
    private int maxDocId;
    private int minDocId;
    
    public Data(int maxDocId, int minDocId){  
        this.maxDocId=maxDocId;
        this.minDocId=minDocId;
        document_id= generateRandomDocId();
    }
            
    public void setEmailAddress(String emailAddress){
        
        this.customer_email = emailAddress;
    }
    
    public String getEmailAddress(){
        
        return customer_email;
    }
    
    public int getDocumentId(){
        
        return document_id;
    }
    
    private int generateRandomDocId(){
         
        //get a random number between 1-`00000 
        Random rand = new Random();
        return rand.nextInt((maxDocId - minDocId)+1)+minDocId;
       
    }
    
}
