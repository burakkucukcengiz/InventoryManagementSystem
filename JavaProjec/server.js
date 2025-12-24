// server.js
const express = require('express');
const cors = require('cors');
const path = require('path');
const nodemailer = require('nodemailer'); 
const app = express();
const PORT = process.env.PORT || 3000;


app.use(cors());

// Middleware (Ara Yazılımlar):
app.use(express.json()); // Frontend'den gelen JSON verilerini okumamızı sağlar.

// Statik Dosya Sunucusu: public klasöründeki her şeyi (HTML, CSS, görseller) sunar.
app.use(express.static(path.join(__dirname, 'public'))); 

// --- SİMÜLE EDİLMİŞ VERİTABANI VERİSİ ---
// Frontend, projeleri çekerken buradan faydalanacak.
const projeler = [
    {
        id: 1,
        baslik: "Modern E-Ticaret Platformu 1",
        aciklama: "React ve Node.js/Express kullanarak geliştirilmiş full-stack uygulama.",
        teknolojiler: ["React", "Node.js", "MongoDB", "Redux"],
        link: "https://www.ornekproje.com",
        resim: "images/ecommerce.jpg"
    },
    {
        id: 2,
        baslik: "Gerçek Zamanlı Sohbet Uygulaması 2",
        aciklama: "WebSockets (Socket.IO) ile anlık mesajlaşma, Vue.js ile arayüz.",
        teknolojiler: ["Vue.js", "Socket.IO", "Express", "PostgreSQL"],
        link: "https://www.ornekproje.com",
        resim: "images/chat.jpg"
    },
    {
        id: 3,
        baslik: "Bulut Tabanlı CRM Yönetim Paneli 3",
        aciklama: "Müşteri ilişkileri yönetimi (CRM) için tasarlanmış, veri görselleştirme içeren yönetim paneli.",
        teknolojiler: ["Python", "Django", "AWS", "Docker"],
        link: "https://www.ornekproje.com",
        resim: "images/crm.jpg"
    }
];

// --- ANA SAYFA YÖNLENDİRMESİ (Hata Düzeltme) ---
// Tarayıcıdan gelen ana sayfa (http://localhost:3000/) isteğine yanıt verir.
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// --- API UÇ NOKTASI 1: Projeleri Çekme ---
app.get('/api/projeler', (req, res) => {
    res.json(projeler);
});

// --- API UÇ NOKTASI 2: İletişim Formu (Nodemailer) ---
app.post('/api/iletisim', async (req, res) => {
    const { name, email, message } = req.body;

    // 1. Mail Göndericisini Tanımlama
    let transporter = nodemailer.createTransport({
        service: 'gmail', 
        auth: {
            user: 'burakkucukcengiz10@gmail.com', // ⚠️ Buraya kendi mailini yaz
            pass: 'Bcan.1931' // ⚠️ BURAYA GMAIL UYGULAMA ŞİFRESİNİ YAZ
        }
    });

    // 2. Mail İçeriği
    let mailOptions = {
        from: `"${name}" <${email}>`, 
        to: 'SENIN_GMAIL_ADRESIN@gmail.com', 
        subject: `[PORTFÖY MESAJI] Yeni Mesaj Gönderen: ${name}`,
        html: `
            <h3>Yeni İletişim Formu Mesajı</h3>
            <p><strong>Kimden:</strong> ${name}</p>
            <p><strong>E-posta:</strong> ${email}</p>
            <p><strong>Mesaj:</strong></p>
            <p style="border: 1px solid #ccc; padding: 10px;">${message}</p>
        `
    };

    try {
        await transporter.sendMail(mailOptions);
        res.status(200).json({ success: true, message: "Mesajınız başarıyla iletildi! Mail kutunuzu kontrol edin." });
    } catch (error) {
        console.error("Mail Gönderme Hatası:", error);
        res.status(500).json({ success: false, message: "Sunucu hatası: Mail gönderimi başarısız. Bilgileri kontrol edin." });
    }
});


// Sunucuyu Başlat
app.listen(PORT, () => {
    console.log(`🚀 Full-Stack Sunucu http://localhost:${PORT} adresinde çalışıyor!`);
});