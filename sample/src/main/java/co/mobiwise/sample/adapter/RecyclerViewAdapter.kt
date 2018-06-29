package co.mobiwise.sample.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import java.util.ArrayList

import co.mobiwise.sample.R
import co.mobiwise.sample.model.Song

class RecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.ExampleViewHolder>() {

    private var songList: List<Song>? = null

    init {
        songList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ExampleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_card, parent, false)
        return ExampleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ExampleViewHolder, position: Int) {
        val song = songList!![position]

        holder.coverName.text = song.songName
        holder.singerName.text = song.singerName
        Picasso.get().load(song.songArt).into(holder.coverIamge)
    }

    override fun getItemCount(): Int {
        return songList!!.size
    }

    fun setSongList(songList: List<Song>) {
        this.songList = songList
        notifyDataSetChanged()
    }


    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var coverIamge: ImageView
        internal var coverName: TextView
        internal var singerName: TextView

        init {
            coverIamge = itemView.findViewById<View>(R.id.cover_photo) as ImageView
            coverName = itemView.findViewById<View>(R.id.cover_name) as TextView
            singerName = itemView.findViewById<View>(R.id.singer_name) as TextView
        }
    }
}
