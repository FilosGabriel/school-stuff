package com.company;

import java.util.HashMap;
import java.util.function.Function;

import static java.lang.System.out;

public class Main {

    private static void dot(int time) throws InterruptedException {
        out.println("dot1");
        Thread.sleep(time * 1000);
        out.println("dot2");

    }

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        HashMap<Character, Integer> actions = new HashMap<>();
        actions.put('*', 1);
        actions.put(' ', 1);
        actions.put('-', 3);
        var encoder = new MorseCode();
        var a = encoder.encodeToMorse("ana are mere");
out.println(a);
//        for (var code : a.toCharArray())
//            dot(actions.get(code));
    }
}
