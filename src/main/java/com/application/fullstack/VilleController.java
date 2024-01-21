package com.application.fullstack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cities")

public class VilleController {

    private List<Ville> cities;

    public VilleController() {
        // Chargez les données du fichier JSON lors de la création du contrôleur
        loadCitiesFromJson();
    }

    @GetMapping("/{latitude}/{longitude}")
    @ResponseBody
    public List<Ville> getNeighborsOfSpecifiedPoint(
            @PathVariable double latitude,
            @PathVariable double longitude) {

        // Définir le périmètre de recherche pour les villes voisines
        double latitudeThreshold = 0.1;
        double longitudeThreshold = 0.5;

        // Filtrer la liste des villes pour obtenir les informations des villes voisines du point spécifié
        List<Ville> result = cities.stream()
                .filter(city ->
                        // Filtrer les villes voisines dans le périmètre défini
                        (city.getLatitude() >= latitude - latitudeThreshold &&
                                city.getLatitude() <= latitude + latitudeThreshold &&
                                city.getLongitude() >= longitude - longitudeThreshold &&
                                city.getLongitude() <= longitude + longitudeThreshold)
                )
                .collect(Collectors.toList());

        // Ajouter le point spécifié aux résultats pour référence
        result.add(new Ville());

        return result.isEmpty() ? null : result;
    }

    private void loadCitiesFromJson() {
        try {
            // Lire le contenu du fichier JSON
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Zourd/Desktop/Spring/fullstack/src/main/resources/MesVilles.json"));

            // Utiliser Jackson pour désérialiser le JSON en une liste de City
            ObjectMapper objectMapper = new ObjectMapper();
            cities = objectMapper.readValue(jsonData, new TypeReference<List<Ville>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception selon vos besoins
        }
    }
}
