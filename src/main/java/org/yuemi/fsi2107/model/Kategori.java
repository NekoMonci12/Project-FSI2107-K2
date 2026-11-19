package org.yuemi.fsi2107.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Kategori transaksi yang dapat dikelola pengguna. */
public class Kategori {
    private static final Map<String, Kategori> DAFTAR = new LinkedHashMap<>();

    private final long id;
    private final String kode;
    private final String label;
    private final JenisTransaksi jenis;
    private final boolean bawaan;

    public Kategori(long id, String kode, String label, JenisTransaksi jenis, boolean bawaan) {
        this.id = id;
        this.kode = Objects.requireNonNull(kode).trim();
        this.label = Objects.requireNonNull(label).trim();
        this.jenis = Objects.requireNonNull(jenis);
        this.bawaan = bawaan;
    }

    public long getId() { return id; }
    public String getKode() { return kode; }
    public String getLabel() { return label; }
    public JenisTransaksi getJenis() { return jenis; }
    public boolean isBawaan() { return bawaan; }

    public static void muatDaftar(List<Kategori> kategori) {
        DAFTAR.clear();
        for (Kategori item : kategori) {
            DAFTAR.put(kunci(item.kode), item);
        }
    }

    public static void daftararkan(Kategori kategori) {
        DAFTAR.put(kunci(kategori.kode), kategori);
    }

    public static void ganti(long id, Kategori kategoriBaru) {
        DAFTAR.values().removeIf(item -> item.id == id);
        daftararkan(kategoriBaru);
    }

    public static void hapus(long id) {
        DAFTAR.values().removeIf(item -> item.id == id);
    }

    public static List<Kategori> values() {
        return new ArrayList<>(DAFTAR.values());
    }

    public static List<Kategori> berdasarkanJenis(JenisTransaksi jenis) {
        List<Kategori> hasil = new ArrayList<>();
        for (Kategori kategori : DAFTAR.values()) {
            if (kategori.jenis == jenis) hasil.add(kategori);
        }
        return hasil;
    }

    public static Kategori cariBerdasarkanLabel(String label) {
        String bersih = label == null ? "" : label.trim();
        for (Kategori kategori : DAFTAR.values()) {
            if (kategori.label.equalsIgnoreCase(bersih)) return kategori;
        }
        return null;
    }

    public static Kategori cariBerdasarkanKode(String kode) {
        return DAFTAR.get(kunci(kode));
    }

    private static String kunci(String kode) {
        return kode == null ? "" : kode.trim().toUpperCase(Locale.ROOT);
    }

    @Override
    public String toString() { return label; }

    @Override
    public boolean equals(Object objek) {
        return objek instanceof Kategori kategori && id == kategori.id;
    }

    @Override
    public int hashCode() { return Long.hashCode(id); }
}
