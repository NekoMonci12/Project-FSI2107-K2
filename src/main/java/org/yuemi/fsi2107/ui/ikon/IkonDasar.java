package org.yuemi.fsi2107.ui.ikon;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Kelas dasar abstrak untuk seluruh ikon vektor di aplikasi.
 *
 * <p>Subkelas cukup menentukan bentuknya lewat {@link #gambar(Graphics2D, int, int, int)};
 * pewarnaan, ukuran, dan pengaturan anti-aliasing ditangani di sini agar
 * seluruh ikon tampak konsisten.</p>
 */
public abstract class IkonDasar implements Icon {

    /** Ukuran sisi ikon dalam piksel. */
    private final int ukuran;

    /** Warna goresan ikon. */
    private Color warna;

    protected IkonDasar(int ukuran, Color warna) {
        this.ukuran = ukuran;
        this.warna = warna == null ? Color.DARK_GRAY : warna;
    }

    /**
     * Mengganti warna goresan ikon tanpa membuat objek baru.
     */
    public void setWarna(Color warnaBaru) {
        this.warna = warnaBaru == null ? Color.DARK_GRAY : warnaBaru;
    }

    @Override
    public int getIconWidth() {
        return ukuran;
    }

    @Override
    public int getIconHeight() {
        return ukuran;
    }

    /**
     * Mengembalikan warna goresan ikon yang sedang dipakai.
     */
    public Color warna() {
        return warna;
    }

    /**
     * Menggambar bentuk ikon pada koordinat {@code (x, y)} berukuran {@code ukuran}.
     *
     * <p>Warna goresan sudah disetel dan anti-aliasing sudah aktif, sehingga
     * subkelas cukup menggambar bentuknya saja.</p>
     *
     * @param g2    konteks gambar yang sudah disiapkan
     * @param x     koordinat kiri atas ikon
     * @param y     koordinat kiri atas ikon
     * @param ukuran panjang sisi ikon dalam piksel
     */
    protected abstract void gambar(Graphics2D g2, int x, int y, int ukuran);

    @Override
    public void paintIcon(Component komponen, Graphics graphics, int x, int y) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(warna);
        gambar(g2, x, y, ukuran);
        g2.dispose();
    }

    /**
     * Membuat sapuan goresan dengan lebar tertentu.
     */
    protected static BasicStroke sapuan(float lebar) {
        return new BasicStroke(lebar, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }
}
