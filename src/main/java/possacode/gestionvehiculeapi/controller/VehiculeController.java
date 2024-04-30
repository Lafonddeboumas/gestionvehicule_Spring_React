package possacode.gestionvehiculeapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import possacode.gestionvehiculeapi.entity.Vehicule;
import possacode.gestionvehiculeapi.service.VehiculeService;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static possacode.gestionvehiculeapi.constant.Constant.PHOTO_DIRECTORY;

@RestController
@RequestMapping("/vehicule")
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule){
        return ResponseEntity.created(URI.create("/vehicule/userID")).body(vehiculeService.createVehicule(vehicule));
    }

    @GetMapping
    public ResponseEntity<Page<Vehicule>> getAllVehicule(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(vehiculeService.getAllVehicule(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicule> getVehicule(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(vehiculeService.getVehicule(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file){
        return ResponseEntity.ok().body(vehiculeService.uploadPhoto(id, file));
    }

    @GetMapping(path  ="/photo/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable(value = "filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
