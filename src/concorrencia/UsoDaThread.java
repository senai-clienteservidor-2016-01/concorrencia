/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concorrencia;

/**
 *
 * @author Francisco
 */
public class UsoDaThread {

    public static void main(String [] args){
        ExemploThread t1 = new ExemploThread();
        t1.start();
        
        ExemploThread t2 = new ExemploThread();
        t2.start();
        
    }
    
}
