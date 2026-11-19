package org.yuemi.fsi2107.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.yuemi.fsi2107.model.JenisTransaksi;
import org.yuemi.fsi2107.model.Kategori;
import org.yuemi.fsi2107.service.KategoriService;
import org.yuemi.fsi2107.ui.ikon.JenisIkon;

/** Dialog pengelolaan kategori transaksi. */
public class KategoriDialog extends JDialog {
  private final KategoriService layanan;
  private final DefaultListModel<Kategori> model = new DefaultListModel<>();
  private final JList<Kategori> daftar = new JList<>(model);
  private final JTextField nama = new JTextField();
  private final JComboBox<JenisTransaksi> jenis = new JComboBox<>(JenisTransaksi.values());

  public KategoriDialog(Window pemilik, KategoriService layanan) {
    super(pemilik, "Kelola Kategori", ModalityType.APPLICATION_MODAL);
    this.layanan = layanan;
    setSize(560, 360);
    setLocationRelativeTo(pemilik);
    TemaUI.siapkanDekorasiGelap(this);

    JPanel isi = new JPanel(new BorderLayout(12, 12));
    isi.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    isi.add(new JScrollPane(daftar), BorderLayout.CENTER);

    JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
    form.add(new JLabel("Nama kategori:"));
    form.add(nama);
    form.add(new JLabel("Jenis:"));
    form.add(jenis);
    isi.add(form, BorderLayout.NORTH);

    daftar.setCellRenderer(new RendererIkon(TemaUI::ikonKategori));

    JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
    TombolBerwarna tambah =
        new TombolBerwarna("Tambah", new Color(0x1565C0)).denganIkon(JenisIkon.TAMBAH);
    TombolBerwarna ubah =
        new TombolBerwarna("Ubah", new Color(0xEF6C00)).denganIkon(JenisIkon.UBAH);
    TombolBerwarna hapus =
        new TombolBerwarna("Hapus", new Color(0xC62828)).denganIkon(JenisIkon.HAPUS);
    TombolBerwarna tutup =
        new TombolBerwarna("Tutup", new Color(0x607D8B), TombolBerwarna.Gaya.GARIS)
            .denganIkon(JenisIkon.TUTUP);
    tambah.addActionListener(e -> tambah());
    ubah.addActionListener(e -> ubah());
    hapus.addActionListener(e -> hapus());
    tutup.addActionListener(e -> dispose());
    tombol.add(tambah);
    tombol.add(ubah);
    tombol.add(hapus);
    tombol.add(tutup);
    isi.add(tombol, BorderLayout.SOUTH);

    daftar.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting() && daftar.getSelectedValue() != null) {
            Kategori terpilih = daftar.getSelectedValue();
            nama.setText(terpilih.getLabel());
            jenis.setSelectedItem(terpilih.getJenis());
          }
        });

    layanan.tambahPendengar(l -> segarkan());
    setContentPane(isi);
    segarkan();
    TemaUI.aturDialogGelap(this, "Kelola Kategori");
    aturModeGelap(TemaUI.isModeGelap());
  }

  private void segarkan() {
    model.clear();
    for (Kategori kategori : layanan.getDaftarKategori()) model.addElement(kategori);
  }

  private void tambah() {
    try {
      layanan.tambah(nama.getText(), (JenisTransaksi) jenis.getSelectedItem());
      nama.setText("");
    } catch (RuntimeException ex) {
      tampilkanGalat(ex);
    }
  }

  private void ubah() {
    Kategori terpilih = daftar.getSelectedValue();
    if (terpilih == null) {
      tampilkanPesan("Pilih kategori yang akan diubah.");
      return;
    }
    try {
      layanan.ubah(terpilih, nama.getText(), (JenisTransaksi) jenis.getSelectedItem());
    } catch (RuntimeException ex) {
      tampilkanGalat(ex);
    }
  }

  private void hapus() {
    Kategori terpilih = daftar.getSelectedValue();
    if (terpilih == null) {
      tampilkanPesan("Pilih kategori yang akan dihapus.");
      return;
    }
    int jawaban =
        TemaUI.tampilkanKonfirmasi(
            this,
            "Hapus kategori \"" + terpilih.getLabel() + "\"?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
    if (jawaban == JOptionPane.YES_OPTION) {
      try {
        layanan.hapus(terpilih);
      } catch (RuntimeException ex) {
        tampilkanGalat(ex);
      }
    }
  }

  private void tampilkanGalat(RuntimeException ex) {
    TemaUI.tampilkanPesan(this, ex.getMessage(), "Kategori Transaksi", JOptionPane.ERROR_MESSAGE);
  }

  private void tampilkanPesan(String pesan) {
    TemaUI.tampilkanPesan(this, pesan, "Kategori Transaksi", JOptionPane.INFORMATION_MESSAGE);
  }

  void aturModeGelap(boolean aktif) {
    getContentPane().setBackground(TemaUI.warnaPanel());
    TemaUI.aturKomponen(this);
    TemaUI.aturComboBox(jenis, aktif, TemaUI::ikonJenisTransaksi);
    TemaUI.aturTextField(nama, aktif);
    daftar.setCellRenderer(new RendererIkon(TemaUI::ikonKategori));
    daftar.setBackground(TemaUI.warnaIsian());
    daftar.setForeground(TemaUI.warnaTeks());
    repaint();
  }
}
