package restoran.util;

import java.util.Locale;

/**
 * Kelas utilitas untuk memformat angka menjadi tampilan rupiah.
 * Tidak memiliki state, sehingga semua method bersifat static.
 */
public final class Rupiah {

    private static final Locale LOCALE_ID = Locale.forLanguageTag("id-ID");

    private Rupiah() {
        // Kelas utilitas tidak boleh dibuat objeknya.
    }

    /**
     * Mengubah angka menjadi teks rupiah, contoh 25000 menjadi "Rp25.000".
     *
     * @param nilai angka yang akan diformat
     * @return teks rupiah
     */
    public static String format(double nilai) {
        return "Rp" + String.format(LOCALE_ID, "%,.0f", nilai);
    }
}
