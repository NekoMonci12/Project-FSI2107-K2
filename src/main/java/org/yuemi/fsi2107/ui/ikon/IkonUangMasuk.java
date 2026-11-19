package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Ikon uang masuk: panah turun mengarah ke dalam wadah.
 */
public final class IkonUangMasuk extends IkonDasar {

    public IkonUangMasuk(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;

        Area wadah = new Area(new Ellipse2D.Float(x + 2 * skala, y + 11.5f * skala,
                12 * skala, 3 * skala));
        g2.fill(wadah);

        Path2D batang = new Path2D.Float();
        batang.moveTo(x + 6.5f * skala, y + 2 * skala);
        batang.lineTo(x + 9.5f * skala, y + 2 * skala);
        batang.lineTo(x + 9.5f * skala, y + 7 * skala);
        batang.lineTo(x + 6.5f * skala, y + 7 * skala);
        batang.closePath();
        g2.fill(batang);

        Path2D kepala = new Path2D.Float();
        kepala.moveTo(x + 8 * skala, y + 11 * skala);
        kepala.lineTo(x + 12.5f * skala, y + 6 * skala);
        kepala.lineTo(x + 3.5f * skala, y + 6 * skala);
        kepala.closePath();
        g2.fill(kepala);
    }
}
