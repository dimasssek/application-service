package ru.kubsu.applicationservice.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.detector.FileTypeDetector;
import ru.kubsu.applicationservice.document.generator.DocumentGenerator;
import ru.kubsu.applicationservice.report.ReportingService;
import ru.kubsu.applicationservice.security.SecurityService;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private DocumentGenerator documentGenerator;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private ReportingService reportingService;

    // Прием и валидация заявок
    public ApplicationResponse processApplication(ApplicationRequest request) {
        // Валидация формата
        validateFileFormat(request.getDocument());
        
        // Обработка в течение ≤5 минут
        CompletableFuture.supplyAsync(() -> {
            ApplicationEntity entity = new ApplicationEntity();
            entity.setData(request.getData());
            entity.setDocument(request.getDocument());
            return applicationRepository.save(entity);
        }).orTimeout(5, TimeUnit.MINUTES);
        
        return new ApplicationResponse("Заявка принята на обработку");
    }
    
    private void validateFileFormat(byte[] file) {
        String fileType = FileTypeDetector.detect(file);
        if (!Arrays.asList("PDF", "JPEG", "TXT").contains(fileType)) {
            throw new InvalidFileFormatException("Неподдерживаемый формат файла");
        }
    }
}