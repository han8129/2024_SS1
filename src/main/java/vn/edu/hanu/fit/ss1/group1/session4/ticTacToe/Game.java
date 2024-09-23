package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.core.Variable;

import java.util.*;

public class Game implements Variable {
    public final static int emptyCell = 0;
    public final static int xCell = 1;
    public final static int oCell = 2;
    public final static int winCount = 3;
    private int boardSize;

    private List<int[]> moves;  // Stores the game states history
    private int winningPlayer;

    public static Game getGame(Variable variable) {
        Game game = new Game();
        game.decode(variable.encode());
        return game;
    }
    public List<int[]> getMoves() {
        return new ArrayList<>(this.moves);
    };

    public int getWinningPlayer() {
        return this.winningPlayer;
    };

    public Game(int boardSize) {
        this.moves = new ArrayList<>();
        this.winningPlayer = emptyCell;  // Save initial empty board state
        this.boardSize = boardSize;
    }

    @Override
    public Variable copy() {
        Game copy = new Game();
        copy.boardSize = this.boardSize;
        copy.moves = new ArrayList<>(this.moves);  // Copy game states
        copy.winningPlayer = this.winningPlayer;
        return copy;
    }

    @Override
    public void randomize() {
        Random random = new Random();
        // slice at even step to ensure player sequence
        int sliceAt = (moves.isEmpty())
            ? 0
            : random.nextInt( moves.size() / 2) * 2;

        List<int[]> newMoves = new ArrayList<>();
        for (int i = 0; i < sliceAt; i++)  {
            newMoves.add(moves.get(i));
        }
        this.moves = newMoves;
        // resume unfinished game after sliced
        run();
    }

    // recursive play random move until draw or any player won
    public void run() {
        int[] lastMove = (moves.isEmpty())
            ? new int[boardSize * boardSize]
            : moves.getLast();

        if (isDraw(lastMove)) {
            this.winningPlayer = emptyCell;
            return;
        }

        Optional<Integer> maybeCell = getRandomEmptyCell(lastMove);
        if (maybeCell.isPresent()) {
            int[] xMove = lastMove.clone();
            xMove[maybeCell.get()] = xCell;
            moves.add(xMove);

            if (isWin(xMove, xCell, maybeCell.get())) {
                this.winningPlayer = xCell;
                return;
            }

            maybeCell = getRandomEmptyCell(xMove);
            if (maybeCell.isPresent()) {
                int[] oMove = xMove.clone();
                oMove[maybeCell.get()] = oCell;
                moves.add(oMove);

                if (isWin(oMove, oCell, maybeCell.get())) {
                    this.winningPlayer = oCell;
                    return;
                }
            }
        }

        run();
    }

    @Override
    public String encode() {
        StringBuilder encodedString = new StringBuilder();
        String winningSymbol = switch (winningPlayer) {
            case 1 -> "X";
            case 2 -> "O";
            default -> "DRAW";
        };
        encodedString.append(String.format("%s [\n", winningSymbol));
        for (int[] board : this.moves) {
            encodedString.append("\t[");
            for (int cell : board) {
                int symbol = switch (cell) {
                    case 0 -> emptyCell;
                    case 1 -> xCell;
                    default -> oCell;
                };
                encodedString.append(String.format("%s, ", symbol));
            }
            encodedString.append("],\n");
        }

        encodedString.append("]");
        return encodedString.toString();  // Return the encoded board as a string
    }

    @Override
    public void decode(String encodedValue) {

        String[] lines = encodedValue.split("\n");

        {
            // game snapshot start at 2nd line
            String line = lines[1];
            line = line.replaceAll("\t\\[", "");
            line = line.replaceAll("],", "");

            this.boardSize = line.split(",\s").length / 2;
        }

        List<int[]> newMoves = new ArrayList<>();
        for (int i = 1; i < lines.length - 1; i++) {
            // \t[0, 1, 2, 3],\n
            String line = lines[i];
            line = line.replaceAll("\t\\[", "");
            line = line.replaceAll("],", "");

            int[] move = new int[boardSize * boardSize];
            String[] cells = line.split(",\s");
            for (int cellIdx = 0; cellIdx < cells.length; cellIdx++) {
                move[cellIdx] = (Integer.parseInt(cells[cellIdx]));
            }

            newMoves.add(move);
        }
        this.moves = newMoves;
        this.winningPlayer =
            switch (lines[0].split("\s")[0]){
                case "X" -> xCell;
                case "O" -> oCell;
                default -> emptyCell;
            };
    }

    @Override
    public String toString() {
        return this.encode();
    }

    private boolean isDraw(int[] board) {
        return Arrays.stream(board)
            .noneMatch(cell -> emptyCell == cell);
    }

    private boolean isWin(int[] board, int player, int move) {
        // check W-E
        {
            int matchCount = 0;
            for (int i = move; i < boardSize; i++) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
            }
        }
        // check East - West
        {
            int matchCount = 0;
            for (int i = move; i >= 0; i--) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
            }
        }

        // North - South
        {
            int matchCount = 0;
            for (int i = move; i < boardSize; i+=boardSize) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
            }
        }

        // South - North
        {
            int matchCount = 0;
            for (int i = move; i >= 0; i-=boardSize) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
            }
        }
        {
            // North East
            int matchCount = 0;
            for (int i = move; i >=0;) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
                i+= ( 1 - boardSize );
            }

            // South West
            for (int i = move; i < boardSize;) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
                i+= (boardSize - 1) ;
            }
        }
        {
            // North West
            int matchCount = 0;
            for (int i = move; i >= 0;) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
                i-= (boardSize + 1) ;
            }

            // South East
            for (int i = move; i < boardSize;) {
                if (matchCount >= winCount) return true;
                if (player == board[i]) {
                    matchCount++;
                }
                i+= (boardSize + 1);
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    private Game() {};

    private Optional<Integer> getRandomEmptyCell(int[] board) {
        Random random = new Random();

        List<Integer> emptyCells = new ArrayList<>(0);
        for (int i = 0; i < board.length; i++) {
            if (emptyCell == board[i]) {
                emptyCells.add(i);
            }
        }

        return (emptyCells.isEmpty())
            ? Optional.empty()
            : Optional.of(emptyCells.get(random.nextInt(emptyCells.size())));
    }

}
