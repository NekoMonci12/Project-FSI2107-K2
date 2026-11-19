package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/** Ikon pensil untuk tombol ubah data. */
public final class IkonUbah extends IkonDasar {

  public IkonUbah(Color warna) {
    super(16, warna);
  }

  @Override
  protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
    float skala = ukuran / 16f;

    Path2D batang = new Path2D.Float();
    batang.moveTo(x + 3 * skala, y + 13 * skala);
    batang.lineTo(x + 5 * skala, y + 9.5f * skala);
    batang.lineTo(x + 11.5f * skala, y + 3 * skala);
    batang.lineTo(x + 13 * skala, y + 4.5f * skala);
    batang.lineTo(x + 6.5f * skala, y + 11 * skala);
    batang.closePath();
    g2.fill(batang);

    g2.setStroke(sapuan(1.4f * skala));
    Path2D kepala = new Path2D.Float();
    kepala.moveTo(x + 10.6f * skala, y + 3.9f * skala);
    kepala.lineTo(x + 12.1f * skala, y + 2.4f * skala);
    kepala.lineTo(x + 13.6f * skala, y + 3.9f * skala);
    kepala.lineTo(x + 12.1f * skala, y + 5.4f * skala);
    kepala.closePath();
    g2.draw(kepala);
  }
}
