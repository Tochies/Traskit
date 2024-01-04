package com.tochie.Traskit.utils;

import com.tochie.Traskit.enums.Constants;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class GeneratorUtil {



    public String generateUniqueString() {
        return generateRandomString() + getCurrentTimestamp();

    }

    private String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TASK_REFERENCE_TIMESTAMP_FORMAT);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    private String generateRandomString() {
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < Constants.TASK_REFERENCE_RANDOM_STRING_SIZE; i++) {
            int index = random.nextInt(Constants.TASK_REFERENCE_CHARACTERS.length());
            randomString.append(Constants.TASK_REFERENCE_CHARACTERS.charAt(index));
        }

        return randomString.toString();
    }



}
