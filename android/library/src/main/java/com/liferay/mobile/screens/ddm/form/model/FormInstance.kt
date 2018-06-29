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

package com.liferay.mobile.screens.ddm.form.model

import com.liferay.mobile.screens.R
import com.liferay.mobile.screens.ddl.model.DDMStructure
import com.liferay.mobile.screens.ddl.model.Field
import com.liferay.mobile.screens.ddl.model.FormFieldKeys
import com.liferay.mobile.screens.thingscreenlet.screens.views.Detail
import com.liferay.mobile.screens.thingscreenlet.screens.views.Scenario
import com.liferay.apio.consumer.model.Relation
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.model.get
import java.util.*

/**
 * @author Paulo Cruz
 * @author Sarai Díaz García
 * @author Victor Oliveira
 */
data class FormInstance @JvmOverloads constructor(
    val name: String,
    val description: String?,
    val defaultLanguage: String,
    val ddmStructure: DDMStructure,
    val isRequiredAuthentication: Boolean = false,
    val isRequiredCaptcha: Boolean = false,
    val redirectURL: String? = null) {

    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, Int> =
            mutableMapOf(
                Detail to R.layout.ddm_form_default
            )

        val converter: (Thing) -> FormInstance = { it: Thing ->

            val name = it["name"] as String

            val description = it["description"] as? String

            val defaultLanguage = it["defaultLanguage"] as String

            val locale = Locale(defaultLanguage)

            val ddmStructure = (it["structure"] as Relation).let {
                getStructure(it, locale)
            }

            val fields = ddmStructure.pages.flatMap { it.fields }

            FormInstance(name, description, defaultLanguage, ddmStructure)
        }


        private fun getStructure(relation: Relation, locale: Locale): DDMStructure {

            val name = relation["name"] as String

            val description = relation["description"] as? String

            val pages = (relation["pages"] as Map<String, Any>).let {
                getPages(it, locale)
            }

            val successPage = relation["successPage"]?.let {
                val successMap = it as Map<*, *>
                val enabled = successMap["isEnabled"] as Boolean
                var headline = ""
                var text = ""

                if (enabled) {
                    headline = successMap["headline"] as String
                    text = successMap["text"] as String
                }
                SuccessPage(headline, text, enabled)
            }

            return DDMStructure(name, description, pages, successPage)
        }

        private fun getPages(mapper: Map<String, Any>, locale: Locale): List<FormPage> {

            return (mapper["member"] as List<Map<String, Any>>).mapTo(mutableListOf(), {

                val headlinePage = it["headline"] as? String ?: ""
                val textPage = it["text"] as? String ?: ""
                val fields = (it["fields"] as Map<String, Any>).let {
                    getFields(it, locale)
                }

                FormPage(headlinePage, textPage, fields)
            })
        }

        private fun getFields(map: Map<String, Any>, locale: Locale): List<Field<*>> {

            if (map["totalItems"] as Double <= 0) {
                return mutableListOf()
            }

            return (map["member"] as List<Map<String, Any>>).mapTo(mutableListOf(), {

                val isAutocomplete = it["isAutocomplete"] as? Boolean
                val isInline = it["isInline"] as? Boolean
                val isLocalizable = it["isLocalizable"] as? Boolean
                val isMultiple = it["isMultiple"] as? Boolean
                val isReadOnly = it["isReadOnly"] as? Boolean
                val isRepeatable = it["isRepeatable"] as? Boolean
                val isRequired = it["isRequired"] as? Boolean
                val isShowAsSwitcher = it["isShowAsSwitcher"] as? Boolean
                val isShowLabel = it["isShowLabel"] as? Boolean
                val isTransient = it["isTransient"] as? Boolean
                val hasFormRules = it["hasFormRules"] as? Boolean
                val label = it["label"] as? String
                val predefinedValue = it["predefinedValue"] as? String
                val tip = it["tip"] as? String
                val dataType = it["dataType"] as? String
                val additionalType = it["additionalType"] as? String
                val name = it["name"] as? String
                val placeholder = it["placeholder"] as? String
                val text = it["text"] as? String
                val displayStyle = it["displayStyle"] as? String
                val grid = it["grid"] as? Map<String, Any>
                val options = (it["options"] as? Map<String, Any>)?.let {
                    it["member"] as? List<Map<String, Any>>
                }
                val validation = it["validation"] as? Map<String, String> ?: emptyMap()

                val attributes = mapKeysToAllValues(isAutocomplete, isInline, isLocalizable, isMultiple, isReadOnly,
                    isRepeatable, isRequired, isShowAsSwitcher, isShowLabel, isTransient, hasFormRules, label,
                    predefinedValue, tip, dataType, additionalType, name, placeholder, text, displayStyle, grid, options,
                    validation)


                val fieldDataType = Field.DataType.assignDataTypeFromString(dataType)
                fieldDataType.createField(attributes, locale, locale)
            })
        }

        fun mapKeysToAllValues(isAutocomplete: Boolean?, isInline: Boolean?, isLocalizable: Boolean?,
            isMultiple: Boolean?, isReadOnly: Boolean?, isRepeatable: Boolean?, isRequired: Boolean?,
            isShowAsSwitcher: Boolean?, isShowLabel: Boolean?, isTransient: Boolean?, hasFormRules: Boolean?,
            label: String?, predefinedValue: String?, tip: String?, dataType: String?, additionalType: String?,
            name: String?, placeholder: String?, text: String?, displayStyle: String?, grid: Map<String, Any>?,
            options: List<Map<String, Any>>?, validation: Map<String, String>): Map<String, Any?> {

            return mapOf(
                FormFieldKeys.IS_AUTOCOMPLETE to isAutocomplete,
                FormFieldKeys.INLINE to isInline,
                FormFieldKeys.IS_LOCALIZABLE to isLocalizable,
                FormFieldKeys.MULTIPLE to isMultiple,
                FormFieldKeys.READ_ONLY to isReadOnly,
                FormFieldKeys.REPEATABLE to isRepeatable,
                FormFieldKeys.REQUIRED to isRequired,
                FormFieldKeys.SWITCHER to isShowAsSwitcher,
                FormFieldKeys.SHOW_LABEL to isShowLabel,
                FormFieldKeys.IS_TRANSIENT to isTransient,
                FormFieldKeys.HAS_FORM_RULES to hasFormRules,
                FormFieldKeys.LABEL to label,
                FormFieldKeys.PREDEFINED_VALUE to predefinedValue,
                FormFieldKeys.TIP to tip,
                FormFieldKeys.DATA_TYPE to dataType,
                FormFieldKeys.TYPE to additionalType,
                FormFieldKeys.NAME to name,
                FormFieldKeys.PLACEHOLDER to placeholder,
                FormFieldKeys.TEXT to text,
                FormFieldKeys.DISPLAY_STYLE to displayStyle,
                FormFieldKeys.GRID to grid,
                FormFieldKeys.OPTIONS to options,
                FormFieldKeys.VALIDATION to validation
            )
        }
    }
}