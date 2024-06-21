package com.example.uzummarketclient.utils

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.mymusicservise.presenter.activity.MainActivity
import com.example.mymusicservise.presenter.service.MyService
import com.example.mymusicservise.utils.MyMusicModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit


fun Any.myLog() = Log.d("TTT", this.toString())
fun Any.myErrorLog() = Log.e("TTT", this.toString())
//fun String.myLog() = Log.d("TTT", this)
fun String.onlyLetters() = all { it.isLetter() }
fun String.myShortToast(context: Context){
    Toast.makeText(context,this , Toast.LENGTH_SHORT).show()
}
private val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION
)
fun Context.getMusicsCursor(searchQuery: String = ""): Flow<Cursor> = flow {
    // Формируем условие фильтрации
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0" +
            if (searchQuery.isNotEmpty()) " AND ${MediaStore.Audio.Media.TITLE} LIKE ?" else ""

    // Параметры для условия фильтрации
    val selectionArgs = if (searchQuery.isNotEmpty()) arrayOf("%$searchQuery%") else null

    val cursor: Cursor? = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${MediaStore.Images.Media.DATE_MODIFIED} DESC" // Вы можете заменить null на ваше условие сортировки, например, "${MediaStore.Audio.Media.TITLE} ASC" для сортировки по названию
    )
    cursor?.let { emit(it) }
}
fun Context.getMusicsCursor(): Flow<Cursor> = flow {
    val cursor: Cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        MediaStore.Audio.Media.IS_MUSIC + "!=0",
        null,
        "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

    ) ?: return@flow
    emit(cursor)
}

fun Cursor.getMusicDataByPosition(pos: Int,context: Context): MusicData {
    this.moveToPosition(pos)
    val artistColumn=this.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    val idColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val titleColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val id = this.getLong(idColumn)
    val title = this.getString(titleColumn)
    val artist = this.getString(artistColumn)
    val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id!!)
    var bitmap =context.getAlbumArt(contentUri)
    return MusicData(
        this.getInt(0),
        this.getString(1),
        this.getString(2),
        this.getString(3),
        this.getLong(4),
        bitmap
    )
}
 fun Context.getAlbumArt(audioUri: Uri): Bitmap?{
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, audioUri)
    val art = retriever.embeddedPicture
    var bitmap = if (art != null) BitmapFactory.decodeByteArray(art, 0, art.size) else null
    retriever.release()
//     if (bitmap!=null){
//         val px = dpToPx(8, this)
//         val croppedBitmap = getRoundedCornerBitmap(bitmap,80)
//         return croppedBitmap
//     }
    return bitmap
}
fun Long.durationConverter(): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )
}
fun SeekBar.setChangeProgress(block: (progress: Int, fromUser: Boolean) -> Unit) {
    this.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            seekBar?.let { block.invoke(it.progress, fromUser) }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })
}

fun FragmentActivity.statusBarTRANSPARENT()=this.apply{
    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    if (Build.VERSION.SDK_INT >= 19) {
//            SYSTEM_UI_FLAG_LAYOUT_STABLE
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        this.window.statusBarColor = Color.TRANSPARENT
    }
   this.window.statusBarColor = Color.TRANSPARENT
}

private fun FragmentActivity.setWindowFlag(bits: Int, on: Boolean) {
    val win = (this as MainActivity).window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}
fun FragmentActivity.setStatusBar(view:View){
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        // Получаем размер статус бара
        val statusBarHeight = resources.getDimensionPixelSize(resourceId)

        // Устанавливаем отступ сверху для корневого элемента макета
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,  // ширина в пикселях
            statusBarHeight // высота в пикселях
        )
        view.layoutParams = params
    }
}
fun Context.isHavePermission(permission:String):Boolean{
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.requestPermission(permission:Array<String>){
    ActivityCompat.requestPermissions(
        (this as MainActivity),
        permission
        ,
        1
    )
}

fun Any.getPosMusic(id:Int):Int{
    MyMusicModel.cursor?.let {
        for (i in 0 until it.count){
            it.moveToPosition(i)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val idCur =it.getInt(0)
            "idCursor=> $idCur".myLog()
            if (id==idCur){
                return i
            }
        }
    }
    "ID=> $id".myLog()
    return 0
}
 fun FragmentActivity.startMyService(play: ServiceEnum) {
    val intent = Intent(this, MyService::class.java)
    intent.putExtra("COMMAND", play)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.startForegroundService(intent)
        "startForegroundService".myLog()
    } else {
        "startService".myLog()
        this.startService(intent)
    }
}


fun TextView.textScrool(){
    this.ellipsize = TextUtils.TruncateAt.MARQUEE
    this.isSingleLine = true
    this.isSelected = true
}
fun TextView.textNotScrool(){
    this.ellipsize = TextUtils.TruncateAt.END
    this.isSingleLine = true
    this.isSelected = true
}
//fun Cursor.getImageUri():Uri{
//
//}
