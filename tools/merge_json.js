
const fs = require('fs');

// Dosya isimleri
const ORIGINAL_EXPORT_FILE = 'original_export.json';
const MOVIES_EXPORT_FILE = 'wepick_movies_export.json';
const FINAL_OUTPUT_FILE = 'wepick_kombine_export.json';

try {
    console.log("Dosyalar okunuyor...");

    // Orijinal export dosyasını oku (içinde 'likes', 'rooms' vb. olabilir)
    // Eğer dosya yoksa boş obje kabul et
    let originalData = {};
    if (fs.existsSync(ORIGINAL_EXPORT_FILE)) {
        const rawOriginal = fs.readFileSync(ORIGINAL_EXPORT_FILE, 'utf-8');
        originalData = JSON.parse(rawOriginal);
    } else {
        console.warn(`UYARI: ${ORIGINAL_EXPORT_FILE} bulunamadı, sadece filmlerle oluşturulacak.`);
    }

    // Yeni çektiğimiz film datalarını oku
    const rawMovies = fs.readFileSync(MOVIES_EXPORT_FILE, 'utf-8');
    const newMoviesData = JSON.parse(rawMovies);

    // ================= BİRLEŞTİRME İŞLEMİ =================

    // Orijinal datadaki 'movies' kısmını tamamen yeni veriyle eziyoruz.
    // Ancak diğer tüm düğümleri (likes, users, rooms vb.) koruyoruz.

    // NOT: newMoviesData yapısı zaten { "movies": { ... } } şeklinde geliyor (scriptimiz öyle üretti).
    // O yüzden direkt assign edebiliriz.

    // Önceki filmleri temizle (istersen merge de yapabilirsin ama 'hariç' dedin)
    // Kullanıcı talebi: "export jsondaki movies hariç kısımları al" -> Yani movies'i tamamen değiştiriyoruz.

    // Yeni yapıyı oluştur
    const finalData = {
        ...originalData,           // Eskideki her şeyi al (likes, users, vb.)
        movies: newMoviesData.movies // Movies'i yenisiyle değiştir
    };

    // Dosyayı yaz
    fs.writeFileSync(FINAL_OUTPUT_FILE, JSON.stringify(finalData, null, 2), 'utf-8');

    console.log(`\n✅ BİRLEŞTİRME BAŞARILI!`);
    console.log(`📂 Oluşturulan Dosya: ${FINAL_OUTPUT_FILE}`);
    console.log(`ℹ️  Bu dosya hem eski verilerini (likes, users) hem de yeni 2000 filmi içeriyor.`);
    console.log(`🔥 Firebase'e yüklerken bu dosyayı kullanabilirsin.`);

} catch (error) {
    console.error("HATA OLUŞTU:", error.message);
}
