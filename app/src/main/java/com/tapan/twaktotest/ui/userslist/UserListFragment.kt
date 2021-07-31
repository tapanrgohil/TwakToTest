package com.tapan.twaktotest.ui.userslist


import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.google.android.material.snackbar.Snackbar
import com.tapan.twaktotest.R
import com.tapan.twaktotest.data.core.Status
import com.tapan.twaktotest.data.exception.ErrorResolverFactory
import com.tapan.twaktotest.databinding.UserListFragmentBinding
import com.tapan.twaktotest.ui.model.UserAdapterModelType
import com.tapan.twaktotest.ui.model.UserUiModel
import com.tapan.twaktotest.ui.userslist.adapter.UserAdapter
import com.tapan.twaktotest.util.NetworkLiveData
import com.tapan.twaktotest.util.handleLoadingResponse
import com.tapan.twaktotest.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UserListFragment : Fragment(R.layout.user_list_fragment) {

    private var searchMenu: MenuItem? = null

    @Inject
    lateinit var errorResolver: ErrorResolverFactory

    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var binding: UserListFragmentBinding
    private val viewModel by viewModels<UserListViewModel>()

    private var searchView: SearchView? = null

    private var queryTextChangedJob: Job? = null

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dispatcher = requireActivity().onBackPressedDispatcher
        dispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                dialog.show {
                    title(text = getString(R.string.are_you_sure))
                    message(text = getString(R.string.are_you_sure_message))
                    positiveButton(text = getString(R.string.yes)) { dialog ->
                        isEnabled = false
                        dialog.dismiss()
                        requireActivity().onBackPressed()
                    }
                    negativeButton(text = getString(R.string.no)) { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        })
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = UserListFragmentBinding.inflate(layoutInflater)
            initUi()
            attachNetworkObserver()
        }
        return binding.root
    }

    private fun attachNetworkObserver() {
        observe(NetworkLiveData) {
            if (it != true) {
                Snackbar.make(
                    binding.root,
                    R.string.no_internet_connectivity,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.ok, View.OnClickListener {

                }).show()
            }
            if (userAdapter.itemCount == 0) {
                viewModel.getAllUsers(0)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_users_list, menu)

        searchMenu = menu.findItem(R.id.menu_search)
        searchView = searchMenu?.actionView as SearchView
        searchView?.apply {
            maxWidth = 2000
            queryHint = getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(500)
                        newText?.let {
                            if (it.isNotEmpty())
                                viewModel.filter(it)
                        }
                    }
                    return true
                }
            })
        }
        searchView?.setOnCloseListener {
            clearSearch()
            false
        }

    }

    private fun clearSearch() {
        viewModel.clearSearch()
        binding.rvUsers.post {
            binding.rvUsers.layoutManager?.scrollToPosition(0)
            binding.rvUsers.addOnScrollListener(scrollListener)
        }
    }


    private fun attachObserver() {
        handleLoadingResponse(viewModel.userListLiveData, binding.progress, {
            if (userAdapter.itemCount > 0) {
                userAdapter.stopShimmer()
            }
        }) {
            binding.progress.onlyError(true)
            /* if ((it.size - userAdapter.itemCount) < 1) { // 1 is progress bar
                 binding.rvUsers.removeOnScrollListener(scrollListener)
             }*/
            userAdapter.submitList(it)
        }
        handleLoadingResponse(viewModel.searchUserLiveData, binding.progress) {
            binding.progress.onlyError(true)
            userAdapter.submitList(it, true)
        }

    }


    private fun initUi() {
        userAdapter = UserAdapter(clickCallBack = { userUiModel, view ->
            navigateToDetailsPage(userUiModel, view)
        }).also { userAdapter = it }

        binding.apply {
            rvUsers.apply {
                adapter = userAdapter
                layoutManager = LinearLayoutManager(context)
            }
            progress.setErrorResolver(errorResolver.create(requireContext()))
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.user_list)

        initScrollListener()

    }

    private fun navigateToDetailsPage(userUiModel: UserUiModel, view: View) {
        val extras = FragmentNavigatorExtras(
            view to "image_${userUiModel.id}"
        )
        findNavController()
            .navigate(
                UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(
                    userUiModel.login, userUiModel.id.toString(), userUiModel.avatarUrl
                ), extras
            )
    }

    private fun initScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (viewModel.userListLiveData.value?.status != Status.LOADING
                    && searchView?.isIconified == true
                    && userAdapter.getLastItem()?.type == UserAdapterModelType.NORMAL
                ) {
                    if (linearLayoutManager != null
                        && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userAdapter.itemCount - 1
                    ) {
                        userAdapter.showBottomProgress()
                        viewModel.getAllUsers(
                            userAdapter.getLastItem()?.userUiModel?.id ?: 0
                        )
                    }
                }
            }
        }
        binding.rvUsers.addOnScrollListener(
            scrollListener
        )
    }
}