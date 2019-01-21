package bsi.mpoo.istock.services;

import java.util.Random;

public class RandomPassword {
    public static String generate(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
