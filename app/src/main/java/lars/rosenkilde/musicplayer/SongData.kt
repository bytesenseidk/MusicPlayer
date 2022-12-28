package lars.rosenkilde.musicplayer

import android.media.MediaMetadataRetriever

class SongData {
    var data = MediaMetadataRetriever().setDataSource(R.raw.dummysong.toString())
    fun getMetadata() {
        println(data.toString())
    }
}

fun main(){
    SongData().getMetadata()
}