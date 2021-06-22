package com.italo.mycustomviews.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.italo.mycustomviews.R
import com.italo.mycustomviews.databinding.ProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val binding = ProgressButtonBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var state: ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    init {
        setLayout(attrs)
        refreshState()
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.ProgressButton
            )

            setBackgroundResource(R.drawable.progress_button_background)

            val titleResId =
                attributes.getResourceId(R.styleable.ProgressButton_progress_button_title, 0)

            if (titleResId != 0) {
                title = context.getString(titleResId)
            }

            val loadingTitleResId =
                attributes.getResourceId(R.styleable.ProgressButton_loading_title, 0)

            if (titleResId != 0) {
                loadingTitle = context.getString(loadingTitleResId)
            }

            attributes.recycle()
        }
    }

    private fun refreshState() {

        state.isEnabled.let {
            isEnabled = it
            isClickable = it
        }

        refreshDrawableState()

        binding.progress.visibility = state.progressVisibility

        when (state) {
            ProgressButtonState.Normal -> {
                binding.textTitle.text = title
            }
            ProgressButtonState.Loading -> binding.textTitle.text = loadingTitle
        }
    }

    fun setLoading(loadingState: Boolean) {
        state = when (loadingState) {
            false -> ProgressButtonState.Normal
            true -> ProgressButtonState.Loading
        }
    }

    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int) {
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }
}
