package lars.rosenkilde.musicplayer

import android.media.MediaMetadataRetriever

class SongData {
    //var data = MediaMetadataRetriever()//.setDataSource(R.raw.dummysong.toString())
    //val song: String = R.raw.dummysong.toString()

    fun getMetadata() {

        print(MetaDataExtractor().albumName)
    } /*
    fun getMetadata() {
//        data = data.setDataSource(song)
        println(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
    }
*/
}


fun main(){
    print(SongData().getMetadata())
    //SongData().getMetadata()
}
