package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;

/** Ikon matahari untuk sakelar tema. */
public final class IkonMatahari extends IkonDasar {

  public IkonMatahari(Color warna) {
    super(16, warna);
  }

  @Override
  protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
    float skala = ukuran / 16f;
    int pusatX = x + Math.round(ukuran / 2f);
    int pusatY = y + Math.round(ukuran / 2f);

    g2.fillOval(
        pusatX - Math.round(4 * skala),
        pusatY - Math.round(4 * skala),
        Math.round(8 * skala),
        Math.round(8 * skala));

    g2.setStroke(sapuan(1.6f * skala));
    for (int sudut = 0; sudut < 360; sudut += 45) {
      double radian = Math.toRadians(sudut);
      int x1 = pusatX + (int) Math.round(Math.cos(radian) * 6 * skala);
      int y1 = pusatY + (int) Math.round(Math.sin(radian) * 6 * skala);
      int x2 = pusatX + (int) Math.round(Math.cos(radian) * 8 * skala);
      int y2 = pusatY + (int) Math.round(Math.sin(radian) * 8 * skala);
      g2.drawLine(x1, y1, x2, y2);
    }
  }
}
