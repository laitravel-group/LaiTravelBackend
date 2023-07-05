package com.laitravel.laitravelbe.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleCloudService {
    final Storage storage;
    private final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudService.class);

    public GoogleCloudService(Storage storage) {
        this.storage = storage;
    }

    public String uploadImage(String bucketName, String placeId, byte[] imageData) {
        if (imageData == null) return "";
        String fileName = placeId + ".jpg";
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.createFrom(blobInfo, new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        Blob blob = storage.get(blobId);

        return blob.getMediaLink();
    }
  public void deleteImageByID (@Value("${GCS.bucket-name}") String bucketName, String placeId) {

        BlobId blobId = BlobId.of(bucketName, placeId);
        Blob blob = storage.get(bucketName, placeId);
        if (blob == null) {
            LOGGER.error(String.format("The object \"%s\" wasn't found in \"%s\"", placeId, bucketName));
            return;
        }

        boolean deleted =  storage.delete(blobId);
        if (deleted) {
            LOGGER.info(String.format("Object \"%s\" was deleted from \"%s\"", placeId, bucketName));
        } else {
            LOGGER.error("The object could not be deleted.");
        }

  }

}