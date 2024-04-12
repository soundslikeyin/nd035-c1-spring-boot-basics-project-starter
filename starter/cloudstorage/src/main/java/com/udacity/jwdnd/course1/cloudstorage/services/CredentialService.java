package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialDisplay;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    private String createEncodedKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    private CredentialDisplay decryptCredential(Credential credential){
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        return new CredentialDisplay(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), credential.getPassword(), decryptedPassword, credential.getUserId());
    }

    public int addCredential(Credential credential){
        String encodedKey = createEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        return credentialMapper.insert(
                new Credential(null, credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserId())
        );
    }

    public Credential getCredential(Integer credentialId){
        return credentialMapper.getCredential(credentialId);
    }

    public List<CredentialDisplay> getAllCredential(Integer userId){
        List<Credential> allCredentials = credentialMapper.getAllCredentialsByUserId(userId);
        List<CredentialDisplay> allCredentialDisplay = new ArrayList<>();

        for ( Credential credential: allCredentials) {
            allCredentialDisplay.add(decryptCredential(credential));
        }

        return allCredentialDisplay;
    }


    public int updateCredential(Credential credential){
        String encodedKey = createEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        return credentialMapper.updateCredential(new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserId()));
    }

    public int deleteCredential(Integer credentialId){
        return credentialMapper.deleteCredential(credentialId);
    }
}


