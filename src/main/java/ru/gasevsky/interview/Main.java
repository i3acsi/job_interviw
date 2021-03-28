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
        List<Integer> nk = getNK(input[0]);
        int n = nk.get(0);
        int k = nk.get(1);

        // k - нужно закрыть, 1 -счетчик для сохранения позиции танции в исходных данных
        final int[] tk = {k, 1};


        Integer dxMax = 0;
        StringJoiner stations = new StringJoiner(" ");

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (String s : input[1].split(" ")) {
            map.compute(Integer.parseInt(s), (key, val) -> {
                int i = tk[1]++;
                if (val != null && tk[0]-- != 0)
                    stations.add(String.valueOf(val));
                return i;
            });
        }

        List<List<Object>> dif = new ArrayList<>();

        Integer k1 = null;
        Integer v1 = null;
        Integer k2 = null;
        Integer v2 = null;
        List<Object> prev = null;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            k2 = entry.getKey();
            v2 = entry.getValue();
            if (k1 != null && v1 != null) {
                int dx = k2 - k1;
                if (dx > dxMax)
                    dxMax = dx;
                List<Object> node = new ArrayList<>();
                node.add(dx);
                node.add(v1);
                node.add(v2);
                node.add(prev);
                dif.add(node);
                if (prev != null)
                    prev.add(node);
                prev = node;
            }
            k1 = k2;
            v1 = v2;
        }

        while (tk[0] > 0) {
            int indexMin = dif.indexOf(Collections.min(dif, Comparator.comparing(l -> (Integer) l.get(0))));
            List<Object> entry = dif.get(indexMin);
            List<Object> left = entry.get(3) == null ? null : (List<Object>) entry.get(3);
            List<Object> right = entry.size() != 5 ? null : entry.get(4) == null ? null : (List<Object>) entry.get(4);
            if ((left == null && right != null) || (left != null && right != null && (int) right.get(0) < (int) left.get(0))) {
                int dx = ((int) entry.get(0)) + ((int) right.get(0));
                int l = (int) entry.get(1);
                int r = (int) right.get(2);
                if (dx > dxMax)
                    dxMax = dx;
                stations.add(String.valueOf((int) entry.get(2)));
//                entry.set(0, dx);
//                entry.set(2, r);
                right.set(0, dx); //установим у провой записи новые значения
                right.set(1, l);
                right.set(3, entry.get(3)); // правая запись меняет ссылку левой записи
                if (left!=null)
                    left.set(4, right);
                dif.remove(indexMin);
                tk[0]--;
            } else if ((right == null && left != null) || (left != null && right != null && (int) left.get(0) < (int) right.get(0))) {
                int dx = ((int) entry.get(0)) + ((int) left.get(0));
                int l = (int) left.get(1);
                int r = (int) entry.get(2);
                stations.add(String.valueOf((int) entry.get(1)));
//                entry.set(0, dx);
//                entry.set(2, r);
                left.set(0, dx);
                left.set(2, r);
                left.set(4, right);
                if (right!=null)
                    right.set(3, left);
                dif.remove(indexMin);
                tk[0]--;
            }
        }
        System.out.println(stations.toString());


        return new String[]{String.valueOf(dxMax), stations.toString()};
    }


    private List<Integer> getNK(String firstString) {
        String[] nk = firstString.split(" ");
        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        return List.of(n, k);
    }

    private List<Integer> getStations(String secondString) {
        List<Integer> result = new ArrayList<>();
        for (String s : secondString.split(" ")) {
            result.add(Integer.parseInt(s.trim()));
        }
        return result;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.processInput(new String[]{"7 4", "6 40 80 12 60 70 80"});


    }
}

//        List<List<Integer>> dif = new ArrayList<>();
//
//        Integer k1 = null;
//        Integer v1 = null;
//        Integer k2 = null;
//        Integer v2 = null;
//
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            k2 = entry.getKey();
//            v2 = entry.getValue();
//            if (k1 != null && v1 != null) {
//                int dx = k2 - k1;
//                if (dx> dxMax)
//                    dxMax = dx;
//                dif.add(List.of(dx, v2, v1));
//            }
//            k1 = k2;
//            v1 = v2;
//        }
//
//        dif.forEach(entry -> System.out.println(entry.get(0) + " " + entry.get(1) + " " + entry.get(2)));

//        dif.sort(Comparator.comparing(l -> l.get(0)));

//        int indexMin = dif.indexOf(Collections.min(dif, Comparator.comparing(l -> l.get(0))));
//        int indexL = indexMin - 1;
//        int indexR = indexMin + 1;
//        if (indexL < 0) {
//            int dx = dif.get(indexMin).get(0) + dif.get(indexR).get(0);
//        } else if (indexR > dif.size() - 1) {
//
//        }


//        TreeMap<Integer, Integer> dif = new TreeMap<>();
//        List<Integer> dif = new ArrayList<>();
//
//        Collection<Integer> c = map.keySet();
//        Integer i1 = null;
//        Integer i2 = null;
//        for (Integer integer : c) {
//            i2 = integer;
//            if (i1!=null){
//                dif.add(i2-i1);
//            }
//            i1 = i2;
//        }
//        dif.forEach(System.out::println);

//        if (n >= 3 && n <= 100000 & k >= 1 && n - 2 >= k && stations.siz
//        e() == n) {
//        } else {
//            throw new RuntimeException("wrong data");
//        }

//            dif.forEach(entry -> System.out.println(
//                    entry.get(0) + " " + entry.get(1) + " " + entry.get(2) + " " +
//                            (entry.get(3) == null ? null : ((List<Object>) entry.get(3)).get(0)) + " " +
//                            (entry.size() != 5 ? null : entry.get(4) == null ? null : ((List<Object>) entry.get(4)).get(0))
//
//            ));

