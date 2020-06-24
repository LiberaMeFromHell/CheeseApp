package ru.lm.cheeseapp.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import ru.lm.cheeseapp.R
import ru.lm.cheeseapp.databinding.ActivityMainBinding
import ru.lm.cheeseapp.model.pojo.Banner
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import ru.lm.cheeseapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView

    private lateinit var recyclerListBanner: RecyclerView
    private lateinit var recyclerListOffer: RecyclerView

    private lateinit var buttonFilterTop: RadioButton
    private lateinit var buttonFilterShops: RadioButton
    private lateinit var buttonFilterGoods: RadioButton

    private lateinit var buttonW: ImageView

    private val bannerList = ArrayList<Banner>()
    private val offerList = ArrayList<OfferRecyclerItem>()
    private val bannerListAdapter = BannerRecyclerAdapter(bannerList)
    private val offerListAdapter = OfferRecyclerAdapter(offerList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.bannerLiveData.observe(this, Observer<List<Banner>> {
            bannerList.clear()
            bannerList.addAll(it)
            bannerListAdapter.submitList(bannerList)
        })

        viewModel.offerLiveData.observe(this, Observer<List<OfferRecyclerItem>> {
            offerList.clear()
            offerList.addAll(it)
            offerListAdapter.submitList(offerList)
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        initRecyclerViews()

        buttonW.setOnClickListener {
            EmptyDialog().show(supportFragmentManager, "EmptyDialog")
        }

        val filterButtons2 = arrayListOf(buttonFilterGoods, buttonFilterShops, buttonFilterTop)
        filterButtons2.forEach { button ->
            button.setOnCheckedChangeListener { compoundButton, b ->
                onCheckedChangeListener(
                    compoundButton,
                    b
                )
            }
        }

        if (savedInstanceState == null) {
            viewModel.onObserveBannerList()
            viewModel.onObserveOfferList()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            searchView.setQuery(query, false)
        }
    }

    private fun bind() {
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val rootView = binding.root
        setContentView(rootView)
        binding.apply {

            this@MainActivity.searchView = searchView
            this@MainActivity.buttonW = buttonW
            this@MainActivity.recyclerListBanner = recyclerListBanner
            this@MainActivity.recyclerListOffer = recyclerListOffer

            this@MainActivity.buttonFilterTop = buttonFilterTop
            this@MainActivity.buttonFilterShops = buttonFilterShops
            this@MainActivity.buttonFilterGoods = buttonFilterGoods
        }
    }

    private fun initRecyclerViews() {

        recyclerListBanner.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerListBanner.adapter = bannerListAdapter
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerListBanner)

        recyclerListOffer.layoutManager = LinearLayoutManager(this)
        recyclerListOffer.adapter = offerListAdapter
        val swipeHelper = object : SwipeHelper(this) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: List<UnderlayButton>?
            ) {
                (underlayButtons as MutableList).add(
                    UnderlayButton(
                        R.drawable.ic_clear,
                        resources.getColor(R.color.colorAccent),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                            }
                        })
                )
            }
        }
        swipeHelper.attachToRecyclerView(recyclerListOffer)
    }

    private fun onCheckedChangeListener(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            when (buttonView.id) {
                R.id.buttonFilterTop -> {
                    buttonFilterGoods.isChecked = false
                    buttonFilterShops.isChecked = false
                }
                R.id.buttonFilterShops -> {
                    buttonFilterGoods.isChecked = false
                    buttonFilterTop.isChecked = false
                }
                R.id.buttonFilterGoods -> {
                    buttonFilterShops.isChecked = false
                    buttonFilterTop.isChecked = false
                }
            }
        }
    }
}
