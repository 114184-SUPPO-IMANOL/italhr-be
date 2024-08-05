package org.apiitalhrbe.utils;

import java.util.Random;

public class TokenGenerator {

    private static final int DEFAULT_MAX_VALUE = 900000;

    private static final int DEFAULT_MIN_VALUE = 100000;

    public static String generateToken() {
        Random random = new Random();

        int randomNumber = random.nextInt(DEFAULT_MAX_VALUE - DEFAULT_MIN_VALUE + 1) + DEFAULT_MIN_VALUE;

        return String.format("%06d", randomNumber);
    }
}
