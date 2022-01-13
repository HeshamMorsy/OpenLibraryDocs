package com.task.openlibrarydocs.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.task.openlibrarydocs.R
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.ui.adapter.ISBNsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var isbnsAdapter: ISBNsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setupUi()
        onNewIntent(intent)
        attachListeners()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (it.hasExtra(DOCUMENT_KEY)) {
                val document = it.extras?.let { extras -> extras[DOCUMENT_KEY] as Document }
                document?.let { doc -> fillUI(doc) }
            }
        }
    }

    private fun setupUi() {
        isbnsRecycler.apply {
            adapter = isbnsAdapter
            layoutManager = GridLayoutManager(context, 1)
        }
    }

    private fun fillUI(document: Document) {
        // set title
        documentDetailsTitleText.text = document.title
        // set author name if exists
        if (document.authorName.isNotEmpty())
            documentDetailsAuthorText.text = document.authorName[0]
        // check if isbn list is not empty to update isbn adapter
        if (document.isbn.isNotEmpty()) {
            isbnsAdapter.clearList()
            // end position of the list is used to subList the adapter isbn list if the size is more than 5
            val endPosition = if (document.isbn.size < 5) document.isbn.size else 5
            isbnsAdapter.addList(ArrayList(document.isbn.subList(0, endPosition)))
        }
    }

    private fun attachListeners() {
        documentDetailsTitleText.setOnClickListener {
            migrateToSearchOnMainActivity(TITLE_QUERY_KEY, documentDetailsTitleText.text.toString())
        }

        documentDetailsAuthorText.setOnClickListener {
            migrateToSearchOnMainActivity(AUTHOR_QUERY_KEY, documentDetailsAuthorText.text.toString())
        }
    }

    private fun migrateToSearchOnMainActivity(queryKey: String, queryValue: String) {
        val intentData = Intent().apply {
            putExtra(QUERY_KEY, queryKey)
            putExtra(QUERY_VALUE, queryValue)
        }
        setResult(RESULT_OK, intentData)
        finish()
    }
}