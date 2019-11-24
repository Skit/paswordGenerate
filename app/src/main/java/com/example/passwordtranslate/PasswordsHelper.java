package com.example.passwordtranslate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

class PasswordsHelper {

    private String[] russians;
    private String[] english;
    private HashMap<String, String> pair;

    PasswordsHelper(String[] ru, String[] en) {
        if(ru.length != en.length) {
            throw new IllegalArgumentException();
        }

        russians = ru;
        english = en;
        pair = new HashMap<>();
    }

    String convert(CharSequence sequence) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < russians.length; i++) {
           pair.put(russians[i], english[i]);
        }

        for (int c = 0; c < sequence.length(); c++) {
            char s = sequence.charAt(c);
            String key = String.valueOf(s);

            if (pair.containsKey(key))
                result.append(pair.get(key));
            else
                result.append(key);
        }

        return result.toString();
    }

    int getQuality(CharSequence sequence) {
        // String.valueOf(Math.log(x) / Math.log(2.0)) - логорифм по основанию 2
        // String.valueOf(Math.log(10) * charSequence.length() / Math.log(2))
        /*
        Pattern pattern = Pattern.compile("(.)\\1{2,}");
        Matcher matcher = pattern.matcher("AAAqwert4444");
        while(matcher.find()) {
            String matchResult = matcher.group();
            Log.i("TEST", String.valueOf(matchResult.charAt(1)));
        }
        */
        return String.valueOf(sequence).getBytes(StandardCharsets.UTF_8).length;
    }
}
