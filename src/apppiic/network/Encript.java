/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apppiic.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pedro Capelo
 */
public class Encript {
    
   public Encript(){

   }
   public void encriptarFile1(String password, String ficheiroEntrada, String ficheiroSaida)
   {
       try {
           //transformar a password numa chave simetrica e inicializar a variaveç para incriptação no mode ENCRYPT_MODE
           byte[] passwordInBytes = password.getBytes("ISO-8859-1");
           Key chave = new SecretKeySpec(passwordInBytes, "Blowfish");
           Cipher cipher = Cipher.getInstance("Blowfish");
           cipher.init(Cipher.ENCRYPT_MODE, chave);
           
           byte[] textoClaro;
            try {
           //encriptar conteudo do ficheiro de input
           File ficheiro = new File(ficheiroEntrada);
           byte[] resultado = new byte[(int) ficheiro.length()];
           FileInputStream in = new FileInputStream(ficheiroEntrada);
           in.read(resultado);
           in.close();
            textoClaro= resultado;
       }
       catch (IOException e) {
           System.out.println("Problem : " + e.getMessage());
            textoClaro= null;
       }
           byte[] textoCryptado = cipher.doFinal(textoClaro);
           
          
           try {
           //guardar conteudo no ficheiro de output ja encriptado
           FileOutputStream out = new FileOutputStream(ficheiroSaida);
           out.write(textoCryptado);
           out.close();
           
       }
       catch (IOException e) {
           System.out.println("Problema ao guardar conteudo no ficheiro output : " + e.getMessage());
       }
       }
       catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
           System.out.println("Ecrypt Error ");
       }
   }
    public void desencriptarFile1(String password, String ficheiroEntrada, String ficheiroSaida)
   {
       try {
           //transformar a password numa chave simetrica e inicializar a variaveç para incriptação no mode DECRYPT_MODE
           byte[] passwordInBytes = password.getBytes("ISO-8859-1");
           Key clef = new SecretKeySpec(passwordInBytes, "Blowfish");
           Cipher cipher = Cipher.getInstance("Blowfish");
           cipher.init(Cipher.DECRYPT_MODE, clef);
           
              //desencriptar conteudo do ficheiro de input
            byte[] textoClaro;
            try {
           File ficheiro = new File(ficheiroEntrada);
           byte[] resultado = new byte[(int) ficheiro.length()];
           FileInputStream in = new FileInputStream(ficheiroEntrada);
           in.read(resultado);
           in.close();
           
            textoClaro= resultado;
       }
       catch (Exception e) {
           System.out.println("Problem : " + e.getMessage());
            textoClaro= null;
       }
           byte[] textodeCryptado = cipher.doFinal(textoClaro);
           
          
           try {
           //guardar conteudo no ficheiro de output ja encriptado
           FileOutputStream out = new FileOutputStream(ficheiroSaida);
           out.write(textodeCryptado);
           out.close();
          
       }
       catch (IOException e) {
           System.out.println("Problema ao guardar conteudo no ficheiro output : " + e.getMessage());
       }
       }
       catch (Exception e) {
           System.out.println("Ecrypt Error ");
       }
       
   }
    
   /**SECOND ENCRYPT**/

