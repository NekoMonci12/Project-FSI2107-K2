package org.yuemi.fsi2107.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.DefaultListCellRenderer;

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
