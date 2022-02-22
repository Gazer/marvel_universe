package ar.com.p39.marvel_universe.character_details.summary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.p39.marvel_universe.databinding.ItemComicBinding

class ListAdapter(
    private val items: List<String>,
    private val onItemClicked: (url: String) -> Unit
) :
    RecyclerView.Adapter<ListAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(private val binding: ItemComicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.name.text = title
            binding.container.setOnClickListener {
                onItemClicked(title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ItemComicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}