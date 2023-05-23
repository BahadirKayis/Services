package com.bahadir.overlayservice.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahadir.core.domain.model.UsageStateUI
import com.bahadir.overlayservice.databinding.ItemAppUsageTimeBinding

class AppsUsageTimeAdapter(private val usageState: List<UsageStateUI>) :
    RecyclerView.Adapter<AppsUsageTimeAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemAppUsageTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(usageState: UsageStateUI) {
            binding.apply {
                textAppName.text = usageState.appName
                imgApp.setImageDrawable(usageState.icon)
                progressBar.setProgress(usageState.usageTime, true)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsUsageTimeAdapter.ViewHolder {
        val binding =
            ItemAppUsageTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppsUsageTimeAdapter.ViewHolder, position: Int) {
        holder.bind(usageState[position])
    }

    override fun getItemCount(): Int = usageState.size
}