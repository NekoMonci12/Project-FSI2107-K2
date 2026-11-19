package org.yuemi.fsi2107.ui;

import org.yuemi.fsi2107.ui.ikon.JenisIkon;

import javax.swing.JToggleButton;
import javax.swing.Icon;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * Sakelar geser (slide toggle) untuk berpindah antara mode terang dan gelap.
 * Knob bulat meluncur ke kanan saat mode gelap aktif, dengan ikon matahari
 * pada mode terang dan ikon bulan pada mode gelap.
 */
final class SakelarTema extends JToggleButton {

    private static final int LEBAR = 62;
    private static final int TINGGI = 30;
    private static final int DIAMETER_KNOB = 24;

    private static final Color WARNA_AKTIF = new Color(0x3D7BF7);
    private static final Color WARNA_TIDAK_AKTIF = new Color(0xFFC93C);
    private static final Color WARNA_KNOB_TERANG = new Color(0xFFFFFF);
    private static final Color WARNA_KNOB_GELAP = new Color(0x2A303A);

    /** Posisi knob antara 0 (kiri, mode terang) dan 1 (kanan, mode gelap). */
    private double posisi;

    private final Timer animasi;

    public SakelarTema(boolean modeGelapAwal) {
        setSelected(modeGelapAwal);
        posisi = modeGelapAwal ? 1.0 : 0.0;
        setFocusable(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setBorder(null);
        setToolTipText("Alihkan tampilan terang atau gelap");
        setPreferredSize(new Dimension(LEBAR, TINGGI));
        setMinimumSize(new Dimension(LEBAR, TINGGI));
        setMaximumSize(new Dimension(LEBAR, TINGGI));

        animasi = new Timer(16, null);
        animasi.setRepeats(true);
        animasi.addActionListener(e -> lanjutAnimasi());
        addActionListener(e -> {
            animasi.start();
            repaint();
        });
    }

    private void lanjutAnimasi() {
        double target = isSelected() ? 1.0 : 0.0;
        if (Math.abs(posisi - target) < 0.01) {
            posisi = target;
            animasi.stop();
        } else {
            posisi += (target - posisi) * 0.25;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int lebar = getWidth();
        int tinggi = getHeight();
        int radius = tinggi;

        g2.setColor(isSelected() ? WARNA_AKTIF : WARNA_TIDAK_AKTIF);
        g2.fill(new RoundRectangle2D.Float(0, 0, lebar, tinggi, radius, radius));

        // Ikon tujuan berada di sisi berlawanan dengan knob.
        Icon ikonTujuan = isSelected()
                ? JenisIkon.BULAN.buat()
                : JenisIkon.MATAHARI.buat();
        int ukuranIkon = ikonTujuan.getIconWidth();
        int ikonX = isSelected() ? 6 : lebar - ukuranIkon - 6;
        int ikonY = (tinggi - ikonTujuan.getIconHeight()) / 2;

        int knobDiameter = DIAMETER_KNOB;
        int jarakSisi = (tinggi - knobDiameter) / 2;
        int knobX = jarakSisi + (int) Math.round(posisi * (lebar - knobDiameter - 2 * jarakSisi));
        int knobY = jarakSisi;

        g2.setColor(isSelected() ? WARNA_KNOB_GELAP : WARNA_KNOB_TERANG);
        g2.fillOval(knobX, knobY, knobDiameter, knobDiameter);

        ikonTujuan.paintIcon(this, g2, ikonX, ikonY);

        g2.dispose();
    }
}
