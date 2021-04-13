package com.sprintsquads.adaptiveplus.ui.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.sprintsquads.adaptiveplus.R
import com.sprintsquads.adaptiveplus.data.models.components.APImageComponent
import com.sprintsquads.adaptiveplus.extensions.loadImage
import com.sprintsquads.adaptiveplus.ui.components.vm.APComponentViewModel
import com.sprintsquads.adaptiveplus.ui.components.vm.APImageComponentViewModel
import com.sprintsquads.adaptiveplus.utils.getColorFromHex
import kotlinx.android.synthetic.main.ap_component_image.view.*


internal class APImageComponentView : APBaseComponentView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(
        context: Context,
        component: APImageComponent,
        componentViewModel: APComponentViewModel?
    ) : super(context, component, componentViewModel)


    override fun initElement() {
        View.inflate(context, R.layout.ap_component_image, this)

        (component as? APImageComponent)?.run {
            apComponentImageView.loadImage(
                url,
                cornerRadius = cornerRadius?.toInt(),
                onResourceReady = {
                    (componentViewModel as? APImageComponentViewModel)?.onImageResourceReady()
                },
                onLoadFailed = {
                    (componentViewModel as? APImageComponentViewModel)?.onImageLoadFailed()
                },
                onLoadProgressUpdate = {
                    (componentViewModel as? APImageComponentViewModel)?.onImageLoadProgressUpdate(it)
                }
            )

            updateImageBorder()
        }
    }

    override fun resume() {}

    override fun pause() {}

    override fun reset() {
        updateImageBorder()
    }

    private fun updateImageBorder() {
        (component as? APImageComponent)?.run {
            border?.let {
                if ((componentViewModel as? APImageComponentViewModel)?.showBorder() == true) {
                    val borderState =
                        if ((componentViewModel as? APImageComponentViewModel)?.isActive() == false) {
                            it.inactive
                        } else {
                            it.active
                        }

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(apComponentLayout)
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.START, borderState.padding.toInt())
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.END, borderState.padding.toInt())
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.TOP, borderState.padding.toInt())
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.BOTTOM, borderState.padding.toInt())
                    constraintSet.applyTo(apComponentLayout)

                    val borderDrawable = GradientDrawable().apply {
                        setStroke(
                            borderState.width.toInt(),
                            getColorFromHex(borderState.color.startColor)
                        )
                        cornerRadius = borderState.cornerRadius.toFloat()
                    }
                    apComponentBorderView.background = borderDrawable
                } else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(apComponentLayout)
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.START, 0)
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.END, 0)
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.TOP, 0)
                    constraintSet.setMargin(
                        apComponentImageView.id, ConstraintSet.BOTTOM, 0)
                    constraintSet.applyTo(apComponentLayout)

                    apComponentBorderView.background = null
                }
            }
        }
    }
}