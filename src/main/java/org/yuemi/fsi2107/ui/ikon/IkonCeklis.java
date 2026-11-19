package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Ikon centang untuk penandaan data terpilih.
 */
public final class IkonCeklis extends IkonDasar {

    public IkonCeklis(Color warna) {
        super(16, warna);
    }

    @Override
    protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
        float skala = ukuran / 16f;

        Area lingkar = new Area(new Ellipse2D.Float(x + 1.5f * skala, y + 1.5f * skala,
                13 * skala, 13 * skala));
        lingkar.subtract(new Area(new Ellipse2D.Float(x + 3.2f * skala, y + 3.2f * skala,
                9.6f * skala, 9.6f * skala)));
        g2.fill(lingkar);

        g2.setStroke(sapuan(1.8f * skala));
        Path2D cek = new Path2D.Float();
        cek.moveTo(x + 5 * skala, y + 8.3f * skala);
        cek.lineTo(x + 7.2f * skala, y + 10.6f * skala);
        cek.lineTo(x + 11.2f * skala, y + 5.4f * skala);
        g2.draw(cek);
    }
}
