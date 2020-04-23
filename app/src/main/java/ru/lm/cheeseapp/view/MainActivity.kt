package ru.lm.cheeseapp.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import ru.lm.cheeseapp.R
import ru.lm.cheeseapp.model.pojo.Banner
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import ru.lm.cheeseapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    lateinit var searchView: SearchView

    @BindView(R.id.recyclerListInfo)
    lateinit var recyclerListBanner: RecyclerView

    @BindView(R.id.recyclerListOffers)
    lateinit var recyclerListOffer: RecyclerView

    @BindView(R.id.buttonFilterTop)
    lateinit var buttonFilterTop: RadioButton

    @BindView(R.id.buttonFilterShops)
    lateinit var buttonFilterShops: RadioButton

    @BindView(R.id.buttonFilterGoods)
    lateinit var buttonFilterGoods: RadioButton

    private val bannerList = ArrayList<Banner>()
    private val offerList = ArrayList<OfferRecyclerItem>()
    private val bannerListAdapter = BannerRecyclerAdapter(bannerList)
    private val offerListAdapter = OfferRecyclerAdapter(offerList)

    @BindView(R.id.buttonW)
    lateinit var button: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getBannerList().observe(this, Observer<List<Banner>> {
            bannerList.clear()
            bannerList.addAll(it)
            bannerListAdapter.notifyDataSetChanged()
        })

        viewModel.getOfferList().observe(this, Observer<List<OfferRecyclerItem>> {
            offerList.clear()
            offerList.addAll(it)
            offerListAdapter.notifyDataSetChanged()
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        initRecyclerViews()

        button.setOnClickListener {
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
        viewModel.onObserveBannerList()
        viewModel.onObserveOfferList()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            searchView.setQuery(query, false)
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
