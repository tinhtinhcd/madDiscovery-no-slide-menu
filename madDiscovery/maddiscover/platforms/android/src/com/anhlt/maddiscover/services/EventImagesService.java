package com.anhlt.maddiscover.services;

import android.content.Context;

import com.anhlt.maddiscover.data.repositories.ImagesRepository;
import com.anhlt.maddiscover.data.tables.EventImages;
import com.anhlt.maddiscover.data.tables.Events;
import com.anhlt.maddiscover.entities.EventImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 4/3/16.
 */
public class EventImagesService {

    ImagesRepository imagesRepository;

    public EventImagesService(Context context) {
        imagesRepository = new ImagesRepository(context);
    }

    public String[] eventImage(Long eventId){
        List<String> images = imagesRepository.getEventImage(eventId);
        return images.toArray(new String[images.size()]);
    }

    public void create(EventImage image) {
        imagesRepository.create(image);
    }

    public void update(EventImage image){
        imagesRepository.update(image);
    }

    public void delete(long id){
        imagesRepository.delete(id);
    }
}
