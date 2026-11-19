package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/** Ikon disket untuk tombol simpan. */
public final class IkonSimpan extends IkonDasar {

  public IkonSimpan(Color warna) {
    super(16, warna);
  }

  @Override
  protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
    float skala = ukuran / 16f;

    // Bingkai disket: persegi membulat dengan sudut kanan atas terpotong
    // dan slot label kosong di bagian bawah.
    Area bingkai =
        new Area(
            new RoundRectangle2D.Float(
                x + 2 * skala, y + 2 * skala, 12 * skala, 12 * skala, 2 * skala, 2 * skala));
    bingkai.subtract(
        new Area(
            new Rectangle2D.Float(x + 9.5f * skala, y + 2 * skala, 4.5f * skala, 4.5f * skala)));
    bingkai.subtract(
        new Area(
            new RoundRectangle2D.Float(
                x + 4.5f * skala,
                y + 9.5f * skala,
                7 * skala,
                4.5f * skala,
                0.6f * skala,
                0.6f * skala)));
    g2.fill(bingkai);

    // Jendela penanda disket di sudut kanan atas.
    g2.fill(
        new RoundRectangle2D.Float(
            x + 10f * skala,
            y + 2.4f * skala,
            3.6f * skala,
            3.6f * skala,
            0.6f * skala,
            0.6f * skala));
  }
}
