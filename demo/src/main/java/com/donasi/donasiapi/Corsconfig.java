package com.donasi.donasiapi;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Konfigurasi Global untuk mengizinkan Cross-Origin Resource Sharing (CORS)
 * dari frontend yang berjalan di port selain 8080 (misalnya 3000, 5173, dll).
 */
@Configuration
public class Corsconfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Menerapkan konfigurasi CORS ke semua endpoint di bawah /api/
        registry.addMapping("/api/**")
                
                // Ganti dengan port/URL aktual tempat frontend Anda berjalan!
                // Contoh: Jika Anda menggunakan Live Server, mungkin cukup 127.0.0.1:5500
                // Atau, jika Anda menggunakan framework JS, mungkin http://localhost:3000 atau 5173.
                // Jika tidak yakin, gunakan "*" untuk Development (TIDAK disarankan untuk Production)
                .allowedOrigins("http://localhost:8080", "http://127.0.0.1:5500", "http://localhost:3000") // <- Sesuaikan ini

                // Mengizinkan semua HTTP methods yang digunakan oleh frontend (GET, POST, dll.)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                
                // Mengizinkan semua header
                .allowedHeaders("*") 
                
                // Penting jika Anda ingin mengirim cookies atau token otorisasi
                .allowCredentials(false); 
    }
}