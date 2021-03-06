package de.codemakers.bot.supreme.game;

import de.codemakers.bot.supreme.commands.arguments.ArgumentList;
import de.codemakers.bot.supreme.entities.AdvancedEmote;
import de.codemakers.bot.supreme.entities.DefaultMessageEvent;
import de.codemakers.bot.supreme.entities.MessageEvent;
import de.codemakers.bot.supreme.entities.MultiObject;
import de.codemakers.bot.supreme.entities.MultiObjectHolder;
import de.codemakers.bot.supreme.listeners.ReactionListener;
import de.codemakers.bot.supreme.permission.ReactionPermissionFilter;
import de.codemakers.bot.supreme.settings.Config;
import de.codemakers.bot.supreme.util.Emoji;
import de.codemakers.bot.supreme.util.Standard;
import de.codemakers.bot.supreme.util.Util;
import java.awt.Color;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 * TicTacToe
 *
 * @author Panzer1119 &amp; Alien Ideology <alien.ideology at alien.org>
 */
public class TicTacToe extends Game {

    private final Board game = new Board(3, 3);
    private MessageEvent event_started;
    private User challenger;
    private User opponent;
    private String piece;
    private int row;
    private int column;
    private User turn;
    private Message message_header = null;
    private Message message_board = null;

    @Override
    public final boolean startGame(ArgumentList arguments, MessageEvent event) {
        try {
            event_started = event;
            challenger = event.getAuthor();
            opponent = arguments.consumeUserFirst();
            if (opponent == null) {
                event.sendMessage(Standard.STANDARD_MESSAGE_DELETING_DELAY, Standard.getNoMessage(event.getAuthor(), "you have to mention someone, to start a game!").build());
                return false;
            }
            turn = Math.random() >= 0.5 ? challenger : opponent;
            message_header = event.sendAndWaitMessage(Standard.getMessageEmbed(Color.GREEN, null).addField(String.format("%s TicTacToe", Emoji.GAME), String.format("Challenger: %s%nOpponent: %s%nTurn: %s", challenger.getAsMention(), opponent.getAsMention(), turn.getAsMention()), true).build());
            message_board = event.sendAndWaitMessage(game.toString());
            ReactionListener.registerListener(message_board, AdvancedEmote.parse(Emoji.MARK_MULTIPLICATION_SIGN), (reaction, emote, guild, user) -> {
                ReactionListener.unregisterListener(message_board, true);
                endGame(null, new DefaultMessageEvent(event) {
                    @Override
                    public final User getAuthor() {
                        return user;
                    }
                });
            }, null, ReactionPermissionFilter.createUsersFilter(opponent, challenger), true);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public final boolean endGame(ArgumentList arguments, MessageEvent event) {
        try {
            if (event.getAuthor() == challenger || event.getAuthor() == opponent) {
                event.sendMessage(Standard.getMessageEmbed(Color.GREEN, null).setTitle(String.format("%s TicTacToe", Emoji.GAME), null).setDescription(String.format("Challenger: %s%nOpponent: %s", challenger.getAsMention(), opponent.getAsMention())).setFooter(String.format("%s ended the game.", event.getAuthor().getName()), null).build());
                deleteMessages();
                game.clearBoard();
                final MultiObjectHolder holder = MultiObjectHolder.of(event.getGuild(), event.getAuthor(), event.getTextChannel());
                final MultiObject<TicTacToe> multiObject = MultiObject.getFirstMultiObject(TicTacToe.class.getName(), holder);
                if (multiObject != null) {
                    multiObject.unregister();
                }
                return true;
            } else {
                event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s do not interfere the game!", Emoji.WARNING, event.getAuthor().getAsMention());
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public final boolean sendInput(ArgumentList arguments, MessageEvent event) {
        if (arguments == null || event == null) {
            return false;
        }
        try {
            switch (arguments.consumeFirst()) {
                case "1":
                    row = 0;
                    column = 0;
                    break;
                case "2":
                    row = 0;
                    column = 1;
                    break;
                case "3":
                    row = 0;
                    column = 2;
                    break;
                case "4":
                    row = 1;
                    column = 0;
                    break;
                case "5":
                    row = 1;
                    column = 1;
                    break;
                case "6":
                    row = 1;
                    column = 2;
                    break;
                case "7":
                    row = 2;
                    column = 0;
                    break;
                case "8":
                    row = 2;
                    column = 1;
                    break;
                case "9":
                    row = 2;
                    column = 2;
                    break;
                default:
                    throw new StringIndexOutOfBoundsException();
            }
            if (event.getAuthor() == opponent) {
                piece = "O";
            } else if (event.getAuthor() == challenger) {
                piece = "X";
            }
            if (event.getAuthor() == challenger || event.getAuthor() == opponent) {
                if (event.getAuthor() != turn) {
                    event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s it's not your turn yet!", Emoji.WARNING, event.getAuthor().getAsMention());
                    return false;
                }
            } else {
                event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s do not interfere the game!", Emoji.WARNING, event.getAuthor().getAsMention());
                return false;
            }
            if (!game.isOccupied(row, column)) {
                game.addPiece(new Piece(piece), row, column);
                game.drawBoard();
            } else {
                event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s the place is occupied. Use your eyes!", Emoji.WARNING, event.getAuthor().getAsMention());
                return false;
            }
            final MultiObjectHolder holder = MultiObjectHolder.of(event.getGuild(), event.getAuthor(), event.getTextChannel());
            final MultiObject<TicTacToe> multiObject = MultiObject.getFirstMultiObject(TicTacToe.class.getName(), holder);
            if (game.getWinner().equals("X")) {
                event.sendMessage(2 * Standard.STANDARD_MESSAGE_DELETING_DELAY, Standard.getMessageEmbed(Color.GREEN, "%s Player %s wins!", Emoji.NO, challenger.getAsMention()).build());
                deleteMessages();
                game.clearBoard();
                if (multiObject != null) {
                    multiObject.unregister();
                }
            } else if (game.getWinner().equals("O")) {
                event.sendMessage(2 * Standard.STANDARD_MESSAGE_DELETING_DELAY, Standard.getMessageEmbed(Color.GREEN, "%s Player %s wins!", Emoji.YES, opponent.getAsMention()).build());
                deleteMessages();
                game.clearBoard();
                if (multiObject != null) {
                    multiObject.unregister();
                }
            } else if (game.isDraw()) {
                event.sendMessage(2 * Standard.STANDARD_MESSAGE_DELETING_DELAY, Standard.getMessageEmbed(Color.GREEN, "%s Draw, no winner. %s", Emoji.NO, Emoji.YES).build());
                deleteMessages();
                game.clearBoard();
                if (multiObject != null) {
                    multiObject.unregister();
                }
            } else {
                switchTurn();
            }
            return true;
        } catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s the number you enter isn't valid.", Emoji.WARNING, event.getAuthor().getAsMention());
            return false;
        } catch (ArrayIndexOutOfBoundsException ex) {
            event.sendMessageFormat(Standard.STANDARD_MESSAGE_DELETING_DELAY, "%s %s invalid place!", Emoji.WARNING, event.getAuthor().getAsMention());
            return false;
        }
    }

    private final void switchTurn() {
        if (challenger == turn) {
            turn = opponent;
        } else {
            turn = challenger;
        }
    }

    private final void deleteMessages() {
        if (!Config.CONFIG.isGuildAutoDeletingCommand(event_started.getGuild().getIdLong())) {
            return;
        }
        Util.sheduleTimerAndRemove(() -> {
            try {
                message_header.delete().queue();
            } catch (Exception ex) {
            }
            try {
                message_board.delete().queue();
            } catch (Exception ex) {
            }
        }, 2 * Standard.STANDARD_MESSAGE_DELETING_DELAY);
    }

    private class Board {

        private final int rows;
        private final int cols;
        private final Piece[][] board;
        private int round;

        public Board(int r, int c) {
            rows = r;
            cols = c;
            board = new Piece[r][c];
            round = 0;
            Piece p;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    p = new Piece();
                    addPiece(p, i, j);
                }
            }
        }

        public final Board drawBoard() {
            message_header.editMessage(Standard.getMessageEmbed(Color.GREEN, null).setTitle(String.format("%s Current Board (Round %d)%n", Emoji.GAME, round), null).setDescription(String.format("Challenger: %s%nOpponent: %s%nTurn: %s", challenger.getAsMention(), opponent.getAsMention(), turn.equals(challenger) ? opponent.getAsMention() : challenger.getAsMention())).setFooter(String.format("%s finished his/her turn", turn.getName()), null).build()).queue();
            message_board.editMessage(toString()).queue();
            round++;
            return this;
        }

        public final Board addPiece(Piece x, int r, int c) {
            board[r][c] = x;
            return this;
        }

        public final Piece[][] getBoard() {
            return board;
        }

        public final boolean isOccupied(int r, int c) {
            final Piece p = board[r][c];
            final String q = p.getID();
            return !q.equals("  ");
        }

        public final Board clearBoard() {
            Piece p;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    p = new Piece();
                    addPiece(p, i, j);
                }
            }
            return this;
        }

        public final boolean isDraw() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!isOccupied(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public final String getWinner() {
            if (board[0][0].equals(board[0][1]) && board[0][1].equals(board[0][2])) {
                return board[0][0].getID();
            } else if (board[1][0].equals(board[1][1]) && board[1][1].equals(board[1][2])) {
                return board[1][0].getID();
            } else if (board[2][0].equals(board[2][1]) && board[2][1].equals(board[2][2])) {
                return board[2][0].getID();
            } else if (board[0][0].equals(board[1][0]) && board[1][0].equals(board[2][0])) {
                return board[0][0].getID();
            } else if (board[0][1].equals(board[1][1]) && board[1][1].equals(board[2][1])) {
                return board[0][1].getID();
            } else if (board[0][2].equals(board[1][2]) && board[1][2].equals(board[2][2])) {
                return board[0][2].getID();
            } else if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
                return board[0][0].getID();
            } else if (board[2][0].equals(board[1][1]) && board[1][1].equals(board[0][2])) {
                return board[2][0].getID();
            } else {
                return "none";
            }
        }

        private final String getEmojiPos(int r, int c) {
            String emoji = "";
            switch (r) {
                case 0:
                    switch (c) {
                        case 0:
                            emoji = Emoji.ONE;
                            break;
                        case 1:
                            emoji = Emoji.TWO;
                            break;
                        case 2:
                            emoji = Emoji.THREE;
                            break;
                        default:
                            break;
                    }
                    break;
                case 1:
                    switch (c) {
                        case 0:
                            emoji = Emoji.FOUR;
                            break;
                        case 1:
                            emoji = Emoji.FIVE;
                            break;
                        case 2:
                            emoji = Emoji.SIX;
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (c) {
                        case 0:
                            emoji = Emoji.SEVEN;
                            break;
                        case 1:
                            emoji = Emoji.EIGHT;
                            break;
                        case 2:
                            emoji = Emoji.NINE;
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            if (isOccupied(r, c)) {
                if (getBoard()[r][c].getID().equals("X")) {
                    emoji = Emoji.NO;
                }
                if (getBoard()[r][c].getID().equals("O")) {
                    emoji = Emoji.YES;
                }
            }
            return emoji;
        }

        @Override
        public final String toString() {
            String out = "";
            for (int i = 0; i < rows; i++) {
                for (int z = 0; z < cols; z++) {
                    out += getEmojiPos(i, z);
                }
                out += Standard.NEW_LINE_DISCORD;
            }
            return out;
        }

    }

    public class Piece {

        private final String id;

        Piece() {
            id = "  ";
        }

        Piece(String x) {
            id = x;
        }

        public final String getID() {
            return id;
        }

        public final boolean equals(Piece p) {
            return this.getID().equals(p.getID()) && !this.getID().equals(" ");
        }

    }
}
