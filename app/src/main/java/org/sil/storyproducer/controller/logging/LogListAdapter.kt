package org.sil.storyproducer.controller.logging

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.sil.storyproducer.R
import org.sil.storyproducer.model.PhaseType
import org.sil.storyproducer.model.Workspace
import org.sil.storyproducer.model.logging.LogEntry
import java.util.*

internal class LogListAdapter(private val context: Context, slide: Int) : BaseAdapter() {

    private val allEntries = ArrayList<LogEntry>()
    private var displayEntries = ArrayList<LogEntry>()

    init {
        for (le in Workspace.activeStory.activityLogs) {
            if (le.appliesToSlideNum(slide)) {
                allEntries.add(le)
            }
        }
        displayEntries = allEntries
    }

    fun updateList(learn: Boolean, draft: Boolean, comCheck: Boolean) {
        displayEntries = ArrayList()
        for (le in allEntries) {
            when(le.phase){
                PhaseType.LEARN -> if(learn) displayEntries.add(le)
                PhaseType.DRAFT -> if(draft) displayEntries.add(le)
                PhaseType.COMMUNITY_CHECK -> if(comCheck) displayEntries.add(le)
                else -> {}
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return displayEntries.size
    }

    override fun getItem(position: Int): LogEntry {
        return displayEntries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cView = convertView
        if (cView == null) {
            cView = LayoutInflater.from(context)
                    .inflate(R.layout.log_list_item, parent, false)
        }

        val date = cView!!.findViewById<TextView>(R.id.textView_logging_date)
        val info = cView.findViewById<TextView>(R.id.textView_logging_type)

        val entry = getItem(position)
        date.text = entry.dateTimeString
        info.text = "${entry.phase.getPrettyName()} - ${entry.description}"
        cView.setBackgroundColor(ContextCompat.getColor(context, entry.phase.getColor()))

        return cView
    }
}
