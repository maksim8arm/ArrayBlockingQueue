package com.company;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> maxA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> maxB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> maxC = new ArrayBlockingQueue<>(100);

    public static int a = 0;
    public static int b = 0;
    public static int c = 0;
    public static int i = 0;
    public static String numA;
    public static String numB;
    public static String numC;

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[10_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 100_000);
        }

        Runnable putText = () -> {
            for (String text : texts) {
                try {
                    maxA.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    maxB.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    maxC.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable takeA = () -> {
            for (int k = 0; k < 10000; k++) {
                try {
                    String text1 = maxA.take();
                    int j = 0;
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == 'a') j++;
                    }
                    if (j > a) {
                        a = j;
                        numA = text1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable takeB = () -> {
            for (int k = 0; k < 10000; k++) {
                try {
                    String text1 = maxB.take();
                    int j = 0;
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == 'b') j++;
                    }
                    if (j > b) {
                        b = j;
                        numB = text1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable takeC = () -> {
            for (int k = 0; k < 10000; k++) {
                try {
                    String text1 = maxC.take();
                    int j = 0;
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == 'c') j++;
                    }
                    if (j > c) {
                        c = j;
                        numC = text1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread threadPutText = new Thread(putText);
        threadPutText.start();

        Thread threadTakeTextA = new Thread(takeA);
        threadTakeTextA.start();

        Thread threadTakeTextB = new Thread(takeB);
        threadTakeTextB.start();

        Thread threadTakeTextC = new Thread(takeC);
        threadTakeTextC.start();

        threadPutText.join();
        threadTakeTextA.join();
        threadTakeTextB.join();
        threadTakeTextC.join();

        System.out.println("мах(" + a + ") кол-во 'а' содержится в: " + numA);
        System.out.println("мах(" + b + ") кол-во 'b' содержится в: " + numB);
        System.out.println("мах(" + c + ") кол-во 'c' содержится в: " + numC);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
