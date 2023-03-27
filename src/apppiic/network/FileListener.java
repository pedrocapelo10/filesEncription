/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic.network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pedro Capelo
 */
public class FileListener extends Thread{
   String filePath, hostname;
    int port;
    FileOutputStream fr;
    byte []b;
    File file;
    InputStream is;
    Socket s;
    int bytesRead;
    int current;
    BufferedOutputStream bos;
    Encript fileEncrypt;
    int nivel;
    WritableGUI gui;
    public FileListener(){
    }
    
    public FileListener( String filePath,String hostname,int port, int nivel,WritableGUI gui) {
        
        this.hostname=hostname;
        this.port=port;
        this.bos = null;
        this.s=null;
        this.nivel=nivel;
        this.filePath=filePath;
        this.fileEncrypt= new Encript();
        this.gui=gui;
    }

    
   

    @Override
    public void run() {
     
            String filePathencryp = null;
           filePathencryp=filePath.substring(0,filePath.length()-4 );
           filePathencryp=filePathencryp+"Encrypt.txt";
            
       try {
           try {
               
               s= new Socket(hostname, port);//connect
               //receive file
               b= new byte[8000000];
               is=s.getInputStream();
           
           fr= new FileOutputStream(filePathencryp);
           bos= new BufferedOutputStream(fr);
           bytesRead = is.read(b,0,b.length);
           current = bytesRead;
           
           do {
               bytesRead =is.read(b, current, (b.length-current));
                if(bytesRead >= 0) current += bytesRead;
           } while(bytesRead > -1);
          
           bos.write(b, 0 , current);
           bos.flush();
          
           
           
           
       }finally {
            if (fr != null) fr.close();
            if (bos != null) bos.close();
            if (s != null) s.close();
        }
    } catch (IOException ex) {
               Logger.getLogger(FileListener.class.getName()).log(Level.SEVERE, null, ex);
    }
           
           
       try {
           if(nivel==1){
               fileEncrypt.desencriptarFile1("secretpass",filePathencryp,filePath);
           }
           if(nivel==2){
               
               
               fileEncrypt.encriptFileKeys(2);
               File file = new File(filePath);
               File fileEncryp = new File(filePathencryp);
               fileEncrypt.desencriptarFile2(fileEncryp,file);
              fileEncrypt.encriptFileKeys(1);
           }
           if(nivel==3){
               fileEncrypt.encriptFileKeys(2);
               File file = new File(filePath);
               File fileEncryp = new File(filePathencryp);
               fileEncrypt.desencriptarFile3(fileEncryp,file,gui);
              fileEncrypt.encriptFileKeys(1);
           }
           
       } catch (Exception ex) {
           Logger.getLogger(FileListener.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
       
       File inputFile=new File(filePathencryp);
        inputFile.delete();
       File outputFile= new File(filePath);
       
       
       //alterar o path do ficheiro encriptado
       Path source = Paths.get(filePathencryp);
       try {
           Files.move(source, source.resolveSibling(filePath));
       } catch (IOException ex) {
           Logger.getLogger(FileListener.class.getName()).log(Level.SEVERE, null, ex);
       }
               
        
    }
}
