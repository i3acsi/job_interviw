package ru.gasevsky.interview;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MainTest {
    private final Main main = new Main();
    private final String delimiter = " ";

    @Test
    public void testInputData() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(3);
        test(new String[]{"4 1", "40 80 50 60"}, 20, list1);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(4);
        list1.add(5);
        test(new String[]{"5 3", "12 96 6 34 73"}, 90, list1);

        list1 = new ArrayList<>();
        list1.add(1);
        test(new String[]{"3 1", "2 2 2"}, 0, list1);
    }

    private void test(String[] input, Integer expected1, List<Integer> expected2) {
        String[] result = main.processInput(input);
        assert result != null;
        Integer first = Integer.parseInt(result[0].trim());

        assertEquals(first, expected1);

        List<Integer> sResult = new ArrayList<Integer>();

        for (String s : result[1].split(delimiter)) {
            sResult.add(Integer.parseInt(s.trim()));
        }
        Collections.sort(sResult);
        Collections.sort(expected2);
        assertEquals(expected2, sResult);
    }
}