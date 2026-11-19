package org.yuemi.fsi2107.ui;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * Ikon bulan sabit (crescent) untuk sakelar tema.
 */
final class IkonBulan implements Icon {

    private static final int UKURAN = 16;

    private final Color warna;

    IkonBulan(Color warna) {
        this.warna = warna;
    }

    @Override
    public void paintIcon(Component komponen, Graphics graphics, int x, int y) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(warna);

        Area bulan = new Area(new Ellipse2D.Float(x + 2, y + 3, 12, 12));
        Area potongan = new Area(new Ellipse2D.Float(x + 7, y, 12, 12));
        bulan.subtract(potongan);
        g2.fill(bulan);
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
