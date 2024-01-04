package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.chess.gui.Table.*;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color PANEL_COLOUR = Color.decode("#F5F5F5");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOUR);
        this.southPanel.setBackground(PANEL_COLOUR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {

        northPanel.removeAll();
        southPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move : moveLog.getMoves()){
            if (move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceTeam().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }
                else if (takenPiece.getPieceTeam().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else {
                    throw new RuntimeException("Should not reach");
                }
            }
        }

        whiteTakenPieces.sort(new Comparator<Piece>() {
            @Override
            public int compare(final Piece p1, final Piece p2) {

                return Integer.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });

        blackTakenPieces.sort(new Comparator<Piece>() {
            @Override
            public int compare(Piece p1, Piece p2) {

                return Integer.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });

        for (final Piece takenPiece : whiteTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(new File("Designs/Basic/" +
                        takenPiece.getPieceTeam().toString().substring(0, 1) +
                        "" + takenPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            }
            catch (final IOException e){
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(new File("Designs/Basic/" +
                        takenPiece.getPieceTeam().toString().substring(0, 1) +
                        "" + takenPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            }
            catch (final IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }
}