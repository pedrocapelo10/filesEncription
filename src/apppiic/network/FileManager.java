/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic.network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Pedro Capelo
 */
public class FileManager {
    public FileManager(){
    
    
    }
    public boolean findCustomer(String pathFile, String name, String password) throws IOException{
        
        File file = new File(pathFile);
        String user="-->User:"+name+"--> Password:"+password;
        
        if (file.createNewFile()) {} else {}
        
        Scanner scanner= new Scanner(file);
         //now read the file line by line...
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
          
            if(line.equals(user)) { 
                return true;
            }
        }
        scanner.close();
    
        return false;
    }
    public boolean signInCustomer(String pathFile, String name,String password) throws  IOException{
        File file= new File(pathFile);
        if (file.createNewFile()) {} else {}

        BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile, true));  //Set true for append mode
        
        writer.newLine();   //Add new line
        writer.write("-->User:"+name+"--> Password:"+password);
        writer.close();
        return true;
    }
}