//using aes e rsa because the rsa algoritm doesnt suport much data so cant encript a file
  
    /**
     * Local da chave privada no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PRIVADA = "C:\\Users\\Pedro Capelo\\Desktop\\Lixo\\private.txt";
    public static final String PATH_CHAVE_PRIVADA_ENCRYPT = "C:\\Users\\Pedro Capelo\\Desktop\\DataCenter\\privateEncrypt.txt";
    /**
     * Local da chave pública no sistema de arquivos.
     */
    public static final String PATH_CHAVE_PUBLICA = "C:\\Users\\Pedro Capelo\\Desktop\\DataCenter\\public.txt";
    public static final String PATH_CHAVE_PUBLICA_ENCRYPT = "C:\\Users\\Pedro Capelo\\Desktop\\DataCenter\\publicEncrypt.txt";


    public static void geraChave() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        //Generating Public and Private Keys
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        
        PublicKey pub = kp.getPublic();
        PrivateKey pvt= kp.getPrivate();
        
        //Saving and Restoring Keys
        try (FileOutputStream out = new FileOutputStream(PATH_CHAVE_PRIVADA)) {
            out.write(kp.getPrivate().getEncoded());
        }

        try (FileOutputStream out = new FileOutputStream(PATH_CHAVE_PUBLICA)) {
            out.write(kp.getPublic().getEncoded());
        }
        
        
  }
  
  /**
   * Verifica se o par de chaves Pública e Privada já foram geradas.
   */
  public static boolean verificaSeExisteChavesNoSO() {
  
    File chavePrivada = new File(PATH_CHAVE_PRIVADA);
    File chavePublica = new File(PATH_CHAVE_PUBLICA);
  
    if (chavePrivada.exists() && chavePublica.exists()) {
      return true;
    }
     
    return false;
  }  
    
  static private void processFile(Cipher ci,InputStream in,OutputStream out)
    throws javax.crypto.IllegalBlockSizeException,
           javax.crypto.BadPaddingException,
           java.io.IOException
{
    byte[] ibuf = new byte[1024];
    int len;
    while ((len = in.read(ibuf)) != -1) {
        byte[] obuf = ci.update(ibuf, 0, len);
        if ( obuf != null ) out.write(obuf);
    }
    byte[] obuf = ci.doFinal();
    if ( obuf != null ) out.write(obuf);
}
    public static void encriptarFile2(File inputFile, File outputFile) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException{
        //generating the AES Key
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        SecureRandom srandom= new SecureRandom();
        //initialization vector
        byte[] iv = new byte[128/8];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        //loading the RSA private key
        
        byte[] bytes = Files.readAllBytes(Paths.get(PATH_CHAVE_PRIVADA));
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pvt = kf.generatePrivate(ks);
        
        //save the AES key
        FileOutputStream out = new FileOutputStream(outputFile);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pvt);
        byte[] b;
        try {
            b = cipher.doFinal(skey.getEncoded());
            out.write(b);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encript.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Write the Initialization Vector
        
        out.write(iv);
        
        //Encrypting the File Contents using the AES Key
        
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
        try (FileInputStream in = new FileInputStream(inputFile)) {
            processFile(ci, in, out);
            out.close();
        }
    }
    public static void desencriptarFile2(File inputFile, File outputFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
        //Load the RSA Public Key from File
        
        byte[] bytes = Files.readAllBytes(Paths.get(PATH_CHAVE_PUBLICA));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pub = kf.generatePublic(ks);
        
        //Load the AES Secret Key
        FileInputStream in = new FileInputStream(inputFile);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pub);
        byte[] b = new byte[256];
        in.read(b);
        byte[] keyb = cipher.doFinal(b);
        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        
        //Read the Initialization Vector
        byte[] iv = new byte[128 / 8];
        in.read(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        //Decrypt the File Contents
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
        try (FileOutputStream out = new FileOutputStream(outputFile)){
            processFile(ci, in, out);
        }   
    }
  
  
  public void encriptFileKeys(int aux) throws  IOException{
      if(aux==1){
        encriptarFile1("secrepass", PATH_CHAVE_PRIVADA, PATH_CHAVE_PRIVADA_ENCRYPT);
        
        File file1= new File(PATH_CHAVE_PRIVADA);
      
        
                FileOutputStream fos = new FileOutputStream(file1);
                fos.close();
                fos=null;
                System.gc(); 
                file1.delete();
               
      }else{
        desencriptarFile1("secrepass",  PATH_CHAVE_PRIVADA_ENCRYPT,PATH_CHAVE_PRIVADA);
        
      }
      
  
  }
  
   /**SECOND ENCRYPT**/
     static private void signFile(Cipher ci,Signature sign,InputStream in,OutputStream out)
    throws javax.crypto.IllegalBlockSizeException,
           javax.crypto.BadPaddingException,
           java.security.SignatureException,
           java.io.IOException
{
    byte[] ibuf = new byte[1024];
    int len;
    while ((len = in.read(ibuf)) != -1) {
        sign.update(ibuf, 0, len);
        byte[] obuf = ci.update(ibuf, 0, len);
        if ( obuf != null ) out.write(obuf);
    }
    byte[] obuf = ci.doFinal();
    if ( obuf != null ) out.write(obuf);
}
    
