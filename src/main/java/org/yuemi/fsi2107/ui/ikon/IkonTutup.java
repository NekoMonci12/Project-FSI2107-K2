package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ikon silang untuk tombol tutup.
 */
public final class IkonTutup extends IkonDasar {

    public IkonTutup(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;
        g2.setStroke(sapuan(1.8f * skala));
        g2.drawLine(x + Math.round(4 * skala), y + Math.round(4 * skala),
                x + Math.round(12 * skala), y + Math.round(12 * skala));
        g2.drawLine(x + Math.round(12 * skala), y + Math.round(4 * skala),
                x + Math.round(4 * skala), y + Math.round(12 * skala));
    }
}
