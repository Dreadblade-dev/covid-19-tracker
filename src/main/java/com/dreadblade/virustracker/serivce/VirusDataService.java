package com.dreadblade.virustracker.serivce;

import com.dreadblade.virustracker.models.Location;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class VirusDataService {

    private final static String NOT_FORMATTED_VIRUS_DATASOURCE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/" +
            "master/csse_covid_19_data/csse_covid_19_daily_reports/%s%d-%s%d-%d.csv";

    private List<Location> allStats = new ArrayList<>();

    public List<Location> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *") // == runs first hour of every day
    public void fetchVirusData() throws IOException, InterruptedException {
        List<Location> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(generateVirusDatasourceUrl(-1)))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);


        request = HttpRequest.newBuilder()
                .uri(URI.create(generateVirusDatasourceUrl(-2)))
                .build();

        httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        csvReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> prevRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        Iterator<CSVRecord> iterator = prevRecords.iterator();

        for(CSVRecord record : records) {
            Location location = new Location();
            location.setState(record.get("Province_State"));
            location.setCountry(record.get("Country_Region"));

            int confirmed = Integer.parseInt(record.get("Confirmed"));
            int deaths = Integer.parseInt(record.get("Deaths"));
            int recovered = Integer.parseInt(record.get("Recovered"));

            int active = 0;
            String activeStr = record.get("Active");

            if (!activeStr.isEmpty() && activeStr != null) {
                active = Integer.parseInt(activeStr);
            }

            location.setLatestTotalConfirmedCases(confirmed);
            location.setLatestTotalDeathCases(deaths);
            location.setLatestTotalRecoveredCases(recovered);
            location.setLatestTotalActiveCases(active);


            if (iterator.hasNext()) {
                CSVRecord prevRecord = iterator.next();
                int prevConfirmed = Integer.parseInt(prevRecord.get("Confirmed"));
                int prevDeaths = Integer.parseInt(prevRecord.get("Deaths"));
                int prevRecovered = Integer.parseInt(prevRecord.get("Recovered"));

                String prevActiveStr = prevRecord.get("Active");
                int prevActive = 0;
                if (!prevActiveStr.isEmpty() && prevActiveStr != null) {
                    prevActive = Integer.parseInt(prevActiveStr);
                }

                location.setConfirmedDelta(confirmed - prevConfirmed);
                location.setDeathDelta(deaths - prevDeaths);
                location.setRecoveredDelta(recovered - prevRecovered);
                location.setActiveDelta(active - prevActive);
            }

            newStats.add(location);
        }

        allStats = newStats;
    }

    private String generateVirusDatasourceUrl(int daysOffset) {
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.add(Calendar.DATE, daysOffset);
        int previousDay = calendar.get(GregorianCalendar.DAY_OF_MONTH) ;
        int currentMonth = calendar.get(GregorianCalendar.MONTH) + 1;
        int currentYear = calendar.get(GregorianCalendar.YEAR);

        String virusDatasourceUrl = "";

        if (previousDay < 10) {
            virusDatasourceUrl = String.format(NOT_FORMATTED_VIRUS_DATASOURCE_URL, "", currentMonth, "0", previousDay, currentYear);
        } else if (currentMonth < 10) {
            virusDatasourceUrl = String.format(NOT_FORMATTED_VIRUS_DATASOURCE_URL, "0", currentMonth, "", previousDay, currentYear);
        } else {
            virusDatasourceUrl = String.format(NOT_FORMATTED_VIRUS_DATASOURCE_URL, "", currentMonth, "", previousDay, currentYear);
        }

        return virusDatasourceUrl;
    }

    public int getTotalCasesWorldwide() {
        int totalCasesWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getLatestTotalConfirmedCases())
                .sum();

        return totalCasesWorldwide;
    }

    public int getTotalRecoveredWorldwide() {
        int totalRecoveredWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getLatestTotalRecoveredCases())
                .sum();

        return totalRecoveredWorldwide;
    }

    public int getTotalDeathsWorldwide() {
        int totalDeathsWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getLatestTotalDeathCases())
                .sum();

        return totalDeathsWorldwide;
    }

    public int getTotalActiveWorldwide() {
        int totalActiveWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getLatestTotalActiveCases())
                .sum();

        return totalActiveWorldwide;
    }

    public int getTotalCasesDeltaWorldwide() {
        int totalCasesDeltaWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getConfirmedDelta())
                .sum();

        return totalCasesDeltaWorldwide;
    }

    public int getTotalRecoveredDeltaWorldwide() {
        int totalRecoveredDeltaWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getRecoveredDelta())
                .sum();

        return totalRecoveredDeltaWorldwide;
    }

    public int getTotalDeathsDeltaWorldwide() {
        int totalDeathsDeltaWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getDeathDelta())
                .sum();

        return totalDeathsDeltaWorldwide;
    }

    public int getTotalActiveDeltaWorldwide() {
        int totalActiveDeltaWorldwide = allStats.stream()
                .mapToInt(stat -> stat.getActiveDelta())
                .sum();

        return totalActiveDeltaWorldwide;
    }
}
