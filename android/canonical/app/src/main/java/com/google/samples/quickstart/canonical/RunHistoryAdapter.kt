package com.google.samples.quickstart.canonical

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class RunHistoryAdapter(
    context: Context,
    private val runHistoryArrayList: ArrayList<ProfileViewModel.SingleRun>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return runHistoryArrayList.size
    }

    override fun getItem(position: Int): Any {
        return runHistoryArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = convertView ?: inflater.inflate(R.layout.single_run_item, parent, false)
        val timeTextView = rowView.findViewById<TextView>(R.id.single_run_time)
        val datetimeTextView = rowView.findViewById<TextView>(R.id.single_run_datetime)

        val singleRun = getItem(position) as ProfileViewModel.SingleRun
        timeTextView.text = singleRun.time
        datetimeTextView.text = singleRun.dateTime

        return rowView
    }

}