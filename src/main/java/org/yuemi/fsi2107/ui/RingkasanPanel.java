package org.yuemi.fsi2107.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.yuemi.fsi2107.model.Profile;
import org.yuemi.fsi2107.model.Transaksi;
import org.yuemi.fsi2107.service.BukuKas;
import org.yuemi.fsi2107.ui.ikon.IkonDasar;
import org.yuemi.fsi2107.ui.ikon.JenisIkon;

/**
 * Panel ringkasan berisi kartu saldo, total uang masuk, total uang keluar, serta total PPN dan PPh
 * yang terkumpul pada profil aktif.
 *
 * <p>Kartu pajak hanya ditampilkan bila profil aktif memungut pajak; profil biasa cukup menampilkan
 * tiga kartu utama beserta keterangan profil.
 */
public class RingkasanPanel extends JPanel {

  private static final Color WARNA_MASUK = new Color(0x2E7D32);
  private static final Color WARNA_KELUAR = new Color(0xC62828);
  private static final Color WARNA_NETRAL = new Color(0x37474F);
  private static final Color WARNA_PAJAK = new Color(0xEF6C00);

  private final JLabel nilaiSaldo = buatNilaiLabel();
  private final JLabel nilaiMasuk = buatNilaiLabel();
  private final JLabel nilaiKeluar = buatNilaiLabel();
  private final JLabel nilaiPpn = buatNilaiLabel();
  private final JLabel nilaiPph = buatNilaiLabel();
  private final JLabel infoJumlah = new JLabel("Belum ada transaksi");
  private final JLabel infoProfil = new JLabel(" ");

  private final JPanel barisKartu = new JPanel();
  private final JPanel kartuSaldo = buatKartu("SALDO SAAT INI", nilaiSaldo, WARNA_NETRAL);
  private final JPanel kartuMasuk =
      buatKartu("TOTAL UANG MASUK", nilaiMasuk, WARNA_MASUK, JenisIkon.UANG_MASUK);
  private final JPanel kartuKeluar =
      buatKartu("TOTAL UANG KELUAR", nilaiKeluar, WARNA_KELUAR, JenisIkon.UANG_KELUAR);
  private final JPanel kartuPpn = buatKartu("TOTAL PPN TERKUMPUL", nilaiPpn, WARNA_PAJAK);
  private final JPanel kartuPph = buatKartu("TOTAL PPH TERKUMPUL", nilaiPph, WARNA_PAJAK);
  private final JPanel selKeterangan = new JPanel(new BorderLayout(0, 2));

  /** Mode tata letak terakhir; {@code null} berarti belum pernah ditata. */
  private Boolean modePajak;

  private long saldoSaatIni;

  public RingkasanPanel() {
    setLayout(new BorderLayout(0, 8));
    setBorder(BorderFactory.createEmptyBorder(14, 14, 6, 14));
    setOpaque(false);

    barisKartu.setOpaque(false);

    infoJumlah.setFont(infoJumlah.getFont().deriveFont(Font.PLAIN, 12f));
    infoJumlah.setForeground(new Color(0x607D8B));

    infoProfil.setFont(infoProfil.getFont().deriveFont(Font.PLAIN, 12f));
    infoProfil.setForeground(new Color(0x37474F));

    // Sel ini mengisi sel kosong pada baris kartu dengan keterangan jumlah
    // transaksi dan profil yang sedang aktif.
    selKeterangan.setOpaque(false);
    selKeterangan.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
    selKeterangan.add(infoJumlah, BorderLayout.NORTH);
    selKeterangan.add(infoProfil, BorderLayout.SOUTH);

    add(barisKartu, BorderLayout.CENTER);
    aturTataLetakKartu(false);
    aturModeGelap(TemaUI.isModeGelap());
  }

  /**
   * Menyusun kartu sesuai kebutuhan profil: profil berpajak memakai dua baris berisi lima kartu,
   * profil tanpa pajak cukup satu baris berisi tiga kartu dan keterangan profil sehingga tidak ada
   * ruang kosong yang terbuang.
   */
  private void aturTataLetakKartu(boolean adaPajak) {
    if (modePajak != null && modePajak == adaPajak) {
      return;
    }
    modePajak = adaPajak;

    barisKartu.removeAll();
    barisKartu.setLayout(adaPajak ? new GridLayout(2, 3, 12, 8) : new GridLayout(1, 4, 12, 8));
    barisKartu.add(kartuSaldo);
    barisKartu.add(kartuMasuk);
    barisKartu.add(kartuKeluar);
    if (adaPajak) {
      barisKartu.add(kartuPpn);
      barisKartu.add(kartuPph);
    }
    barisKartu.add(selKeterangan);

    barisKartu.revalidate();
    barisKartu.repaint();
  }

  private static JLabel buatNilaiLabel() {
    JLabel label = new JLabel("Rp0");
    label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
    label.setForeground(WARNA_NETRAL);
    return label;
  }

  private static JPanel buatKartu(String judul, JLabel nilai, Color warnaJudul) {
    return buatKartu(judul, nilai, warnaJudul, null);
  }

