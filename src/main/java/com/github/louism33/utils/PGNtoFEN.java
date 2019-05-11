package com.github.louism33.utils;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class PGNtoFEN {

    // transform pgn file into one fen per move

    public static void main(String[] args) throws IOException {
        final String dir = System.getProperty("user.dir");
        File file = new File(dir + "/pgnExampleFile.pgn");
        String dest = dir + "/result.fen";

        System.out.println("file at " + file.toString());

        file = new File("/home/louis/IdeaProjects/ChessCore/pgnExampleFile.pgn");
        dest = "/home/louis/IdeaProjects/ChessCore/result.fen";
        
        if (!file.exists()) {
            System.out.println("source file does not exist, creating and exiting");
            file.createNewFile();
            return;
        }
        
        try {
            transformPGNFile(file, dest);
            final List<TexelPosLoader.TexelPos> texelPosList = TexelPosLoader.readFile(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void transformPGNFile(File pgnFile, String destinationUrl) throws Exception {

        try (BufferedReader br = new BufferedReader(new FileReader(pgnFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(destinationUrl))) {
            
            String line;
            StringBuilder pgn = new StringBuilder(512);
            List<String> pgns = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.length() < 1) {
                    final String s = pgn.toString();
                    if (s.length() > 0) {
                        pgns.add(s);
                    }
                    pgn = new StringBuilder(512);
                } else {
                    if (line.charAt(0) != '[') {
                        pgn.append(line);
                        pgn.append(' ');
                    }
                }

            }
            final String pg = pgn.toString();
            if (pg.length() > 0) {
                pgns.add(pg);
            }
            
            for (int p = 0; p < pgns.size(); p++) {
                final String pgn1 = pgns.get(p);
                final PGNParser.TexelObject texelObject = PGNParser.parsePGNForTexel(pgn1);
                final int numberOfBookMoves = texelObject.numberOfBookMoves;
                List<String> s = texelObject.allMoves;

                StringBuilder sb = new StringBuilder(1024);
                
                Chessboard board = new Chessboard();
                // ignore actual checkmate move
                for (int i = 0; i < s.size() - 1; i++) {
                    String move = s.get(i);

                    move = move.trim();

                    int move1 = 0;
                    try {
                        move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
                    } catch (Throwable e) {
                        System.out.println(board);
                        System.out.println(s.get(Math.max(i - 1, 0)));
                        System.out.println(s.get(i));
                        System.out.println(s.get(Math.min(i + 1, s.size())));
                        System.out.println(move);
                        System.out.println(MoveParser.toString(move1));
                        e.printStackTrace();
                    }
                    board.makeMoveAndFlipTurn(move1);

                    if (i >= numberOfBookMoves) {
                        if (texelObject.winner == 1) {
                            sb.append(board.toFenString()).append(" c9 \"1-0\";\n");
                        } else if (texelObject.winner == 0) {
                            sb.append(board.toFenString()).append(" c9 \"0-1\";\n");
                        } else {
                            sb.append(board.toFenString()).append(" c9 \"1/2-1/2\";\n");
                        }
                    }
                }
                bw.write(sb.toString());
                bw.flush();
            }
            
        }

    }
}