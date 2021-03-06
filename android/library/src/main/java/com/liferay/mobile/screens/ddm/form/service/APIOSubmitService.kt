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

package com.liferay.mobile.screens.ddm.form.service

import com.liferay.apio.consumer.exception.ThingNotFoundException
import com.liferay.apio.consumer.exception.ThingWithoutOperationException
import com.liferay.apio.consumer.model.Operation
import com.liferay.apio.consumer.model.Relation
import com.liferay.apio.consumer.model.Thing
import com.liferay.mobile.screens.ddl.form.util.FormConstants
import com.liferay.mobile.screens.ddl.model.Field
import com.liferay.mobile.screens.ddm.form.serializer.FieldValueSerializer

/**
 * @author Paulo Cruz
 */
class APIOSubmitService : BaseAPIOService() {

	fun submit(formThing: Thing, currentRecordThing: Thing?, fields: MutableList<Field<*>>,
		isDraft: Boolean = false, onSuccess: (Thing) -> Unit, onError: (Throwable) -> Unit) {

		val recordsRelation = formThing.attributes["formRecords"] as? Relation

		currentRecordThing?.let {
			submit(it.id, "update", fields, isDraft, onSuccess, onError)
		} ?: recordsRelation?.let {
			submit(it.id, "create", fields, isDraft, onSuccess, onError)
		} ?: onError(ThingNotFoundException())
	}

	fun submit(thingId: String, operationId: String,
		fields: MutableList<Field<*>>, isDraft: Boolean,
		onSuccess: (Thing) -> Unit, onError: (Throwable) -> Unit) {

		getThing(thingId, { thing ->
			thing.getOperation(operationId)?.let { operation ->
				performSubmit(thing, operation, fields, isDraft, onSuccess, onError)
			} ?: onError(ThingWithoutOperationException(thing.id, operationId))
		}, onError)
	}

	private fun getThing(thingId: String, onSuccess: (Thing) -> Unit,
		onError: (Throwable) -> Unit) {

		apioConsumer.fetchResource(thingId) { result ->
			result.fold(onSuccess, onError)
		}
	}

	private fun performSubmit(thing: Thing, operation: Operation, fields: MutableList<Field<*>>,
		isDraft: Boolean = false, onSuccess: (Thing) -> Unit, onError: (Throwable) -> Unit) {

		apioConsumer.performOperation(thing.id, operation.id, fillFields = {
			mapOf(
				Pair("isDraft", isDraft),
				Pair(FormConstants.FIELD_VALUES, FieldValueSerializer.serialize(fields) { !it.isTransient })
			)
		}) { result ->
			result.fold(onSuccess, onError)
		}
	}
}
