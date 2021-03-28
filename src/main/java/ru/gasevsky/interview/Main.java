package ru.gasevsky.interview;

import java.util.*;

/**
 * Входные данные:
 * Первая строка содежит два числа:
 * n - общее количество заправочных станций
 * k - количество станций, который должны прекратить работу
 * Вторая строка содержит последовательность p1, p2 ...pn (0<= pi <= 10^9).
 * Где pi - координата i-й станции. Возможно что есть станции с одинаковой координатой.
 * Порядок станций в входных данных - произвольный.
 * ##################################################
 * Выходные данные:
 * Первая строка:
 * Максимольно возможное расстояние между соседними станциями.
 * Вторая строка:
 * k целых чисел - номера станций, которые будут остановлены.
 * Номера соответсвуют положению во входной строке
 * Если ответов несколько - можно вывести любой.
 */

public class Main {
    private final String delimiter;

    public Main(String delimiter) {
        this.delimiter = delimiter;
    }

    public Main() {
        this.delimiter = " ";
    }

    public String[] processInput(String[] input) {
        final int[] nk = {getNK(input[0]).get(1), 1};

        int dxMax = 0;
        StringJoiner stations = new StringJoiner(" ");

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (String s : input[1].split(" ")) {
            map.compute(Integer.parseInt(s), (key, val) -> {
                int i = nk[1]++;
                if (val != null && nk[0]-- != 0)
                    stations.add(String.valueOf(val));
                return i;
            });
        }

        LinkedList<List<Integer>> dif = new LinkedList<>();

        Integer k1 = null, v1 = null, k2, v2;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            k2 = entry.getKey();
            v2 = entry.getValue();
            if (k1 != null && v1 != null) {
                int dx = k2 - k1;
                if (dx > dxMax)
                    dxMax = dx;
                List<Integer> node = new ArrayList<>();
                node.add(dx);
                node.add(v1);
                node.add(v2);
                dif.add(node);
            }
            k1 = k2;
            v1 = v2;
        }

        while (nk[0] > 0) {
            int indexMin = dif.indexOf(Collections.min(dif, Comparator.comparing(l -> (Integer) l.get(0))));
            List<Integer> entry = dif.get(indexMin);
            List<Integer> left = indexMin == 0 ? null : dif.get(indexMin - 1);
            List<Integer> right = indexMin == dif.size() - 1 ? null : dif.get(indexMin + 1);
            if ((left == null && right != null) || (left != null && right != null && right.get(0) < left.get(0))) {
                int dx = (entry.get(0)) + (right.get(0));
                int l = entry.get(1);
                if (dx > dxMax)
                    dxMax = dx;
                stations.add(String.valueOf((int) entry.get(2)));
                right.set(0, dx);
                right.set(1, l);
                dif.remove(indexMin);
                nk[0]--;
            } else if ((right == null && left != null) || (left != null && right != null && left.get(0) < right.get(0))) {
                int dx = (entry.get(0)) + (left.get(0));
                int r = entry.get(2);
                if (dx > dxMax)
                    dxMax = dx;
                stations.add(String.valueOf((int) entry.get(1)));
                left.set(0, dx);
                left.set(2, r);
                dif.remove(indexMin);
                nk[0]--;
            }
        }
        return new String[]{String.valueOf(dxMax), stations.toString()};
    }

    private List<Integer> getNK(String firstString) {
        String[] nk = firstString.split(" ");
        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        return List.of(n, k);
    }
}