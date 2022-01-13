package com.task.openlibrarydocs.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.openlibrarydocs.R
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.ui.adapter.DocumentsAdapter
import com.task.openlibrarydocs.ui.dialogs.ErrorDialog
import com.task.openlibrarydocs.utils.DataState
import com.task.openlibrarydocs.utils.Extensions.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DOCUMENT_KEY = "documentKey"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var documentsAdapter: DocumentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        subscribeObservers()
        attachListeners()
    }

    private fun setupUI() {
        listRecycler.apply {
            adapter = documentsAdapter
            layoutManager = GridLayoutManager(context, 1)
        }
    }

    private fun subscribeObservers() {
        // observe on data state
        viewModel.dataState.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success<List<Document>> -> {
                    displayProgressBar(false)
                    appendDocuments(dataState.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
                is DataState.CLEAR -> {
                    displayProgressBar(false)
                    documentsAdapter.clearList()
                }
            }
        })

        // listen to recycler view holder's click listener
        lifecycleScope.launch {
            documentsAdapter.actionChannel.consumeAsFlow().collect {
                openDetailsActivity(it)
            }
        }
    }

    private fun displayError(message: String?) {
        // prepare error dialog properties
        // 1- title
        val dialogTitle = resources.getString(R.string.error)
        // 2- error message
        val dialogMessage = message ?: resources.getString(R.string.unkown_error)
        // 3- button text
        val btnText = resources.getString(R.string.close)
        // show error dialog
        ErrorDialog(
            title = dialogTitle,
            message = dialogMessage,
            btnText = btnText
        )
        .show(supportFragmentManager, "ErrorDialog")

    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        documentsAdapter.updateHideFooter(!isDisplayed)
            if (isDisplayed)
                listRecycler.scrollToPosition(documentsAdapter.itemCount - 1)
    }

    private fun appendDocuments(docs: List<Document>) {
        documentsAdapter.addList(ArrayList(docs))
    }

    private fun openDetailsActivity(document: Document){
        val detailsIntent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DOCUMENT_KEY, document)
        }
        startForResult.launch(detailsIntent)
    }

    // register for details activity results if the user clicked on author name or title
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            //do stuff here
        }
    }

    private fun attachListeners() {
        // detect scroll position in recycler view
        listRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // detect last complete visible index
                val lastCompleteVisibleIndex = (recyclerView.layoutManager as GridLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                // send the last complete visible item to the viewModel
                viewModel.onChangeDocumentScrollPosition(lastCompleteVisibleIndex)
                // check if we need to perform get next page request
                if ((lastCompleteVisibleIndex + 1) >= viewModel.page * PAGE_SIZE && documentsAdapter.hideFooter) {
                    viewModel.setStateEvent(MainStateEvent.NextPageEvent)
                }
            }
        })

        // text watcher on search edit text
        searchET
            .textChanges()
            .debounce(500)
            .map { it.toString() }
            .onEach {
                viewModel.updateQueryKey(DEFAULT_QUERY_KEY)
                viewModel.updateQueryValue(it)
                viewModel.setStateEvent(MainStateEvent.RestartPaginationEvent)
            }
            .launchIn(lifecycleScope)
    }
}