package com.uniimarket.app.sell.view

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.net.URISyntaxException

class PathUtil {

    lateinit var uri1 : Uri
    /*
 * Gets the file path of the given Uri.
 */
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri:Uri): String? {
        uri1 = uri
        var needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri1))
        {
            if (isExternalStorageDocument(uri1))
            {
                val docId = DocumentsContract.getDocumentId(uri1)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return ""+Environment.getExternalStorageDirectory() + "/" + split[1]
            }
            else if (isDownloadsDocument(uri1))
            {
                val id = DocumentsContract.getDocumentId(uri1)
                uri1 = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            }
            else if (isMediaDocument(uri))
            {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                if ("image" == type)
                {
                    uri1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                else if ("video" == type)
                {
                    uri1 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                else if ("audio" == type)
                {
                    uri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf<String>(split[1])
            }
        }
        if ("content".equals(uri.getScheme(), ignoreCase = true))
        {
            val projection = arrayOf<String>(MediaStore.Images.Media.DATA)
            var cursor: Cursor ?= null
            try
            {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst())
                {
                    return cursor.getString(column_index)
                }
            }
            catch (e:Exception) {}
        }
        else if ("file".equals(uri.getScheme(), ignoreCase = true))
        {
            return uri.path
        }
        return null
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri):Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri:Uri):Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri:Uri):Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

}
