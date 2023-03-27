/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pedro Capelo
 */
public class FileTransmitter extends Thread{
    
    ServerSocket server;
    int port;
    WritableGUI gui;
    String host, filePath;
    FileInputStream fr;
    byte []b;
    File file;
    String hostname;
    BufferedInputStream bis ;
    OutputStream os;
    Socket clientSocket; 
    int nivel;
    String deleteFile;
    Encript fileEncrypt;
            
    public FileTransmitter(WritableGUI gui,int port, String host,String filePath,String hostname,int nivel) throws IOException{
        this.port=port;
        this.gui=gui;
        this.host=host;
        this.filePath=filePath;
        this.os=null;
        this.clientSocket =null;
        this.bis=null;
        this.hostname=hostname;
        
        this.nivel=nivel;
        this.fileEncrypt= new Encript();
        
        
    }
    public FileTransmitter(){
        try {
            server= new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    @Override
    public void run(){
         File outputFile = null;
        try {
            try {
                server= new ServerSocket(port);
                while(true){
                try {
                    
                    String filePathencryp = null;
                    filePathencryp=filePath.substring(0,filePath.length()-4 );
                    filePathencryp=filePathencryp+"Encrypt.txt";
                    
                    if(nivel==1){//chaves simetricas
                        fileEncrypt.encriptarFile1("secretpass",filePath,filePathencryp);
                    }
                    if(nivel==2){//chaves assimetricas
                        // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
                        if (!fileEncrypt.verificaSeExisteChavesNoSO()) {
                            // Método responsável por gerar um par de chaves usando o algoritmo RSA e
                            // armazena as chaves nos seus respectivos arquivos.
                            fileEncrypt.geraChave();
                            fileEncrypt.encriptFileKeys(1);
                        }
                        
                        fileEncrypt.encriptFileKeys(2);
                        File file= new File(filePath);
                        File fileEncryp = new File(filePathencryp);
                        fileEncrypt.encriptarFile2( file,fileEncryp);
                        
                        fileEncrypt.encriptFileKeys(1);
                    }
                    if(nivel==3){//assinatura digital
                        // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
                        if (!fileEncrypt.verificaSeExisteChavesNoSO()) {
                            // Método responsável por gerar um par de chaves usando o algoritmo RSA e
                            // armazena as chaves nos seus respectivos arquivos.
                            fileEncrypt.geraChave();
                            fileEncrypt.encriptFileKeys(1);
                        }
                        
                        fileEncrypt.encriptFileKeys(2);
                        File file= new File(filePath);
                        File fileEncryp = new File(filePathencryp);
                        fileEncrypt.encriptarFile3(file,fileEncryp);
                        
                        fileEncrypt.encriptFileKeys(1);
                        
                    }
                    

                    clientSocket = server.accept();
         
                    outputFile= new File(filePathencryp);
                    
                  
                    b= new byte[(int)outputFile.length()];//tamanho
                    
                    fr= new FileInputStream(filePathencryp);//path do file
                   
                    bis = new BufferedInputStream(fr);//
                   
                    
                    bis.read(b,0,b.length);
                    
                    os= clientSocket.getOutputStream();
                   
                    os.write(b,0,b.length);
                   
                    os.flush();
                   
                
               
                    
                    
                }   catch (Exception ex) {
                        Logger.getLogger(FileTransmitter.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (clientSocket!=null) clientSocket.close();
                    if(outputFile!=null) outputFile.delete();
                   
                }           
          }
        } finally {
            if (server != null) server.close();
          }
            
        } catch (IOException ex) {
            Logger.getLogger(FileTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
            
                    
    } 
    
}
              
        
       