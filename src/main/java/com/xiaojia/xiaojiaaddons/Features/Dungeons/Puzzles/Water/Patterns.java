package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Patterns {
    public static final ArrayList<Pattern> patterns = new ArrayList<>();

    public static void load(String res) {
        JsonParser parser = new JsonParser();
        // res = {"patterns": [{pattern}, ]}
        JsonObject fullJson = parser.parse(res).getAsJsonObject();
        JsonArray uuidsArray = fullJson.getAsJsonArray("patterns");
        for (JsonElement jsonElement : uuidsArray) {
            JsonObject patternObj = jsonElement.getAsJsonObject();
            // pattern = "board": ["1 1 c", "1 2 ccl", ], "ops": [{"flag": 11, "time": 66, "operations": [{operation}, ]}, ]
            JsonArray board = patternObj.getAsJsonArray("board");
            EnumState[][] boardState = new EnumState[WaterUtils.height][WaterUtils.width];
            for (JsonElement boardElement : board) {
                String str = boardElement.getAsString();
                String[] obj = str.split(" ");
                int i = Integer.parseInt(obj[0]);
                int j = Integer.parseInt(obj[1]);
                EnumState state = EnumState.valueOf(obj[2]);
                boardState[i][j] = state;
            }

            JsonArray ops = patternObj.getAsJsonArray("ops");
            for (JsonElement opElement : ops) {
                int flag = opElement.getAsJsonObject().get("flag").getAsInt();
                int time = opElement.getAsJsonObject().get("time").getAsInt();

                JsonArray operationsArr = opElement.getAsJsonObject().getAsJsonArray("operations");
                TreeMap<Integer, EnumOperation> operations = new TreeMap<>();
                for (JsonElement operationElement : operationsArr) {
                    int opTime = operationElement.getAsJsonObject().get("time").getAsInt();
                    EnumOperation operation = EnumOperation.valueOf(operationElement.getAsJsonObject().get("type").getAsString());
                    operations.put(opTime, operation);
                }
                Operation op = new Operation(operations, time);
                Pattern pattern = getPattern(boardState);
                pattern.insert(flag, op);
            }
        }
    }

    public static void printPatterns() {
        String res = "{\"patterns\": [";
        for (Pattern pattern : patterns) {
            String s = "{";
            s += String.format("\"board\": [%s]", getBoardString(pattern.board));
            s += ", \"ops\": [";
            ArrayList<String> flags = new ArrayList<>();
            for (int i = 0; i < 32; i++) {
                if (pattern.operations[i] != null && pattern.operations[i].time < 150) {
                    Operation operation = pattern.operations[i];
                    flags.add(String.format("{\"flag\": %d, \"time\": %d, \"operations\": %s}",
                            i, operation.time, getOperationString(operation.operations)));
                }
            }
            s += flags.stream().reduce("", (a, b) -> a.equals("") ? b : a + ", " + b);
            s += "]";
            s += "}";
            res += s + ",";
        }
        res = res.substring(0, res.length() - 1);
        res += "]}";
        System.out.println(res);
    }

    public static String getPatternString(EnumState[][] board, int flag, int time, TreeMap<Integer, EnumOperation> operations) {
        String s = "{";
        s += String.format("\"board\": [%s]", getBoardString(board));
        s += ", \"ops\": [";
        s += String.format("{\"flag\": %d, \"time\": %d, \"operations\": %s}", flag, time, getOperationString(operations));
        s += "]}";
        return s;
    }

    private static String getOperationString(TreeMap<Integer, EnumOperation> operations) {
        ArrayList<String> res = new ArrayList<>();
        for (Map.Entry<Integer, EnumOperation> operation : operations.entrySet()) {
            res.add(String.format("{\"time\": %d, \"type\": \"%s\"}", operation.getKey(), operation.getValue()));
        }
        return String.format("[%s]", res.stream().reduce("", (a, b) -> a.equals("") ? b : a + ", " + b));
    }

    private static String getBoardString(EnumState[][] board) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < WaterUtils.height; i++)
            for (int j = 0; j < WaterUtils.width; j++)
                res.add(String.format("\"%d %d %s\"", i, j, board[i][j]));
        return res.stream().reduce("", (a, b) -> a.equals("") ? b : a + ", " + b);
    }

    public static Operation getOperation(EnumState[][] board, int flag) {
        for (Pattern pattern : patterns) {
            if (pattern.equals(board)) {
                if (pattern.operations[flag] != null)
                    return pattern.operations[flag];
                return null;
            }
        }
        return null;
    }

    private static Pattern getPattern(EnumState[][] state) {
        for (Pattern pattern : patterns)
            if (pattern.equals(state))
                return pattern;
        Pattern res = new Pattern(state);
        patterns.add(res);
        return res;
    }

    public static class Pattern {
        public EnumState[][] board;
        public Operation[] operations = new Operation[32];

        public Pattern(EnumState[][] board) {
            this.board = board;
        }

        public Pattern(EnumState[][] board, Operation[] operations) {
            this.board = board;
            this.operations = operations;
        }

        public boolean equals(EnumState[][] board) {
            for (int i = 0; i < WaterUtils.height; i++)
                for (int j = 0; j < WaterUtils.width; j++)
                    if (board[i][j] != this.board[i][j]) return false;
            return true;
        }

        public void insert(int flag, Operation operation) {
            if (operations[flag] == null || operations[flag].time >= operation.time) {
                operations[flag] = operation;
            }
        }
    }

    public static class Operation {
        public TreeMap<Integer, EnumOperation> operations;
        public int time;

        public Operation(TreeMap<Integer, EnumOperation> op, int time) {
            this.operations = op;
            this.time = time;
        }

        public Operation() {
            operations = null;
            time = (int) 1e9;
        }
    }
}
