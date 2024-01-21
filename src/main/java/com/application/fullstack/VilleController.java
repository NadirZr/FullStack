package com.application.fullstack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
    public Ville etCityByCoordinates(@PathVariable double latitude, @PathVariable double longitude) {
        // Filtrer la liste des villes pour obtenir les informations de la ville spécifiée
        return cities.stream()
                .filter(city -> city.getLatitude() == latitude && city.getLongitude() == longitude)
                .findFirst()
                .orElse(null); // Renvoie null si la ville n'est pas trouvée
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
