/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.screens.viewsets.defaultviews.ddm.form.fields

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.liferay.mobile.screens.ddl.form.view.DDLFieldViewModel
import com.liferay.mobile.screens.ddl.model.Field
import com.liferay.mobile.screens.thingscreenlet.delegates.bindNonNull
import rx.Observable

/**
 * @author Paulo Cruz
 */
class DDMFieldRepeatableItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr), DDLFieldViewModel<Field<*>> {

    private val addRepeatableButton
        by bindNonNull<Button>(com.liferay.mobile.screens.R.id.liferay_repeatable_field_add)

    private val removeRepeatableButton
        by bindNonNull<Button>(com.liferay.mobile.screens.R.id.liferay_repeatable_field_remove)

    private lateinit var field: Field<*>
    private lateinit var parentView: View

    private var fieldIndex: Int = 0
    private val isBaseField: Boolean
        get() = fieldIndex == 0

    private var fieldLayoutId: Int = 0
    private lateinit var listener: RepeatableActionListener
    private var onChangedValueObservable = Observable.empty<Field<*>>()

    override fun getOnChangedValueObservable(): Observable<Field<*>> {
        return onChangedValueObservable
    }

    fun setRepeatableItemSettings(fieldIndex: Int, fieldLayoutId: Int, listener: RepeatableActionListener) {

        this.fieldIndex = fieldIndex
        this.fieldLayoutId = fieldLayoutId
        this.listener = listener
    }

    private fun setupRepeatableActions() {
        if (!isBaseField) {
            removeRepeatableButton.visibility = View.VISIBLE

            removeRepeatableButton.setOnClickListener {
                (parent as? LinearLayout)?.let {
                    listener.onRepeatableFieldRemoved(this)
                }
            }
        }

        addRepeatableButton.setOnClickListener {
            listener.onRepeatableFieldAdded(fieldIndex + 1)
        }
    }

    private fun setupRepeatableField() {
        val inflater = LayoutInflater.from(context)

        val fieldView = inflater.inflate(fieldLayoutId, this, false)

        (fieldView as? DDLFieldViewModel<*>)?.let {
            it.field = field
            this.addView(fieldView)

            onChangedValueObservable = onChangedValueObservable
                .mergeWith(fieldView.onChangedValueObservable.map { field })
        }
    }

    override fun getField(): Field<*> {
        return field
    }

    override fun setField(field: Field<*>) {
        this.field = field

        refresh()
    }

    override fun refresh() {
        setupRepeatableActions()
        setupRepeatableField()
    }

    override fun onPostValidation(valid: Boolean) {}

    override fun getParentView(): View {
        return parentView
    }

    override fun setParentView(view: View) {
        parentView = view
    }

    override fun setUpdateMode(enabled: Boolean) {
        this.isEnabled = enabled
    }

}
