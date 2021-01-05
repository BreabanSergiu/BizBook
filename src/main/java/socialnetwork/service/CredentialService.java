package socialnetwork.service;

import socialnetwork.domain.Credential;
import socialnetwork.repository.database.CredentialsDbRepository;

public class CredentialService {

    CredentialsDbRepository credentialsDbRepository;

    public CredentialService(CredentialsDbRepository credentialsDbRepository) {
        this.credentialsDbRepository = credentialsDbRepository;
    }

    public Credential addCredential(Credential credential){
        return credentialsDbRepository.save(credential);
    }

    public Credential findOne(String userName){
        return credentialsDbRepository.findOne(userName);
    }
}
