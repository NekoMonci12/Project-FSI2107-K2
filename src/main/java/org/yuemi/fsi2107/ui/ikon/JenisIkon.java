package org.yuemi.fsi2107.ui.ikon;

import java.awt.Color;

/**
 * Daftar jenis ikon yang tersedia di aplikasi.
 *
 * <p>Setiap jenis tahu cara membangun ikonnya sendiri, sehingga pemanggil
 * cukup menyebut jenisnya tanpa perlu mengenal kelas konkret ikon.</p>
 */
public enum JenisIkon {

    /** Ikon bulan sabit untuk mode gelap. */
    BULAN {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonBulan(warna);
        }
    },

    /** Ikon matahari untuk mode terang. */
    MATAHARI {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonMatahari(warna);
        }
    },

    /** Ikon label kategori untuk penanda kelompok data. */
    KATEGORI {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonKategori(warna);
        }
    },

    /** Ikon pensil untuk tombol ubah data. */
    UBAH {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonUbah(warna);
        }
    },

    /** Ikon silang untuk tombol tutup. */
    TUTUP {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonTutup(warna);
        }
    },

    /** Ikon penambah data. */
    TAMBAH {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonTambah(warna);
        }
    },

    /** Ikon tong sampah. */
    HAPUS {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonHapus(warna);
        }
    },

    /** Ikon orang untuk pemilih profil. */
    PROFIL {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonProfil(warna);
        }
    },

    /** Ikon uang masuk. */
    UANG_MASUK {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonUangMasuk(warna);
        }
    },

    /** Ikon uang keluar. */
    UANG_KELUAR {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonUangKeluar(warna);
        }
    },

    /** Ikon centang untuk data terpilih. */
    CEKLIS {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonCeklis(warna);
        }
    },

    /** Ikon disket untuk tombol simpan. */
    SIMPAN {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonSimpan(warna);
        }
    },

    /** Ikon panah kecil ke bawah untuk tombol combo box. */
    PANAH {
        @Override
        public IkonDasar buat(Color warna) {
            return new IkonPanah(warna);
        }
    };

    /**
     * Membangun ikon dengan warna tertentu.
     *
     * @param warna warna goresan ikon
     * @return ikon siap pakai
     */
    public abstract IkonDasar buat(Color warna);

    /**
     * Membangun ikon dengan warna bawaan jenis ini.
     */
    public IkonDasar buat() {
        return buat(warnaBawaan());
    }

    /**
     * Warna bawaan yang paling kontras untuk jenis ikon ini.
     */
    public Color warnaBawaan() {
        return switch (this) {
            case BULAN -> new Color(0xEAF1FF);
            case MATAHARI -> new Color(0x7A5B00);
            case UANG_MASUK, CEKLIS -> new Color(0x2E7D32);
            case UANG_KELUAR, HAPUS -> new Color(0xC62828);
            default -> new Color(0x546E7A);
        };
    }
}
