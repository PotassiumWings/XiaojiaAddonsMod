package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.TreeMap;

public class Patterns {
    public Pattern[] patterns = new Pattern[32];
    public static void load(String res) {
        JsonParser parser = new JsonParser();
        // res = {"patterns": [{pattern}, ]}
        JsonObject fullJson = parser.parse(res).getAsJsonObject();
        JsonArray uuidsArray = fullJson.getAsJsonArray("patterns");
        for (JsonElement jsonElement : uuidsArray) {
            JsonObject patternObj = jsonElement.getAsJsonObject();
            // pattern = "board": ["1 1 c", "1 2 ccl", ], "flag": "11", "time": "66", "operations": [{operation}, ]
            JsonArray board = patternObj.getAsJsonArray("board");
            EnumState[][] boardState = new EnumState[WaterUtils.height][WaterUtils.width];
            for (JsonElement boardElement : board) {
                String str = boardElement.getAsString();

            }
        }
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
    }

    public static class Operation {
        public TreeMap<Integer, EnumOperation> operations;

        public Operation() {
            operations = new TreeMap<>();
        }

        public Operation(TreeMap<Integer, EnumOperation> op) {
            this.operations = op;
        }
    }
}
