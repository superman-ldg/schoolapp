package com.example.schooassitant

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

//上传字节数组的类设置
class MyrequestBody: RequestBody() {
    lateinit var bytes:ByteArray
    override fun contentType(): MediaType? {
        return "text/x-markdown; charset=utf-8".toMediaTypeOrNull();
    }

    override fun writeTo(sink: BufferedSink) {
        sink.outputStream().write(bytes)
    }
}

object Utils{
    /**
     * Btimap转数组
     */
    fun btimapToBtyes(bitmap: Bitmap?): ByteArray {
        val baos = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        }
        return baos.toByteArray()
    }

    /**
     * 字节数组转换位图
     */
    fun getBitmap(bytes: ByteArray): Bitmap? {
        val opts = BitmapFactory.Options()
        if(bytes!=null) {
            val Bitmaps = BitmapFactory.decodeByteArray(
                    bytes, 0, bytes.size,
                    opts
            );
            return Bitmaps
        }
        return null
    }

    fun pressImage(image:Bitmap): Bitmap? {
       val baos =ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
         var options = 100
        while (baos.toByteArray().size / 1024 > 50&&options>0) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        val isBm =ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        val bitmap = BitmapFactory.decodeStream(isBm, null, null)//把ByteArrayInputStream数据生成图片
        return bitmap
    }




    //获得书本的请求
    

    //发布书本的请求

    //获得招募信息的请求

    //发布招募

    //获得我的发布书本

    //获得我的招募表




    //用于更新Activity,书本的
   /* private fun getData(list: List<Book>)
    {
        runOnUiThread{
            for(a in list)
            {
                data.add(a)
            }
            val layoutManager= LinearLayoutManager(this)
            myview2.layoutManager=layoutManager
            val adapter=BookAdapter(data)
            myview2.adapter=adapter
        }
    }*/

    //处理从相机获取的图片
     fun rotateIfRequired(bitmap: Bitmap,Image: File):Bitmap{
        val exif= ExifInterface(Image.path)
        val orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap,270)
            else ->bitmap
        }
    }

    //处理从相机获取的图片
     fun rotateBitmap(bitmap:Bitmap,degree:Int):Bitmap{
        val matrix= Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }


}