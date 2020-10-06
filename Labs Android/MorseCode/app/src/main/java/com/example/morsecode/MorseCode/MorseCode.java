package com.example.morsecode.MorseCode;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Collectors;

public class MorseCode {
    private static Map<String, String> code;
    private static Map<String, String> reverseCode;
    private static String SPACE_LETTER = "_";
    private static String SPACE_WORD = " ";

    public MorseCode() {
        code = new HashMap<>(getCode());
        reverseCode = code
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private static String decodeWord(String word) {
        return Arrays.stream(word.split(SPACE_LETTER))
                .map(l -> reverseCode.get(l))
                .collect(Collectors.joining(""));
    }

    private static String encodeWord(String word) {
        return word.toLowerCase()
                .codePoints()
                .mapToObj(c -> String.valueOf((char) c))
                .map(e -> code.get(e))
                .collect(Collectors.joining(SPACE_LETTER));
    }

    public String decode(String input) {
        return Arrays.stream(input.split(SPACE_WORD))
                .map(MorseCode::decodeWord)
                .collect(Collectors.joining(" "));
    }

    public String encodeToMorse(String input) {
        return Arrays
                .stream(input.split(" "))
                .map(MorseCode::encodeWord)
                .collect(Collectors.joining(SPACE_WORD));
    }

    public String encodeToMorseInternational(String input) {
        return encodeToMorse(input).replaceAll(" ", "       ").replaceAll("_", "   ");
    }

    public String getEncoding(String input) {
        return code.get(input);
    }

    private Map<String, String> getCode() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "*-");
        map.put("b", "-***");
        map.put("c", "-*-*");
        map.put("d", "-**");
        map.put("e", "*");
        map.put("f", "**-*");
        map.put("g", "--*");
        map.put("h", "****");
        map.put("i", "**");
        map.put("j", "*---");
        map.put("k", "-*-");
        map.put("l", "*-**");
        map.put("m", "--");
        map.put("n", "-*");
        map.put("o", "---");
        map.put("p", "*--*");
        map.put("q", "--*-");
        map.put("r", "*-*");
        map.put("s", "***");
        map.put("t", "-");
        map.put("u", "**-");
        map.put("v", "***-");
        map.put("w", "*--");
        map.put("x", "-**-");
        map.put("y", "-*--");
        map.put("z", "--**");
        map.put("0", "-----");
        map.put("1", "*----");
        map.put("2", "**---");
        map.put("3", "***--");
        map.put("4", "****-");
        map.put("5", "*****");
        map.put("6", "-****");
        map.put("7", "--***");
        map.put("8", "---**");
        map.put("9", "----*");
        map.put(" ", SPACE_WORD);
        map.put(",", "--**--");
        map.put(".", "*-*-*-");
        map.put("?", "**--**");
        return map;
    }
}