public static void encriptarFile3(File inputFile, File outputFile) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException, SignatureException{
        //generating the AES Key
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        SecureRandom srandom= new SecureRandom();
        
        //initialization vector
        byte[] iv = new byte[128/8];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        //loading the RSA private key
        
        byte[] bytes = Files.readAllBytes(Paths.get(PATH_CHAVE_PRIVADA));
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pvt = kf.generatePrivate(ks);
        
        //save the AES key
        FileOutputStream out = new FileOutputStream(outputFile);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pvt);
        byte[] b;
        try {
            b = cipher.doFinal(skey.getEncoded());
            out.write(b);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encript.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Write the Initialization Vector
        
        out.write(iv);
        
        //Encrypt the Message and Sign it
        
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(pvt); // Sign using A's private key

        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
        try (FileInputStream in = new FileInputStream(inputFile)) {
            signFile(ci, sign, in, out);
        }
        byte[] s = sign.sign();
        out.write(s);
        out.close();
    }

static private void authFile(Cipher ci,Signature ver,InputStream in,OutputStream out,long dataLen)
    throws javax.crypto.IllegalBlockSizeException,
           javax.crypto.BadPaddingException,
           java.security.SignatureException,
           java.io.IOException
{
    byte[] ibuf = new byte[1024];
    while (dataLen > 0) {
        int max = (int)(dataLen > ibuf.length ? ibuf.length : dataLen);
        int len = in.read(ibuf, 0, max);
        if ( len < 0 ) throw new java.io.IOException("Insufficient data");
        dataLen -= len;
        byte[] obuf = ci.update(ibuf, 0, len);
        if ( obuf != null ) {
            out.write(obuf);
            ver.update(obuf);
        }
    }
    byte[] obuf = ci.doFinal();
    if ( obuf != null ) {
        out.write(obuf);
        ver.update(obuf);
    }
}


    public static void desencriptarFile3(File inputFile, File outputFile,WritableGUI gui) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, SignatureException{
        //Decrypting the AES Key
        
    long dataLen = (inputFile.length())- 256/*AES Key*/- 16/*IV*/- 256/*Signature*/;
                 
    
        //Load the RSA Public Key from File
        
        byte[] bytes = Files.readAllBytes(Paths.get(PATH_CHAVE_PUBLICA));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pub = kf.generatePublic(ks);
        
        //Load the AES Secret Key
        FileInputStream in = new FileInputStream(inputFile);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pub);
        byte[] b = new byte[256];
        in.read(b);
        byte[] keyb = cipher.doFinal(b);
        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        
        //Read the Initialization Vector
        byte[] iv = new byte[128 / 8];
        in.read(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        
        // Decrypting the Message and Verifying the Signature
        Signature ver = Signature.getInstance("SHA256withRSA");
        ver.initVerify(pub); // Using B's public key
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            authFile(ci, ver, in, out, dataLen);
        }
        byte[] s = new byte[256];
        int len = in.read(s);
        if (!ver.verify(s)) {
        try {
            gui.write("Ficheiro corrompido");
            throw new Exception("Signature not valid: ");
        } catch (Exception ex) {
            Logger.getLogger(Encript.class.getName()).log(Level.SEVERE, null, ex);
        }
        } 
        else{
            gui.write("Ficheiro Intacto");
        }
    }
}