package org.yuemi.fsi2107.ui;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Ikon matahari untuk sakelar tema.
 */
final class IkonMatahari implements Icon {

    private static final int UKURAN = 16;

    private final Color warna;

    IkonMatahari(Color warna) {
        this.warna = warna;
    }

    @Override
    public void paintIcon(Component komponen, Graphics graphics, int x, int y) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(warna);

        int pusatX = x + UKURAN / 2;
        int pusatY = y + UKURAN / 2;
        g2.fillOval(pusatX - 4, pusatY - 4, 8, 8);
        for (int sudut = 0; sudut < 360; sudut += 45) {
            double radian = Math.toRadians(sudut);
            int x1 = pusatX + (int) Math.round(Math.cos(radian) * 6);
            int y1 = pusatY + (int) Math.round(Math.sin(radian) * 6);
            int x2 = pusatX + (int) Math.round(Math.cos(radian) * 8);
            int y2 = pusatY + (int) Math.round(Math.sin(radian) * 8);
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return UKURAN;
    }

    @Override
    public int getIconHeight() {
        return UKURAN;
    }
}
