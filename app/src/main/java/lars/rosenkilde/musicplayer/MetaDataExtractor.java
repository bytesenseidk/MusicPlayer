package lars.rosenkilde.musicplayer;

import android.media.MediaMetadataRetriever;
import java.net.URL;

public class MetaDataExtractor {
    URL songPath = MetaDataExtractor.class.getResource("dummysong.mp3");
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
}

