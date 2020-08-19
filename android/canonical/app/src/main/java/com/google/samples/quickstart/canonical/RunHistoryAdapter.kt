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
        val rowView : View
        val holder : ViewHolder
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.single_run_item, parent, false)
            holder = ViewHolder(rowView)
            rowView.tag = holder
        } else {
            rowView = convertView
            holder = rowView.tag as ViewHolder
        }

        val singleRun = getItem(position) as ProfileViewModel.SingleRun
        holder.timeTextView.text = singleRun.time
        holder.datetimeTextView.text = singleRun.dateTime

        return rowView
    }

    private class ViewHolder(row: View) {
        val timeTextView: TextView = row.findViewById(R.id.single_run_time)
        val datetimeTextView: TextView = row.findViewById(R.id.single_run_datetime)
    }

}

