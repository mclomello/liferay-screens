/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.screens.viewsets.defaultviews.ddm.form.fields;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.liferay.mobile.screens.R;
import com.liferay.mobile.screens.ddl.form.view.DDLFieldViewModel;
import com.liferay.mobile.screens.ddl.model.Option;
import com.liferay.mobile.screens.ddm.form.model.CheckboxMultipleField;
import com.liferay.mobile.screens.util.AndroidUtil;
import com.liferay.mobile.screens.util.StringUtils;
import com.liferay.mobile.screens.viewsets.defaultviews.util.ThemeUtil;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author Paulo Cruz
 */
public class DDMFieldCheckboxMultipleView extends LinearLayout
    implements DDLFieldViewModel<CheckboxMultipleField>, CompoundButton.OnCheckedChangeListener {

    protected View parentView;

    private CheckboxMultipleField field;
    private LinearLayout linearLayout;
    private TextView hintTextView;
    private Observable<CheckboxMultipleField> onChangedValueObservable = Observable.empty();

    public DDMFieldCheckboxMultipleView(Context context) {
        super(context);
    }

    public DDMFieldCheckboxMultipleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DDMFieldCheckboxMultipleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Option opt = (Option) compoundButton.getTag();

        if (isChecked) {
            field.selectOption(opt);
        } else {
            field.clearOption(opt);
        }
    }

    @Override
    public CheckboxMultipleField getField() {
        return field;
    }

    @Override
    public void setField(final CheckboxMultipleField field) {
        this.field = field;

        if (field.isShowLabel()) {
            TextView label = findViewById(R.id.liferay_ddm_label);

            label.setText(field.getLabel());
            label.setVisibility(VISIBLE);

            if (field.isRequired()) {
                label.append(ThemeUtil.getRequiredSpannable(getContext()));
            }
        }

        if (field.isInline()) {
            linearLayout.setOrientation(HORIZONTAL);
        }

        LayoutParams layoutParams = calculateLayoutParams(field.isInline(), field.showAsSwitcher());

        List<Option> availableOptions = field.getAvailableOptions();

        for (Option opt : availableOptions) {
            if (field.showAsSwitcher()) {
                Switch switchView = createSwitchView(opt, layoutParams);
                addView(field, switchView);
            } else {
                CheckBox checkBoxView = createCheckBoxView(opt, layoutParams);
                addView(field, checkBoxView);
            }
        }

        if (!StringUtils.isNullOrEmpty(this.field.getTip())) {
            hintTextView.setText(this.field.getTip());
            hintTextView.setVisibility(VISIBLE);
        }

        refresh();
    }

    @Override
    public void refresh() {
        List<Option> selectedOptions = field.getCurrentValue();

        if (selectedOptions != null) {
            for (Option opt : selectedOptions) {
                CompoundButton compoundButton = findViewWithTag(opt);

                if (compoundButton != null) {
                    compoundButton.setChecked(true);
                }
            }
        }
    }

    @Override
    public void onPostValidation(boolean valid) {
        String errorText = valid ? null : getContext().getString(R.string.required_value);

        if (field.isShowLabel()) {
            TextView label = findViewById(R.id.liferay_ddm_label);
            label.setError(errorText);
        } else {
            List<Option> availableOptions = field.getAvailableOptions();
            Option opt = availableOptions.get(0);
            CheckBox checkBox = findViewWithTag(opt);
            if (checkBox != null) {
                checkBox.setError(errorText);
            }
        }
    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(View view) {
        parentView = view;
    }

    @Override
    public Observable<CheckboxMultipleField> getOnChangedValueObservable() {
        return onChangedValueObservable;
    }

    @Override
    public void setUpdateMode(boolean enabled) {
        if (this.field.isShowLabel()) {
            TextView label = findViewById(R.id.liferay_ddm_label);
            AndroidUtil.updateViewState(label, enabled);
        }

        List<Option> availableOptions = field.getAvailableOptions();
        if (availableOptions != null) {
            for (Option opt : availableOptions) {
                CompoundButton compoundButton = findViewWithTag(opt);
                compoundButton.setEnabled(enabled);
            }
        }

        setEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        linearLayout = findViewById(R.id.linear_layout_multiple_checkbox);
        hintTextView = findViewById(R.id.liferay_ddm_hint);

        setSaveEnabled(true);
    }

    private void addView(CheckboxMultipleField field, CompoundButton switchView) {
        linearLayout.addView(switchView);
        onChangedValueObservable = onChangedValueObservable.mergeWith(getMappedObservable(field, switchView));
    }

    private Observable<CheckboxMultipleField> getMappedObservable(final CheckboxMultipleField field,
        final CompoundButton switchView) {

        return RxCompoundButton.checkedChanges(switchView).doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                onCheckedChanged(switchView, aBoolean);
            }
        }).distinctUntilChanged().map(new Func1<Boolean, CheckboxMultipleField>() {
            @Override
            public CheckboxMultipleField call(Boolean aBoolean) {
                return field;
            }
        });
    }

    private CheckBox createCheckBoxView(Option option, LayoutParams layoutParams) {

        CheckBox checkBoxView = new CheckBox(getContext());
        checkBoxView.setLayoutParams(layoutParams);
        checkBoxView.setText(option.label);
        checkBoxView.setTag(option);
        checkBoxView.setOnCheckedChangeListener(this);
        checkBoxView.setTypeface(getTypeface());
        checkBoxView.setSaveEnabled(true);

        if (field.isInline()) {
            checkBoxView.setGravity(Gravity.TOP);
        }

        return checkBoxView;
    }

    private Switch createSwitchView(Option option, LayoutParams layoutParams) {

        Switch switchView = new Switch(getContext());
        switchView.setLayoutParams(layoutParams);
        switchView.setText(option.label);
        switchView.setTag(option);
        switchView.setOnCheckedChangeListener(this);
        switchView.setTypeface(getTypeface());
        switchView.setSaveEnabled(true);

        if (field.isInline()) {
            switchView.setGravity(Gravity.TOP);
        }

        return switchView;
    }

    private LayoutParams calculateLayoutParams(Boolean isInline, Boolean showAsSwitcher) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if (!showAsSwitcher || isInline) {
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.weight = 1.0f;
        }

        return layoutParams;
    }

    private Typeface getTypeface() {
        //FIXME replace with constructor with styles when we have the drawables
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return Typeface.DEFAULT;
        }
        return Typeface.create("sans-serif-light", Typeface.NORMAL);
    }
}
