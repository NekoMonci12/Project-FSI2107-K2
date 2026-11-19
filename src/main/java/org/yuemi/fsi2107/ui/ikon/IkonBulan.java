package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/** Ikon bulan sabit (crescent) untuk sakelar tema. */
public final class IkonBulan extends IkonDasar {

  public IkonBulan(Color warna) {
    super(16, warna);
  }

  @Override
  protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
    float skala = ukuran / 16f;
    Area bulan =
        new Area(new Ellipse2D.Float(x + 2 * skala, y + 3 * skala, 12 * skala, 12 * skala));
    Area potongan = new Area(new Ellipse2D.Float(x + 7 * skala, y, 12 * skala, 12 * skala));
    bulan.subtract(potongan);
    g2.fill(bulan);
  }
}
