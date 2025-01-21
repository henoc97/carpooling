package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.databinding.ItemDiscussionBinding
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.presentation.viewholders.DiscussionViewHolder

class DiscussionAdapter(private val discussions: List<Discussion>) : RecyclerView.Adapter<DiscussionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val ui = ItemDiscussionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscussionViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return discussions.size
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        holder.discussion = discussions[position]
    }

}