package org.yuemi.fsi2107.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.yuemi.fsi2107.model.JenisTransaksi;
import org.yuemi.fsi2107.model.Kategori;
import org.yuemi.fsi2107.storage.Penyimpanan;

/** Layanan CRUD untuk kategori transaksi. */
public class KategoriService {
  private final Penyimpanan penyimpanan;
  private final List<Consumer<KategoriService>> pendengar = new ArrayList<>();

  public KategoriService(Penyimpanan penyimpanan) {
    this.penyimpanan = Objects.requireNonNull(penyimpanan, "penyimpanan tidak boleh null");
    muatDariPenyimpanan();
  }

  public List<Kategori> getDaftarKategori() {
    return Collections.unmodifiableList(Kategori.values());
  }

  public List<Kategori> berdasarkanJenis(JenisTransaksi jenis) {
    return Kategori.berdasarkanJenis(jenis);
  }

  public Kategori tambah(String label, JenisTransaksi jenis) {
    String bersih = validasiLabel(label);
    Objects.requireNonNull(jenis, "jenis tidak boleh null");
    if (Kategori.cariBerdasarkanLabel(bersih) != null) {
      throw new IllegalArgumentException("Kategori dengan nama tersebut sudah ada.");
    }
    Kategori kategori = penyimpanan.simpanKategori(bersih, jenis);
    Kategori.daftararkan(kategori);
    beriTahuPendengar();
    return kategori;
  }

  public Kategori ubah(Kategori lama, String label, JenisTransaksi jenis) {
    Objects.requireNonNull(lama, "kategori tidak boleh null");
    String bersih = validasiLabel(label);
    Objects.requireNonNull(jenis, "jenis tidak boleh null");

    Kategori samaNama = Kategori.cariBerdasarkanLabel(bersih);
    if (samaNama != null && samaNama.getId() != lama.getId()) {
      throw new IllegalArgumentException("Kategori dengan nama tersebut sudah ada.");
    }

    Kategori kategoriBaru = penyimpanan.ubahKategori(lama.getId(), bersih, jenis);
    kategoriBaru = new Kategori(lama.getId(), lama.getKode(), bersih, jenis, lama.isBawaan());
    Kategori.ganti(lama.getId(), kategoriBaru);
    beriTahuPendengar();
    return kategoriBaru;
  }

  public boolean hapus(Kategori kategori) {
    Objects.requireNonNull(kategori, "kategori tidak boleh null");
    if (kategori.isBawaan()) {
      throw new IllegalArgumentException("Kategori bawaan tidak dapat dihapus.");
    }
    if (!penyimpanan.hapusKategori(kategori.getId())) {
      throw new IllegalArgumentException(
          "Kategori tidak dapat dihapus karena masih dipakai transaksi.");
    }
    Kategori.hapus(kategori.getId());
    beriTahuPendengar();
    return true;
  }

  public void tambahPendengar(Consumer<KategoriService> pendengarBaru) {
    pendengar.add(Objects.requireNonNull(pendengarBaru));
  }

  private void muatDariPenyimpanan() {
    List<Kategori> tersimpan = penyimpanan.muatSemuaKategori();
    Kategori.muatDaftar(tersimpan);
  }

  private static String validasiLabel(String label) {
    String bersih = label == null ? "" : label.trim();
    if (bersih.isEmpty()) {
      throw new IllegalArgumentException("Nama kategori belum diisi.");
    }
    if (bersih.length() > 50) {
      throw new IllegalArgumentException("Nama kategori maksimal 50 karakter.");
    }
    return bersih;
  }

  private void beriTahuPendengar() {
    for (Consumer<KategoriService> satuPendengar : List.copyOf(pendengar)) {
      satuPendengar.accept(this);
    }
  }
}
