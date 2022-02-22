package ar.com.p39.marvel_universe.character_list.adapters

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.p39.marvel_universe.R
import ar.com.p39.marvel_universe.databinding.ItemCharacterBinding
import ar.com.p39.marvel_universe.network_models.Character
import com.squareup.picasso.Picasso
import javax.inject.Inject


class CharactersAdapter @Inject constructor(private var picasso: Picasso, private val isTable: Boolean, private val onItemClicked: (Character) -> Unit) :
    PagingDataAdapter<Character, CharactersAdapter.CharacterViewHolder>(CharacterComparator) {

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            picasso,
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class CharacterViewHolder(
        private val picasso: Picasso,
        private val binding: ItemCharacterBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) = with(binding) {
            // TODO: Use safe-args
            if (isTable) {
                binding.moreInfo.setOnClickListener {
                    onItemClicked(character)
                }
            } else {
                val bundle = Bundle().apply {
                    putString("characterId", character.id)
                    putString("characterName", character.name)
                }
                // TODO: Add shared element transition
                binding.moreInfo.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_charactersFragment_to_characterDetailsFragment,
                        bundle
                    )
                )
            }
            binding.name.text = character.name
            binding.description.text = when {
                character.description.isNotBlank() -> character.description
                character.comics.returned > 0 -> createList(
                    "Comics",
                    character.comics.items.take(5).map { it.name },
                )
                character.events.returned > 0 -> createList(
                    "Events",
                    character.events.items.take(5).map { it.name },
                )
                character.series.returned > 0 -> createList(
                    "Series",
                    character.series.items.take(5).map { it.name },
                )
                character.stories.returned > 0 -> createList(
                    "Stories",
                    character.stories.items.take(5).map { it.name },
                )
                else -> "No extra data"
            }

            val url = "${character.thumbnail.path}/portrait_uncanny.${character.thumbnail.extension}"

            if (url.contains("image_not_available")) {
                picasso.load(R.mipmap.placeholder)
                    .resize(0, 250)
                    .centerCrop()
                    .into(binding.image)
            } else {
                picasso
                    .load(url)
                    .placeholder(R.mipmap.placeholder)
                    .resize(0, 250)
                    .centerCrop()
                    .into(binding.image)
            }
        }

        private fun createList(title: String, lines: List<String>): CharSequence {
            val sb = SpannableStringBuilder("$title:\n")
            lines.forEach {
                val line: CharSequence = "${it}\n"
                val spannable: Spannable = SpannableString(line)
                spannable.setSpan(
                    BulletSpan(8),
                    0,
                    spannable.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                sb.append(spannable)
            }
            return sb
        }
    }

    object CharacterComparator : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }
}