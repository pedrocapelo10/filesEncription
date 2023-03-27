/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Capelo
 */

//SERVER
public class MessageReceive extends Thread{
    ServerSocket server;
    int port,nivel;
    WritableGUI gui;
    String host;
    
    
    
    public MessageReceive(WritableGUI gui,int port, String host){
        this.port=port;
        this.gui=gui;
        this.host=host;
        try {
            server= new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.nivel=0;
    }
    public MessageReceive(){
        try {
            server= new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public int devolveNivel(MessageReceive message){
        return message.nivel;
    }
    @Override
    public void run(){
        Socket clientSocket;
        try {
            while((clientSocket = server.accept())!=null){
                InputStream is= clientSocket.getInputStream();//guardar dados
                BufferedReader br= new BufferedReader(new InputStreamReader(is));
                char c;
                int number=0; 
                String lineFile="";
                String line= br.readLine();
                
                
                lineFile=line.substring(line.length()-4,line.length()-1);
                 lineFile=line.substring(line.length()-4,line.length()-1);
                if(line.length()>10 && "461".equals(lineFile)){
                c=line.charAt(line.length()-1);//recebe o nivel
                number=Character.getNumericValue(c);
                
                lineFile=line.substring(line.length()-4,line.length()-1);
                line=line.substring(0,line.length()-4);
                }
                
                if(number==1||number==2||number==3){
                    this.nivel=number;
                }
                if("461".equals(lineFile)){
                    gui.enableSendFile(1);
                }
                if(line != null){
                    gui.write(line);
                    
                }
            
               
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MessageReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
