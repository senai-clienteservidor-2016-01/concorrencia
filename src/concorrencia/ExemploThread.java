/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concorrencia;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class ExemploThread extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            System.out.printf("%d - %d\n", getId(), i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExemploThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
