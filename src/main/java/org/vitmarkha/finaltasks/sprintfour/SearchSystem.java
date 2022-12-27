package org.vitmarkha.finaltasks.sprintfour;

import java.io.*;
import java.util.*;

public class SearchSystem {

    private static final Map<String, Map<Integer, Integer>> docs = new HashMap<>();
    private static final Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
    private static final StringBuilder output = new StringBuilder();

    public static void main(String[] args) throws IOException {
        input();
        makeMaxRelevanceLine();
        output();
    }

    private static void makeMaxRelevanceLine() {
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : result.entrySet()) {
            Map<Integer, Integer> map = entry.getValue();

            for (int i = 0; i < 5; i++) {
                if (map.size() == 0)
                    break;

                int keyMaxRelevance = Collections.max(map.keySet(), (x, y) -> {
                    if (map.get(x) - map.get(y) == 0)
                        return y - x;
                    else
                        return map.get(x) - map.get(y);
                });

                output.append(keyMaxRelevance + 1).append(' ');
                map.remove(keyMaxRelevance);
            }
            output.append('\n');
        }
    }

    private static void input() throws IOException {
        final int N;
        final int M;

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            N = Integer.parseInt(reader.readLine());
            parsDocs(N, reader);
            M = Integer.parseInt(reader.readLine());
            parsRequest(M, reader);
        }
    }

    private static void parsDocs(final int N, final BufferedReader reader) throws IOException {
        StringTokenizer tokenizer;

        for (int i = 0; i < N; i++) {
            tokenizer = new StringTokenizer(reader.readLine());

            while (tokenizer.hasMoreTokens())
                putMapByWord(tokenizer.nextToken(), i);
        }
    }

    private static void putMapByWord(String word, int indexDoc) {
        Map<Integer, Integer> map;

        if (!docs.containsKey(word)) {
            map = new HashMap<>();
            map.put(indexDoc, 1);
            docs.put(word, map);
        } else {
            map = docs.get(word);
            if (!map.containsKey(indexDoc))
                map.put(indexDoc, 1);
            else
                map.put(indexDoc, map.get(indexDoc) + 1);
        }
    }

    private static void parsRequest(final int N, final BufferedReader reader) throws IOException {
        StringTokenizer tokenizer;
        Set<String> set;

        for (int indexRequests = 0; indexRequests < N; indexRequests++) {
            tokenizer = new StringTokenizer(reader.readLine());
            set = new HashSet<>();

            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();

                if (!docs.containsKey(word) || set.contains(word))
                    continue;
                if (!result.containsKey(indexRequests)) {
                    Map<Integer, Integer> map = new HashMap<>(docs.get(word));
                    result.put(indexRequests, map);
                }
                else
                    sumMap(indexRequests, word);
                set.add(word);
            }
        }
    }

    private static void sumMap(int indexRequests, String word) {
        for (Map.Entry<Integer, Integer> map : docs.get(word).entrySet())
            result.get(indexRequests).merge(map.getKey(), map.getValue(), Integer::sum);
    }

    private static void output() throws IOException {
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        writer.write(output.toString());
        writer.flush();
    }
}
