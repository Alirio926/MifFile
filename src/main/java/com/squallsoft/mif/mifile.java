/*
 * Copyright (C) 2020 Alirio Freire
 *
 * Este software é livre: você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 3 da
 * Licença, ou (a seu critério) qualquer versão posterior.
 *
 * Este programa é distribuído na esperança de que possa ser útil,
 * mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
 * a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 * Licença Pública Geral GNU para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto
 * com este programa, Se não, veja <http://www.gnu.org/licenses/>.
 */
package com.squallsoft.mif;
  
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mifile {
    
    public static void main(String[] args) {
        
       if(args.length > 0){
            if(args[0].equalsIgnoreCase("MIF")){
                if(args.length != 5){
                    System.out.println("error: expecting 5 command line arguements\n"
                                      + "1. MIF\n"
                                      + "2. the input file name\n"
                                      + "3. the output file name\n"
                                      + "4. DEPTH\n"
                                      + "5. WIDTH");
                     System.exit(0);
                }           
                String arg[] = {args[1], args[2], args[3], args[4]};
                mif(arg);
            }else if(args[0].equalsIgnoreCase("SPLIT")){
                if(args.length != 4){
                      System.out.println("error: expecting 4 command line arguements\n"
                                  + "1. SPLIT\n"
                                  + "2. the input file name\n"
                                  + "3. the output file name\n"
                                  + "4. size out in bytes");
                }
                String arg[] = {args[1], args[2], args[3]};
                split(arg);
            }
       }
       System.out.println("Error: expecting 5 command line arguements\n"
                                      + "\t1. MIF\n"
                                      + "\t2. the input file name\n"
                                      + "\t3. the output file name\n"
                                      + "\t4. DEPTH\n"
                                      + "\t5. WIDTH");

        System.out.println("Error: expecting 4 command line arguements\n"
                          + "\t1. SPLIT\n"
                          + "\t2. the input file name\n"
                          + "\t3. the output file name\n"
                          + "\t4. size out in bytes");
            
    }
    public static void mif(String[] args){
         try {
            RandomAccessFile  mifFile, srcFile;
            
            File mif = new File(args[1]);
            if(mif.createNewFile()){
                System.out.println("File Created");
            }else{
                System.out.println("File already exists"); 
                mif.delete();
                mif.createNewFile();
            }
            
            mifFile = new RandomAccessFile(mif,"rw");
            srcFile = new RandomAccessFile(new File(args[0]),"r");
            // Head
            int b;
            int size = Integer.decode(args[2]);
            int DEPTH = Integer.decode(args[2]);
            int WIDTH = Integer.decode(args[3]);
            String headMif = String.format(
                    "--The size of memory in words\n"
                    + "DEPTH=%d;\n"
                    +"--The size of data in bits\n"
                    + "WIDTH=%d;\n"
                    +"--The radix for address values\n"
                    + "ADDRESS_RADIX=HEX;\n"
                    +"--The radix for address values\n"
                    + "DATA_RADIX=HEX;\n"
                    +"--Start of (address : data pairs)\n"
                    + "CONTENT BEGIN\n", DEPTH, WIDTH);
            
            System.out.println(headMif);
            mifFile.writeBytes(headMif);
            
            for(int addr=0; addr < size; addr++){
                mifFile.writeBytes(String.format("%02X:%02X;\n", addr, srcFile.read()));
            }
            mifFile.writeBytes("END;");
            
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(mifile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void split(String[] args){
        try {
            RandomAccessFile  dstFile, srcFile;
            File dst = new File(args[1]);
            if(dst.createNewFile()){
                System.out.println("File Created");
            }else{
                System.out.println("File already exists");
                dst.delete();
                dst.createNewFile();
            }   
            dstFile = new RandomAccessFile(dst,"rw");
            srcFile = new RandomAccessFile(new File(args[0]),"r");
            int size = Integer.decode(args[2]);
            int i=0;
            for(;i < size; i++){
                dstFile.write(srcFile.read());
            }
            
            dstFile.close();
            srcFile.close();
            System.out.printf("Gravados: 0x%04X.", i);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(mifile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
            
}