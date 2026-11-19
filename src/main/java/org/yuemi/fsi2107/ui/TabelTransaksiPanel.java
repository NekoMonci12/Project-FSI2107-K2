package org.yuemi.fsi2107.ui;

import org.yuemi.fsi2107.model.Profile;
import org.yuemi.fsi2107.model.Transaksi;
import org.yuemi.fsi2107.service.BukuKas;
import org.yuemi.fsi2107.ui.ikon.JenisIkon;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.event.TableModelEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel tabel yang menampilkan seluruh transaksi beserta tombol penghapusan.
 *
 * <p>Kolom PPh, PPN, dan Bersih hanya ditampilkan bila profil aktif memungut
 * pajak; profil biasa cukup menampilkan nominal transaksinya.</p>
 */
public class TabelTransaksiPanel extends JPanel {

    private static final String[] JUDUL_KOLOM =
            {"No", "Waktu", "Jenis", "Kategori", "Deskripsi", "Nominal", "PPh", "PPN", "Bersih", "Pilih"};

    private static final int[] LEBAR_KOLOM = {40, 120, 100, 100, 220, 120, 110, 100, 120, 50};

    /** Indeks model kolom pajak: PPh, PPN, dan nominal bersih. */
    private static final int KOLOM_PPH = 6;
    private static final int KOLOM_PPN = 7;
    private static final int KOLOM_BERSIH = 8;
    private static final int KOLOM_PILIH = 9;

    private static final Color WARNA_BARIS_MASUK = new Color(0xE8F5E9);
    private static final Color WARNA_BARIS_KELUAR = new Color(0xFFEBEE);
    private static final Color WARNA_TEKS_MASUK = new Color(0x1B5E20);
    private static final Color WARNA_TEKS_KELUAR = new Color(0xB71C1C);

    private final BukuKas bukuKas;
    private final DefaultTableModel modelTabel;
    private final JTable tabel;
    private final JScrollPane scrollPane;
    private final JPanel pojokHeader = new JPanel();
    private final TombolBerwarna tombolPilihSemua;

    /** Daftar transaksi yang sedang ditampilkan, sejajar indeks dengan baris tabel. */
    private List<Transaksi> transaksiTampil = new ArrayList<>();

    /** Apakah kolom pajak sedang ditampilkan. */
    private boolean modePajak;

