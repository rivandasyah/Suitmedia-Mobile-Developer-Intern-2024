package com.rivaphy.suitmediatestapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rivaphy.suitmediatestapp.ThirdScreenAdapter
import com.rivaphy.suitmediatestapp.ThirdScreenViewModel
import com.rivaphy.suitmediatestapp.api.ApiConfig
import com.rivaphy.suitmediatestapp.databinding.ActivityThirdScreenBinding
import com.rivaphy.suitmediatestapp.response.DataItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView

class ThirdScreenActivity : AppCompatActivity() {

    private lateinit var adapter: ThirdScreenAdapter
    private lateinit var binding: ActivityThirdScreenBinding
    private val viewModel: ThirdScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ivThirdScreenBack.setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        setupSwipeRefreshLayout()

        viewModel.data.observe(this) { data ->
            if (data.isNotEmpty()) {
                hideEmptyState()
                adapter.addData(data)
            } else {
                showEmptyState()
            }
            hideLoading()
        }

        viewModel.isLastPage.observe(this) { isLastPage ->
            if (isLastPage) {
                showToast("No more data available")
            }
        }

        viewModel.isError.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast("Error: $it")
            }
            hideLoading()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }


        if (viewModel.data.value.isNullOrEmpty()) {
            loadData()
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.refreshThirdScreen.setOnRefreshListener {
            viewModel.resetPage()
            adapter.clearData()
            loadData()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.refreshThirdScreen.isRefreshing = isLoading
        }
    }

    private fun setupRecyclerView() {
        adapter = ThirdScreenAdapter(mutableListOf(), object : ThirdScreenAdapter.OnItemClickListener {
            override fun onItemClick(user: DataItem) {
                val intent = Intent()
                intent.putExtra(USER_TAG, "${user.firstName} ${user.lastName}")
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvThirdScreen.layoutManager = layoutManager
        binding.rvThirdScreen.adapter = adapter

        binding.rvThirdScreen.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadData()
                }
            }
        })
    }

    private fun loadData() {
        if (viewModel.isLoading.value == true) return

        showLoading()
        viewModel.loadData(ApiConfig().getApiService())
    }

    private fun showLoading() {
        binding.pgThirdScreen.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pgThirdScreen.visibility = View.GONE
        binding.refreshThirdScreen.isRefreshing = false
    }

    private fun showEmptyState() {
        binding.rvThirdScreen.visibility = View.GONE
        binding.emptyStateView.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.rvThirdScreen.visibility = View.VISIBLE
        binding.emptyStateView.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USER_TAG = "selected_user_name"
    }
}
