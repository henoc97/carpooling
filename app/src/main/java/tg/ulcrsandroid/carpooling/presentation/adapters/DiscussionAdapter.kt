package tg.ulcrsandroid.carpooling.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.databinding.ItemDiscussionBinding
import tg.ulcrsandroid.carpooling.domain.models.Discussion
import tg.ulcrsandroid.carpooling.presentation.viewholders.DiscussionViewHolder

class DiscussionAdapter(private var discussions: MutableList<Discussion>) : RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    abstract class DiscussionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(discussion: Discussion)
    }

    class SentMessageViewHolder(view: View) : DiscussionViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val sendDate: TextView = view.findViewById(R.id.sendDate)

        override fun bind(discussion: Discussion) {
            messageText.text = discussion.message
            sendDate.text = "${discussion.horodatage.hours}:${discussion.horodatage.minutes}"
        }
    }

    class ReceivedMessageViewHolder(view: View) : DiscussionViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val sendDate: TextView = view.findViewById(R.id.sendDate)

        override fun bind(discussion: Discussion) {
            messageText.text = discussion.message
            sendDate.text = "${discussion.horodatage.hours}:${discussion.horodatage.minutes}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_send, parent, false)
                SentMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
                ReceivedMessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        holder.bind(discussions[position])
    }

    override fun getItemCount() = discussions.size

    override fun getItemViewType(position: Int): Int {
        return if (discussions[position].isSent()) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    fun addDiscussion(discussion: Discussion) {
        if (discussions.contains(discussion)) {
            return
        }

        discussions.add(discussion)
        notifyItemInserted(discussions.size - 1)
    }

    fun resetDiscussions(l : MutableList<Discussion>) {
        discussions = l
        notifyDataSetChanged()
    }

}