    public TabelTransaksiPanel(BukuKas bukuKas) {
        this.bukuKas = bukuKas;

        setLayout(new BorderLayout(0, 6));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xCFD8DC)),
                " Daftar Transaksi "));

        modelTabel = new DefaultTableModel(JUDUL_KOLOM, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return kolom == KOLOM_PILIH;
            }

            @Override
            public Class<?> getColumnClass(int kolom) {
                return kolom == KOLOM_PILIH ? Boolean.class : Object.class;
            }
        };

        tabel = new JTable(modelTabel);
        tabel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabel.setRowHeight(26);
        tabel.setFillsViewportHeight(true);
        tabel.setShowGrid(true);
        tabel.setGridColor(TemaUI.warnaGaris());
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.setDefaultRenderer(Object.class, new PerenderBaris());
        tabel.getTableHeader().setDefaultRenderer(new PerenderHeader());
        modelTabel.addTableModelListener(peristiwa -> {
            if (peristiwa.getColumn() == KOLOM_PILIH
                    || peristiwa.getType() == TableModelEvent.UPDATE
                    || peristiwa.getType() == TableModelEvent.DELETE) {
                perbaruiTombolPilihSemua();
            }
        });

        aturKolomPajak(true);

        TombolBerwarna tombolHapus = new TombolBerwarna("Hapus Batch", new Color(0xC62828))
                .denganIkon(JenisIkon.HAPUS);
        tombolHapus.addActionListener(e -> hapusTransaksiTerpilih());

        tombolPilihSemua = new TombolBerwarna("Pilih Semua", new Color(0x607D8B),
                TombolBerwarna.Gaya.GARIS).denganIkon(JenisIkon.CEKLIS);
        tombolPilihSemua.addActionListener(e -> pilihSemuaTransaksi());

        JPanel panelKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelKiri.setOpaque(false);
        panelKiri.add(tombolPilihSemua);

        JPanel panelBawah = new JPanel(new BorderLayout(8, 0));
        panelBawah.setOpaque(false);
        panelBawah.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panelBawah.add(panelKiri, BorderLayout.WEST);
        panelBawah.add(tombolHapus, BorderLayout.EAST);

        scrollPane = new JScrollPane(tabel);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);
        aturModeGelap(TemaUI.isModeGelap());
    }

    /**
     * Menyusun ulang kolom tabel: kolom pajak disertakan hanya bila profil aktif
     * memungut pajak. Model data tetap sembilan kolom sehingga indeks model tidak
     * bergeser; hanya column model yang dibangun ulang.
     */
    private void aturKolomPajak(boolean adaPajak) {
        if (modePajak == adaPajak && tabel.getColumnModel().getColumnCount() > 0) {
            return;
        }
        modePajak = adaPajak;

        DefaultTableColumnModel kolom = new DefaultTableColumnModel();
        for (int i = 0; i < JUDUL_KOLOM.length; i++) {
            boolean kolomPajak = i == KOLOM_PPH || i == KOLOM_PPN || i == KOLOM_BERSIH;
            if (kolomPajak && !adaPajak) {
                continue;
            }
            TableColumn tabelKolom = new TableColumn(i);
            tabelKolom.setHeaderValue(JUDUL_KOLOM[i]);
            tabelKolom.setPreferredWidth(LEBAR_KOLOM[i]);
            kolom.addColumn(tabelKolom);
        }
        tabel.setColumnModel(kolom);
        tabel.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Menyusun ulang isi tabel berdasarkan data terbaru dari buku kas.
     */
    public void perbarui(BukuKas bukuKas) {
        Profile profil = bukuKas.getProfilAktif();
        aturKolomPajak(profil != null && profil.punyaPajak());

        long idTerpilih = -1;
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih >= 0 && barisTerpilih < transaksiTampil.size()) {
            idTerpilih = transaksiTampil.get(barisTerpilih).getId();
        }

        modelTabel.setRowCount(0);
        transaksiTampil = new ArrayList<>(bukuKas.getDaftarTransaksi());

        int nomor = 1;
        for (Transaksi transaksi : transaksiTampil) {
            modelTabel.addRow(new Object[]{
                    nomor++,
                    transaksi.getWaktuTerformat(),
                    transaksi.getJenis().getLabel(),
                    transaksi.getKategori().getLabel(),
                    transaksi.getDeskripsi(),
                    Transaksi.formatNominal(transaksi.getNominal()),
                    Transaksi.formatNominal(transaksi.getPph()),
                    Transaksi.formatNominal(transaksi.getPpn()),
                    Transaksi.formatNominal(transaksi.getNominalBersih()),
                    false
            });
        }
        perbaruiTombolPilihSemua();

        if (idTerpilih > 0) {
            for (int i = 0; i < transaksiTampil.size(); i++) {
                if (transaksiTampil.get(i).getId() == idTerpilih) {
                    tabel.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    private void hapusTransaksiTerpilih() {
        List<Transaksi> daftarHapus = new ArrayList<>();
        for (int baris = 0; baris < modelTabel.getRowCount(); baris++) {
            Object dipilih = modelTabel.getValueAt(baris, KOLOM_PILIH);
            if (Boolean.TRUE.equals(dipilih) && baris < transaksiTampil.size()) {
                daftarHapus.add(transaksiTampil.get(baris));
            }
        }

        if (daftarHapus.isEmpty()) {
            TemaUI.tampilkanPesan(this,
                    "Pilih minimal satu transaksi dengan mencentang kolom Pilih.",
                    "Belum Ada Pilihan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int jawaban = TemaUI.tampilkanKonfirmasi(this,
                "Hapus " + daftarHapus.size() + " transaksi yang dipilih?",
                "Konfirmasi Hapus Batch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (jawaban == JOptionPane.YES_OPTION) {
            for (Transaksi transaksi : daftarHapus) {
                bukuKas.hapusTransaksi(transaksi.getId());
            }
        }
    }

    private void pilihSemuaTransaksi() {
        if (modelTabel.getRowCount() == 0) {
            return;
        }
        boolean pilihSemua = jumlahTerpilih() != modelTabel.getRowCount();
        for (int baris = 0; baris < modelTabel.getRowCount(); baris++) {
            modelTabel.setValueAt(pilihSemua, baris, KOLOM_PILIH);
        }
        perbaruiTombolPilihSemua();
    }

    private int jumlahTerpilih() {
        int jumlah = 0;
        for (int baris = 0; baris < modelTabel.getRowCount(); baris++) {
            if (Boolean.TRUE.equals(modelTabel.getValueAt(baris, KOLOM_PILIH))) {
                jumlah++;
            }
        }
        return jumlah;
    }

    private void perbaruiTombolPilihSemua() {
        int jumlahBaris = modelTabel.getRowCount();
        if (jumlahBaris > 0 && jumlahTerpilih() == jumlahBaris) {
            tombolPilihSemua.setText("Batal Pilih Semua");
        } else {
            tombolPilihSemua.setText("Pilih Semua");
        }
    }

    void aturModeGelap(boolean aktif) {
        setBackground(TemaUI.warnaPanel());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaUI.warnaGaris()),
                " Daftar Transaksi ", TitledBorder.LEADING, TitledBorder.TOP,
                null, TemaUI.warnaTeks()));
        tabel.setBackground(TemaUI.warnaIsian());
        tabel.setForeground(TemaUI.warnaTeks());
        tabel.setSelectionBackground(aktif ? new Color(0x46536A) : new Color(0xBBDEFB));
        tabel.setSelectionForeground(TemaUI.warnaTeks());
        tabel.setGridColor(TemaUI.warnaGaris());
        tabel.getTableHeader().setBackground(TemaUI.warnaPanel());
        tabel.getTableHeader().setForeground(TemaUI.warnaTeks());
        tabel.getTableHeader().setOpaque(true);
        scrollPane.setBackground(TemaUI.warnaPanel());
        scrollPane.setBorder(BorderFactory.createLineBorder(TemaUI.warnaGaris()));
        scrollPane.getViewport().setBackground(TemaUI.warnaPanel());
        TemaUI.aturScrollbarGelap(scrollPane.getVerticalScrollBar(), aktif);
        TemaUI.aturScrollbarGelap(scrollPane.getHorizontalScrollBar(), aktif);
        pojokHeader.setBackground(TemaUI.warnaPanel());
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, pojokHeader);
        repaint();
    }

    private static class PerenderHeader extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabel, Object nilai, boolean terpilih,
                                                       boolean punyaFokus, int baris, int kolom) {
            super.getTableCellRendererComponent(tabel, nilai, terpilih, punyaFokus, baris, kolom);
            setOpaque(true);
            setBackground(TemaUI.warnaPanel());
            setForeground(TemaUI.warnaTeks());
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, TemaUI.warnaGaris()));
            setHorizontalAlignment(LEFT);
            return this;
        }
    }

    /**
     * Mewarnai baris sesuai jenis transaksi: hijau untuk uang masuk, merah untuk uang keluar.
     */
    private static class PerenderBaris extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabel, Object nilai, boolean terpilih,
                                                       boolean punyaFokus, int baris, int kolom) {
            Component komponen = super.getTableCellRendererComponent(
                    tabel, nilai, terpilih, punyaFokus, baris, kolom);

            Object jenis = tabel.getValueAt(baris, 2);
            boolean masuk = jenis != null && jenis.toString().equalsIgnoreCase("Uang Masuk");

            if (terpilih) {
                komponen.setBackground(tabel.getSelectionBackground());
                komponen.setForeground(tabel.getSelectionForeground());
            } else if (TemaUI.isModeGelap()) {
                komponen.setBackground(masuk ? new Color(0x17251C) : new Color(0x29191C));
                komponen.setForeground(masuk ? new Color(0x9AB99D) : new Color(0xC99A9D));
            } else {
                komponen.setBackground(masuk ? WARNA_BARIS_MASUK : WARNA_BARIS_KELUAR);
                komponen.setForeground(masuk ? WARNA_TEKS_MASUK : WARNA_TEKS_KELUAR);
            }

            if (kolom == 0 || kolom >= 5) {
                setHorizontalAlignment(RIGHT);
            } else {
                setHorizontalAlignment(LEFT);
            }
            return komponen;
        }
    }
}
