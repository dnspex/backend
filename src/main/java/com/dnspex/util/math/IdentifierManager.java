package com.dnspex.util.math;

import java.util.Random;

public class IdentifierManager {
    public static String generate(String name) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefhiklmnopqrstuvwxz0123456789";
        Random random = new Random();

        StringBuilder code = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = random.nextInt(base.length());
            char randomChar = base.charAt(index);
            code.append(randomChar);
        }

        return name + "_" + code;
    }
}
