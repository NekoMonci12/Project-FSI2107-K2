package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/** Ikon orang untuk pemilih profil. */
public final class IkonProfil extends IkonDasar {

  public IkonProfil(Color warna) {
    super(16, warna);
  }

  @Override
  protected void gambar(Graphics2D g2, int x, int y, int ukuran) {
    float skala = ukuran / 16f;

    Area kepala =
        new Area(new Ellipse2D.Float(x + 5 * skala, y + 1.5f * skala, 6 * skala, 6 * skala));
    g2.fill(kepala);

    Area badan =
        new Area(
            new RoundRectangle2D.Float(
                x + 2.5f * skala, y + 8.5f * skala, 11 * skala, 7 * skala, 4 * skala, 4 * skala));
    g2.fill(badan);
  }
}
