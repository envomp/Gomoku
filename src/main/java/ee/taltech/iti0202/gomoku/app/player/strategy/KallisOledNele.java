package ee.taltech.iti0202.gomoku.app.player.strategy;

import ee.taltech.iti0202.gomoku.app.board.*;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ee.taltech.iti0202.gomoku.app.board.CustomBoard.*;

public class KallisOledNele extends Strategy { // <3
    private static final int winScore = 1000000000;
    private static int depth;
    private CustomBoard board;
    private static int evaluationCount;

    @Override
    public ILocation getMove(IBoard board, boolean isWhite) {
        depth = board.getWidth() * board.getHeight() <= 100 ? 4 : 3;

        int count = Math.toIntExact(Arrays.stream(board.getMatrix())
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .count());

        int[][] matrix = new int[board.getHeight()][board.getWidth()];

        if (count == 0) {
            return new Location(board.getHeight() / 2, board.getHeight() / 2);
        } else {
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getHeight(); x++) {
                    if (board.getMatrix()[y][x] == null) {
                        matrix[y][x] = EMPTY;
                    } else if (board.getMatrix()[y][x] == Stone.WHITE) {
                        matrix[y][x] = PLAYER_WHITE;
                    } else {
                        matrix[y][x] = PLAYER_BLACK;
                    }
                }
            }
        }

        this.board = new CustomBoard(matrix);
        evaluationCount = 0;
        return Objects.requireNonNull(calculateNextMove()).getLocation();
    }

    private static double evaluateBoardForWhite(CustomBoard board, boolean blacksTurn) {
        evaluationCount++;

        double blackScore = board.getScore(true, blacksTurn);
        double whiteScore = board.getScore(false, blacksTurn);

        if (blackScore == 0) blackScore = 1.0;

        return whiteScore / blackScore;
    }

    private WeightedLocation calculateNextMove() {
        WeightedLocation move = new WeightedLocation();
        long startTime = System.currentTimeMillis();
        WeightedLocation bestMove = searchWinningMove(board);
        if (bestMove != null) {
            move.y = bestMove.y;
            move.x = bestMove.x;

        } else {
            bestMove = miniMax(depth, board, true, -1.0, winScore, startTime);
            if (bestMove.y == null) {
                move = null;
            } else {
                move.y = bestMove.y;
                move.x = bestMove.x;
            }
        }
        System.out.println("Cases calculated: " + evaluationCount + " Calculation time: " + (System.currentTimeMillis() - startTime) + " ms");
        evaluationCount = 0;
        return move;
    }

    private static WeightedLocation miniMax(int depth, CustomBoard board, boolean max, double alpha, double beta, long startTime) {

        if (depth == 0 || System.currentTimeMillis() - startTime > 1000 && depth == 1) {
            return new WeightedLocationBuilder()
                    .setWeight(evaluateBoardForWhite(board, !max))
                    .create();
        }

        List<WeightedLocation> allPossibleMoves = board.generateMoves();
        if (allPossibleMoves.size() == 0) {

            return new WeightedLocationBuilder()
                    .setWeight(evaluateBoardForWhite(board, !max))
                    .create();
        }

        WeightedLocation bestMove = new WeightedLocation();

        if (max) {
            bestMove.weight = -1.0;
            for (WeightedLocation move : allPossibleMoves) {
				CustomBoard dummyBoard = new CustomBoard(board.boardMatrix);
                dummyBoard.addStone(move.y, move.x, false);

                WeightedLocation tempMove = miniMax(depth - 1, dummyBoard, false, alpha, beta, startTime);
                if (tempMove.weight > alpha) {
                    alpha = tempMove.weight;
                }

                if (tempMove.weight >= beta) {
                    return tempMove;
                }
                if (tempMove.weight > bestMove.weight) {
                    bestMove = tempMove;
                    bestMove.y = move.y;
                    bestMove.x = move.x;
                }
            }

        } else {
            bestMove.weight = (double) winScore;
            bestMove.y = allPossibleMoves.get(0).y;
            bestMove.x = allPossibleMoves.get(0).x;
            for (WeightedLocation move : allPossibleMoves) {
				CustomBoard dummyBoard = new CustomBoard(board.boardMatrix);
                dummyBoard.addStone(move.y, move.x, true);

                WeightedLocation tempMove = miniMax(depth - 1, dummyBoard, true, alpha, beta, startTime);

                if (tempMove.weight < beta) {
                    beta = tempMove.weight;
                }

                if (tempMove.weight <= alpha) {
                    return tempMove;
                }
                if (tempMove.weight < bestMove.weight) {
                    bestMove = tempMove;
                    bestMove.y = move.y;
                    bestMove.x = move.x;
                }
            }
        }
        return bestMove;
    }

    private static WeightedLocation searchWinningMove(CustomBoard board) {
        List<WeightedLocation> allPossibleMoves = board.generateMoves();
        WeightedLocation winningMove = new WeightedLocation();

        for (WeightedLocation move : allPossibleMoves) {
            evaluationCount++;
			CustomBoard dummyBoard = new CustomBoard(board.boardMatrix);
            dummyBoard.addStone(move.y, move.x, false);

            if (dummyBoard.getScore(false, false) >= winScore) {
                winningMove.y = move.y;
                winningMove.x = move.x;
                return winningMove;
            }
        }
        return null;
    }

    @Override
    public void onGameOver() {

    }

    @Override
    public String getName() {
        return "I love you so much, Nele <3";
    }
}
