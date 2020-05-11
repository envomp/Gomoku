package ee.taltech.iti0202.gomoku.app.board;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CustomBoard {
	public static final int PLAYER_BLACK = 1;
	public static final int PLAYER_WHITE = -1;
	public static final int EMPTY = 0;
	private int height;
	private int width;

	public int[][] boardMatrix;

	public CustomBoard(int[][] matrix) {
		this.height = matrix.length;
		this.width = matrix[0].length;

		boardMatrix = new int[this.height][this.width];
		IntStream.range(0, matrix.length).forEach(i -> System.arraycopy(matrix[i], 0, boardMatrix[i], 0, matrix.length));
	}

	public void addStone(int y, int x, boolean black) {
		boardMatrix[y][x] = black ? PLAYER_BLACK : PLAYER_WHITE;
	}

	public ArrayList<WeightedLocation> generateMoves() {
		ArrayList<WeightedLocation> moveList = new ArrayList<>();

		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {

				if (boardMatrix[y][x] != EMPTY) {
					continue;
				}
				if (y > 0) {
					if (x > 0 && (boardMatrix[y - 1][x - 1] != EMPTY || boardMatrix[y][x - 1] != EMPTY)) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
						continue;
					}
					if (x < this.width - 1 && (boardMatrix[y - 1][x + 1] != EMPTY || boardMatrix[y][x + 1] != EMPTY)) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
						continue;
					}
					if (boardMatrix[y - 1][x] != EMPTY) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
					}
				}
				if (y < this.height - 1) {
					if (x > 0 && (boardMatrix[y + 1][x - 1] != EMPTY || boardMatrix[y][x - 1] != EMPTY)) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
						continue;
					}
					if (x < this.width - 1 && (boardMatrix[y + 1][x + 1] != EMPTY || boardMatrix[y][x + 1] != EMPTY)) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
						continue;
					}
					if (boardMatrix[y + 1][x] != EMPTY) {
						moveList.add(new WeightedLocationBuilder().setY(y).setX(x).create());
					}
				}
			}
		}
		return moveList;
	}

	/**
	 * @return returns an integer in range of Integer.MIN_VALUE to Integer.MAX_VALUE
	 */
	public int getScore(boolean forBlack, boolean blacksTurn) {
		return evaluateHorizontal(forBlack, blacksTurn)
				+ evaluateVertical(forBlack, blacksTurn)
				+ evaluateDiagonal(forBlack, blacksTurn);
	}

	private int evaluateHorizontal(boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		for (int[] matrix : boardMatrix) {
			for (int i : matrix) {
				if (i == (forBlack ? PLAYER_BLACK : PLAYER_WHITE)) {
					consecutive++;
				} else {
					if (i == EMPTY) {
						if (consecutive > 0) {
							blocks--;
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 1;
					} else {
						if (consecutive > 0) {
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 2;
					}
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
			}
			consecutive = 0;
			blocks = 2;
		}
		return score;
	}

	private int evaluateVertical(boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		for (int j = 0; j < boardMatrix[0].length; j++) {
			for (int[] matrix : boardMatrix) {
				if (matrix[j] != (forBlack ? PLAYER_BLACK : PLAYER_WHITE)) {
					if (matrix[j] == EMPTY) {
						if (consecutive > 0) {
							blocks--;
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 1;
					} else {
						if (consecutive > 0) {
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 2;
					}
				} else {
					consecutive++;
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
			}
			consecutive = 0;
			blocks = 2;
		}
		return score;
	}

	private int evaluateDiagonal(boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;
		// From bottom-left to top-right diagonally
		for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
			int iStart = Math.max(0, k - boardMatrix.length + 1);
			int iEnd = Math.min(boardMatrix.length - 1, k);
			for (int i = iStart; i <= iEnd; ++i) {
				int j = k - i;
				if (boardMatrix[i][j] == (forBlack ? PLAYER_BLACK : PLAYER_WHITE)) {
					consecutive++;
				} else {
					if (boardMatrix[i][j] == EMPTY) {
						if (consecutive > 0) {
							blocks--;
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 1;
					} else {
						if (consecutive > 0) {
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 2;
					}
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
			}
			consecutive = 0;
			blocks = 2;
		}

		for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
			int iStart = Math.max(0, k);
			int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
			for (int i = iStart; i <= iEnd; ++i) {
				int j = i - k;
				if (boardMatrix[i][j] == (forBlack ? PLAYER_BLACK : PLAYER_WHITE)) {
					consecutive++;
				} else {
					if (boardMatrix[i][j] == EMPTY) {
						if (consecutive > 0) {
							blocks--;
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 1;
					} else {
						if (consecutive > 0) {
							score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
							consecutive = 0;
						}
						blocks = 2;
					}
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
			}
			consecutive = 0;
			blocks = 2;
		}
		return score;
	}

	private int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
		final int winGuarantee = 1000000;
		if (blocks == 2 && count < 5) return 0;
		switch (count) {
			case 5:
				return 1000000000;

			case 4:
				if (currentTurn) return winGuarantee;
				else {
					if (blocks == 0) return winGuarantee / 4;
					else return 200;
				}

			case 3:
				if (blocks == 0) {
					if (currentTurn) return 50000;
					else return 200;
				} else {
					if (currentTurn) return 10;
					else return 5;
				}

			case 2:
				if (blocks == 0) {
					if (currentTurn) return 7;
					else return 5;
				} else {
					return 3;
				}

			case 1:
				return 1;
		}
		return 2000000000;
	}
}