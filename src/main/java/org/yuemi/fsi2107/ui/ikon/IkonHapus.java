package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Ikon tong sampah untuk tombol hapus.
 */
public final class IkonHapus extends IkonDasar {

    public IkonHapus(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;

        // Tutup tong.
        Area tutup = new Area(new RoundRectangle2D.Float(x + 5 * skala, y + 2 * skala,
                6 * skala, 1.8f * skala, 1 * skala, 1 * skala));
        g2.fill(tutup);

        // Badan tong dengan dua garis vertikal sebagai detail.
        Area badan = new Area(new RoundRectangle2D.Float(x + 3.5f * skala, y + 4.5f * skala,
                9 * skala, 9.5f * skala, 1.5f * skala, 1.5f * skala));
        badan.subtract(new Area(new Rectangle2D.Float(x + 6.3f * skala, y + 6.5f * skala,
                1.1f * skala, 5.5f * skala)));
        badan.subtract(new Area(new Rectangle2D.Float(x + 8.6f * skala, y + 6.5f * skala,
                1.1f * skala, 5.5f * skala)));
        g2.fill(badan);
    }
}
