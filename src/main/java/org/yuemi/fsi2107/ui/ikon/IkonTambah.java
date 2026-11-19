package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ikon tanda tambah untuk tombol tambah data.
 */
public final class IkonTambah extends IkonDasar {

    public IkonTambah(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;
        g2.setStroke(sapuan(2f * skala));
        g2.drawLine(x + Math.round(8 * skala), y + Math.round(3 * skala),
                x + Math.round(8 * skala), y + Math.round(13 * skala));
        g2.drawLine(x + Math.round(3 * skala), y + Math.round(8 * skala),
                x + Math.round(13 * skala), y + Math.round(8 * skala));
    }
}
