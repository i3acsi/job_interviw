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

        TreeMap<Integer, LinkedList<Object[]>> dif = new TreeMap<>();

        Object[] left = null;
        Integer k1 = null, v1 = null, k2, v2;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            k2 = entry.getKey();
            v2 = entry.getValue();
            if (k1 != null && v1 != null) {
                int dx = k2 - k1;
                if (dx > dxMax)
                    dxMax = dx;
                Object[] node = new Object[5];
                node[0] = dx;
                node[1] = v1;
                node[2] = v2;
                node[3] = left;
                node[4] = null;
                if (left != null)
                    left[4] = node;
                left = node;
                dif.compute(dx, (key, val) -> {
                    if (val == null)
                        val = new LinkedList<>();
                    val.add(node);
                    return val;
                });
            }
            k1 = k2;
            v1 = v2;
        }

        while (nk[0] > 0) {
            LinkedList<Object[]> minDxEntry = dif.entrySet().stream().findFirst().get().getValue();
            Object[] entry = minDxEntry.removeFirst();
            if (minDxEntry.isEmpty())
                dif.remove((int) entry[0]);
            Object[] leftEntry = entry[3] == null ? null : (Object[]) entry[3];
            Object[] rightEntry = entry[4] == null ? null : (Object[]) entry[4];
            if ((leftEntry == null && rightEntry != null) || (leftEntry != null && rightEntry != null && (int) rightEntry[0] < (int) leftEntry[0])) {
                LinkedList<Object[]> list = dif.get((int) rightEntry[0]);
                list.remove(rightEntry);
                if (list.isEmpty())
                    dif.remove((int) rightEntry[0]);

                int dx = ((int) entry[0]) + ((int) rightEntry[0]);
                if (dx > dxMax)
                    dxMax = dx;
                Object[] node = new Object[5]; // создаем новую запись
                node[0] = dx;
                node[1] = entry[1];
                node[2] = rightEntry[2];
                node[3] = entry[3];
                node[4] = rightEntry[4];

                if (entry[3] != null) { // переписываем ссылки слева и справа на новую ноду
                    ((Object[]) entry[3])[4] = node;
                }
                if (rightEntry[4] != null) { // переписываем ссылки слева и справа на новую ноду
                    ((Object[]) rightEntry[4])[3] = node;
                }


                dif.compute(dx, (key, val) -> {
                    if (val == null)
                        val = new LinkedList<>();
                    val.add(node);
                    return val;
                });

                stations.add(String.valueOf((int) entry[2]));
                nk[0]--;
            } else if ((rightEntry == null && leftEntry != null) || (leftEntry != null && rightEntry != null && (int) leftEntry[0] < (int) rightEntry[0])) {
                LinkedList<Object[]> list = dif.get((int) leftEntry[0]);
                list.remove(leftEntry);
                if (list.isEmpty())
                    dif.remove((int) leftEntry[0]);

                int dx = ((int) entry[0]) + ((int) leftEntry[0]);
                if (dx > dxMax)
                    dxMax = dx;
                Object[] node = new Object[5]; // создаем новую запись
                node[0] = dx;
                node[1] = leftEntry[1];
                node[2] = entry[2];
                node[3] = leftEntry[3];
                node[4] = entry[4];

                if (entry[4] != null) { // переписываем ссылки слева и справа на новую ноду
                    ((Object[]) entry[4])[3] = node;
                }
                if (leftEntry[3] != null) { // переписываем ссылки слева и справа на новую ноду
                    ((Object[]) leftEntry[3])[4] = node;
                }

                dif.compute(dx, (key, val) -> {
                    if (val == null)
                        val = new LinkedList<>();
                    val.add(node);
                    return val;
                });

                stations.add(String.valueOf((int) entry[1]));
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

    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
        map.put(4, "d");

//        System.out.println(map.entrySet().iterator().next().getKey());
        Map.Entry entry = map.entrySet().iterator().next();
        System.out.println(entry.getKey() + " " + entry.getValue());
        map.remove(entry.getKey());
        map.put(5, "e");
        entry = map.entrySet().iterator().next();
        System.out.println(entry.getKey() + " " + entry.getValue());
    }
}