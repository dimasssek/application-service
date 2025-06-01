package ru.kubsu.applicationservice.report;

@Service
public class ReportingService {
    
    // Генерация ежедневных отчетов
    public byte[] generateDailyReport() {
        List<ApplicationEntity> applications = applicationRepository.findByDate(LocalDate.now());
        
        // PDF отчет
        byte[] pdfReport = PdfGenerator.generate(applications);
        
        // XLSX отчет
        byte[] xlsxReport = ExcelGenerator.generate(applications);
        
        return chooseFormat(pdfReport, xlsxReport);
    }
    
    // Дашборд аналитики
    @Scheduled(fixedRate = 60000) // Обновление каждую минуту
    public DashboardData updateDashboard() {
        DashboardData data = new DashboardData();
        data.setApplicationsCount(applicationRepository.countToday());
        data.setApprovalRate(calculateApprovalRate());
        return data;
    }
}