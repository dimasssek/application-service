package ru.kubsu.applicationservice.detector;

public class FileTypeDetector {
    public static String detect(byte[] file) {
        // Логика определения типа файла по заголовкам
        if (isPdf(file)) return "PDF";
        if (isJpeg(file)) return "JPEG";
        if (isText(file)) return "TXT";
        return "UNKNOWN";
    }
    
    private static boolean isPdf(byte[] file) {
        return file[0] == 0x25 && file[1] == 0x50 && file[2] == 0x44 && file[3] == 0x46;
    }
}