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
    String addCredentialFailMessage = "Adding credential was unsuccessful, please try again";
    String updateCredentialFailMessage = "Credential update was unsuccessful, please try again";
    String deleteCredentialFail = "Credential delete was unsuccessful, please try again";

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

    public void addCredential(Credential credential, Integer userId) throws Exception {
        String encodedKey = createEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = new Credential(
                null, credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userId);

        int result = credentialMapper.insert(newCredential);
        if (result < 1) {
            throw new Exception(addCredentialFailMessage);
        }
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

    public void updateCredential(Credential credential, Integer userId) throws Exception{
        String encodedKey = createEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = new Credential(
                credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userId);

        int result = credentialMapper.updateCredential(newCredential);
        if (result < 1) {
            throw new Exception (updateCredentialFailMessage);
        }
    }

    public void deleteCredential(Integer credentialId) throws Exception{
        int result = credentialMapper.deleteCredential(credentialId);
        if (result < 1) {
            throw new Exception(deleteCredentialFail);
        }
    }
}


