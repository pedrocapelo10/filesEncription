/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic;

import java.io.File;

/**
 *
 * @author Pedro Capelo
 */
public class ButtonsThread extends Thread {
    private javax.swing.JToggleButton listen;
    private javax.swing.JButton send;
    private javax.swing.JButton sendFile;
    private javax.swing.JButton addFile;
    File file,file1,file2;
    public ButtonsThread(javax.swing.JToggleButton listen,javax.swing.JButton send, javax.swing.JButton sendFile,javax.swing.JButton addFile ) {
        this.listen = listen;
        this.send=send;
        this.sendFile=sendFile;
        this.addFile=addFile;
        
    }
        @Override
    public void run(){
        while(true){
            System.out.println("");
            if(listen.isSelected()){
                
                send.setEnabled(true);
               
                addFile.setEnabled(true);
            }else{
                
                send.setEnabled(false);
                sendFile.setEnabled(false);
                addFile.setEnabled(false);
            }
            
        }
    
    
    
    }
}
