package org.yuemi.fsi2107.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.DefaultListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * Palet warna bersama untuk mode terang dan gelap.
 */
final class TemaUI {

    private static boolean modeGelap;

    private TemaUI() {
    }

    static void setModeGelap(boolean aktif) {
        modeGelap = aktif;
        UIManager.put("OptionPane.background", warnaPanel());
        UIManager.put("OptionPane.messageForeground", warnaTeks());
        UIManager.put("Panel.background", warnaPanel());
        UIManager.put("Button.background", aktif ? new Color(0x252A33) : Color.WHITE);
        UIManager.put("Button.foreground", warnaTeks());
    }

    static boolean isModeGelap() {
        return modeGelap;
    }

    static void tampilkanPesan(Component induk, Object pesan, String judul, int tipe) {
        if (!modeGelap) {
            JOptionPane.showMessageDialog(induk, pesan, judul, tipe);
            return;
        }
        tampilkanDialog(induk, pesan, judul, tipe, JOptionPane.DEFAULT_OPTION);
    }

    static int tampilkanKonfirmasi(Component induk, Object pesan, String judul,
                                    int pilihan, int tipe) {
        if (!modeGelap) {
            return JOptionPane.showConfirmDialog(induk, pesan, judul, pilihan, tipe);
        }
        return tampilkanDialog(induk, pesan, judul, tipe, pilihan);
    }

    private static int tampilkanDialog(Component induk, Object pesan, String judul,
                                       int tipe, int pilihan) {
        JOptionPane panelPesan = new JOptionPane(pesan, tipe, pilihan);
        panelPesan.setUI(new BasicOptionPaneUI());
        panelPesan.setIcon(null);
        panelPesan.setBackground(warnaPanel());
        panelPesan.setForeground(warnaTeks());
        panelPesan.setOpaque(true);

        Window pemilik = induk instanceof Window window
                ? window : induk == null ? null : SwingUtilities.getWindowAncestor(induk);
        JDialog dialog = new JDialog(pemilik, judul, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel bilahJudul = buatBilahJudul(judul, () -> {
            panelPesan.setValue(JOptionPane.CLOSED_OPTION);
            dialog.dispose();
        });

        JPanel isiDialog = new JPanel(new BorderLayout());
        isiDialog.setBackground(warnaPanel());
        isiDialog.setBorder(BorderFactory.createLineBorder(warnaGaris()));
        isiDialog.add(bilahJudul, BorderLayout.NORTH);
        isiDialog.add(panelPesan, BorderLayout.CENTER);
        dialog.setContentPane(isiDialog);
        aturKomponenDialog(panelPesan);

        panelPesan.addPropertyChangeListener(event -> {
            if (JOptionPane.VALUE_PROPERTY.equals(event.getPropertyName())
                    && dialog.isVisible()
                    && panelPesan.getValue() != JOptionPane.UNINITIALIZED_VALUE) {
                dialog.dispose();
            }
        });
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "tutupDialog");
        dialog.getRootPane().getActionMap().put("tutupDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                panelPesan.setValue(JOptionPane.CLOSED_OPTION);
                dialog.dispose();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(induk);
        dialog.setVisible(true);
        Object nilai = panelPesan.getValue();
        return nilai instanceof Integer ? (Integer) nilai : JOptionPane.CLOSED_OPTION;
    }

    static void siapkanDekorasiGelap(JDialog dialog) {
        if (modeGelap) {
            dialog.setUndecorated(true);
        }
    }

    static void aturDialogGelap(JDialog dialog, String judul) {
        if (!modeGelap) {
            return;
        }

        Container isiAsli = dialog.getContentPane();
        JPanel isiDialog = new JPanel(new BorderLayout());
        isiDialog.setBackground(warnaPanel());
        isiDialog.setBorder(BorderFactory.createLineBorder(warnaGaris()));
        isiDialog.add(buatBilahJudul(judul, dialog::dispose), BorderLayout.NORTH);
        isiDialog.add(isiAsli, BorderLayout.CENTER);
        dialog.setContentPane(isiDialog);
        dialog.setBackground(warnaPanel());
        aturKomponenDialog(isiDialog);
    }

    private static JPanel buatBilahJudul(String judul, Runnable tutup) {
        JPanel bilahJudul = new JPanel(new BorderLayout());
        bilahJudul.setBackground(new Color(0x14181E));
        bilahJudul.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 6));
        JLabel teksJudul = new JLabel(judul);
        teksJudul.setForeground(warnaTeks());
        bilahJudul.add(teksJudul, BorderLayout.CENTER);

