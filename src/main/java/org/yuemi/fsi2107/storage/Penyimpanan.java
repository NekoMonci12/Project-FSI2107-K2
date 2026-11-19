package org.yuemi.fsi2107.storage;

import org.yuemi.fsi2107.model.JenisTransaksi;
import org.yuemi.fsi2107.model.Kategori;
import org.yuemi.fsi2107.model.Profile;
import org.yuemi.fsi2107.model.Transaksi;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Kontrak lapisan penyimpanan data transaksi dan profil.
 *
 * <p>Perintah SQL berbeda antar basis data, namun seluruh aplikasi
 * hanya mengenal antarmuka ini sehingga berganti backend tidak menyentuh
 * lapisan layanan maupun antarmuka pengguna.</p>
 */
public interface Penyimpanan extends AutoCloseable {

    /**
     * Menyiapkan struktur penyimpanan, misalnya membuat tabel bila belum ada.
     */
    void siapkan();

    /**
     * Membaca seluruh transaksi milik sebuah profil.
     */
    List<Transaksi> muatSemua(long profileId);

    /**
     * Membaca seluruh profil yang tersimpan.
     */
    List<Profile> muatSemuaProfile();

    /**
     * Menyimpan satu transaksi baru pada sebuah profil dan mengembalikan objek
     * transaksi beserta id yang diberikan basis data.
     *
     * @param pph PPh yang dipotong dari transaksi
     * @param ppn PPN yang dipotong atau ditambahkan
     */
    Transaksi simpan(long profileId, JenisTransaksi jenis, Kategori kategori, String deskripsi,
                     long nominal, LocalDateTime waktu, long pph, long ppn);

    /**
     * Menyimpan satu profil baru dan mengembalikan objek profil beserta id yang
     * diberikan basis data.
     */
    Profile simpanProfile(Profile profile);

    /**
     * Menghapus satu transaksi berdasarkan id. Mengembalikan {@code true} bila
     * ada transaksi yang terhapus.
     */
    boolean hapus(long id);

    /**
     * Menghapus seluruh transaksi milik sebuah profil dan mengembalikan
     * jumlah baris yang berhasil dihapus.
     */
    int hapusSemua(long profileId);

    /**
     * Menghapus sebuah profil beserta seluruh transaksinya.
     * Mengembalikan {@code true} bila ada profil yang terhapus.
     */
    boolean hapusProfile(long id);

    /** Membaca seluruh kategori yang tersimpan. */
    List<Kategori> muatSemuaKategori();

    /** Menyimpan kategori baru. */
    Kategori simpanKategori(String label, JenisTransaksi jenis);

    /** Mengubah nama dan jenis kategori. */
    Kategori ubahKategori(long id, String label, JenisTransaksi jenis);

    /** Menghapus kategori bila tidak dipakai transaksi. */
    boolean hapusKategori(long id);

    /**
     * Melepaskan seluruh sumber daya yang dipakai penyimpanan.
     */
    @Override
    void close();
}
