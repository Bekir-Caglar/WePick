
const axios = require('axios');
const fs = require('fs');
const freekeys = require('freekeys');

// ================= KONFİGÜRASYON =================
// Daha fazla film için sayfa sayılarını arttırdık
const PAGE_LIMITS = {
    popular: 50,
    top_rated: 50,
    upcoming: 10,
    now_playing: 10
};

// Çıktı dosyasının adı
const CIKTI_DOSYASI = "wepick_movies_export.json";

// Aynı anda kaç istek atılsın? (Hızlandırmak için paralel istek sayısı)
const CONCURRENCY_LIMIT = 20;

// =================================================

const GENRE_MAPPING = {
    28: "ACTION",
    12: "ADVENTURE",
    16: "ANIMATION",
    35: "COMEDY",
    80: "CRIME",
    99: "DOCUMENTARY",
    18: "DRAMA",
    10751: "FAMILY",
    14: "FANTASY",
    36: "HISTORY",
    27: "HORROR",
    10402: "MUSIC",
    9648: "MYSTERY",
    10749: "ROMANCE",
    878: "SCI_FI",
    53: "THRILLER",
    10752: "WAR",
    37: "WESTERN"
};

// Basit bir concurrency kontrolcüsü
async function asyncPool(poolLimit, array, iteratorFn) {
    const ret = [];
    const executing = [];
    for (const item of array) {
        const p = Promise.resolve().then(() => iteratorFn(item, array));
        ret.push(p);

        if (poolLimit <= array.length) {
            const e = p.then(() => executing.splice(executing.indexOf(e), 1));
            executing.push(e);
            if (executing.length >= poolLimit) {
                await Promise.race(executing);
            }
        }
    }
    return Promise.all(ret);
}

async function getMovies() {
    try {
        console.log("API Anahtarı alınıyor...");
        const keys = await freekeys();
        const TMDB_API_KEY = keys.tmdb_key;

        if (!TMDB_API_KEY) throw new Error("TMDB Key bulunamadı!");

        console.log(`Key bulundu: ${TMDB_API_KEY.substring(0, 5)}...`);

        let allMoviesMap = {};
        let movieIdsToFetch = new Set(); // ID'leri burada toplayıp duplicatelemeyi önleyeceğiz

        // 1. ADIM: Listelerden SADECE ID'leri topla (Paralel)
        console.log("\n🚀 Sayfalar taranıyor ve film listeleri çekiliyor...");

        const endpoints = [
            { name: "top_rated", limit: PAGE_LIMITS.top_rated },
            { name: "popular", limit: PAGE_LIMITS.popular },
            { name: "upcoming", limit: PAGE_LIMITS.upcoming },
            { name: "now_playing", limit: PAGE_LIMITS.now_playing }
        ];

        let pageRequests = [];
        for (const endpoint of endpoints) {
            for (let page = 1; page <= endpoint.limit; page++) {
                pageRequests.push({ endpoint: endpoint.name, page: page });
            }
        }

        await asyncPool(CONCURRENCY_LIMIT, pageRequests, async ({ endpoint, page }) => {
            try {
                const listUrl = `https://api.themoviedb.org/3/movie/${endpoint}?api_key=${TMDB_API_KEY}&language=en-US&page=${page}`;
                const response = await axios.get(listUrl, { timeout: 10000 });
                const results = response.data.results || [];

                process.stdout.write("."); // İlerleme çubuğu gibi nokta koy

                results.forEach(m => movieIdsToFetch.add(m.id));
            } catch (err) {
                // Hata olursa çok dert etme, diğer sayfaya geç
                // console.error(`Hata: ${endpoint} p${page}`);
            }
        });

        console.log(`\n\n🎯 Toplam ${movieIdsToFetch.size} unique film ID'si bulundu. Detaylar çekiliyor...`);

        // 2. ADIM: Film Detaylarını Çek (Paralel)
        const allMovieIds = Array.from(movieIdsToFetch);

        await asyncPool(CONCURRENCY_LIMIT, allMovieIds, async (movieId) => {
            try {
                // Detay + Cast + External ID (IMDb ID için)
                const detailsUrl = `https://api.themoviedb.org/3/movie/${movieId}?api_key=${TMDB_API_KEY}&language=tr-TR&append_to_response=credits,external_ids`;
                const detailsResp = await axios.get(detailsUrl, { timeout: 10000 });
                const details = detailsResp.data;

                const imdbId = details.imdb_id || details.external_ids?.imdb_id;

                if (!imdbId) return;

                // Kategorileri eşleştir
                const tmdbGenres = details.genres || [];
                let myCategories = [];
                for (const g of tmdbGenres) {
                    if (GENRE_MAPPING[g.id]) myCategories.push(GENRE_MAPPING[g.id]);
                }

                if (myCategories.length === 0) return;

                // Cast, Director, Poster
                const cast = details.credits?.cast?.slice(0, 4).map(c => c.name).join(", ") || "";
                const director = details.credits?.crew?.find(c => c.job === 'Director')?.name || "";
                const poster = details.poster_path ? `https://image.tmdb.org/t/p/w500${details.poster_path}` : "";

                // Plot handling (TR fallback EN)
                let plot = details.overview;
                if (!plot) {
                    try {
                        const enDetails = await axios.get(`https://api.themoviedb.org/3/movie/${movieId}?api_key=${TMDB_API_KEY}&language=en-US`);
                        plot = enDetails.data.overview;
                    } catch (e) { }
                }

                const movieData = {
                    title: details.title,
                    imdbId: imdbId,
                    categories: myCategories,
                    plot: plot || "",
                    actors: cast,
                    director: director,
                    poster: poster,
                    year: details.release_date ? details.release_date.substring(0, 4) : "",
                    imdbRating: details.vote_average ? details.vote_average.toFixed(1).toString() : "",
                    language: "Turkish, English"
                };

                // Thread-safe assignment (JS is single threaded event loop, so this is safe)
                allMoviesMap[imdbId] = movieData;

                // Progress
                if (Object.keys(allMoviesMap).length % 50 === 0) {
                    process.stdout.write(`\r✅ ${Object.keys(allMoviesMap).length} film işlendi...`);
                }

            } catch (err) {
                // console.error(`Detay hatası ${movieId}: ${err.message}`);
            }
        });

        return allMoviesMap;

    } catch (error) {
        console.error("Genel Hata:", error);
        return {};
    }
}

async function main() {
    console.time("Toplam Süre");
    const moviesMap = await getMovies();
    const count = Object.keys(moviesMap).length;

    if (count > 0) {
        // Firebase export formatı
        const exportData = {
            movies: moviesMap
        };

        fs.writeFileSync(CIKTI_DOSYASI, JSON.stringify(exportData, null, 2), 'utf-8');
        console.log(`\n\n===========================================`);
        console.log(`✅ İŞLEM TAMAMLANDI!`);
        console.log(`🎬 Toplam ${count} unique film kaydedildi.`);
        console.log(`📁 Çıktı: ${CIKTI_DOSYASI}`);
        console.log(`===========================================`);
    } else {
        console.log("❌ Film kaydedilemedi.");
    }
    console.timeEnd("Toplam Süre");
}

main();
