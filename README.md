# 🧭 Hayat Pusulası (Life Compass)

## 🎓 Akademik Bağlam
[cite_start]Bu proje, Siirt Üniversitesi Bilgisayar Mühendisliği Bölümü **"Mobil Programlama"** dersi final projesi olarak geliştirilmiştir[cite: 2, 4, 6].

## 🎯 Proje Vizyonu (Gamified Productivity)
[cite_start]Hayat Pusulası, kullanıcıların günlük görevlerini (To-Do) ve günlük tutma (Journal) alışkanlıklarını "oyunlaştırma" (gamification) tekniği ile birleştiren yenilikçi bir mobil uygulamadır[cite: 18]. [cite_start]Uygulama, kullanıcılara orta çağ (medieval) temalı bir atmosfer sunarak, sıkıcı olabilecek rutin işleri birer RPG (Rol Yapma Oyunu) macerasına dönüştürür[cite: 19]. [cite_start]Kullanıcılar görevleri tamamladıkça tecrübe puanı (XP) ve ödüller kazanarak motivasyonlarını artırırlar[cite: 20]. 

[cite_start]Bu mimari; motivasyon eksikliğini giderir, düzenli alışkanlık kazanılmasını sağlar ve kişisel üretkenliğin somut verilerle takip edilmesine olanak tanır[cite: 22, 23, 24].

## ⚙️ Teknik Mimari ve Sistem Özellikleri

[cite_start]Proje, sağlam bir veri katmanı ve gelişmiş kullanıcı deneyimi sunmak üzere aşağıdaki teknik altyapı ile inşa edilmiştir[cite: 26]:

* [cite_start]**Kimlik Doğrulama ve Güvenli Oturum (Login/Session):** Kullanıcı doğrulama işlemleri yerel SQLite veritabanındaki "users" tablosunda sorgulanarak gerçekleştirilir[cite: 29]. [cite_start]Uygulama içinde "Beni Hatırla" seçeneği ile oturum verileri SharedPreferences ("MedievalPrefs") üzerinde şifreli ve güvenli olarak saklanır, başarılı girişler "current_active_user" anahtarıyla oturuma mühürlenir[cite: 30, 32].
* [cite_start]**Merkezi Yönlendirme (Main Hub):** Ana menü, kullanıcıyı diğer modüllere yönlendiren merkezi bir router görevi görür ve "geri" tuşu (back stack) yaşam döngüsü yönetimini koordine eder[cite: 35, 36].
* [cite_start]**Dinamik Görev Zamanlayıcısı (Quests):** Görevler, SQLite "quests" tablosunda tutulur ve aktif kullanıcıya göre filtrelenerek RecyclerView üzerinde dinamik listelenir[cite: 39, 40]. [cite_start]Süreli görevler için arka planda her saniye tetiklenen (Runnable ve Handler altyapısı ile) bir zamanlayıcı sistemi kurularak arayüz eşzamanlı olarak güncellenir[cite: 41, 42].
* [cite_start]**Kognitif Günlük ve 3D Animasyonlar (Journal):** Günlük kayıtları, tarih ve kullanıcı adından oluşan bileşik anahtar (Composite Key) mantığıyla "journal" tablosunda saklanır[cite: 46]. [cite_start]Parşömen görünümü elde etmek için onDraw metodu ezilerek (override) "LinedEditText" adında özel bir arayüz (Custom View) sınıfı kodlanmıştır[cite: 48, 49]. [cite_start]Sayfa geçişleri, Android ObjectAnimator ve CameraDistance API'leri kullanılarak 3D kitap çevirme efektleriyle canlandırılmıştır[cite: 50, 51].
* [cite_start]**Analitik ve Seviye Algoritması (Stats):** Kullanıcı verileri, karmaşık SQL sorguları (Count, Filter) ile analiz edilir[cite: 54]. [cite_start]Başarı durumu, tamamlanan görev sayısına dayanan matematiksel bir "Level/XP" formülüyle hesaplanıp ProgressBar üzerinde görselleştirilir[cite: 55].
* [cite_start]**Otonom Kahin (Oracle):** RPG deneyimini güçlendirmek amacıyla, "Random" sınıfı entegrasyonuyla rastgele motivasyon sözleri ve kader çarkı algoritması geliştirilmiştir[cite: 58].
