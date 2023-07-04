package com.laitravel.laitravelbe.gcs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GCSService {
    final Storage storage;

    public  GCSService(Storage storage) {
        this.storage = storage;
    }

    public String uploadImage(
             String bucketName, String placeID, byte[] contents) {
        placeID = placeID + ".jpg";
        BlobId blobId = BlobId.of(bucketName, placeID);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.createFrom(blobInfo, new ByteArrayInputStream(contents));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        Blob blob = storage.get(blobId);
        String mediaLink = blob.getMediaLink();

        return mediaLink;
    }
  public void deleteImageByID (
          @Value("${GCS.bucket-name}") String bucketName, String placeID) throws IOException {

        BlobId blobId = BlobId.of(bucketName, placeID);
        Blob blob = storage.get(bucketName, placeID);
        if (blob == null) {
          System.out.println("The object " + placeID + " wasn't found in " + bucketName);
          return;
        }

        boolean deleted =  storage.delete(blobId);
        if(deleted) {
            System.out.println("Object " + placeID + " was deleted from " + bucketName);
        } else {
            System.out.println("The object could not be deleted.");
        }

  }

}