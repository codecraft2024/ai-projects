package com.instasimulator.reports;

import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.common.util.IdGenerator;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.reports.model.ExecutionReport;
import com.instasimulator.stress.stats.StressStatistics;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates HTML, CSV, and JSON execution reports.
 */
@Component
public class ReportGenerator {

    public ExecutionReport build(List<ScenarioResult> results, StressStatistics.Snapshot stress) {
        List<Long> durations = results.stream().map(ScenarioResult::getDurationMs).sorted().toList();
        long success = results.stream().filter(ScenarioResult::isSuccess).count();
        long failed = results.size() - success;
        long avg = durations.isEmpty() ? 0 : durations.stream().mapToLong(Long::longValue).sum() / durations.size();

        return ExecutionReport.builder()
                .reportId(IdGenerator.shortId())
                .generatedAt(Instant.now())
                .title("InstaSimulator Execution Report")
                .scenarioResults(new ArrayList<>(results))
                .stressSnapshot(stress)
                .totalScenarios(results.size())
                .successfulScenarios(success)
                .failedScenarios(failed)
                .averageDurationMs(avg)
                .p50(percentile(durations, 50))
                .p90(percentile(durations, 90))
                .p95(percentile(durations, 95))
                .p99(percentile(durations, 99))
                .build();
    }

    public Path writeJson(ExecutionReport report, Path directory) throws IOException {
        Files.createDirectories(directory);
        Path file = directory.resolve("report-" + report.getReportId() + ".json");
        Files.writeString(file, JsonUtils.toJson(report), StandardCharsets.UTF_8);
        return file;
    }

    public Path writeCsv(ExecutionReport report, Path directory) throws IOException {
        Files.createDirectories(directory);
        Path file = directory.resolve("report-" + report.getReportId() + ".csv");
        StringBuilder sb = new StringBuilder("executionId,scenario,status,durationMs,userId,correlationId\n");
        for (ScenarioResult result : report.getScenarioResults()) {
            sb.append(result.getExecutionId()).append(',')
                    .append(result.getScenarioName()).append(',')
                    .append(result.getStatus()).append(',')
                    .append(result.getDurationMs()).append(',')
                    .append(result.getUserId()).append(',')
                    .append(result.getCorrelationId()).append('\n');
        }
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
        return file;
    }

    public Path writeHtml(ExecutionReport report, Path directory) throws IOException {
        Files.createDirectories(directory);
        Path file = directory.resolve("report-" + report.getReportId() + ".html");
        String rows = report.getScenarioResults().stream()
                .map(r -> "<tr><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%s</td></tr>"
                        .formatted(r.getExecutionId(), r.getScenarioName(), r.getStatus(),
                                r.getDurationMs(), r.getCorrelationId()))
                .collect(Collectors.joining("\n"));

        String html = """
                <!DOCTYPE html>
                <html><head><meta charset="UTF-8"><title>%s</title>
                <style>
                body{font-family:Segoe UI,Arial,sans-serif;margin:2rem;background:#f7f8fa;color:#1f2937}
                h1{margin-bottom:.25rem} .meta{color:#6b7280;margin-bottom:1.5rem}
                .cards{display:flex;gap:1rem;flex-wrap:wrap;margin-bottom:1.5rem}
                .card{background:#fff;border:1px solid #e5e7eb;border-radius:8px;padding:1rem 1.25rem;min-width:140px}
                .card b{display:block;font-size:1.4rem}
                table{width:100%%;border-collapse:collapse;background:#fff}
                th,td{border:1px solid #e5e7eb;padding:.6rem;text-align:left;font-size:.92rem}
                th{background:#111827;color:#fff}
                </style></head><body>
                <h1>%s</h1>
                <div class="meta">Generated %s · Report %s</div>
                <div class="cards">
                  <div class="card"><span>Total</span><b>%d</b></div>
                  <div class="card"><span>Success</span><b>%d</b></div>
                  <div class="card"><span>Failed</span><b>%d</b></div>
                  <div class="card"><span>Avg ms</span><b>%d</b></div>
                  <div class="card"><span>P95</span><b>%d</b></div>
                </div>
                <table><thead><tr><th>Execution</th><th>Scenario</th><th>Status</th><th>Duration</th><th>Correlation</th></tr></thead>
                <tbody>%s</tbody></table>
                </body></html>
                """.formatted(
                report.getTitle(), report.getTitle(), report.getGeneratedAt(), report.getReportId(),
                report.getTotalScenarios(), report.getSuccessfulScenarios(), report.getFailedScenarios(),
                report.getAverageDurationMs(), report.getP95(), rows);

        Files.writeString(file, html, StandardCharsets.UTF_8);
        return file;
    }

    private static long percentile(List<Long> sorted, int pct) {
        if (sorted.isEmpty()) {
            return 0;
        }
        int index = Math.min(sorted.size() - 1, (int) Math.ceil(pct / 100.0 * sorted.size()) - 1);
        return sorted.get(Math.max(0, index));
    }
}
