package com.example.schooassitant
import okhttp3.*
object  HttpUtils {

    val Url="http://10.242.213.81:8080/mySchoolApp/"

    /**
     * Login
     * Register
     * UserModifyInformation
     * getBook
     * getRecruit
     * getPicture
     * savePicture
     * UserDelBook
     * UserDelRecruit
     * UserDelOrder
     * UserGetMyBook
     * UserGetMyRecruit
     * UserGetMyOrder
     * UserOrderBook
     * UserSellBok
     * UserReleaseRecruit
     * UserQueryBookByName
     * UserQueryBookByType
     */

    /**
     * get请求返回字符串数据
     */
    fun GetRequest(Url:String):String{

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(Url)
                    .build()
            val response = client.newCall(request).execute()
            val Data = response.body?.string()
            return Data.toString()
    }

    /**
     * post请求提交字符串数据
     */
    fun PostRequest(Url:String,key:String,value:String):String{
        try {
            val client = OkHttpClient()
            val requestBody= FormBody.Builder()
                    .add(key,value)
                    .build()
            val request = Request.Builder()
                    .url(Url)
                    .post(requestBody)
                    .build()
            val response = client.newCall(request).execute()
            val result=response.body?.string().toString()
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return "0"
        }
    }

    /**
     * get请求返回字节数组(获取图片)
     */
    fun GetBytesRequest(Url:String,url:String,name:String): ByteArray? {
        try {
            val client = OkHttpClient()
            val requestBody1 = FormBody.Builder()
                    .add("url",url)
                    .add("name",name)
                    .build()
            val request = Request.Builder()
                    .url(Url)
                    .post(requestBody1)
                    .build()
            val response = client.newCall(request).execute()
            val bytes = response.body?.bytes()
            return bytes
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * post请求提交字节数组(上传图片)
     */
    fun PostBytesRequest(Url:String,bytes:ByteArray):String{
        try {
            val client = OkHttpClient()
            val data=MyrequestBody()
                 data.bytes=bytes
            val request = Request.Builder()
                    .url(Url)
                    .post(data)
                    .build()
            val response = client.newCall(request).execute()
            val result = response.body?.string().toString()
            return result
        } catch (e: Exception) {
            return "false"
        }
    }


}