package com.speakwithai.basestructure.ui.music.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.utils.DownloadUtil
import com.speakwithai.basestructure.data.model.response.music.ResultX


class SongAdapter(val context: Context, var mymediaplyer: MediaPlayer) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    val TAG="welcome"
    val intentFilter: IntentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

    //    private var mymediaplyer:MediaPlayer=MediaPlayer()
    private var isreleased=false
    lateinit var preferenceManager: SharedPreferences
    lateinit var downloadmanager: DownloadManager
    var lastid="welcome"



    var mList=ArrayList<ResultX>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sample_song, parent, false)


        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time=(28000..33000).random()
        var realtime: Int =mList[position].trackTimeMillis




        if (time<(mList[position].trackTimeMillis)){realtime=time}
        Log.d(TAG, "onBindViewHolder: "+realtime)
        val seconds=realtime/1000
        val mn=seconds/60
        val sec=seconds%60
        val ans=mList[position].artistName+"||0$mn.$sec⏲"
        holder.details.text=ans
        holder.trackname.text=mList[position].trackName

        holder.btn.setOnClickListener {
            val url = mList[position].previewUrl
            val fileName = mList[position].artistId.toString()
            DownloadUtil.downloadMusic(holder.itemView.context, url, fileName)
        }

        holder.itemView.setOnClickListener {
            val bol=mList[position].trackId.toString()==lastid
            if (mymediaplyer.isPlaying and bol){
                mymediaplyer.reset()
                holder.play.visibility  = View.VISIBLE
                holder.pause.visibility  = View.GONE

            }
            else{
                lastid=mList[position].trackId.toString()
                mymediaplyer.reset()
                mymediaplyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mymediaplyer.setDataSource(mList[position].previewUrl)
                mymediaplyer.prepare()
                mymediaplyer.start()
                holder.play.visibility  = View.GONE
                holder.pause.visibility  = View.VISIBLE


            }
        }




    }
    fun setmlist(v:ArrayList<ResultX>){
        mList=v

    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val btn: Button =ItemView.findViewById(R.id.setringtone)
        val details: TextView =ItemView.findViewById(R.id.info)
        val trackname: TextView =ItemView.findViewById(R.id.trackname)
        val play: ImageView =ItemView.findViewById(R.id.play)
        val pause: ImageView =ItemView.findViewById(R.id.pause)



    }

    private fun GetFile() {
        //Retrieve the saved request id
        Log.d(TAG, "GetFile: successfully downloaded")
    }


}