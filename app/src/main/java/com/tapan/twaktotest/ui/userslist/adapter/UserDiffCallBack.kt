package com.tapan.twaktotest.ui.userslist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tapan.twaktotest.ui.model.UserAdapterModel

class UserDiffCallBack : DiffUtil.ItemCallback<UserAdapterModel>() {
    override fun areItemsTheSame(oldItem: UserAdapterModel, newItem: UserAdapterModel): Boolean {
        return oldItem.type == newItem.type
                && oldItem.isLoading == newItem.isLoading
                && oldItem.userUiModel?.id == newItem.userUiModel?.id
    }

    override fun areContentsTheSame(oldItem: UserAdapterModel, newItem: UserAdapterModel): Boolean {
        return oldItem.isLoading == newItem.isLoading &&
                oldItem.userUiModel?.id == newItem.userUiModel?.id
                && oldItem.isFilterApplied == newItem.isFilterApplied
                && oldItem.userUiModel?.note == newItem.userUiModel?.note
                && oldItem.userUiModel?.bio == newItem.userUiModel?.bio
    }
}