package socialnetwork.service;

import socialnetwork.domain.Photo;
import socialnetwork.repository.Repository;

public class PhotoService {

    private Repository<Long, Photo> photoRepository;

    public PhotoService(Repository<Long, Photo> photoRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     *
     * @param id id of the user for whim we want the profile picture
     * @return a photo
     */
    public Photo getPhoto(Long id){
        return photoRepository.findOne(id);
    }

    /**
     *
     * @param photo that you want to changr
     */
    public void changePhoto(Photo photo){
        if(photoRepository.findOne(photo.getId())!= null){
            photoRepository.update(photo);
        }
        else{
            photoRepository.save(photo);
        }

    }
}
