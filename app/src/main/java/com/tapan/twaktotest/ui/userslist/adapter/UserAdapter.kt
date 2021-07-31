package com.tapan.twaktotest.ui.userslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tapan.twaktotest.R
import com.tapan.twaktotest.databinding.ItemProgressBinding
import com.tapan.twaktotest.databinding.ItemUserBinding
import com.tapan.twaktotest.ui.model.UserAdapterModel
import com.tapan.twaktotest.ui.model.UserAdapterModelType
import com.tapan.twaktotest.ui.model.UserUiModel
import com.tapan.twaktotest.util.toInvertColor
import com.tapan.twaktotest.util.visible

class UserAdapter(private val clickCallBack: (UserUiModel, View) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userDiffCallBack = UserDiffCallBack()
    private val asyncDiffUtil = AsyncListDiffer(this, userDiffCallBack)


    @Synchronized
    fun submitList(userList: List<UserAdapterModel>, fromSearch: Boolean = false) {
        if (userList.size < getCurrentList().size && !fromSearch) {
            return//continue progress
        }
        val newList = userList.mapIndexed { index, userAdapterModel ->
            userAdapterModel.isFilterApplied = ((index + 1) % 4 == 0)
            userAdapterModel.isLoading = getCurrentList().isNullOrEmpty()
            userAdapterModel
        }
        asyncDiffUtil.submitList(newList)
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemUserBinding.bind(view)
        fun bind(userModel: UserAdapterModel) {
            val user = userModel.userUiModel ?: return
            binding.ivUser.load(user.avatarUrl)
            binding.tvUserName.text = user.login
            binding.tvDescription.text = user.bio
            ViewCompat.setTransitionName(binding.ivUser, "image_${user.id}")

            itemView.setOnClickListener {
                clickCallBack.invoke(user, binding.ivUser)
            }
            binding.ivNote.visible(!user.note.isNullOrEmpty())
            setColorFilters(userModel)
            binding.shimmer.post {
                if (userModel.isLoading == true) {
                    binding.shimmer.showShimmer(true)
                } else {
                    binding.shimmer.hideShimmer()
                }
            }
        }

        private fun setColorFilters(user: UserAdapterModel) {
            if (user.isFilterApplied) {
                binding.ivUser.toInvertColor()
            } else {
                binding.ivUser.clearColorFilter()
            }
        }


    }

    inner class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemProgressBinding.bind(view)
        fun bind(userAdapterModel: UserAdapterModel) {
            val type: UserAdapterModelType = userAdapterModel.type
            if (type is UserAdapterModelType.PROGRESS && !type.message.isNullOrEmpty()) {
                binding.progress.onStopLoading(false, message = type.message)
            } else {
                binding.progress.onInit()
                binding.progress.onStartLoading()
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LayoutInflater.from(parent.context)
            .let {
                if (viewType == 1) {
                    val view = it.inflate(R.layout.item_user, parent, false)
                    UserViewHolder(view)
                } else {
                    val view = it.inflate(R.layout.item_progress, parent, false)
                    ProgressViewHolder(view)
                }
            }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProgressViewHolder) {
            holder.bind(getCurrentList()[position])
        } else if (holder is UserViewHolder) {
            getCurrentList()[position].let { holder.bind(it) }
        }
    }

    override fun getItemCount(): Int {
        return asyncDiffUtil.currentList.size
    }

    private fun getCurrentList(): MutableList<UserAdapterModel> {
        return asyncDiffUtil.currentList
    }

    override fun getItemViewType(position: Int): Int {
        return if (getCurrentList()[position].type == UserAdapterModelType.NORMAL) {
            1
        } else {
            2
        }
    }

    fun getItemAtPosition(position: Int) = getCurrentList()[position].userUiModel

    fun getLastItem() = if (getCurrentList().isNotEmpty()) {
        getCurrentList().last()
    } else {
        null
    }

    fun showBottomProgress() {
        val newList = getCurrentList().toMutableList()
        newList
            .add(UserAdapterModel(null, false, UserAdapterModelType.PROGRESS(null)))
        asyncDiffUtil.submitList(newList)
    }

    fun stopShimmer() {
        val userList = getCurrentList().filter {
            it.isLoading = false
            it.type == UserAdapterModelType.NORMAL
        }
        asyncDiffUtil.submitList(userList)
        notifyDataSetChanged()
    }

}