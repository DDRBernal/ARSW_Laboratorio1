/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {

    public static void main(String a[]){
        /*
        CountThread thread1 = new CountThread();
        thread1.run(0,100);

        CountThread thread2 = new CountThread();
        thread2.run(99,200);

        CountThread thread3 = new CountThread();
        thread3.run(200,300);
        */
        //Asincronica
        CountThread thread = new CountThread(0,100);
        CountThread thread2 = new CountThread(99,200);
        CountThread thread3 = new CountThread(199,300);
        thread.start();
        thread2.start();
        thread3.start();
    }
    
}
