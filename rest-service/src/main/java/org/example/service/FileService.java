package org.example.service;

import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}
