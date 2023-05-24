public class KalahaGame {
    private int[] board;
    private int currentPlayer;
    private boolean isExtraTurn;

    public KalahaGame() {
        board = new int[14];
        currentPlayer = 1;
        isExtraTurn = false;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) {
                board[i] = 6;
            }
        }
    }

    public void makeMove(int pitIndex) {
        if (isGameOver() || !isValidMove(pitIndex)) {
            return;
        }
        int seeds = board[pitIndex];
        board[pitIndex] = 0;
        int currentIndex = pitIndex;
        while (seeds > 0) {
            // move the current pit index to the next pit, skipping the Kalaha house
            currentIndex = (currentIndex + 1) % 14;
            if ((currentPlayer == 1 && currentIndex == 13) || (currentPlayer == 2 && currentIndex == 6)) {
                continue;
            }
            board[currentIndex]++;
            seeds--;
            if (seeds == 0) {
                // Extra turn for currentPlayer if the last seed lands in their Kalaha house
                // Capture seeds if the last seed lands in an empty pit of the current player
                if (currentPlayer == 1 && currentIndex == 6) {
                    isExtraTurn = true;
                } else if (currentPlayer == 2 && currentIndex == 13) {
                    isExtraTurn = true;
                }
                if (isLastSeedInOwnEmptyPit(currentIndex)) {
                    isExtraTurn = false;
                    captureSeeds(currentIndex);
                }
            }
        }
        // Switch players if isExtraTurn == false
        if (!isExtraTurn) {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }
    }

    private boolean isValidMove(int pitIndex) {
        if (currentPlayer == 1 && pitIndex >= 0 && pitIndex <= 5 && board[pitIndex] != 0) {
            return true;
        } else if (currentPlayer == 2 && pitIndex >= 7 && pitIndex <= 12 && board[pitIndex] != 0) {
            return true;
        }
        return false;
    }

    private boolean isLastSeedInOwnEmptyPit(int pitIndex) {
        return board[pitIndex] == 1 && pitIndex != 6 && pitIndex != 13 && board[12 - pitIndex] != 0;
    }

    private void captureSeeds(int pitIndex) {
        int oppositeIndex = 12 - pitIndex;
        int currentPlayerKalahaIndex = (currentPlayer == 1) ? 6 : 13;
        int capturedSeeds = board[oppositeIndex] + 1;

        board[currentPlayerKalahaIndex] += capturedSeeds;
        board[oppositeIndex] = 0;
        board[pitIndex] = 0;
    }

    public int[] getBoard() {
        return board.clone();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getExtraTurn() {
        return isExtraTurn;
    }

    public boolean isGameOver() {
        boolean player1PitsEmpty = true;
        boolean player2PitsEmpty = true;

        for (int i = 0; i < 6; i++) {
            if (board[i] != 0) {
                player1PitsEmpty = false;
                break;
            }
        }

        for (int i = 7; i < 13; i++) {
            if (board[i] != 0) {
                player2PitsEmpty = false;
                break;
            }
        }

        return player1PitsEmpty || player2PitsEmpty;
    }
}