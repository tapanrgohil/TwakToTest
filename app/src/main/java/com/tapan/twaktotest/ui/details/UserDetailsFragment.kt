package com.tapan.twaktotest.ui.details


import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.tapan.twaktotest.R
import com.tapan.twaktotest.databinding.UserDetailsFragmentBinding
import com.tapan.twaktotest.ui.model.UserUiModel
import com.tapan.twaktotest.util.handleLoadingResponse

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.user_details_fragment) {

    private val extras by navArgs<UserDetailsFragmentArgs>()
    private lateinit var binding: UserDetailsFragmentBinding
    private val viewModel by viewModels<UserDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        extras.login.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
            viewModel.getUserDetails(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UserDetailsFragmentBinding.bind(view)

        initUi()
        attachObserver()
    }


    private fun attachObserver() {
        handleLoadingResponse(viewModel.userDetailsLiveData) {
            setData(it)
        }
    }

    private fun setData(it: UserUiModel) {
        binding.apply {
            hlvFollower.value = it.followers?.toString() ?: "-"
            hlvFollowing.value = it.following?.toString() ?: "-"
            hlvName.value = it.name ?: "-"
            hlvCompany.value = it.company ?: "-"
            hlvBlog.value = it.blog ?: "-"
            hlvEmail.value = it.email ?: "-"
            hlvTwitter.value = it.twitterUsername ?: "-"
            hlvBio.value = it.bio ?: "-"
            hlvLocation.value = it.location ?: "-"
            if (etNote.text.toString().isEmpty())
                etNote.setText(it.note.orEmpty())
        }
    }

    private fun initUi() {

        binding.apply {
            ViewCompat.setTransitionName(ivUser, "image_${extras.id}")
            extras.avatarUrl?.let {
                ivUser.load(it)
            }
            btSave.setOnClickListener {
                saveNote()
            }
        }

    }

    private fun saveNote() {
        val note = binding.etNote.text.toString()
        viewModel.saveNote(note, extras.id)

    }

}