        JButton tombolTutup = new JButton("x");
        aturTombolDialog(tombolTutup);
        tombolTutup.addActionListener(event -> tutup.run());
        bilahJudul.add(tombolTutup, BorderLayout.EAST);
        return bilahJudul;
    }

    private static void aturKomponenDialog(Component komponen) {
        if (komponen instanceof JPanel panel) {
            panel.setOpaque(true);
            panel.setBackground(warnaPanel());
        } else if (komponen instanceof TombolBerwarna) {
            return;
        } else if (komponen instanceof JLabel label) {
            label.setForeground(warnaTeks());
        } else if (komponen instanceof JButton tombol) {
            aturTombolDialog(tombol);
        } else if (komponen instanceof JComboBox<?> combo) {
            aturComboBox(combo, true);
        } else if (komponen instanceof JTextField teks) {
            aturTextField(teks, true);
        } else if (komponen instanceof JComponent jKomponen) {
            jKomponen.setOpaque(true);
            jKomponen.setBackground(warnaPanel());
            jKomponen.setForeground(warnaTeks());
        }

        if (komponen instanceof Container wadah) {
            for (Component anak : wadah.getComponents()) {
                aturKomponenDialog(anak);
            }
        }
    }

    private static void aturTombolDialog(JButton tombol) {
        tombol.setUI(new BasicButtonUI());
        tombol.setBackground(new Color(0x252A33));
        tombol.setForeground(warnaTeks());
        tombol.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(warnaGaris()),
                BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        tombol.setOpaque(true);
        tombol.setContentAreaFilled(true);
        tombol.setFocusPainted(false);
    }

    static Color warnaLatar() {
        return modeGelap ? new Color(0x101216) : new Color(0xF5F7F8);
    }

    static Color warnaPanel() {
        return modeGelap ? new Color(0x171B22) : Color.WHITE;
    }

    static Color warnaKartu() {
        return modeGelap ? new Color(0x1B2028) : Color.WHITE;
    }

    static Color warnaTeks() {
        return modeGelap ? new Color(0xD1D5DC) : new Color(0x263238);
    }

    static Color warnaTeksSekunder() {
        return modeGelap ? new Color(0x969EAB) : new Color(0x546E7A);
    }

    static Color warnaGaris() {
        return modeGelap ? new Color(0x2A303A) : new Color(0xCFD8DC);
    }

    static Color warnaIsian() {
        return modeGelap ? new Color(0x11151B) : Color.WHITE;
    }

    static void aturComboBox(JComboBox<?> combo, boolean aktif) {
        Object modeSebelumnya = combo.getClientProperty("tema.modeGelap");
        if (!(modeSebelumnya instanceof Boolean) || (Boolean) modeSebelumnya != aktif) {
            if (aktif) {
                combo.setUI(new BasicComboBoxUI() {
                    @Override
                    public void paintCurrentValueBackground(Graphics graphics, Rectangle bounds,
                                                            boolean hasFocus) {
                        graphics.setColor(warnaIsian());
                        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                    }

                    @Override
                    protected JButton createArrowButton() {
                        JButton tombolPanah = new JButton();
                        tombolPanah.setUI(new javax.swing.plaf.basic.BasicButtonUI());
                        tombolPanah.setIcon(ikonPanah());
                        tombolPanah.setFocusable(false);
                        tombolPanah.setBorder(BorderFactory.createEmptyBorder());
                        tombolPanah.setContentAreaFilled(false);
                        tombolPanah.setBorderPainted(false);
                        tombolPanah.setBackground(warnaIsian());
                        tombolPanah.setForeground(warnaTeks());
                        tombolPanah.setOpaque(false);
                        tombolPanah.setPreferredSize(new java.awt.Dimension(20, 18));
                        return tombolPanah;
                    }

                    @Override
                    protected ComboPopup createPopup() {
                        BasicComboPopup popup = new BasicComboPopup(comboBox) {
                            @Override
                            protected JScrollPane createScroller() {
                                JScrollPane scroller = super.createScroller();
                                scroller.setBackground(warnaPanel());
                                scroller.setBorder(BorderFactory.createLineBorder(warnaGaris()));
                                scroller.getViewport().setBackground(warnaIsian());
                                list.setBackground(warnaIsian());
                                list.setForeground(warnaTeks());
                                aturScrollbarGelap(scroller.getVerticalScrollBar(), true);
                                return scroller;
                            }
                        };
                        popup.setOpaque(true);
                        popup.setBackground(warnaPanel());
                        popup.setBorder(BorderFactory.createLineBorder(warnaGaris()));
                        popup.getList().setBackground(warnaIsian());
                        popup.getList().setForeground(warnaTeks());
                        return popup;
                    }
                });
            } else {
                combo.updateUI();
            }
            combo.putClientProperty("tema.modeGelap", aktif);
        }

        combo.setOpaque(true);
        combo.setBackground(warnaIsian());
        combo.setForeground(warnaTeks());
        combo.setBorder(BorderFactory.createLineBorder(warnaGaris()));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> daftar, Object nilai, int indeks,
                                                           boolean terpilih, boolean punyaFokus) {
                super.getListCellRendererComponent(daftar, nilai, indeks, terpilih, punyaFokus);
                setOpaque(true);
                setBackground(terpilih
                        ? (modeGelap ? new Color(0x252E3B) : new Color(0xBBDEFB))
                        : warnaIsian());
                setForeground(terpilih && !modeGelap
                        ? new Color(0x263238) : warnaTeks());
                if (modeGelap) {
                    setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
                }
                return this;
            }
        });
    }

    static void aturTextField(JTextField teks, boolean aktif) {
        Object borderAsli = teks.getClientProperty("tema.borderAsli");
        if (borderAsli == null) {
            borderAsli = teks.getBorder();
            teks.putClientProperty("tema.borderAsli", borderAsli);
        }

        teks.setOpaque(true);
        teks.setBackground(warnaIsian());
        teks.setForeground(warnaTeks());
        teks.setCaretColor(warnaTeks());
        teks.setSelectionColor(aktif ? new Color(0x303A49) : new Color(0xB3D4FC));
        teks.setBorder(aktif
                ? BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(warnaGaris()),
                        BorderFactory.createEmptyBorder(2, 4, 2, 4))
                : (Border) borderAsli);
    }

    static void aturScrollbarGelap(JScrollBar scrollbar, boolean aktif) {
        if (aktif) {
            scrollbar.setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    thumbColor = new Color(0x343B46);
                    trackColor = warnaPanel();
                }

                @Override
                protected JButton createDecreaseButton(int orientasi) {
                    return buatTombolKosong();
                }

                @Override
                protected JButton createIncreaseButton(int orientasi) {
                    return buatTombolKosong();
                }
            });
        } else {
            scrollbar.updateUI();
        }
        scrollbar.setBackground(warnaPanel());
        scrollbar.setForeground(warnaTeksSekunder());
        scrollbar.setOpaque(true);
    }

    private static JButton buatTombolKosong() {
        JButton tombol = new JButton();
        tombol.setPreferredSize(new java.awt.Dimension(0, 0));
        tombol.setBorderPainted(false);
        tombol.setOpaque(false);
        return tombol;
    }

    private static Icon ikonPanah() {
        return new Icon() {
            @Override
            public void paintIcon(Component komponen, Graphics graphics, int x, int y) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setColor(modeGelap ? new Color(0x8B929D) : new Color(0x546E7A));
                int tengah = x + getIconWidth() / 2;
                int atas = y + getIconHeight() / 2 - 1;
                g2.drawLine(tengah - 3, atas, tengah, atas + 3);
                g2.drawLine(tengah, atas + 3, tengah + 3, atas);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 10;
            }

            @Override
            public int getIconHeight() {
                return 8;
            }
        };
    }
}
