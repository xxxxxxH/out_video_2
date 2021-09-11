package net.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import net.entity.FolderEntity
import net.entity.VideoEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VideoManager {
    var data: HashMap<String, ArrayList<VideoEntity>> = HashMap()
    companion object {
        private var instance: VideoManager? = null
            get() {
                field?.let {

                } ?: run {
                    field = VideoManager()
                }
                return field
            }

        @Synchronized
        fun get(): VideoManager {
            return instance!!
        }
    }

    private fun getVideoThumbnail(id: Int, context: Context): Bitmap {
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inDither = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(
            context.contentResolver,
            id.toLong(),
            MediaStore.Images.Thumbnails.MICRO_KIND,
            options
        )
        return bitmap
    }

    @SuppressLint("Recycle")
    fun getAllVideos(context: Context): ArrayList<VideoEntity> {
        var videos: ArrayList<VideoEntity>? = null
        val cursor = context.contentResolver?.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
            null, null
        )
        cursor?.let {
            videos = ArrayList()
            while (it.moveToNext()) {
                val id = cursor.getInt(
                    cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                )

                val img = getVideoThumbnail(id, context)

                val name = cursor
                    .getString(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                    )
                val size = cursor
                    .getLong(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                    )
                val url = cursor
                    .getString(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    )
                val duration = cursor
                    .getInt(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    )
                val time = cursor
                    .getInt(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
                    )

                val video = VideoEntity()
                video.name = name.toString()
                video.size = Utils.byteToString(size)
                video.url = url.toString()
                video.duration = TimeUtils().times(duration.toLong())
                video.bitmap = img
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                video.time = simpleDateFormat.format(time.toLong())
                videos!!.add(video)
            }
            it.close()
        }
        return videos!!
    }

    fun getFolderVideos(videoList: ArrayList<VideoEntity>): ArrayList<FolderEntity> {
        var result = ArrayList<FolderEntity>()
        var folderName: HashSet<String> = HashSet()
        val imgUrl = ArrayList<Bitmap>()
        for (item in videoList) {
            val url = item.url.split("/")
            val name = url[url.size - 2]
            folderName.add(name)
            imgUrl.add(item.bitmap)
        }

        for (item in folderName) {
            val video: ArrayList<VideoEntity> = ArrayList()
            for (entity in videoList) {
                if (entity.url.contains(item)) {
                    video.add(entity)
                }
            }
            data[item] = video
        }

        data.forEach {
            val entity = FolderEntity()
            entity.name = it.key
            entity.num = it.value.size
            entity.url = getCover(it)
            result.add(entity)
        }
        return result
    }

    fun getMap():HashMap<String, ArrayList<VideoEntity>>{
        return data
    }

    private fun getCover(d: Map.Entry<String, ArrayList<VideoEntity>>): Array<Bitmap?> {
        val values = d.value
        val result = arrayOfNulls<Bitmap>(values.size)
        when (values.size) {
            1 -> {
                result[0] = (values[0].bitmap)
            }
            2 -> {
                result[0] = (values[0].bitmap)
                result[1] = (values[1].bitmap)
            }
            3 -> {
                result[0] = (values[0].bitmap)
                result[1] = (values[1].bitmap)
                result[2] = (values[2].bitmap)
            }
            4 -> {
                result[0] = (values[0].bitmap)
                result[1] = (values[1].bitmap)
                result[2] = (values[2].bitmap)
                result[3] = (values[3].bitmap)
            }
        }
        return result
    }
}