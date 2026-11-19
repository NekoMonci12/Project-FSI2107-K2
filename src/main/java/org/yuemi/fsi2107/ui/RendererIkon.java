package org.yuemi.fsi2107.ui;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.util.function.Function;

/**
 * Renderer daftar yang menampilkan ikon di depan teks setiap item.
 *
 * <p>Ikon diambil dari fungsi penyedia yang menerima nilai item dan
 * mengembalikan ikon, sehingga daftar bisa memakai ikon yang berbeda
 * menurut jenis datanya. Warna ikon ikut menyesuaikan mode tampilan
 * karena penyedia ikon evaluated saat gambar.</p>
 */
final class RendererIkon extends DefaultListCellRenderer {

    private final Function<Object, Icon> penyediaIkon;

    RendererIkon(Function<Object, Icon> penyediaIkon) {
        this.penyediaIkon = penyediaIkon;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> daftar, Object nilai, int indeks,
                                                  boolean terpilih, boolean punyaFokus) {
        super.getListCellRendererComponent(daftar, nilai, indeks, terpilih, punyaFokus);

        boolean modeGelap = TemaUI.isModeGelap();
        setOpaque(true);
        setBackground(terpilih
                ? (modeGelap ? new Color(0x252E3B) : new Color(0xBBDEFB))
                : TemaUI.warnaIsian());
        setForeground(terpilih && !modeGelap
                ? new Color(0x263238) : TemaUI.warnaTeks());
        if (modeGelap) {
            setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        }

        setIcon(nilai == null || penyediaIkon == null ? null : penyediaIkon.apply(nilai));
        return this;
    }
}
