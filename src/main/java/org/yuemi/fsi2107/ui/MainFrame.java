package org.yuemi.fsi2107.ui;

import org.yuemi.fsi2107.service.BukuKas;
import org.yuemi.fsi2107.service.ProfileService;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Jendela utama aplikasi: menggabungkan pemilih profil, ringkasan, formulir input,
 * dan tabel transaksi.
 */
public class MainFrame extends JFrame {

    /** Tinggi minimum area tabel transaksi agar tetap bisa dibaca. */
    private static final int TINGGI_MINIMUM_TABEL = 170;

    private final BukuKas bukuKas;
    private final RingkasanPanel ringkasanPanel;
    private final TabelTransaksiPanel tabelTransaksiPanel;
    private final JSplitPane pemisah;
    private final FormTransaksiPanel formTransaksiPanel;
    private final ProfilPanel profilPanel;
    private final JPanel isi;
    private final SakelarTema tombolTema;

    public MainFrame(BukuKas bukuKas, ProfileService profileService) {
        super("Aplikasi Pencatatan Keuangan - Uang Masuk & Uang Keluar");
        this.bukuKas = bukuKas;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(760, 680));
        setLocationRelativeTo(null);

        ringkasanPanel = new RingkasanPanel();
        tabelTransaksiPanel = new TabelTransaksiPanel(bukuKas);
        formTransaksiPanel = new FormTransaksiPanel(bukuKas);
        profilPanel = new ProfilPanel(profileService);

        pemisah = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formTransaksiPanel, tabelTransaksiPanel);
        pemisah.setResizeWeight(0.0);
        pemisah.setBorder(BorderFactory.createEmptyBorder(0, 14, 14, 14));
        pemisah.setContinuousLayout(true);
        pemisah.setOpaque(true);
        pemisah.setBackground(TemaUI.warnaLatar());

        JPanel barisAtas = new JPanel(new BorderLayout());
        barisAtas.setOpaque(false);
        barisAtas.add(profilPanel, BorderLayout.CENTER);

        tombolTema = new SakelarTema(TemaUI.isModeGelap());
        tombolTema.addActionListener(e -> aturModeGelap(tombolTema.isSelected()));
        JPanel sisiTema = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sisiTema.setOpaque(false);
        sisiTema.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 14));
        sisiTema.add(tombolTema);
        barisAtas.add(sisiTema, BorderLayout.EAST);

        JPanel bagianAtas = new JPanel();
        bagianAtas.setLayout(new BoxLayout(bagianAtas, BoxLayout.Y_AXIS));
        bagianAtas.setOpaque(false);
        bagianAtas.add(barisAtas);
        bagianAtas.add(ringkasanPanel);

        isi = new JPanel(new BorderLayout());
        isi.setBackground(TemaUI.warnaLatar());
        isi.add(bagianAtas, BorderLayout.NORTH);
        isi.add(pemisah, BorderLayout.CENTER);

        setContentPane(isi);
        setSize(920, 700);
        aturModeGelap(TemaUI.isModeGelap());

        // Divider disesuaikan setiap kali jendela berubah ukuran agar tabel
        // transaksi selalu mendapat ruang minimum.
        pemisah.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent peristiwa) {
                aturDivider();
            }
        });
        SwingUtilities.invokeLater(this::aturDivider);

        bukuKas.tambahPendengar(this::segarkanTampilan);

        // Setiap perpindahan profil memuat ulang transaksi milik profil tersebut.
        profileService.tambahPendengar(layanan ->
                bukuKas.setProfilAktif(layanan.getProfilAktif()));

        bukuKas.setProfilAktif(profileService.getProfilAktif());
        segarkanTampilan(bukuKas);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent peristiwa) {
                tutupAplikasi();
            }
        });
    }

    /**
     * Menempatkan divider agar formulir pas dengan isinya, tetapi tabel
     * transaksi tetap mendapat ruang minimum pada jendela yang rendah.
     */
    private void aturDivider() {
        int tinggiSplit = pemisah.getHeight();
        if (tinggiSplit <= 0) {
            return;
        }
        int tinggiFormulir = Math.max(180, formTransaksiPanel.getPreferredSize().height);
        int batasAtas = tinggiSplit - TINGGI_MINIMUM_TABEL;
        pemisah.setDividerLocation(Math.max(120, Math.min(tinggiFormulir, batasAtas)));
    }

    private void segarkanTampilan(BukuKas bukuKas) {
        ringkasanPanel.perbarui(bukuKas);
        tabelTransaksiPanel.perbarui(bukuKas);
    }

    private void aturModeGelap(boolean aktif) {
        TemaUI.setModeGelap(aktif);
        tombolTema.setToolTipText(aktif ? "Kembali ke tampilan terang" : "Alihkan ke tampilan gelap");
        isi.setBackground(TemaUI.warnaLatar());
        getRootPane().setBackground(TemaUI.warnaLatar());
        pemisah.setBackground(TemaUI.warnaLatar());
        if (aktif) {
            pemisah.setUI(new BasicSplitPaneUI() {
                @Override
                public BasicSplitPaneDivider createDefaultDivider() {
                    return new BasicSplitPaneDivider(this) {
                        @Override
                        public void paint(Graphics graphics) {
                            graphics.setColor(TemaUI.warnaLatar());
                            graphics.fillRect(0, 0, getWidth(), getHeight());
                        }
                    };
                }
            });
        } else {
            pemisah.updateUI();
        }
        profilPanel.aturModeGelap(aktif);
        ringkasanPanel.aturModeGelap(aktif);
        formTransaksiPanel.aturModeGelap(aktif);
        tabelTransaksiPanel.aturModeGelap(aktif);
        isi.repaint();
    }

    /**
     * Menutup jendela, menutup lapisan penyimpanan data, lalu mengakhiri aplikasi.
     */
    private void tutupAplikasi() {
        try {
            bukuKas.tutup();
        } finally {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Menyusun dan menampilkan jendela utama pada event dispatch thread
     * memakai tampilan bawaan sistem.
     */
    public static void jalankan(BukuKas bukuKas, ProfileService profileService) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    // Tampilan bawaan Java tetap dipakai bila tema sistem tidak tersedia.
                }
                new MainFrame(bukuKas, profileService).setVisible(true);
            });
        } catch (Exception ex) {
            throw new IllegalStateException("Gagal menjalankan jendela utama aplikasi.", ex);
        }
    }
}
