package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class MorseCode {
    private static Map<String, String> code;
    private static Map<String, String> reverseCode;
    private static String SPACE_LETTER = "   ";
    private static String SPACE_WORD = "       ";

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

    private Map<String, String> getCode() {
        return Map.ofEntries(
                Map.entry("a", "*-"),
                Map.entry("b", "-***"),
                Map.entry("c", "-*-*"),
                Map.entry("d", "-**"),
                Map.entry("e", "*"),
                Map.entry("f", "**-*"),
                Map.entry("g", "--*"),
                Map.entry("h", "****"),
                Map.entry("i", "**"),
                Map.entry("j", "*---"),
                Map.entry("k", "-*-"),
                Map.entry("l", "*-**"),
                Map.entry("m", "--"),
                Map.entry("n", "-*"),
                Map.entry("o", "---"),
                Map.entry("p", "*--*"),
                Map.entry("q", "--*-"),
                Map.entry("r", "*-*"),
                Map.entry("s", "***"),
                Map.entry("t", "-"),
                Map.entry("u", "**-"),
                Map.entry("v", "***-"),
                Map.entry("w", "*--"),
                Map.entry("x", "-**-"),
                Map.entry("y", "-*--"),
                Map.entry("z", "--**"),
                Map.entry("0", "-----"),
                Map.entry("1", "*----"),
                Map.entry("2", "**---"),
                Map.entry("3", "***--"),
                Map.entry("4", "****-"),
                Map.entry("5", "*****"),
                Map.entry("6", "-****"),
                Map.entry("7", "--***"),
                Map.entry("8", "---**"),
                Map.entry("9", "----*"),
                Map.entry(" ", SPACE_WORD),
                Map.entry(",","--**--"),
                Map.entry(".","*-*-*-"),
                Map.entry("?","**--**")
                );

    }
}