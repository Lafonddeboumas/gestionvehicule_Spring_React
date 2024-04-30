package possacode.gestionvehiculeapi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import possacode.gestionvehiculeapi.entity.Vehicule;
import possacode.gestionvehiculeapi.repository.VehiculeRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static possacode.gestionvehiculeapi.constant.Constant.PHOTO_DIRECTORY;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;


    public Page<Vehicule> getAllVehicule(int page, int size){
        return vehiculeRepository.findAll(PageRequest.of(page, size, Sort.by("marque")));
    }

    public Vehicule getVehicule(String id){
        return vehiculeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No Vehicule was found"));
    }

    public Vehicule createVehicule(Vehicule vehicule){
        return vehiculeRepository.save(vehicule);
    }

    public void deleteVehicule(String id){
        vehiculeRepository.deleteById(id);
    }

    public String uploadPhoto(String id, MultipartFile file){
        log.info("Saving picture for USER ID {}",id);
        Vehicule vehicule =  getVehicule(id);
        String photoUrl = photoFunction.apply(id,file);
        vehicule.setPhoto(photoUrl);
        vehiculeRepository.save(vehicule);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name ->name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile, String> photoFunction =(id, image) ->{
        String filename = id+fileExtension.apply(image.getOriginalFilename());
        try{
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){Files.createDirectories(fileStorageLocation);}
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/vehicule/image/"+id+fileExtension.apply(image.getOriginalFilename()))
                    .toUriString();
        }catch (Exception exception){
            throw  new RuntimeException("Unable to save image");
        }

    };

}
