package com.rivaphy.suitmediatestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rivaphy.suitmediatestapp.databinding.ItemUserNameBinding
import com.rivaphy.suitmediatestapp.response.DataItem

class ThirdScreenAdapter(
    private var users: MutableList<DataItem>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ThirdScreenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, itemClickListener)
    }

    override fun getItemCount(): Int = users.size

    fun addData(newItems: List<DataItem>) {
        val startPosition = users.size
        users.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun clearData() {
        users.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserNameBinding.bind(itemView)

        fun bind(user: DataItem?, itemClickListener: OnItemClickListener) {
            user?.let {
                binding.tvItemUserName.text = "${it.firstName} ${it.lastName}"
                binding.tvItemUserEmail.text = it.email

                Glide.with(itemView.context)
                    .load(it.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.img)
                    .into(binding.ivItemUserName)

                itemView.setOnClickListener {
                    itemClickListener.onItemClick(user)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: DataItem)
    }
}