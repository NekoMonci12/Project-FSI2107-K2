package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Ikon label kategori untuk penanda kelompok data.
 */
public final class IkonKategori extends IkonDasar {

    public IkonKategori(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;

        Area label = new Area(new RoundRectangle2D.Float(x + 2 * skala, y + 3.5f * skala,
                12 * skala, 9 * skala, 2 * skala, 2 * skala));
        label.subtract(new Area(new Ellipse2D.Float(x + 4.6f * skala, y + 6.1f * skala,
                2.8f * skala, 2.8f * skala)));
        g2.fill(label);
    }
}