  /** Menyusun kartu ringkasan, dengan ikon opsional di sebelah kiri judul. */
  private static JPanel buatKartu(String judul, JLabel nilai, Color warnaJudul, JenisIkon ikon) {
    JPanel kartu = new JPanel();
    kartu.setLayout(new BoxLayout(kartu, BoxLayout.Y_AXIS));
    kartu.setBackground(Color.WHITE);
    kartu.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCFD8DC)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));

    JLabel labelJudul = new JLabel(judul);
    labelJudul.setFont(labelJudul.getFont().deriveFont(Font.BOLD, 11f));
    labelJudul.setForeground(warnaJudul);
    if (ikon != null) {
      labelJudul.setIcon(ikon.buat(warnaJudul));
      labelJudul.setIconTextGap(6);
    }

    kartu.add(labelJudul);
    kartu.add(Box.createRigidArea(new Dimension(0, 4)));
    kartu.add(nilai);
    return kartu;
  }

  /** Menyesuaikan seluruh tampilan ringkasan dengan kondisi buku kas terbaru. */
  public void perbarui(BukuKas bukuKas) {
    Profile profil = bukuKas.getProfilAktif();
    boolean adaPajak = profil != null && profil.punyaPajak();
    aturTataLetakKartu(adaPajak);

    long saldo = bukuKas.getSaldo();
    saldoSaatIni = saldo;
    nilaiSaldo.setText(Transaksi.formatNominal(saldo));
    nilaiSaldo.setForeground(saldo < 0 ? WARNA_KELUAR : WARNA_MASUK);

    nilaiMasuk.setText(Transaksi.formatNominal(bukuKas.getTotalMasuk()));
    nilaiKeluar.setText(Transaksi.formatNominal(bukuKas.getTotalKeluar()));

    if (adaPajak) {
      nilaiPpn.setText(Transaksi.formatNominal(bukuKas.getTotalPpn()));
      nilaiPph.setText(Transaksi.formatNominal(bukuKas.getTotalPph()));
    }

    int jumlah = bukuKas.getJumlahTransaksi();
    infoJumlah.setText(
        jumlah == 0 ? "Belum ada transaksi" : "Total " + jumlah + " transaksi tercatat");

    perbaruiInfoProfil(profil);
  }

  /**
   * Menampilkan informasi tambahan milik profil aktif, misalnya catatan profil dan setelan pajak
   * yang berlaku. Profil tanpa informasi khusus menampilkan baris kosong.
   */
  private void perbaruiInfoProfil(Profile profil) {
    if (profil == null) {
      infoProfil.setText(" ");
      return;
    }
    List<String> baris = profil.infoTambahan();
    infoProfil.setText(baris.isEmpty() ? " " : String.join("   |   ", baris));
  }

  void aturModeGelap(boolean aktif) {
    Color warnaKartu = TemaUI.warnaKartu();
    Color warnaTeks = TemaUI.warnaTeks();
    for (JPanel kartu : List.of(kartuSaldo, kartuMasuk, kartuKeluar, kartuPpn, kartuPph)) {
      kartu.setBackground(warnaKartu);
      kartu.setBorder(
          BorderFactory.createCompoundBorder(
              BorderFactory.createLineBorder(TemaUI.warnaGaris()),
              BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }
    nilaiSaldo.setForeground(saldoSaatIni < 0 ? WARNA_KELUAR : WARNA_MASUK);
    nilaiMasuk.setForeground(warnaTeks);
    nilaiKeluar.setForeground(warnaTeks);
    nilaiPpn.setForeground(warnaTeks);
    nilaiPph.setForeground(warnaTeks);
    aturWarnaJudul(kartuSaldo, WARNA_NETRAL, aktif);
    aturWarnaJudul(kartuMasuk, WARNA_MASUK, aktif);
    aturWarnaJudul(kartuKeluar, WARNA_KELUAR, aktif);
    aturWarnaJudul(kartuPpn, WARNA_PAJAK, aktif);
    aturWarnaJudul(kartuPph, WARNA_PAJAK, aktif);
    if (aktif) {
      nilaiSaldo.setForeground(saldoSaatIni < 0 ? new Color(0xC99A9D) : new Color(0x9AB99D));
    }
    infoJumlah.setForeground(TemaUI.warnaTeksSekunder());
    infoProfil.setForeground(warnaTeks);
    repaint();
  }

  private static void aturWarnaJudul(JPanel kartu, Color warna, boolean modeGelap) {
    JLabel judul = (JLabel) kartu.getComponent(0);
    if (!modeGelap) {
      judul.setForeground(warna);
    } else if (warna.equals(WARNA_MASUK)) {
      judul.setForeground(new Color(0x75A17A));
    } else if (warna.equals(WARNA_KELUAR)) {
      judul.setForeground(new Color(0xBB777B));
    } else if (warna.equals(WARNA_PAJAK)) {
      judul.setForeground(new Color(0xB28A68));
    } else {
      judul.setForeground(new Color(0x9AA3B0));
    }
    if (judul.getIcon() instanceof IkonDasar ikon) {
      ikon.setWarna(judul.getForeground());
    }
  }
}
