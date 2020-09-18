package com.dreadblade.virustracker.controller;

import com.dreadblade.virustracker.models.Location;
import com.dreadblade.virustracker.serivce.VirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private VirusDataService virusDataService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("locationStatistics", virusDataService.getAllStats());
        addStatsToModel(model);
        return "main";
    }

    @PostMapping("/")
    public String main(@RequestParam String filter, Model model) {
        List<Location> stats = virusDataService.getAllStats().stream()
                .filter(stat -> stat.getState().contains(filter) || stat.getCountry().contains(filter))
                .collect(Collectors.toList());

        model.addAttribute("locationStatistics", stats);
        addStatsToModel(model);
        return "main";
    }

    public void addStatsToModel(Model model) {
        model.addAttribute("totalCasesWorldwide", virusDataService.getTotalCasesWorldwide());
        model.addAttribute("totalDeathsWorldwide", virusDataService.getTotalDeathsWorldwide());
        model.addAttribute("totalRecoveredWorldwide", virusDataService.getTotalRecoveredWorldwide());
        model.addAttribute("totalActiveWorldwide", virusDataService.getTotalActiveWorldwide());
        model.addAttribute("totalCasesDeltaWorldwide", virusDataService.getTotalCasesDeltaWorldwide());
        model.addAttribute("totalDeathsDeltaWorldwide", virusDataService.getTotalDeathsDeltaWorldwide());
        model.addAttribute("totalRecoveredDeltaWorldwide", virusDataService.getTotalRecoveredDeltaWorldwide());
        model.addAttribute("totalActiveDeltaWorldwide", virusDataService.getTotalActiveDeltaWorldwide());
    }
}
