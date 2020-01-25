/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyperaware.android.firebasejetpack.activity.allstockspagedrv

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.hyperaware.android.firebasejetpack.R
import com.hyperaware.android.firebasejetpack.activity.livepricehistory.StockPriceHistoryActivity
import com.hyperaware.android.firebasejetpack.viewmodel.PagedStockPricesViewModel
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class AllStocksPagedRecyclerViewActivity : AppCompatActivity() {

    private val auth by inject<FirebaseAuth>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The model
        val stocksViewModel: PagedStockPricesViewModel by viewModels()

        // The root view/scaffolding
        setContentView(R.layout.activity_multi_tracker_recycler_view)
        toolbar.setTitle(R.string.track_stocks_rv)

        findViewById<RecyclerView>(R.id.rv_stocks).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AllStocksPagedRecyclerViewActivity)
            adapter = StocksPagedListAdapter(
                stocksViewModel,
                this@AllStocksPagedRecyclerViewActivity,
                ItemClickListener()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser == null) {
            finish()
        }
    }

    private inner class ItemClickListener : StocksPagedListAdapter.ItemClickListener<StockViewHolder> {
        override fun onItemClick(holder: StockViewHolder) {
            startActivity(StockPriceHistoryActivity.newIntent(
                this@AllStocksPagedRecyclerViewActivity,
                holder.ticker!!)
            )
        }
    }

}
