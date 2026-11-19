package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ikon panah kecil ke bawah untuk tombol combo box.
 */
public final class IkonPanah extends IkonDasar {

    public IkonPanah(Color warna) {
        super(10, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        int tengah = x + ukuran / 2;
        int atas = y + ukuran / 2 - 1;

        g2.setStroke(sapuan(1.4f));
        g2.drawLine(tengah - 3, atas, tengah, atas + 3);
        g2.drawLine(tengah, atas + 3, tengah + 3, atas);
    }
}
